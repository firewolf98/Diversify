import os
import random
import json
import pickle
import numpy as np
from textblob import TextBlob
import nltk
from nltk.stem import WordNetLemmatizer
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
from tensorflow.keras.models import load_model

os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2' #questo prova a rimuovere gli errori di keras (spoiler: non funziona)

lemmatizer = WordNetLemmatizer()
intents = json.loads(open('../data/intents.json', encoding='utf-8').read())

words = pickle.load(open('../models/words.pkl', 'rb'))
classes = pickle.load(open('../models/classes.pkl', 'rb'))
model = load_model('../models/chatbot_model.keras')


# Funzione per pulire e pre-processare la frase
def clean_up_sentence(sentence):
    sentence_words = nltk.word_tokenize(sentence)
    sentence_words = [lemmatizer.lemmatize(word) for word in sentence_words]
    return sentence_words


# Funzione per creare una "bag of words"
def bag_of_words(sentence):
    sentence_words = clean_up_sentence(sentence)
    bag = [0] * len(words)
    for w in sentence_words:
        for i, word in enumerate(words):
            if word == w:
                bag[i] = 1
    return np.array(bag)


# Funzione per ottenere la previsione della classe usando la rete neurale
def predict_class(sentence):
    bow = bag_of_words(sentence)
    res = model.predict(np.array([bow]))[0]
    ERROR_THRESHOLD = 0.25
    results = [[i, r] for i, r in enumerate(res) if r > ERROR_THRESHOLD]
    results.sort(key=lambda x: x[1], reverse=True)
    return_list = []
    if not results:
        return []  # Nessuna predizione valida
    return [{'intent': classes[r[0]], 'probability': str(r[1])} for r in results]


# Funzione per ottenere una risposta in base all'intent
def get_response(intents_list, intents_json):
    if not intents_list:  # Se intents_list è vuoto
        return "Sorry, I didn't understand that. Could you please try again?"
    tag = intents_list[0]['intent']
    list_of_intents = intents_json['intents']
    for i in list_of_intents:
        if i['tag'] == tag:
            result = random.choice(i['responses'])
            break
    return result


# Funzione di correttore per l'input dell'utente
def correct_input(sentence):
    if "diversify" or "brigid" in sentence:
        return sentence
    corrected = TextBlob(sentence).correct()
    return str(corrected)


# Funzione di Similarità Coseno per trovare il miglior pattern corrispondente
def get_best_match(user_input, patterns):
    # Aggiungi l'input dell'utente alla lista dei pattern
    vectorizer = TfidfVectorizer().fit_transform([user_input] + patterns)
    cosine_similarities = cosine_similarity(vectorizer[0:1], vectorizer[1:])
    best_match_index = cosine_similarities.argmax()  # Prendi il pattern con la similarità più alta
    return best_match_index


# Converte il match del tag da JSON a stringa
def get_tag(match):
    if match:
        x = str(match['tag'])
        print(x)
    else:
        print("tag not found")


# Funzione che servirà per inviare il messaggio di risposta a Diversify
def send_to_diversify(tag, message):
    print("Sending to Diversify")
    tag = str(tag)
    risposta = {
        "message": message,
        "tag": tag
    }
    json_string = json.dumps(risposta)

# Funzione per l'interazione con la chatbot
print("Hi there! I'm Brigid, and I'm here to help you!")
while True:
    message = input("> ")
    corrected_message = correct_input(message)  # Corregge il testo in input
    print(f"Corrected input: {corrected_message}")  # Mostra l'input corretto (opzionale)

    # Prepara una lista di tutti i pattern nel dataset
    patterns = [pattern for intent in intents['intents'] for pattern in intent['patterns']]

    # Ottieni il miglior match con la similarità coseno
    best_match_index = get_best_match(corrected_message, patterns)

    # Trova l'intent corrispondente
    matched_intent = None
    start = 0
    for intent in intents['intents']:
        num_patterns = len(intent['patterns'])
        if best_match_index < start + num_patterns:
            matched_intent = intent
            break
        start += num_patterns

    # Se troviamo un intent corrispondente, recuperiamo la risposta
    if matched_intent:
        res = get_response([{'intent': matched_intent['tag'], 'probability': 1.0}], intents)
        send_to_diversify(get_tag(matched_intent), res)
    else:
        res = "Sorry, I didn't understand that. Could you please try again?"
        send_to_diversify(get_tag(None), res)
    print(res)