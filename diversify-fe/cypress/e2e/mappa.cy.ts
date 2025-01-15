describe('Testing interattivo della mappa', () => {
  beforeEach(() => {
    cy.intercept('GET', '/api/paesi', {
      statusCode: 200,
      body: [
        {
          idPaese: 1,
          nome: "Italia",
          forum: ["forum1", "forum2"],
          campagneCrowdfunding: ["campagna1, campagna2"],
          benchmark: [
            {
              idBenchmark: "idBenchmark1",
              tipo: "criticitaGenerale",
              gravita: "4",
              descrizione: "descrizione"
            },{
              idBenchmark: "idBenchmark2",
              tipo: "criticitaLgbt",
              gravita: "4",
              descrizione: "descrizione"
            },{
              idBenchmark: "idBenchmark3",
              tipo: "criticitaDisabilita",
              gravita: "4",
              descrizione: "descrizione"
            },{
              idBenchmark: "idBenchmark4",
              tipo: "criticitaRazzismo",
              gravita: "4",
              descrizione: "descrizione"
            },{
              idBenchmark: "idBenchmark5",
              tipo: "criticitaDonne",
              gravita: "4",
              descrizione: "descrizione"
            }
          ],
          linkImmagineBandiera: "https://upload.wikimedia.org/wikipedia/commons/thumb/0/03/Flag_of_Italy.svg/1280px-Flag_of_Italy.svg.png",
          documentiInformativi: [
            {
              idDocumentoInformativo: "idDocumento1",
              titolo: "titolo1",
              descrizione: "desc",
              contenuto: "cont",
              idPaese: "1",
              linkImmagine: "link",
              linkVideo: ""
            },
            {
              idDocumentoInformativo: "idDocumento2",
              titolo: "titolo2",
              descrizione: "desc",
              contenuto: "cont",
              idPaese: "1",
              linkImmagine: "link",
              linkVideo: ""
            },
          ]
        }
      ]
    }).as('getCountries');

    cy.intercept('GET', '/api/forums/by-paese/Italia', {
      statusCode: 200,
      body: [
        {
          idForum: 1,
          titolo: 'Forum dei Diritti Umani',
          descrizione: 'Un forum per discutere i diritti umani in vari paesi.',
          paese: 'Italia',
          post: [
            {
              idPost: 1,
              titolo: 'L\'importanza dei diritti civili',
              idAutore: 'Giuseppe Rossi',
              contenuto: 'Il diritto alla libertà di espressione è fondamentale in una società democratica.',
              dataCreazione: '2025-01-01T12:00:00Z',
              like: 5,
              commenti: [
                { idCommento: 1, autore: 'Maria Bianchi', contenuto: 'Sono d\'accordo, la libertà di espressione è essenziale.' },
                { idCommento: 2, autore: 'Luigi Verdi', contenuto: 'Purtroppo, non è sempre garantita in ogni parte del mondo.' }
              ],
              idForum: 1
            },
            {
              idPost: 2,
              titolo: 'I diritti delle minoranze',
              idAutore: 'Laura Neri',
              contenuto: 'Le minoranze etniche e religiose spesso sono vittime di discriminazioni.',
              dataCreazione: '2025-01-02T14:30:00Z',
              like: 8,
              commenti: [
                { idCommento: 3, autore: 'Carlo Galli', contenuto: 'È fondamentale proteggere le minoranze.' },
                { idCommento: 4, autore: 'Anna Russo', contenuto: 'Dobbiamo fare di più per sensibilizzare l\'opinione pubblica.' }
              ],
              idForum: 1
            }
          ]
        },
        {
          idForum: 2,
          titolo: 'Forum sui Diritti LGBT',
          descrizione: 'Uno spazio per discutere e sensibilizzare sui diritti delle persone LGBT.',
          paese: 'Italia',
          post: [
            {
              idPost: 3,
              titolo: 'Leggi per la protezione delle persone LGBT',
              idAutore: 'Marco Sala',
              contenuto: 'Esistono leggi che proteggono le persone LGBT, ma spesso non sono sufficientemente applicate.',
              dataCreazione: '2025-01-03T09:00:00Z',
              like: 12,
              commenti: [
                { idCommento: 5, autore: 'Giulia Conti', contenuto: 'Sì, l\'applicazione delle leggi è un problema serio.' },
                { idCommento: 6, autore: 'Francesco Rizzo', contenuto: 'La comunità deve fare sentire la propria voce per il cambiamento.' }
              ],
              idForum: 2
            },
            {
              idPost: 4,
              titolo: 'L\'inclusività nelle scuole',
              idAutore: 'Alessandra Gori',
              contenuto: 'Molte scuole italiane stanno implementando politiche più inclusive per gli studenti LGBT.',
              dataCreazione: '2025-01-04T16:45:00Z',
              like: 20,
              commenti: [
                { idCommento: 7, autore: 'Lucia Ferro', contenuto: 'Ottima iniziativa, ma c\'è ancora tanto lavoro da fare.' },
                { idCommento: 8, autore: 'Stefano Mancini', contenuto: 'L\'inclusività nelle scuole è fondamentale per una società migliore.' }
              ],
              idForum: 2
            }
          ]
        },
        {
          idForum: 3,
          titolo: 'Forum sui Diritti',
          descrizione: 'Uno spazio.',
          paese: 'Italia',
          post: []
        }
      ], // Dati mockati per i forum
    }).as('getForums');

    // Carica la pagina che contiene la mappa
    cy.visit('/'); // Sostituisci con il tuo URL
  });

    it('verifica che la mappa sia visibile', () => {
      // Verifica che la mappa sia visibile
      cy.get('#map').should('be.visible');
    });
  
    it.only('verifica che i pin vengano aggiunti sulla mappa', () => {
      // Attendere che la richiesta della mappa o dei pin venga completata (se applicabile)
      cy.wait(2000); // O un altro valore in base alla tua applicazione
      
      // Verifica che i pin siano visibili
      cy.get('img[src="pin-mappa.png"]').should('exist');
    });
    
  
    it('verifica che la selezione di una categoria cambi i pin', () => {
      // Cambia la categoria
      cy.get('#dropdown-container select').select('criticitaLgbt');
      
      // Verifica che i pin siano stati cambiati in base alla categoria selezionata
      cy.get('img[src^="benchmark/"]').should('have.length.greaterThan', 0);
  
      // Verifica che la categoria venga effettivamente cambiata
      cy.get('#dropdown-container select').should('have.value', 'criticitaLgbt');
    });
  
    it('verifica l\'apertura del popup quando un paese è cliccato', () => {
      // Simula un clic su un pin della mappa
      cy.get('img[src="pin-mappa.png"]').first().click();
      
      // Verifica che il popup venga aperto
      cy.get('.popup').should('be.visible');
    });
  
    it('verifica che il popup si chiuda cliccando all\'esterno', () => {
      // Clicca su un pin per aprire il popup
      cy.get('img[src="pin-mappa.png"]').first().click();
      
      // Verifica che il popup sia visibile
      cy.get('.popup').should('be.visible');
      
      // Clicca all'esterno del popup
      cy.get('body').click(0, 0);
      
      // Verifica che il popup sia chiuso
      cy.get('.popup').should('not.exist');
    });
  });
  