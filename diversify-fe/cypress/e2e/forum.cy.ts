describe('Componente Forum', () => {
  beforeEach(() => {
    // Intercetta la richiesta per ottenere i dati dell'utente (se necessario)
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
        ruolo: false,
        banned: false
      }
    }).as('getUtente');


    cy.intercept('GET', '/api/paesi', {
      statusCode: 200,
      body: [
        {
          idPaese: 1,
          nome: "Italia",
          forum: ["forum1", "forum2"],
          campagneCrowdfunding: ["campagna1, campagna2"],
          benchmark: [
            { idBenchmark: "idBenchmark1", tipo: "criticitaGenerale", gravita: "4", descrizione: "descrizione" },
            { idBenchmark: "idBenchmark2", tipo: "criticitaLgbt", gravita: "4", descrizione: "descrizione" },
            { idBenchmark: "idBenchmark3", tipo: "criticitaDisabilita", gravita: "4", descrizione: "descrizione" },
            { idBenchmark: "idBenchmark4", tipo: "criticitaRazzismo", gravita: "4", descrizione: "descrizione" },
            { idBenchmark: "idBenchmark5", tipo: "criticitaDonne", gravita: "4", descrizione: "descrizione" }
          ],
          linkImmagineBandiera: "https://upload.wikimedia.org/wikipedia/commons/thumb/0/03/Flag_of_Italy.svg/1280px-Flag_of_Italy.svg.png",
          documentiInformativi: [
            { idDocumentoInformativo: "idDocumento1", titolo: "titolo1", descrizione: "desc", contenuto: "cont", idPaese: "1", linkImmagine: "link", linkVideo: "" },
            { idDocumentoInformativo: "idDocumento2", titolo: "titolo2", descrizione: "desc", contenuto: "cont", idPaese: "1", linkImmagine: "link", linkVideo: "" }
          ]
        }
      ]
    }).as('getCountries');

    // Intercetta la chiamata API per ottenere i forum
    cy.intercept('GET', '/api/forums/by-paese/Italia', {
      statusCode: 200,
      body: [
        {
          idForum: 1,
          titolo: 'Forum dei Diritti Umani',
          descrizione: 'Un forum per discutere i diritti umani in vari paesi.',
          paese: 'Italia',
          post: [
            { idPost: 1, titolo: 'L\'importanza dei diritti civili', idAutore: 'Giuseppe Rossi', contenuto: 'Il diritto alla libertà di espressione è fondamentale in una società democratica.' },
            { idPost: 2, titolo: 'I diritti delle minoranze', idAutore: 'Laura Neri', contenuto: 'Le minoranze etniche e religiose spesso sono vittime di discriminazioni.' }
          ]
        },
        {
          idForum: 2,
          titolo: 'Forum sui Diritti LGBT',
          descrizione: 'Uno spazio per discutere e sensibilizzare sui diritti delle persone LGBT.',
          paese: 'Italia',
          post: [
            { idPost: 3, titolo: 'Leggi per la protezione delle persone LGBT', idAutore: 'Marco Sala', contenuto: 'Esistono leggi che proteggono le persone LGBT, ma spesso non sono sufficientemente applicate.' },
            { idPost: 4, titolo: 'L\'inclusività nelle scuole', idAutore: 'Alessandra Gori', contenuto: 'Molte scuole italiane stanno implementando politiche più inclusive per gli studenti LGBT.' }
          ]
        },{
          idForum: 3,
          titolo: 'Forum sui Diritti LGBT',
          descrizione: 'Uno spazio per discutere e sensibilizzare sui diritti delle persone LGBT.',
          paese: 'Italia',
          post: [
            { idPost: 3, titolo: 'Leggi per la protezione delle persone LGBT', idAutore: 'Marco Sala', contenuto: 'Esistono leggi che proteggono le persone LGBT, ma spesso non sono sufficientemente applicate.' },
            { idPost: 4, titolo: 'L\'inclusività nelle scuole', idAutore: 'Alessandra Gori', contenuto: 'Molte scuole italiane stanno implementando politiche più inclusive per gli studenti LGBT.' }
          ]
        }
      ]
    }).as('getForums');

    // Imposta il token mockato nel localStorage (simile al secondo esempio)
    cy.visit('/forum?country=Italia&forumId=1', {
      onBeforeLoad: (win) => {
        win.localStorage.setItem('auth_token', 'mocked-jwt-token');
      }
    });
  });

  it('Dovrebbe mostrare il titolo "Forum" nella sidebar', () => {
    cy.visit('forum?country=Italia&forumId=1');
    cy.get('.forum-sidebar h3').should('contain.text', 'Forum');
  });

  it('Dovrebbe mostrare il link "Torna alla mappa"', () => {
    cy.get('.forum-sidebar .back-link').should('contain.text', '← Torna alla mappa');
  });

  it('Dovrebbe generare dinamicamente i bottoni per ogni forum', () => {
    cy.get('.forum-sidebar ul li button', { timeout: 10000 })
      .should('have.length', 3)
      .each((button, index) => {
        const expectedTitles = ['Forum dei Diritti Umani', 'Forum sui Diritti LGBT', 'Forum sui Diritti'];
        cy.wrap(button).should('contain.text', expectedTitles[index]);
      });
  });

  it('Dovrebbe permettere di selezionare un forum', () => {
    cy.get('.forum-sidebar ul li button').first().click();
    cy.get('.forum-main h3').should('contain.text', 'Post del Forum');
  });

  it('Dovrebbe mostrare i post associati al forum selezionato', () => {
    cy.get('.forum-sidebar ul li button').first().click();
    cy.get('.forum-main .forum-post', { timeout: 10000 })
      .should('have.length', 2)
      .each((post) => {
        cy.wrap(post).find('a').should('not.be.empty');
        cy.wrap(post).find('p').should('contain.text', 'Postato da');
      });
  });

  it('Dovrebbe verificare la risposta dell\'API per i forum', () => {
    cy.request('GET', '/api/forums/by-paese/Italia')
      .then((response) => {
        expect(response.status).to.eq(200);
        expect(response.body).to.have.length.greaterThan(0);
      });
  });
});
