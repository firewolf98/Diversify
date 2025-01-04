describe('Segnalazioni Utenti', () => {
    beforeEach(() => {
        // Visita la pagina del generale amministratore
        cy.visit('/generale-amministratore');
        // Seleziona il componente "Segnalazione Paese"
        cy.contains('Segnalazioni Utenti').click();
      });
  
    it('dovrebbe mostrare la lista delle segnalazioni', () => {
      // Verifica che la lista delle segnalazioni sia visibile
      cy.get('.report-list')
        .should('be.visible')
        .and('contain', 'Lingaggio offensivo')  // Verifica se un motivo esiste nella lista
        .and('contain', 'Contenuto inappropriato');
    });
  
    it('dovrebbe aprire il popup di ban al clic sul pulsante "Ban"', () => {
      // Clicca sul pulsante "Ban" del primo report
      cy.get('.report-item').first().find('button').click();
  
      // Verifica che il popup di conferma sia visibile
      cy.get('.ban-popup-wrapper')
        .should('be.visible')
        .and('contain', 'Conferma Ban');
      
      // Verifica che il nome dell\'utente segnalato nel popup sia corretto
      cy.get('.ban-popup-wrapper')
        .should('contain', 'utenteSegnalato1');
    });
  
    it('dovrebbe bannare l\'utente e rimuovere la segnalazione dalla lista', () => {
      // Clicca sul pulsante "Ban" del primo report
      cy.get('.report-item').first().find('button').click();
  
      // Clicca sul pulsante di conferma del ban
      cy.get('.ban-popup-wrapper').find('button').contains('Conferma Ban').click();
  
      // Verifica che la lista delle segnalazioni contenga ora 3 elementi invece di 4
      cy.get('.report-list')
        .children()
        .should('have.length', 3);
  
      // Verifica che il report bannato non esista più
      cy.get('.report-list').should('not.contain', 'utenteSegnalato1');
    });
  
    it('dovrebbe annullare il ban quando clicco su Annulla', () => {
      // Clicca sul pulsante "Ban" del primo report
      cy.get('.report-item').first().find('button').click();
  
      // Clicca su "Annulla" nel popup
      cy.get('.ban-popup-wrapper').find('button').contains('Annulla').click();
  
      // Verifica che il popup sia chiuso e la lista rimanga invariata
      cy.get('.report-list').children().should('have.length', 4);
    });
  });
  