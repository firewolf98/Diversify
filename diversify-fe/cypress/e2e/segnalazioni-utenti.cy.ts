describe('Segnalazioni Utenti', () => {
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

    // Mock delle segnalazioni
    cy.intercept('GET', '/segnalazioni/getAllSegnalazioni', {
      statusCode: 200,
    }).as('getSegnalazioni');

    // Visita la pagina dell'area personale
    cy.visit('/generale-amministratore', {
      onBeforeLoad: (win) => {
        win.localStorage.setItem('auth_token', 'mocked-jwt-token');
      },
    });

    // Assicurati che l'elemento "Segnalazione Utenti" sia visibile e cliccabile
    cy.contains('Segnalazioni Utenti', { timeout: 10000 }).should('be.visible').click();
  });

  it('dovrebbe mostrare la lista delle segnalazioni', () => {
    cy.get('.report-list', { timeout: 10000 }).should('be.visible')
      .and('contain', 'Lingaggio offensivo')
  });

  it('dovrebbe aprire il popup di ban al clic sul pulsante "Ban"', () => {
    // Clicca sul pulsante "Ban" della prima segnalazione
    cy.get('.report-item').first().find('button').click();

    // Verifica che il popup di ban sia visibile
    cy.get('.ban-popup-wrapper', { timeout: 10000 }).should('be.visible')
      .and('contain', 'Conferma Ban')
      .and('contain', 'utenteSegnalato1'); // Modifica questo con il dato corretto (può essere il nome utente)
  });

  it('dovrebbe bannare l\'utente e rimuovere la segnalazione dalla lista', () => {
    cy.get('.report-item').first().find('button').click();

    // Clicca sul pulsante di conferma del ban
    cy.get('.ban-popup-wrapper').find('button').contains('Conferma Ban').click();

    // Verifica che la lista delle segnalazioni ora contenga 1 elemento (dato che 1 segnalazione è stata rimossa)
    cy.get('.report-list').children().should('have.length', 3);

    // Verifica che la segnalazione bannata non sia più presente
    cy.get('.report-list').should('not.contain', 'utenteSegnalato7');
  });

  it('dovrebbe annullare il ban quando clicco su Annulla', () => {
    cy.get('.report-item').first().find('button').click();

    // Clicca su "Annulla" nel popup
    cy.get('.ban-popup-wrapper').find('button').contains('Annulla').click();

    // Verifica che il popup sia chiuso e che la lista rimanga invariata (2 segnalazioni)
    cy.get('.report-list').children().should('have.length', 4);
  });
});
