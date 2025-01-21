// TEST per la homepage
describe('Home Page', () => {
    // Test di base: Verifica che la pagina si carichi
    it('Dovrebbe caricare la homepage', () => {
      cy.visit('');
    });

    // Test: Verifica la presenza dei principali componenti della homepage
    it('Dovrebbe mostrare le componenti dell\'homepage', () => {
        cy.visit('');

        // Verifica il contenitore principale
        cy.get('.homepage-container').should('exist');

        // Verifica che l'header sia presente
        cy.get('app-header').should('exist');

        // Verifica che il footer sia presente
        cy.get('app-footer').should('exist');

        // Verifica che la mappa sia presente
        cy.get('app-map').should('exist');
    });
});

// TEST per la componente header
describe('Header Component', () => {
    // Test: Verifica che l'header sia presente
    it('Dovrebbe mostrare l\'header', () => {
        cy.visit('');
        cy.get('header.header-container').should('exist');
    });

    // Test: Verifica la barra di ricerca
    it('Dovrebbe mostrare la barra di ricerca', () => {
        cy.visit('/'); // Assicurati di essere nella homepage
        cy.get('.search-container').should('exist');
        cy.get('.search-container input[type="text"]')
            .should('have.attr', 'placeholder', 'Cerca un Paese')
            .and('be.visible');
    });

    it('Dovrebbe filtrare i Paesi', () => {
        cy.visit('/');
        const searchTerm = 'Ita';
        cy.get('.search-container input[type="text"]').type(searchTerm);
        cy.get('.search-container ul li').should('contain.text', 'Italia');
    });

    // Test: Verifica i pulsanti di autenticazione
// Test: Verifica che i pulsanti di autenticazione siano visibili per gli utenti non loggati
    it('Dovrebbe mostrare i bottoni "accedi" e "registrati"', () => {
        // Visita la pagina iniziale
        cy.visit('');
    
        // Verifica che i pulsanti "Accedi" e "Registrati" siano visibili e contengano i testi corretti
        cy.get('.right-container .auth-buttons > :nth-child(1)').should('contain.text', 'Accedi');
        cy.get('.right-container .auth-buttons > :nth-child(2)').should('contain.text', 'Registrati');
    });
  

    // Test: Verifica il menu hamburger da telefono
    it('Dovrebbe mostrare il menù hamburger su mobile', () => {
        cy.viewport(375, 667); // Imposta la risoluzione mobile (esempio per iPhone 6/7/8)
        cy.visit('');
        cy.get('.menu-icon-container').click({ force: true });
        cy.get('.menu-container').should('have.class', 'active');
        cy.get('.menu-icon-container').click({ force: true });
        cy.get('.menu-container').should('not.have.class', 'active');
    });

    // Test: Verifica comportamento per utenti non loggati
    // Test: Verifica comportamento per utenti non loggati
    it('Dovrebbe mostrare il tasto "accedi" nella barra laterale su mobile', () => {
        cy.viewport(375, 667); // Imposta la risoluzione mobile (esempio per iPhone 6/7/8)
        cy.visit('');

        // Simula un clic sull'icona del menu per dispositivi mobili
        cy.get('.menu-icon').click();

        // Verifica che il menu sia visibile
        cy.get('.menu-container').should('have.class', 'active');

        // Verifica che il pulsante "Accedi" sia visibile e contenga il testo corretto
        cy.get('.menu-container > .auth-buttons > :nth-child(1)')
            .should('exist') // Verifica che l'elemento esista
            .and('be.visible') // Verifica che sia visibile
            .and('contain.text', 'Accedi') // Verifica che contenga il testo "Accedi"
            .click(); // Simula il clic sul pulsante

        // Aggiungi ulteriori verifiche se necessario
        cy.url().should('include', 'loggato'); // Verifica che l'URL includa "loggato" dopo il clic
    });

    it('Dovrebbe mostrare il tasto "registrati" nella barra laterale su mobile', () => {
        cy.viewport(375, 667); // Imposta la risoluzione mobile (esempio per iPhone 6/7/8)
        cy.visit('');

        // Simula il clic sull'icona del menu
        cy.get('.menu-icon').click();

        // Verifica che il menu sia visibile
        cy.get('.menu-container').should('have.class', 'active');

        // Trova il pulsante "Registrati" e verifica il testo, visibilità e cliccabilità
        cy.get('.menu-container > .auth-buttons > :nth-child(2)')
            .should('exist') // Verifica che esista
            .and('be.visible') // Verifica che sia visibile
            .and('contain.text', 'Registrati') // Verifica il testo "Registrati"
            .click(); // Simula il clic

        // Verifica che l'URL cambi correttamente alla pagina di registrazione
        cy.url().should('include', '/registrato');
    });
});

// TEST per la componente Footer
describe('Footer Component', () => {
    // Test: Verifica che il footer sia presente
    it('Dovrebbe mostrare il footer', () => {
        cy.visit('');
        cy.get('app-footer').should('exist');
    });

    // Test: Verifica i link nel footer
    it('Dovrebbe mostrare i link nei footer', () => {
        cy.visit('');
        
        // Verifica la presenza del contenitore dei link
        cy.get('.footer-container').should('exist');
        cy.get('.footer-links').should('exist');

        // Verifica i singoli link
        cy.get('.footer-link').contains('Diversify').should('have.attr', 'href', '/');
        cy.get('.footer-link').contains('Privacy Policy').should('have.attr', 'href', 'https://www.iubenda.com/privacy-policy/47658031');
        cy.get('.footer-link').contains('Cookie Policy').should('have.attr', 'href','https://www.iubenda.com/privacy-policy/47658031/cookie-policy');
        cy.get('.footer-link').contains('Chi siamo').should('have.attr', 'href', '/chi-siamo');
    });

    // Test: Verifica che il link "Privacy Policy" carichi correttamente la pagina
    it('Dovrebbe mostrare il "privacy policy"', () => {
        cy.visit('');
        
        // Prevenire il reindirizzamento esterno e verificare il comportamento del link
        cy.get('.footer-link').contains('Privacy Policy').then(($link) => {
            const href = $link.prop('href');
            
            // Assert che l'URL contenga la parte giusta
            expect(href).to.include('https://www.iubenda.com/privacy-policy/47658031');
            
            // Simula il clic senza seguire il reindirizzamento
            cy.visit(href);
        });
    });

    // Test: Verifica che il link "Cookie Policy" carichi correttamente la pagina
    it('Dovrebbe mostrare la pagina dei "cookie policy"', () => {
        cy.visit('');
                // Prevenire il reindirizzamento esterno e verificare il comportamento del link
                cy.get('.footer-link').contains('Cookie Policy').then(($link) => {
                    const href = $link.prop('href');
                    
                    // Assert che l'URL contenga la parte giusta
                    expect(href).to.include('https://www.iubenda.com/privacy-policy/47658031/cookie-policy');
                    
                    // Simula il clic senza seguire il reindirizzamento
                    cy.visit(href);
                });
            });

    // Test: Verifica che il link "Chi siamo" carichi correttamente la pagina
    it('Dovrebbe mostrare la pagina "chi siamo?"', () => {
        cy.visit('');
        cy.get('.footer-link').contains('Chi siamo').click();
        cy.location('pathname').should('include', '/chi-siamo');
    });

});
