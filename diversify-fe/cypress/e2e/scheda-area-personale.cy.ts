// cypress/e2e/scheda-area-personale.spec.ts

describe('Area Personale', () => {
    beforeEach(() => {
      // Visita la pagina dell'area personale
      cy.visit('/scheda-area-personale');
    });
  
    it('Dovrebbe visualizzare correttamente i dettagli dell\'utente', () => {
      // Verifica che i dettagli dell'utente siano visibili
      cy.get('.scheda-container').should('be.visible');
      cy.contains('h2', 'Area Personale').should('be.visible');
      cy.contains('p', 'Username: utente123').should('be.visible');
      cy.contains('p', 'Nome: Mario').should('be.visible');
      cy.contains('p', 'Cognome: Rossi').should('be.visible');
      cy.contains('p', 'Email: mario.rossi@example.com').should('be.visible');
      cy.contains('p', 'Domanda personale: Nome del tuo primo animale?').should('be.visible');
    });
  
    it('Dovrebbe aprire e chiudere il form di modifica password', () => {
      // Verifica che il pulsante "Modifica Password" sia visibile e cliccabile
      cy.contains('button', 'Modifica Password').should('be.visible').click();
  
      // Verifica che il form di modifica password sia visibile
      cy.get('.modifica-password').should('be.visible');
  
      // Verifica che i campi del form siano presenti
      cy.get('input[id="vecchiaPassword"]').should('be.visible');
      cy.get('input[id="password"]').should('be.visible');
      cy.get('input[id="confermaPassword"]').should('be.visible');
  
      // Chiudi il form di modifica password
      cy.contains('button', 'Modifica Password').click();
    });
  
    it('Dovrebbe aprire e chiudere il form di eliminazione account', () => {
      // Verifica che il pulsante "Elimina Account" sia visibile e cliccabile
      cy.contains('button', 'Elimina Account').should('be.visible').click();
  
      // Verifica che il form di eliminazione account sia visibile
      cy.get('.elimina-account').should('be.visible');
  
      // Verifica che il campo della password sia presente
      cy.get('input[id="passwordEliminazione"]').should('be.visible');
  
      // Chiudi il form di eliminazione account
      cy.contains('button', 'Annulla').click();
    });
  
    it('Dovrebbe mostrare/nascondere la password nel form di modifica password', () => {
      // Apri il form di modifica password
      cy.contains('button', 'Modifica Password').click();
  
      // Verifica che la password sia nascosta di default
      cy.get('input[id="vecchiaPassword"]').should('have.attr', 'type', 'password');
  
      // Clicca sul pulsante per mostrare la password
      cy.get('.password-container button.toggle-password').first().click();
  
      // Verifica che la password sia visibile
      cy.get('input[id="vecchiaPassword"]').should('have.attr', 'type', 'text');
  
      // Clicca di nuovo per nascondere la password
      cy.get('.password-container button.toggle-password').first().click();
      cy.get('input[id="vecchiaPassword"]').should('have.attr', 'type', 'password');
    });
  
    it('Dovrebbe mostrare un errore se le password non coincidono nel form di modifica password', () => {
      // Apri il form di modifica password
      cy.contains('button', 'Modifica Password').click();
  
      // Inserisci una nuova password e una conferma password diversa
      cy.get('input[id="password"]').type('NuovaPassword123!');
      cy.get('input[id="confermaPassword"]').type('PasswordDiversa123!');
  
      // Verifica che venga mostrato un messaggio di errore
      cy.contains('Le password non corrispondono').should('be.visible');
    });
  
    it('Dovrebbe disabilitare il pulsante di salvataggio se il form di modifica password è invalido', () => {
      // Apri il form di modifica password
      cy.contains('button', 'Modifica Password').click();
  
      // Verifica che il pulsante di salvataggio sia disabilitato inizialmente
      cy.contains('button', 'Salva').should('be.disabled');
  
      // Inserisci una vecchia password valida
      cy.get('input[id="vecchiaPassword"]').type('VecchiaPassword123!');
  
      // Inserisci una nuova password valida
      cy.get('input[id="password"]').type('NuovaPassword123!');
  
      // Inserisci una conferma password valida
      cy.get('input[id="confermaPassword"]').type('NuovaPassword123!');
  
      // Verifica che il pulsante di salvataggio sia abilitato
      cy.contains('button', 'Salva').should('not.be.disabled');
    });
  });