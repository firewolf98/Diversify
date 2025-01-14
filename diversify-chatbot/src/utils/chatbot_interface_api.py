from flask import Flask, request, jsonify
from flask_cors import CORS

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

lemmatizer = WordNetLemmatizer()
intents = json.loads(open('../data/intents.json', encoding='utf-8').read())

words = pickle.load(open('../models/words.pkl', 'rb'))
classes = pickle.load(open('../models/classes.pkl', 'rb'))
model = load_model('../models/chatbot_model.keras')

app = Flask(__name__)
CORS(app)  # Abilita CORS per tutte le rotte

# Funzioni esistenti (clean_up_sentence, bag_of_words, predict_class, ecc.)

def clean_up_sentence(sentence):
    sentence_words = nltk.word_tokenize(sentence)
    sentence_words = [lemmatizer.lemmatize(word) for word in sentence_words]
    return sentence_words

def bag_of_words(sentence):
    sentence_words = clean_up_sentence(sentence)
    bag = [0] * len(words)
    for w in sentence_words:
        for i, word in enumerate(words):
            if word == w:
                bag[i] = 1
    return np.array(bag)

def predict_class(sentence):
    bow = bag_of_words(sentence)
    res = model.predict(np.array([bow]))[0]
    ERROR_THRESHOLD = 0.25
    results = [[i, r] for i, r in enumerate(res) if r > ERROR_THRESHOLD]
    results.sort(key=lambda x: x[1], reverse=True)
    return [{'intent': classes[r[0]], 'probability': str(r[1])} for r in results]

def get_response(intents_list, intents_json):
    if not intents_list:
        return "Sorry, I didn't understand that. Could you please try again?"
    tag = intents_list[0]['intent']
    for intent in intents_json['intents']:
        if intent['tag'] == tag:
            return random.choice(intent['responses'])
    return "Sorry, I didn't understand that."

def correct_input(sentence):
    if "diversify" in sentence or "brigid" in sentence:
        return sentence
    corrected = TextBlob(sentence).correct()
    return str(corrected)

def get_best_match(user_input, patterns):
    vectorizer = TfidfVectorizer().fit_transform([user_input] + patterns)
    cosine_similarities = cosine_similarity(vectorizer[0:1], vectorizer[1:])
    best_match_index = cosine_similarities.argmax()
    return best_match_index

@app.route('/chat', methods=['POST'])
def chat():
    # Recupera la richiesta JSON dal client
    data = request.get_json()
    user_input = data.get('message', '')

    if not user_input:
        return jsonify({"error": "Missing 'message' in request"}), 400

    corrected_message = correct_input(user_input)

    patterns = [pattern for intent in intents['intents'] for pattern in intent['patterns']]
    best_match_index = get_best_match(corrected_message, patterns)

    matched_intent = None
    start = 0
    for intent in intents['intents']:
        num_patterns = len(intent['patterns'])
        if best_match_index < start + num_patterns:
            matched_intent = intent
            break
        start += num_patterns

    if matched_intent:
        response = get_response([{'intent': matched_intent['tag'], 'probability': 1.0}], intents)
        tag = matched_intent['tag']
    else:
        response = "Sorry, I didn't understand that. Could you please try again?"
        tag = "unknown"

    return jsonify({"tag": tag, "response": response})

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)
