// TEST per la homepage
describe('Home Page, Icona Brigid e apertura chatbot', () => {
    // Test di base: Verifica che la pagina si carichi
    it('should load the home page', () => {
      cy.visit('');
    });

    // Test: Verifica la presenza dei principali componenti della homepage
    it('should display the homepage components', () => {
        cy.visit('');

        // Verifica il contenitore principale
        cy.get('.homepage-container').should('exist');

        // Verifica che l'header sia presente
        cy.get('app-header').should('exist');

        // Verifica che il footer sia presente
        cy.get('app-footer').should('exist');

        // Verifica che la mappa sia presente
        cy.get('app-map').should('exist');

        // Verifica che l'icona di Brigid sia presente
        cy.get('app-icona-brigid').should('exist');
        cy.get('app-icona-brigid .brigid-icon img')
            .should('have.attr', 'src', 'brigid.png') // Controlla l'attributo src
            .and('be.visible'); // Assicurati che sia visibile
        cy.contains('app-icona-brigid .chat-label', 'Chatta con Brigid');

        // Verifica che il pulsante del chatbot sia presente
        cy.get('app-apertura-chat-bot').should('exist');
    });

    // Test: Verifica l'interazione con l'icona di Brigid
    it('should allow clicking on the Brigid icon', () => {
        cy.visit('');

        // Simula un clic sull'icona di Brigid
        cy.get('app-icona-brigid .brigid-icon').click();
    
        // Simula un clic per chiudere la finestra della chatbot
        cy.get('app-icona-brigid .brigid-icon').click();

        // Verifica che la finestra della chatbot sia chiusa
        cy.get('.chat-window') // Sostituisci con il selettore corretto della finestra di chat
            .should('not.exist'); // Assicurati che non esista più
    });
});

// TEST per la componente header
describe('Header Component', () => {
    // Test: Verifica che l'header sia presente
    it('should display the header', () => {
        cy.visit('');
        cy.get('header.header-container').should('exist');
    });

    // Test: Verifica la barra di ricerca
    it('should display the search bar when on the home page', () => {
        cy.visit('/'); // Assicurati di essere nella homepage
        cy.get('.search-container').should('exist');
        cy.get('.search-container input[type="text"]')
            .should('have.attr', 'placeholder', 'Cerca un Paese')
            .and('be.visible');
    });

    it('should filter countries in the search dropdown', () => {
        cy.visit('/');
        const searchTerm = 'Ita';
        cy.get('.search-container input[type="text"]').type(searchTerm);
        cy.get('.search-container ul li').should('contain.text', 'Italia');
    });

    // Test: Verifica i pulsanti di autenticazione
    it('should display login and register buttons when not logged in', () => {
        cy.visit('');
        cy.window().then(win => {
            win.localStorage.setItem('isLoggedIn', 'false'); // Simula uno stato non loggato
        });
        cy.reload();
        cy.get('.right-container > :nth-child(1) > .auth-buttons > .auth-button').click();
            cy.get('.right-container > .auth-buttons > :nth-child(1)').contains('Accedi').should('exist');
            cy.get('.right-container > .auth-buttons > :nth-child(2)').contains('Registrati').should('exist');
    });

    it('should display profile icon and logout button when logged in', () => {
        cy.visit('');
        cy.window().then(win => {
            win.localStorage.setItem('isLoggedIn', 'true'); // Simula uno stato loggato
        });
        cy.reload();
            cy.get('.right-container > :nth-child(1) > .auth-buttons > .auth-button').contains('Logout').should('exist');
            cy.get('.profile-icon').should('exist').and('be.visible');
    });

    // Test: Verifica il menu hamburger
    it('should toggle the mobile menu when menu icon is clicked', () => {
        cy.viewport(375, 667); // Imposta la risoluzione mobile (esempio per iPhone 6/7/8)
        cy.visit('');
        cy.get('.menu-icon-container').click({ force: true });
        cy.get('.menu-container').should('have.class', 'active');
        cy.get('.menu-icon-container').click({ force: true });
        cy.get('.menu-container').should('not.have.class', 'active');
    });

    // Test: Verifica la navigazione tramite menu
    it('should navigate to the profile page when clicking on profile', () => {
        cy.viewport(375, 667); // Imposta la risoluzione mobile (esempio per iPhone 6/7/8)
        cy.visit('');
        cy.window().then(win => {
            win.localStorage.setItem('isLoggedIn', 'true'); // Simula uno stato loggato
        });
        cy.reload();

        // Simula il clic sull'icona del menu per aprirlo
        cy.get('.menu-icon-container').should('be.visible').click();
        
        // Ora il menu dovrebbe essere visibile, verifica che il link del profilo sia visibile
        cy.get('.menu-items > a').should('be.visible').click();
        
        // Verifica che la navigazione sia avvenuta correttamente
        cy.location('pathname').should('include', '/scheda-area-personale');
    });

    // Test: Verifica comportamento per utenti non loggati
    it('should navigate to login pages when buttons are clicked', () => {
        cy.viewport(375, 667); // Imposta la risoluzione mobile (esempio per iPhone 6/7/8)
        cy.visit('');
        cy.window().then(win => {
            win.localStorage.setItem('isLoggedIn', 'false'); // Imposta lo stato di login a "false" per simulare un utente non loggato
        });
        
    
    // Verifica che il pulsante "Accedi" sia presente nel DOM
    cy.get('.menu-icon').click();
    cy.get('.menu-items > .auth-buttons > .auth-button').click();
    cy.get('.menu-icon').click();
    cy.get('.menu-container > .auth-buttons > :nth-child(1)')
        .should('exist') // Verifica che l'elemento esista
        .and('be.visible'); // Verifica che sia visibile
    
    // Verifica che il pulsante "Accedi" contenga il testo corretto
    cy.get('.menu-container > .auth-buttons > :nth-child(1)')        
        .contains('Accedi') // Verifica che contenga "Accedi"
        .should('be.visible')
        .click(); // Simula il clic
});

    it('should navigate to register pages when buttons are clicked', () => {
        cy.viewport(375, 667); // Imposta la risoluzione mobile (esempio per iPhone 6/7/8)
        cy.visit('');
        cy.window().then(win => {
            win.localStorage.setItem('isLoggedIn', 'true'); // Imposta lo stato di login a "true" per simulare un utente non loggato
        });

        cy.get('.menu-icon').click();
        cy.get('.menu-items > .auth-buttons > .auth-button').click();
        cy.get('.menu-icon').click();
        cy.get('.menu-container > .auth-buttons > :nth-child(2)').contains('Registrati').should('be.visible').click();
        cy.url().should('include', '/registrato');
    });
});

// TEST per la componente Footer
describe('Footer Component', () => {
    // Test: Verifica che il footer sia presente
    it('should display the footer', () => {
        cy.visit('');
        cy.get('app-footer').should('exist');
    });

    // Test: Verifica i link nel footer
    it('should display the correct footer links', () => {
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
    it('should navigate to the privacy policy page', () => {
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
    it('should navigate to the cookie policy page', () => {
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
    it('should navigate to the "Chi siamo" page', () => {
        cy.visit('');
        cy.get('.footer-link').contains('Chi siamo').click();
        cy.location('pathname').should('include', '/chi-siamo');
    });

});
