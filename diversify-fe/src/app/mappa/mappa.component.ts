import { Component, OnInit, AfterViewInit } from '@angular/core';
import * as ol from 'ol';
import { OSM } from 'ol/source';
import { View } from 'ol';
import { Tile as TileLayer } from 'ol/layer';
import Map from 'ol/Map';
import { Overlay } from 'ol';
//import per i pin:
import { Feature } from 'ol';
import { Point } from 'ol/geom';
import { Style, Icon } from 'ol/style';
import VectorSource from 'ol/source/Vector';
import VectorLayer from 'ol/layer/Vector';
//import per pin dinamici:
import { ComponentFactoryResolver, ViewContainerRef, ComponentRef } from '@angular/core';
import { PopupGridComponent } from '../popup-grid/popup-grid.component';

@Component({
  selector: 'app-map',
  standalone: true,
  templateUrl: './mappa.component.html',
  styleUrls: ['./mappa.component.css']
})
export class MapComponent implements OnInit, AfterViewInit {
  private map: Map | undefined;

  constructor(private viewContainerRef: ViewContainerRef, 
              private componentFactoryResolver: ComponentFactoryResolver) {}

  ngOnInit(): void {}

  ngAfterViewInit(): void {
    this.initializeMap();
  }

  private initializeMap(): void {
    const osmLayer = new TileLayer({
      source: new OSM()
    });

    const view = new View({
      center: [0, 0],
      zoom: 2
    });

    this.map = new Map({
      target: 'map',
      layers: [osmLayer],
      view: view
    });

    this.map.on('click', (event: any) => {
      const coordinate = event.coordinate;
      const clickedFeature = this.map?.forEachFeatureAtPixel(event.pixel, (feature) => feature);

      if (clickedFeature) {
        const countryName = clickedFeature.get('name');
        this.openPopupGrid(countryName, coordinate);
      }
    });

    // Definisci i dati dei paesi
    const countries = [
      { name: "Italia", coordinates: [1398226.38, 5161471.19], criticita: 1, tipoCriticita: "diritti delle donne"},     		  	    // Roma
      { name: "Francia", coordinates: [261800, 6270564], criticita: 1, tipoCriticita: "diritti contro il razzismo"},            			    // Parigi
      { name: "Germania", coordinates: [1491538.66, 6893050.21], criticita: 0 },    			    // Berlino
      { name: "Spagna", coordinates: [-412323.77, 4926736.21], criticita: 0 },      			    // Madrid
      { name: "Portogallo", coordinates: [-1017076.81, 4679891.74], criticita: 0 }, 			    // Lisbona
      { name: "Regno Unito", coordinates: [-14243.57, 6711459.82], criticita: 0 },  			    // Londra
      { name: "Irlanda", coordinates: [-696920.12, 7047883.07], criticita: 0 },     			    // Dublino
      { name: "Belgio", coordinates: [484416.76, 6594201.06], criticita: 0 },       			    // Bruxelles
      { name: "Paesi Bassi", coordinates: [544603.57, 6867871.26], criticita: 0 },  			    // Amsterdam
      { name: "Svizzera", coordinates: [829527.62, 5933730.25], criticita: 0 },     			    // Berna
      { name: "Austria", coordinates: [1822580.10, 6141580.50], criticita: 0 },     			    // Vienna
      { name: "Polonia", coordinates: [2338451.85, 6842165.97], criticita: 4, tipoCriticita: "diritti contro il razzismo"},     			    // Varsavia
      { name: "Svezia", coordinates: [2011302.35, 8251037.95], criticita: 0 },      			    // Stoccolma
      { name: "Norvegia", coordinates: [1195453.01, 8380446.92], criticita: 0 },    			    // Oslo
      { name: "Finlandia", coordinates: [2776567.57, 8437079.46], criticita: 0 },    			    // Helsinki
      { name: "Danimarca", coordinates: [1399266.15, 7496299.91], criticita: 0 },   			    // Copenaghen
      { name: "Estonia", coordinates: [2754719.61, 8275405.92], criticita: 2 },      		      // Tallinn
      { name: "Lettonia", coordinates:  [2683346.93, 7749797.64], criticita: 2 },     		    // Riga
      { name: "Lituania", coordinates: [2814477.07, 7301414.56], criticita: 2 },     			    // Vilnius
      { name: "Lussemburgo", coordinates: [682345.66, 6379214.54], criticita: 2 },  			    // Lussemburgo
      { name: "Malta", coordinates: [1615635.30, 4286733.78], criticita: 0 },       			    // La Valletta
      { name: "Slovacchia", coordinates: [1904515.60, 6132156.30], criticita: 0 },  			    // Bratislava
      { name: "Slovenia", coordinates: [1614909.70, 5788362.07], criticita: 0 },    			    // Lubiana
      { name: "Croazia", coordinates: [1778627.45, 5750367.74], criticita: 0 },     		      // Zagabria
      { name: "Serbia", coordinates: [2277379.96, 5592941.10], criticita: 0 },     		        // Belgrado
      { name: "Bosnia ed Erzegovina", coordinates: [2046799.05, 5442545.32], criticita: 0 },  // Sarajevo
      { name: "Montenegro", coordinates: [2144201.41, 5227326.33], criticita: 0 },  			    // Podgorica
      { name: "Albania", coordinates: [2206125.23, 5060868.63], criticita: 3 },     			    // Tirana
      { name: "Macedonia del Nord", coordinates: [2385789.09, 5160354.53], criticita: 4, tipoCriticita: "diritti contro il razzismo" }, 	  // Skopje
      { name: "Kosovo", coordinates: [2355936.19, 5260961.26], criticita: 0 },      			    // Pristina
      { name: "Bulgaria", coordinates:  [2595413.16, 5265924.33], criticita: 2 },     	    	// Sofia
      { name: "Romania", coordinates: [2905627.16, 5533187.12], criticita: 5, tipoCriticita: "diritti LGBT"},    			      // Bucarest
      { name: "Ungheria", coordinates: [2119708.13, 6023674.28], criticita: 0 },    			    // Budapest
      { name: "Moldavia", coordinates: [3209372.07, 5945988.65], criticita: 0 },    			    // Chisinău
      { name: "Cipro", coordinates:  [3714061.19, 4187224.21], criticita: 3, tipoCriticita: "diritti per le persone con disabilità" },       			    // Nicosia
      { name: "Turchia", coordinates: [3657294.42, 4854444.62], criticita: 0 },     			    // Ankara
      { name: "Ucraina", coordinates: [3397758.97, 6524454.58], criticita: 0 }, 				      // Kiev
      { name: "Grecia", coordinates: [2642137.67, 4576076.42], criticita: 1, tipoCriticita: "diritti per le persone con disabilità" }, 				        // Atena
      { name: "Russia", coordinates: [4187505.32, 7509131.29], criticita: 5, tipoCriticita: "diritti LGBT" }, 				        // Mosca
      { name: "Bielorussia", coordinates: [3068143.88, 7151813.43], criticita: 0 }, 		    	// Minsk
      { name: "Repubblica Ceca", coordinates: [204005231.5, 645183456], criticita: 0}         // Praga NON FUNZIONA
    ];

    // Crea le feature dei marker, includendo un secondo pin per i paesi con criticita 2
    const features = countries.map(country => {
      const feature = new Feature({
        geometry: new Point(country.coordinates),
        name: country.name
      });

      // Aggiungi uno stile per i marker
      feature.setStyle(new Style({
        image: new Icon({
          src: "pin-mappa.png", // Cambia con il percorso della tua icona
          scale: 0.3 // Regola la dimensione
        })
      }));

      //BENCHMARK
      // Aggiungi il secondo pin per i paesi con criticita 1
      if (country.criticita === 1) {
        const secondFeature = new Feature({
          geometry: new Point([
            country.coordinates[0] + 50000, // Sposta leggermente la coordinata
            country.coordinates[1] + 50000  // Sposta leggermente la coordinata
          ]),
          name: `${country.name}`
        });
        
        // Aggiungi uno stile diverso per il secondo pin, se desiderato
        secondFeature.setStyle(new Style({
          image: new Icon({
            src: "benchmark/benchmark-verde.png", // Nuovo percorso per l'icona del secondo pin
            scale: 0.3
          })
        }));

        return [feature, secondFeature]; // Restituisci entrambe le feature
      }
      // Aggiungi il secondo pin per i paesi con criticita 2
      if (country.criticita === 2) {
        const secondFeature = new Feature({
          geometry: new Point([
            country.coordinates[0] + 50000, // Sposta leggermente la coordinata
            country.coordinates[1] + 50000  // Sposta leggermente la coordinata
          ]),
          name: `${country.name}`
        });

        // Aggiungi uno stile diverso per il secondo pin, se desiderato
        secondFeature.setStyle(new Style({
          image: new Icon({
            src: "benchmark/benchmark-giallo.png", // Nuovo percorso per l'icona del secondo pin
            scale: 0.3
          })
        }));

        return [feature, secondFeature]; // Restituisci entrambe le feature
      }
      // Aggiungi il secondo pin per i paesi con criticita 3
      if (country.criticita === 3) {
        const secondFeature = new Feature({
          geometry: new Point([
            country.coordinates[0] + 50000, // Sposta leggermente la coordinata
            country.coordinates[1] + 50000  // Sposta leggermente la coordinata
          ]),
          name: `${country.name}`
        });

        // Aggiungi uno stile diverso per il secondo pin, se desiderato
        secondFeature.setStyle(new Style({
          image: new Icon({
            src: "benchmark/benchmark-arancione.png", // Nuovo percorso per l'icona del secondo pin
            scale: 0.3
          })
        }));

        return [feature, secondFeature]; // Restituisci entrambe le feature
      }
      // Aggiungi il secondo pin per i paesi con criticita 4
      if (country.criticita === 4) {
        const secondFeature = new Feature({
          geometry: new Point([
            country.coordinates[0] + 50000, // Sposta leggermente la coordinata
            country.coordinates[1] + 50000  // Sposta leggermente la coordinata
          ]),
          name: `${country.name}`
        });

        // Aggiungi uno stile diverso per il secondo pin, se desiderato
        secondFeature.setStyle(new Style({
          image: new Icon({
            src: "benchmark/benchmark-rosso.png", // Nuovo percorso per l'icona del secondo pin
            scale: 0.3
          })
        }));

        return [feature, secondFeature]; // Restituisci entrambe le feature
      }
      // Aggiungi il secondo pin per i paesi con criticita 5
      if (country.criticita === 5) {
        const secondFeature = new Feature({
          geometry: new Point([
            country.coordinates[0] + 50000, // Sposta leggermente la coordinata
            country.coordinates[1] + 50000  // Sposta leggermente la coordinata
          ]),
          name: `${country.name}`
        });

        // Aggiungi uno stile diverso per il secondo pin, se desiderato
        secondFeature.setStyle(new Style({
          image: new Icon({
            src: "benchmark/benchmark-nero.png", // Nuovo percorso per l'icona del secondo pin
            scale: 0.3
          })
        }));

        return [feature, secondFeature]; // Restituisci entrambe le feature
      }

      return feature; // Solo il pin principale
    }).flat(); // Appiattisce l'array di features per aggiungere i pin secondari

    // Crea il layer per i marker
    const vectorSource = new VectorSource({ features });
    const vectorLayer = new VectorLayer({ source: vectorSource });

    // Aggiungi il layer alla mappa
    this.map.addLayer(vectorLayer);
  }

  private openPopupGrid(countryName: string, coordinate: any): void {
    // Rimuovi eventuali pop-up esistenti
    const existingPopups = document.querySelectorAll('.popup');
    existingPopups.forEach(popup => popup.remove());
    
    // Crea un nuovo elemento per il pop-up
    const popupElement = document.createElement('div');
    popupElement.classList.add('popup');
     
    // Aggiungi il componente PopupGrid al pop-up
    const componentRef: ComponentRef<PopupGridComponent> = this.viewContainerRef.createComponent(PopupGridComponent);
    componentRef.instance.countryName = countryName; // Passa il nome del paese al componente
    popupElement.appendChild(componentRef.location.nativeElement);
    
    // Aggiungi il pop-up al body
    document.body.appendChild(popupElement);
  }
}
