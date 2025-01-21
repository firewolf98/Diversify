describe('Test Area Personale', () => {
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
    

    // Visita la pagina 'scheda-personale' e imposta il token JWT mockato nel localStorage
    cy.visit('/scheda-area-personale', {
      onBeforeLoad: (win) => {
        win.localStorage.setItem('auth_token', 'mocked-jwt-token');
      },
    });

    // Aspetta la richiesta per recuperare i dati dell'utente
    cy.wait('@getUtente');
  });

  it('Dovrebbe visualizzare correttamente i dettagli dell\'utente', () => {
    // Verifica che i dettagli dell'utente siano visibili
    cy.get('p').contains('Username: user');
    cy.get('p').contains('Nome: nome');
    cy.get('p').contains('Cognome: cognome');
    cy.get('p').contains('Email: email@gmail.com');
    cy.get('p').contains('Domanda personale: come si chiama tuo padre?');
  });

  it('Dovrebbe visualizzare i pulsanti di azione per la modifica password e l\'eliminazione account', () => {
    // Verifica la presenza dei pulsanti per la modifica password e l'eliminazione account
    cy.get('.form-actions').within(() => {
      cy.get('button').contains('Modifica Password').should('be.visible');
      cy.get('button').contains('Elimina Account').should('be.visible');
    });
  });

  describe('Modifica Password', () => {
    it('Dovrebbe visualizzare la sezione di modifica password quando clicco "Modifica Password"', () => {
      // Clicca sul pulsante per mostrare la sezione di modifica password
      cy.get('button').contains('Modifica Password').click();
      cy.get('.modifica-password').should('be.visible');
    });

    it('Dovrebbe nascondere e mostrare le password quando clicco sull\'icona di visibilità', () => {
      // Clicca sul pulsante "Modifica Password" per visualizzare la sezione
      cy.get('button').contains('Modifica Password').click();
    
      // Assicurati che il primo .password-container sia visibile
      cy.get('.password-container').first().should('be.visible').within(() => {
        // Verifica che le password siano inizialmente nascoste
        cy.get('input[id="vecchiaPassword"]').should('have.length', 1);
    
        // Clicca sull'icona per mostrare la password
        cy.get('.toggle-password').first().click();
        cy.get('input[type="text"]').should('have.length', 1);
    
        // Clicca di nuovo per nascondere la password
        cy.get('.toggle-password').first().click();
        cy.get('input[id="vecchiaPassword"]').should('have.length', 1);
      });
    });
    
    
  });

  describe('Eliminazione Account', () => {
    it('Dovrebbe visualizzare la sezione di eliminazione account quando clicco "Elimina Account"', () => {
      // Clicca sul pulsante per mostrare la sezione di eliminazione account
      cy.get('button').contains('Elimina Account').click();
      cy.get('.elimina-account').should('be.visible');
    });

    it('Dovrebbe visualizzare gli errori di validazione nella password per l\'eliminazione', () => {
      // Mostra la sezione di eliminazione account
      cy.get('button').contains('Elimina Account').click();
    
      // Lascia il campo password vuoto
      cy.get('.elimina-account input[type="password"]').focus();
    
      // Clicca fuori dal form per attivare la validazione
      cy.get('body').click(); // Simula un clic sul body o in un'altra parte della pagina
    
      // Verifica che vengano visualizzati gli errori di validazione
      cy.get('.error-message').should('contain', 'La password è obbligatoria');
    });    

    it('Dovrebbe disabilitare il pulsante di eliminazione account se il form è invalido', () => {
      // Mostra la sezione di eliminazione account
      cy.get('button').contains('Elimina Account').click();
    
      // Individua il campo password e puliscilo
      cy.get('#passwordEliminazione').clear();
    
      // Simula la perdita di focus per attivare eventuali validazioni
      cy.get('body').click();
    
      // Verifica che il pulsante sia disabilitato
      cy.get('button.btn-danger').contains('Elimina')
      .should('be.visible') // Assicura che il pulsante sia visibile
      .and('not.have.css', 'pointer-events', 'none'); // Assicura che non sia disabilitato da CSS
    });
    
  });
});
