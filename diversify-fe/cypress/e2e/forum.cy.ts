describe('Componente Forum', () => {
  beforeEach(() => {
    // Intercetta la chiamata API per ottenere i forum (simulazione del backend)
    cy.intercept('GET', '/api/forums/by-paese/Italia', {
      statusCode: 200,
      body: [
        {
          idForum: 1,
          titolo: 'Forum dei Diritti Umani',
          descrizione: 'Un forum per discutere i diritti umani in vari paesi.',
          paese: 'Italia',
          post: [
            {
              idPost: 1,
              titolo: 'L\'importanza dei diritti civili',
              idAutore: 'Giuseppe Rossi',
              contenuto: 'Il diritto alla libertà di espressione è fondamentale in una società democratica.',
              dataCreazione: '2025-01-01T12:00:00Z',
              like: 5,
              commenti: [
                { idCommento: 1, autore: 'Maria Bianchi', contenuto: 'Sono d\'accordo, la libertà di espressione è essenziale.' },
                { idCommento: 2, autore: 'Luigi Verdi', contenuto: 'Purtroppo, non è sempre garantita in ogni parte del mondo.' }
              ],
              idForum: 1
            },
            {
              idPost: 2,
              titolo: 'I diritti delle minoranze',
              idAutore: 'Laura Neri',
              contenuto: 'Le minoranze etniche e religiose spesso sono vittime di discriminazioni.',
              dataCreazione: '2025-01-02T14:30:00Z',
              like: 8,
              commenti: [
                { idCommento: 3, autore: 'Carlo Galli', contenuto: 'È fondamentale proteggere le minoranze.' },
                { idCommento: 4, autore: 'Anna Russo', contenuto: 'Dobbiamo fare di più per sensibilizzare l\'opinione pubblica.' }
              ],
              idForum: 1
            }
          ]
        },
        {
          idForum: 2,
          titolo: 'Forum sui Diritti LGBT',
          descrizione: 'Uno spazio per discutere e sensibilizzare sui diritti delle persone LGBT.',
          paese: 'Italia',
          post: [
            {
              idPost: 3,
              titolo: 'Leggi per la protezione delle persone LGBT',
              idAutore: 'Marco Sala',
              contenuto: 'Esistono leggi che proteggono le persone LGBT, ma spesso non sono sufficientemente applicate.',
              dataCreazione: '2025-01-03T09:00:00Z',
              like: 12,
              commenti: [
                { idCommento: 5, autore: 'Giulia Conti', contenuto: 'Sì, l\'applicazione delle leggi è un problema serio.' },
                { idCommento: 6, autore: 'Francesco Rizzo', contenuto: 'La comunità deve fare sentire la propria voce per il cambiamento.' }
              ],
              idForum: 2
            },
            {
              idPost: 4,
              titolo: 'L\'inclusività nelle scuole',
              idAutore: 'Alessandra Gori',
              contenuto: 'Molte scuole italiane stanno implementando politiche più inclusive per gli studenti LGBT.',
              dataCreazione: '2025-01-04T16:45:00Z',
              like: 20,
              commenti: [
                { idCommento: 7, autore: 'Lucia Ferro', contenuto: 'Ottima iniziativa, ma c\'è ancora tanto lavoro da fare.' },
                { idCommento: 8, autore: 'Stefano Mancini', contenuto: 'L\'inclusività nelle scuole è fondamentale per una società migliore.' }
              ],
              idForum: 2
            }
          ]
        },
        {
          idForum: 3,
          titolo: 'Forum sui Diritti',
          descrizione: 'Uno spazio.',
          paese: 'Italia',
          post: []
        }
      ], // Dati mockati per i forum
    }).as('getForums');

    // Intercetta la chiamata API per ottenere i post associati al forum (simulazione del backend)
    cy.intercept('GET', '/api/posts/by-forum/*', { // Modifica l'endpoint se necessario
      statusCode: 200,
      body: [
        { idPost: 1, titolo: 'L\'importanza dei diritti civili', id_autore: 'Giuseppe Rossi' },
        { idPost: 2, titolo: 'I diritti delle minoranze', id_autore: 'Laura Neri' },
      ], // Dati mockati per i post
    }).as('getPosts');

    // Visita la pagina del forum
    cy.visit('/forum'); // Sostituisci con l'URL o il percorso corretto

    // Aspetta che la chiamata API simulata per i forum sia completata
    cy.wait('@getForums');
  });

  it('Dovrebbe mostrare il titolo "Forum" nella sidebar', () => {
    cy.get('.forum-sidebar h3').should('contain.text', 'Forum');
  });

  it('Dovrebbe mostrare il link "Torna alla mappa"', () => {
    cy.get('.forum-sidebar .back-link').should('contain.text', '← Torna alla mappa');
  });

  it('Dovrebbe generare dinamicamente i bottoni per ogni forum', () => {
    // Controlla che ci siano i bottoni per ciascun forum
    cy.get('.forum-sidebar ul li button', { timeout: 10000 })
      .should('have.length', 3) // Ci sono due forum mockati
      .each((button, index) => {
        const expectedTitles = ['Forum dei Diritti Umani', 'Forum sui Diritti LGBT', 'Forum sui Diritti'];
        cy.wrap(button).should('contain.text', expectedTitles[index]);
      });
  });

  it('Dovrebbe permettere di selezionare un forum', () => {
    // Simula il click su un forum
    cy.get('.forum-sidebar ul li button').first().click();
    
    // Verifica che i post siano visibili nella sezione principale
    cy.get('.forum-main h3').should('contain.text', 'Post del Forum');
  });

  it('Dovrebbe mostrare i post associati al forum selezionato', () => {
    // Simula il click sul primo forum
    cy.get('.forum-sidebar ul li button').first().click();
    
    // Aggiungi un log per tracciare il flusso
    cy.log('Click sul forum, aspettiamo la chiamata API per i post');
    

    // Verifica che i post siano visibili nella sezione principale
    cy.get('.forum-main .forum-post', { timeout: 10000 }) // Timeout più lungo
      .should('have.length', 2) // Verifica che ci siano 2 post mockati
      .each((post) => {
        cy.wrap(post).find('a').should('not.be.empty'); // Verifica che ogni post abbia un link
        cy.wrap(post).find('p').should('contain.text', 'Postato da'); // Verifica che contenga l'autore
      });
  });

  // Opzionale: Verifica con chiamata API diretta per garantire che l'API restituisca i dati corretti
  it('Dovrebbe verificare la risposta dell\'API per i forum', () => {
    cy.request('GET', '/api/forums/by-paese/Italia')
      .then((response) => {
        expect(response.status).to.eq(200);
        expect(response.body).to.have.length.greaterThan(0); // Controlla che ci siano forum
      });
  });
});
