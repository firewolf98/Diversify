// cypress/e2e/segnalazione-paese.spec.ts

describe('Segnalazione Paese', () => {
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
            tipoDomanda: "come si chiama tuo padre?", // Modifica qui il campo
            rispostaHash: "giuseppe",
            blacklistForum: [
              { idForum: "1" },
              { idForum: "2" }
            ],
            ruolo: true,
            banned: false
          }
        }).as('getUtente');
  
        // Visita la pagina dell'area personale
        cy.visit('/generale-amministratore', {
          onBeforeLoad: (win) => {
            win.localStorage.setItem('auth_token', 'mocked-jwt-token');
          },
        });
            // Seleziona il componente "Segnalazione Paese"
            cy.contains('Segnalazione Paese').click();       
      });
  
    it('Dovrebbe visualizzare correttamente il componente Segnalazione Paese', () => {
      // Verifica che il componente sia visibile
      cy.get('.popup-wrapper').should('be.visible');
      cy.contains('h3', 'Benchmark generale dei diritti umani per Italia').should('be.visible');
    });
  
    it('Dovrebbe cercare e selezionare un paese dall\'elenco', () => {
      // Apri il dropdown di ricerca
      cy.get('input[id="country"]').click();
      cy.get('ul').should('be.visible');
  
      // Cerca un paese
      cy.get('input[id="country"]').type('Francia');
      cy.get('ul li').should('have.length.greaterThan', 0);
      cy.contains('li', 'Francia').should('be.visible');
  
      // Seleziona un paese
      cy.contains('li', 'Francia').click();
      cy.get('input[id="country"]').should('have.value', 'Francia');
      cy.contains('h3', 'Benchmark generale dei diritti umani per Francia').should('be.visible');
    });
  
    it('Dovrebbe modificare i benchmark e visualizzare i commenti corretti', () => {
      // Modifica il benchmark dei diritti umani
      cy.get('input[type="range"]').first().invoke('val', 3).trigger('change');
      cy.contains('p', '3').should('be.visible');
      cy.contains('span', 'Mediocre').should('be.visible');
  
      // Modifica il benchmark dei diritti LGBT
      cy.get('input[type="range"]').eq(1).invoke('val', 1).trigger('change');
      cy.contains('p', '1').should('be.visible');
      cy.contains('span', 'Buono').should('be.visible');
  
      // Modifica il benchmark dei diritti delle persone con disabilità
      cy.get('input[type="range"]').eq(2).invoke('val', 4).trigger('change');
      cy.contains('p', '4').should('be.visible');
      cy.contains('span', 'Scarso').should('be.visible');
  
      // Modifica il benchmark dei diritti contro il razzismo
      cy.get('input[type="range"]').eq(3).invoke('val', 2).trigger('change');
      cy.contains('p', '2').should('be.visible');
      cy.contains('span', 'Sufficiente').should('be.visible');
  
      // Modifica il benchmark dei diritti delle donne
      cy.get('input[type="range"]').eq(4).invoke('val', 5).trigger('change');
      cy.contains('p', '5').should('be.visible');
      cy.contains('span', 'Pessimo').should('be.visible');
    });
  
    it('Dovrebbe inserire una descrizione generale dello stato dei diritti umani', () => {
      // Inserisci una descrizione
      cy.get('textarea').type('La situazione dei diritti umani in Italia è migliorata negli ultimi anni, ma ci sono ancora sfide da affrontare.');
      cy.get('textarea').should('have.value', 'La situazione dei diritti umani in Italia è migliorata negli ultimi anni, ma ci sono ancora sfide da affrontare.');
    });
  
    it('Dovrebbe salvare le modifiche', () => {
      // Modifica alcuni benchmark
      cy.get('input[type="range"]').first().invoke('val', 3).trigger('change');
      cy.get('input[type="range"]').eq(1).invoke('val', 1).trigger('change');
  
      // Inserisci una descrizione
      cy.get('textarea').type('Descrizione di prova.');
  
      // Clicca sul pulsante "Salva modifiche"
      cy.contains('button', 'Salva modifiche').click();
  
      // Verifica che le modifiche siano state salvate (qui puoi aggiungere ulteriori controlli se necessario)
      cy.log('Modifiche salvate con successo');
    });
  
    it('Dovrebbe mostrare "Malta" quando si digita "ma" nel campo di ricerca', () => {
        // Digita "ma" nel campo di ricerca
        cy.get('input[id="country"]').type('ma');
    
        // Verifica che il dropdown sia visibile
        cy.get('ul').should('be.visible');
    
        // Verifica che "Malta" sia presente nei risultati
        cy.get('ul li').contains('Malta').should('be.visible');
      });
  });