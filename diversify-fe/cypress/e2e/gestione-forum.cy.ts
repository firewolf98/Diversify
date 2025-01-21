describe('Test visualizzazione dei forum', () => {
    beforeEach(() => {
      cy.intercept('GET', '/utenti/recupera_utente', {
        statusCode: 200,
        body: {
          idUtente: "1",
          nome: "nome",
          cognome: "cognome",
          codiceFiscale: "1234567891123456",
          email: "email@gmail.com",
          username: "user",
          passwordHash: "passworD1!",
          tipoDomanda: "come si chiama tuo padre?", 
          rispostaHash: "giuseppe",
          blacklistForum: [
            { idForum: "1" },
            { idForum: "2" }
          ],
          ruolo: true,
          banned: false
        }
      }).as('getUtente');
  
      cy.intercept('GET', '/api/forums', {
        statusCode: 200,
        body: [
          {
            idForum: 1,
            titolo: 'Forum dei Diritti Umani',
            descrizione: 'Un forum per discutere i diritti umani in vari paesi.',
            paese: 'Italia',
            post: [
              { idPost: 1, titolo: 'L\'importanza dei diritti civili', idAutore: 'Giuseppe Rossi', contenuto: 'Il diritto alla libertà di espressione è fondamentale in una società democratica.' },
              { idPost: 2, titolo: 'I diritti delle minoranze', idAutore: 'Laura Neri', contenuto: 'Le minoranze etniche e religiose spesso sono vittime di discriminazioni.' }
            ]
          },
          {
            idForum: 2,
            titolo: 'Forum sui Diritti LGBT',
            descrizione: 'Uno spazio per discutere e sensibilizzare sui diritti delle persone LGBT.',
            paese: 'Italia',
            post: [
              { idPost: 3, titolo: 'Leggi per la protezione delle persone LGBT', idAutore: 'Marco Sala', contenuto: 'Esistono leggi che proteggono le persone LGBT, ma spesso non sono sufficientemente applicate.' },
              { idPost: 4, titolo: 'L\'inclusività nelle scuole', idAutore: 'Alessandra Gori', contenuto: 'Molte scuole italiane stanno implementando politiche più inclusive per gli studenti LGBT.' }
            ]
          },
          {
            idForum: 3,
            titolo: 'Forum sui Diritti LGBT, parte 2',
            descrizione: 'Uno spazio per discutere e sensibilizzare sui diritti delle persone LGBT.',
            paese: 'Italia',
            post: [
              { idPost: 3, titolo: 'Leggi per la protezione delle persone LGBT', idAutore: 'Marco Sala', contenuto: 'Esistono leggi che proteggono le persone LGBT, ma spesso non sono sufficientemente applicate.' },
              { idPost: 4, titolo: 'L\'inclusività nelle scuole', idAutore: 'Alessandra Gori', contenuto: 'Molte scuole italiane stanno implementando politiche più inclusive per gli studenti LGBT.' }
            ]
          }
        ]
      }).as('getForums');
  
      cy.visit('/generale-amministratore', {
        onBeforeLoad: (win) => {
          win.localStorage.setItem('auth_token', 'mocked-jwt-token');
        },
      });

      cy.contains('Gestione Forum').click();        
    });
  
    it('Dovrebbe visualizzare correttamente tutti i forum', () => {
      cy.wait('@getForums'); // Assicura che i forum siano stati caricati
  
      cy.get('.posts ul').should('be.visible');
      cy.get('.posts ul')
        .should('contain', 'Forum dei Diritti Umani')
        .should('contain', 'Forum sui Diritti LGBT')
        .should('contain', 'Forum sui Diritti LGBT, parte 2');
    });

    it('Dovrebbe visualizzare titolo e descrizione di ogni forum', () => {
      cy.wait('@getForums'); // Assicura che i forum siano stati caricati
      
      // Verifica che il primo forum contenga il titolo e la descrizione corretti
      cy.get('.posts ul li').first().find('.forum-details').should('contain.text', 'Forum dei Diritti Umani');
      cy.get('.posts ul li').first().find('.forum-details').should('contain.text', 'Un forum per discutere i diritti umani in vari paesi');
      
      // Verifica che il secondo forum contenga il titolo e la descrizione corretti
      cy.get('.posts ul li').eq(1).find('.forum-details').should('contain.text', 'Forum sui Diritti LGBT');
      cy.get('.posts ul li').eq(1).find('.forum-details').should('contain.text', 'Uno spazio per discutere e sensibilizzare sui diritti delle persone LGBT');
    });
});
