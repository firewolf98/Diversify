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
  private countries: Array<any>;  // Aggiungi questa proprietà per la lista dei paesi

  constructor(private viewContainerRef: ViewContainerRef, 
              private componentFactoryResolver: ComponentFactoryResolver) {
    // Inizializza countries direttamente nel costruttore
    this.countries = [
      { name: "Italia", coordinates: [1398226.38, 5161471.19], criticita: 1, tipoCriticita: "diritti delle donne" },
      { name: "Francia", coordinates: [261800, 6270564], criticita: 1, tipoCriticita: "diritti contro il razzismo" },
      { name: "Germania", coordinates: [1491538.66, 6893050.21], criticita: 0 },
      { name: "Spagna", coordinates: [-412323.77, 4926736.21], criticita: 0 },
      { name: "Portogallo", coordinates: [-1017076.81, 4679891.74], criticita: 0 },
      { name: "Regno Unito", coordinates: [-14243.57, 6711459.82], criticita: 0 },
      { name: "Irlanda", coordinates: [-696920.12, 7047883.07], criticita: 0 },
      { name: "Belgio", coordinates: [484416.76, 6594201.06], criticita: 0 },
      { name: "Paesi Bassi", coordinates: [544603.57, 6867871.26], criticita: 0 },
      { name: "Svizzera", coordinates: [829527.62, 5933730.25], criticita: 0 },
      { name: "Austria", coordinates: [1822580.10, 6141580.50], criticita: 0 },
      { name: "Polonia", coordinates: [2338451.85, 6842165.97], criticita: 4, tipoCriticita: "diritti contro il razzismo" },
      { name: "Svezia", coordinates: [2011302.35, 8251037.95], criticita: 0 },
      { name: "Norvegia", coordinates: [1195453.01, 8380446.92], criticita: 0 },
      { name: "Finlandia", coordinates: [2776567.57, 8437079.46], criticita: 0 },
      { name: "Danimarca", coordinates: [1399266.15, 7496299.91], criticita: 0 },
      { name: "Estonia", coordinates: [2754719.61, 8275405.92], criticita: 2, tipoCriticita: "diritti contro il razzismo" },
      { name: "Lettonia", coordinates: [2683346.93, 7749797.64], criticita: 2, tipoCriticita: "diritti contro il razzismo" },
      { name: "Lituania", coordinates: [2814477.07, 7301414.56], criticita: 2, tipoCriticita: "diritti contro il razzismo" },
      { name: "Lussemburgo", coordinates: [682345.66, 6379214.54], criticita: 2, tipoCriticita: "diritti contro il razzismo" },
      { name: "Malta", coordinates: [1615635.30, 4286733.78], criticita: 0 },
      { name: "Slovacchia", coordinates: [1904515.60, 6132156.30], criticita: 0 },
      { name: "Slovenia", coordinates: [1614909.70, 5788362.07], criticita: 0 },
      { name: "Croazia", coordinates: [1778627.45, 5750367.74], criticita: 0 },
      { name: "Serbia", coordinates: [2277379.96, 5592941.10], criticita: 0 },
      { name: "Bosnia ed Erzegovina", coordinates: [2046799.05, 5442545.32], criticita: 0 },
      { name: "Montenegro", coordinates: [2144201.41, 5227326.33], criticita: 0 },
      { name: "Albania", coordinates: [2206125.23, 5060868.63], criticita: 3, tipoCriticita: "diritti LGBT" },
      { name: "Macedonia del Nord", coordinates: [2385789.09, 5160354.53], criticita: 4, tipoCriticita: "diritti contro il razzismo" },
      { name: "Kosovo", coordinates: [2355936.19, 5260961.26], criticita: 0 },
      { name: "Bulgaria", coordinates: [2595413.16, 5265924.33], criticita: 2 },
      { name: "Romania", coordinates: [2905627.16, 5533187.12], criticita: 5, tipoCriticita: "diritti LGBT" },
      { name: "Ungheria", coordinates: [2119708.13, 6023674.28], criticita: 0 },
      { name: "Moldavia", coordinates: [3209372.07, 5945988.65], criticita: 0 },
      { name: "Cipro", coordinates: [3714061.19, 4187224.21], criticita: 3, tipoCriticita: "diritti per le persone con disabilità" },
      { name: "Turchia", coordinates: [3657294.42, 4854444.62], criticita: 0 },
      { name: "Ucraina", coordinates: [3397758.97, 6524454.58], criticita: 0 },
      { name: "Grecia", coordinates: [2642137.67, 4576076.42], criticita: 1, tipoCriticita: "diritti per le persone con disabilità" },
      { name: "Russia", coordinates: [4187505.32, 7509131.29], criticita: 5, tipoCriticita: "diritti LGBT" },
      { name: "Bielorussia", coordinates: [3068143.88, 7151813.43], criticita: 0 },
      { name: "Repubblica Ceca", coordinates: [204005231.5, 645183456], criticita: 0 }
    ];
  }

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
        const tipoCriticita = clickedFeature.get('tipoCriticita');
        this.openPopupGrid(countryName, coordinate);
      }
    });

    const features = this.countries.map(country => {
      const feature = new Feature({
        geometry: new Point(country.coordinates),
        name: country.name
      });

      feature.set('tipoCriticita', country.tipoCriticita);
      feature.setStyle(new Style({
        image: new Icon({
          src: "pin-mappa.png", 
          scale: 0.3
        })
      }));

      if (country.criticita === 1) {
        const secondFeature = new Feature({
          geometry: new Point([
            country.coordinates[0] + 50000,
            country.coordinates[1] + 50000
          ]),
          name: `${country.name}`
        });

        secondFeature.set('tipoCriticita', country.tipoCriticita);
        secondFeature.setStyle(new Style({
          image: new Icon({
            src: "benchmark/benchmark-verde.png", 
            scale: 0.3
          })
        }));

        return [feature, secondFeature];
      }
      if (country.criticita === 2) {
        const secondFeature = new Feature({
          geometry: new Point([
            country.coordinates[0] + 50000,
            country.coordinates[1] + 50000
          ]),
          name: `${country.name}`
        });

        secondFeature.set('tipoCriticita', country.tipoCriticita);
        secondFeature.setStyle(new Style({
          image: new Icon({
            src: "benchmark/benchmark-giallo.png", 
            scale: 0.3
          })
        }));

        return [feature, secondFeature];
      }
      if (country.criticita === 3) {
        const secondFeature = new Feature({
          geometry: new Point([
            country.coordinates[0] + 50000,
            country.coordinates[1] + 50000
          ]),
          name: `${country.name}`
        });

        secondFeature.set('tipoCriticita', country.tipoCriticita);
        secondFeature.setStyle(new Style({
          image: new Icon({
            src: "benchmark/benchmark-arancione.png", 
            scale: 0.3
          })
        }));

        return [feature, secondFeature];
      }
      if (country.criticita === 4) {
        const secondFeature = new Feature({
          geometry: new Point([
            country.coordinates[0] + 50000,
            country.coordinates[1] + 50000
          ]),
          name: `${country.name}`
        });

        secondFeature.set('tipoCriticita', country.tipoCriticita);
        secondFeature.setStyle(new Style({
          image: new Icon({
            src: "benchmark/benchmark-rosso.png", 
            scale: 0.3
          })
        }));

        return [feature, secondFeature];
      }
      if (country.criticita === 5) {
        const secondFeature = new Feature({
          geometry: new Point([
            country.coordinates[0] + 50000,
            country.coordinates[1] + 50000
          ]),
          name: `${country.name}`
        });

        secondFeature.set('tipoCriticita', country.tipoCriticita);
        secondFeature.setStyle(new Style({
          image: new Icon({
            src: "benchmark/benchmark-nero.png", 
            scale: 0.3
          })
        }));

        return [feature, secondFeature];
      }

      return feature;
    }).flat();

    const vectorSource = new VectorSource({ features });
    const vectorLayer = new VectorLayer({ source: vectorSource });

    this.map.addLayer(vectorLayer);
  }

  private openPopupGrid(countryName: string, coordinate: any): void {
    const country = this.countries.find(c => c.name === countryName);
    const tipoCriticita = country ? country.tipoCriticita : '';  // Assegna una stringa vuota se non c'è
  
    // Rimuovi eventuali pop-up esistenti
    const existingPopups = document.querySelectorAll('.popup');
    existingPopups.forEach(popup => popup.remove());
    
    const popupElement = document.createElement('div');
    popupElement.classList.add('popup');
    
    const componentRef = this.viewContainerRef.createComponent(PopupGridComponent);
    componentRef.instance.countryName = countryName;
    componentRef.instance.tipoCriticita = tipoCriticita;  // Passa la stringa vuota se non c'è
  
    popupElement.appendChild(componentRef.location.nativeElement);
    document.body.appendChild(popupElement);
  }
  
}
