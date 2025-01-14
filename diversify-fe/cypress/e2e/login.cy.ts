describe('Modulo di Login', () => {
  beforeEach(() => {
    // Visita la pagina contenente il modulo di login
    cy.visit('/loggato'); // Modifica l'URL in base alla tua configurazione
  });

  it('dovrebbe mostrare un errore se l\'email è vuota', () => {
    cy.get('input#email').clear(); // Pulisce il campo email
    cy.get('button[type="submit"]').should('be.disabled'); // Verifica che il pulsante di invio sia disabilitato
    cy.get('button[type="submit"]').click({ force: true }); // Forza il clic sul pulsante se necessario (per testare)
    
    // Verifica che venga mostrato il messaggio di errore
    cy.get('.error-message')
      .contains("L'email è obbligatoria.")
      .should('be.visible');
  });
  

  it('dovrebbe mostrare un errore se l\'email non è valida', () => {
    cy.get('input#email').type('emailnonvalida');
    cy.get('button[type="submit"]').click({ force: true });

    cy.get('.error-message')
      .contains('Inserisci un\'email valida.')
      .should('be.visible');
  });

  it('dovrebbe mostrare un errore se la password è vuota', () => {
    cy.get('input#password').clear();
    cy.get('button[type="submit"]').click({ force: true });

    cy.get('.error-message')
      .contains('La password è obbligatoria.')
      .should('be.visible');
  });

  it('dovrebbe mostrare un errore se la password è troppo corta', () => {
    cy.get('input#password').type('breve');
    cy.get('button[type="submit"]').click({ force: true });

    cy.get('.error-message')
      .contains('La password deve essere lunga almeno 8 caratteri.')
      .should('be.visible');
  });

  it('dovrebbe mostrare un errore se la password non rispetta il pattern', () => {
    cy.get('input#password').type('password123');
    cy.get('button[type="submit"]').click({ force: true });

    cy.get('.error-message')
      .contains('La password deve contenere almeno una maiuscola, un numero e un carattere speciale.')
      .should('be.visible');
  });

  it('dovrebbe abilitare il pulsante di invio quando il modulo è valido', () => {
    cy.get('input#email').type('test@example.com');
    cy.get('input#password').type('Password1!');
    cy.get('button[type="submit"]').should('not.be.disabled');
  });

  it('dovrebbe navigare alla pagina di registrazione quando il link "Registrati" viene cliccato', () => {
    cy.get('a').contains('Registrati').click();
    cy.url().should('include', '/registrato');
  });

  it('dovrebbe navigare alla pagina di recupero password quando il link "Clicca qui per recuperare" viene cliccato', () => {
    cy.get('a').contains('Clicca qui per recuperare').click();
    cy.url().should('include', '/recupero-password'); // Modifica l'URL del recupero password se necessario
  });
});
