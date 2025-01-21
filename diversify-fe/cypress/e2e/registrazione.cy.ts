describe('Test RegistrazioneFormComponent', () => {
  beforeEach(() => {
    // Intercetta la richiesta POST per il login senza il token
    cy.intercept('POST', '/utenti/login', {
      statusCode: 200,
      body: {
        idUtente: '1',
        nome: 'nome',
        cognome: 'cognome',
        codiceFiscale: 'RSSMRA85M01H501Z',
        email: 'email@gmail.com',
        username: 'user',
        passwordHash: 'passworD1!',
        rispostaHash: 'cane',
        blacklistForum: [{ idForum: '1' }, { idForum: '2' }],
        ruolo: false,
        banned: false,
      },
    }).as('getUtente');

    // Visita la pagina di registrazione
    cy.visit('/registrato'); 
  });

  it('Visualizza il modulo di registrazione', () => {
    cy.get('form').should('exist');
  });

  it('Compila correttamente il modulo di registrazione', () => {
    cy.get('input[id="nome"]').type('Mario');
    cy.get('input[id="cognome"]').type('Rossi');
    cy.get('input[id="email"]').type('mario.rossi@example.com');
    cy.get('select[id="domanda"]').select('Come si chiama tuo padre?');
    cy.get('input[id="risposta"]').type('Luigi');
    cy.get('input[id="password"]').type('Password1!');
    cy.get('input[id="confermaPassword"]').type('Password1!');
    cy.get('input[id="cf"]').type('RSSMRA85M01H501Z');
    cy.get('input[id="username"]').type('mario123');

    // Invio del modulo
    cy.get('form').submit();
    cy.url().should('include', '/registrato');
  });

  it('Valida la corrispondenza delle password', () => {
    cy.get('input[id="password"]').type('Password1!');
    cy.get('input[id="confermaPassword"]').type('Password1!');
    cy.get('form').then($form => {
      expect($form[0].checkValidity()).to.be.true;
    });
  });

  it('Rifiuta password non corrispondenti', () => {
    cy.get('input[id="password"]').type('Password1!');
    cy.get('input[id="confermaPassword"]').type('Password2!');
    cy.get('input[id="confermaPassword"]').blur();
    
    cy.get('.error-message').contains('Le password non corrispondono').should('be.visible');
  });

  it('Verifica la validazione dell\'email', () => {
    // Inserisci un'email non valida
    cy.get('input[id="email"]').type('invalid-email');
  
    // Simula un clic fuori (blur) per attivare la validazione
    cy.get('input[id="email"]').blur();
  
    // Verifica che la validazione non passi
    cy.get('form').then($form => {
      expect($form[0].checkValidity()).to.be.false;
    });
  
    // Verifica che il messaggio di errore sia visibile
    cy.get('.error-message').contains('L\'email non è valida').should('be.visible');
  });
  

  it('Verifica la validazione del codice fiscale', () => {
    cy.get('input[id="cf"]').type('INVALIDCF');
  
    // Simula il "click fuori" per attivare la validazione
    cy.get('input[id="cf"]').blur();
  
    // Verifica che il tasto di invio sia disabilitato
    cy.get('button[type="submit"]').should('be.disabled');
  
    // Verifica che venga visualizzato un errore di validazione specifico per il codice fiscale
    cy.get('.error-message').contains('Il codice fiscale deve contenere esattamente 16 caratteri').should('be.visible');
  });

  it('Verifica che il pulsante di registrazione sia disabilitato con il modulo invalido', () => {
    cy.get('input[id="nome"]').type('Mario');
    cy.get('input[id="cognome"]').type('Rossi');
    cy.get('input[id="email"]').type('mario.rossi@example.com');
    cy.get('input[id="password"]').type('Password1!');
    cy.get('input[id="confermaPassword"]').type('Password1!');
    cy.get('input[id="cf"]').type('RSSMRA85M01H501Z');
    cy.get('input[id="username"]').type('mario123');
    cy.get('button[type="submit"]').should('be.disabled');
  });

  it('Controlla il funzionamento del link per il login', () => {
    cy.get('.login-link a').click();
    cy.url().should('include', '/loggato');
  });
});
