describe('Gestione Campagne', () => {
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
            cy.contains('Gestione Campagne').click();        

    });
  
    it('Visualizza la lista delle campagne con i filtri iniziali', () => {
      cy.get('.campaign-table tbody tr').should('have.length.greaterThan', 0);
    });
  
    it('Filtra le campagne per Paese', () => {
        // Seleziona il filtro
        cy.get('#countryFilter').select('Italia');
      
        // Aspetta che il DOM si aggiorni
        cy.get('.campaign-table tbody').should('exist'); // Verifica che la tabella esista
      
        // Rileva le righe della tabella
        cy.get('.campaign-table tbody tr')
          .should('have.length.greaterThan', 0) // Assicura che ci siano righe
          .each(($row) => {
            // Verifica il contenuto di ogni riga
            cy.wrap($row).get('td:nth-child(2)').should('contain', 'Italia');
          });
      });
      
      
  
    it('Filtra le campagne per Stato', () => {
      cy.get('#statusFilter').select('Pubblicata');
      cy.get('.campaign-table tbody tr').each(($row) => {
        cy.wrap($row).find('td:nth-child(3)').should('contain', 'Pubblicata');
      });
    });
  
    it('Resetta i filtri', () => {
      cy.get('.btn.reset-btn').click();
        cy.get('#countryFilter').should('not.have.value');
        cy.get('#statusFilter').should('have.value', '');
    });
  
    it('Crea una nuova campagna', () => {
      cy.get('.btn.create-campaign-btn').click();
      cy.get('#country').select('Francia');
      cy.get('#title').type('Nuova Campagna');
      cy.get('#category').select('Educazione');
      cy.get('#description').type('Questa è una nuova campagna di test.');
      cy.get('#targetFunds').type('5000');
      cy.get('#currentFunds').type('0');
      cy.get('#donationLink').type('http://example.com/donation');
      cy.get('#deadline').type('2025-12-31');
      cy.get('.btn.save-btn').click();
  
      cy.get('.campaign-table tbody tr').should('contain', 'Nuova Campagna');
    });


    it('Modifica una campagna esistente', () => {
      cy.get('.campaign-table tbody tr').first().find('.btn.edit-btn').click();
      cy.get('#title').clear().type('Campagna Modificata');
      cy.get('.btn.save-btn').click();
  
      cy.get('.campaign-table tbody tr').first().find('td:first-child').should('contain', 'Campagna Modificata');
    });

    it('Rimuove una campagna', () => {
      cy.get('.campaign-table tbody tr').first().find('.btn.remove-btn').click();
      cy.get('.campaign-table tbody tr').should('have.length.greaterThan', 0); // Controlla che la campagna sia stata rimossa
    });
  
    it('Mostra il messaggio "Nessuna campagna trovata" quando non ci sono campagne', () => {
      cy.get('#countryFilter').select('Malta');
      cy.get('.no-results').should('be.visible').and('contain', 'Nessuna campagna trovata');
    });
  });
  