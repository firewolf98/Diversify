describe('Test del componente Post e Commenti', () => {
  beforeEach(() => {
    // Intercetta la richiesta per ottenere i dati dell'utente (se necessario)
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
        tipoDomanda: "come si chiama tuo padre?", // Modifica qui il campo
        rispostaHash: "giuseppe",
        blacklistForum: [
          { idForum: "1" },
          { idForum: "2" }
        ],
        ruolo: false,
        banned: false
      }
    }).as('getUtente');
    
    // Intercetta la chiamata API per ottenere i forum
    cy.intercept('GET', '/api/forums/by-paese/Italia', {
      statusCode: 200,
      body: [
        {
          idForum: 1,
          titolo: 'Forum dei Diritti Umani',
          descrizione: 'Un forum per discutere i diritti umani in vari paesi.',
          paese: 'Italia',
          post: [
            { idPost: 1, titolo: 'La Diversità di Genere', idAutore: 'Lucia', contenuto: 'Il diritto alla libertà di espressione è fondamentale in una società democratica.' },
            { idPost: 2, titolo: 'I diritti delle minoranze', idAutore: 'Laura Neri', contenuto: 'Le minoranze etniche e religiose spesso sono vittime di discriminazioni.' }
          ]
        }
      ]
  }).as('getForums');
    

  // Visita la pagina 'post' e imposta il token JWT mockato nel localStorage
  cy.visit('/post/1/1', {
      onBeforeLoad: (win) => {
        win.localStorage.setItem('auth_token', 'mocked-jwt-token');
      },
    });
  });
  
    it('Verifica la presenza degli elementi principali del post', () => {
      cy.get('.background-container').should('exist');
      cy.get('.post-title').should('contain', 'La Diversità di Genere');
      cy.get('.back-link').should('contain', '← Torna alla mappa');
      cy.get('.post-author-name').should('contain', 'Lucia');
    });
  
    it('Interagisce con il pulsante "Mi Piace"', () => {
        cy.get('.post-footer .like-button')
          .should('contain', '👍 Mi Piace')
          .click()
          .should('contain', '👎 Togli Mi Piace');
      });
      
  
    it('Aggiunge un commento', () => {
      const commento = 'Questo è un nuovo commento di test!';
  
      cy.get('.comment-textarea').type(commento);
      cy.get('.comment-button').click();
  
      // Verifica che il nuovo commento sia visibile nella lista dei commenti
      cy.get('app-commento').should('contain', commento);
    });
  
    it('Apre e chiude la modale di segnalazione', () => {
      cy.get('.flag-button').click();
  
      // Verifica che la modale sia visibile
      cy.get('.report-modal').should('be.visible');
  
      cy.get('.cancel-button').click();
  
      // Verifica che la modale non sia più visibile
      cy.get('.report-modal').should('not.exist');
    });
  
    it('Compila e conferma la segnalazione', () => {
      const motivoSegnalazione = 'Contenuto inappropriato';
  
      cy.get('.flag-button').click();
      cy.get('.report-textarea').type(motivoSegnalazione);
      cy.get('.confirm-button').click();
  
      // Verifica che la modale si chiuda dopo la conferma
      cy.get('.report-modal').should('not.exist');
    });
  
    it('Naviga indietro alla mappa', () => {
      cy.get('.back-link').click();
  
      // Verifica che la navigazione sia avvenuta
      // Aggiorna con l'assert corretto per la tua applicazione (ad esempio, verifica il path)
      cy.url().should('include', '/'); // Cambia con il path corretto della mappa
    });
  });
  