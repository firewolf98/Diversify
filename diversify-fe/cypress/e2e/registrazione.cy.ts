describe('Test RegistrazioneFormComponent', () => {
    beforeEach(() => {
      cy.visit('/registrato'); // Cambia con il percorso corretto
    });
  
    it('Visualizza il modulo di registrazione', () => {
      cy.get('form').should('exist');
    });
  
    it('Compila il modulo di registrazione', () => {
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
      });
      
  
    it('Valida le password corrispondenti', () => {
      cy.get('input[id="password"]').type('Password1!');
      cy.get('input[id="confermaPassword"]').type('Password1!');
      cy.get('form').then($form => {
        expect($form[0].checkValidity()).to.be.true;
      });
    });
  
    it('Rifiuta password non corrispondenti', () => {
        // Inserisci la password principale
        cy.get('input[id="password"]').type('Password1!');
        
        // Inserisci una password diversa per conferma
        cy.get('input[id="confermaPassword"]').type('Password2!');
        
        // Simula il clic fuori dall'input di conferma password
        cy.get('input[id="confermaPassword"]').blur();
      
        // Verifica che il messaggio di errore sia visibile dopo il blur
        cy.get('.error-message').contains('Le password non corrispondono').should('be.visible');
      });      
  
      it.only('Dovrebbe mostrare un messaggio di errore se il codice fiscale esiste', () => {
        cy.intercept('POST', '/path/to/api/verificaCodiceFiscale', {
          statusCode: 200,
          body: { exists: true },  // Simula una risposta positiva per codice fiscale esistente
        }).as('verificaCodiceFiscale');
        
        cy.visit('/registrato');  // Visita la pagina
        cy.get('input[id="cf"]').type('RSSMRA85M01H501Z');  // Inserisci un codice fiscale già esistente
        cy.get('input[id="cf"]').blur();  // Simula il "clic fuori" dal campo
        
        cy.wait('@verificaCodiceFiscale');  // Aspetta la chiamata intercettata
      
        // Verifica che il messaggio di errore sia visibile
        cy.get('.error-message').should('be.visible'); 
        cy.get('.error-message').contains('Il codice fiscale è già presente nel sistema').should('be.visible');
      });
        
        it('Mostra errore per username esistente', () => {
        // Step 1: Inserisci un username già esistente
        cy.get('input[id="username"]').type('mario123');  // Username esistente
      
        // Step 2: Simula la perdita di focus (clic fuori dal campo)
        cy.get('input[id="username"]').blur();  // Il campo perde il focus
      
        // Step 3: Verifica che il messaggio di errore venga visualizzato
        cy.get('.error-message')  // Seleziona l'elemento che contiene gli errori
          .should('be.visible')  // Verifica che l'errore sia visibile
          .and('contain', 'Username già esistente');  // Verifica che il messaggio contenga il testo corretto
      });
      
  
      it('Invia correttamente il modulo', () => {
        cy.get('input[id="nome"]').type('Mario');
        cy.get('input[id="cognome"]').type('Rossi');
        cy.get('input[id="email"]').type('mario.rossi@example.com');
        cy.get('input[id="password"]').type('Password1!');
        cy.get('input[id="confermaPassword"]').type('Password1!');
        cy.get('input[id="cf"]').type('RSSMRA85M01H501Z');
        cy.get('input[id="username"]').type('nuovoutente');
        
        // Interazione con il select
        cy.get('select[id="domanda"]').select('Come si chiama tuo padre?');
        
        cy.get('input[id="risposta"]').type('Giovanni');
      });
      
  });
  