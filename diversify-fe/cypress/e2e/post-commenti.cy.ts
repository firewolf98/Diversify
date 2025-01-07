describe('Test del componente Post e Commenti', () => {
    beforeEach(() => {
      // Naviga alla pagina del post prima di ogni test
      cy.visit('/post'); // Cambia con il path corretto della tua applicazione
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
  