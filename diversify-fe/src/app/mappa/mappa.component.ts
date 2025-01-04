import { Component, OnInit, AfterViewInit } from '@angular/core';
import * as ol from 'ol';
import { OSM } from 'ol/source';
import { View } from 'ol';
import { Tile as TileLayer } from 'ol/layer';
import Map from 'ol/Map';
import { Feature } from 'ol';
import { Point } from 'ol/geom';
import { Style, Icon } from 'ol/style';
import VectorSource from 'ol/source/Vector';
import VectorLayer from 'ol/layer/Vector';
import { ViewContainerRef } from '@angular/core';
import { PopupGridComponent } from '../popup-grid/popup-grid.component';

@Component({
  selector: 'app-map',
  standalone: true,
  templateUrl: './mappa.component.html',
  styleUrls: ['./mappa.component.css']
})
export class MapComponent implements OnInit, AfterViewInit {
  private map: Map | undefined;
  private countries: Array<any>;  // Lista dei paesi
  private category: string = 'criticitaGenerale'; // Categoria selezionata
  private vectorLayer: VectorLayer | undefined;

  constructor(private viewContainerRef: ViewContainerRef) {
    this.countries = [
      { name: "Italia", coordinates: [1398226.38, 5161471.19], criticitaGenerale: 1, criticitaLgbt: 4, criticitaDisabilita: 2, criticitaRazzismo: 3, criticitaDonne: 4 },
      { name: "Francia", coordinates: [261800, 6270564], criticitaGenerale: 3, criticitaLgbt: 1, criticitaDisabilita: 2, criticitaRazzismo: 4, criticitaDonne: 0 },
      { name: "Germania", coordinates: [1491538.66, 6893050.21], criticitaGenerale: 2, criticitaLgbt: 0, criticitaDisabilita: 4, criticitaRazzismo: 3, criticitaDonne: 1 },
      { name: "Spagna", coordinates: [-412323.77, 4926736.21], criticitaGenerale: 0, criticitaLgbt: 3, criticitaDisabilita: 1, criticitaRazzismo: 2, criticitaDonne: 4 },
      { name: "Portogallo", coordinates: [-1017076.81, 4679891.74], criticitaGenerale: 1, criticitaLgbt: 2, criticitaDisabilita: 3, criticitaRazzismo: 0, criticitaDonne: 4 },
      { name: "Regno Unito", coordinates: [-14243.57, 6711459.82], criticitaGenerale: 2, criticitaLgbt: 0, criticitaDisabilita: 1, criticitaRazzismo: 3, criticitaDonne: 4 },
      { name: "Irlanda", coordinates: [-696920.12, 7047883.07], criticitaGenerale: 1, criticitaLgbt: 2, criticitaDisabilita: 0, criticitaRazzismo: 4, criticitaDonne: 3 },
      { name: "Belgio", coordinates: [484416.76, 6594201.06], criticitaGenerale: 0, criticitaLgbt: 1, criticitaDisabilita: 4, criticitaRazzismo: 2, criticitaDonne: 3 },
      { name: "Paesi Bassi", coordinates: [544603.57, 6867871.26], criticitaGenerale: 4, criticitaLgbt: 2, criticitaDisabilita: 3, criticitaRazzismo: 1, criticitaDonne: 0 },
      { name: "Svizzera", coordinates: [829527.62, 5933730.25], criticitaGenerale: 3, criticitaLgbt: 4, criticitaDisabilita: 2, criticitaRazzismo: 0, criticitaDonne: 1 },
      { name: "Austria", coordinates: [1822580.10, 6141580.50], criticitaGenerale: 2, criticitaLgbt: 0, criticitaDisabilita: 1, criticitaRazzismo: 4, criticitaDonne: 2 },
      { name: "Polonia", coordinates: [2338451.85, 6842165.97], criticitaGenerale: 1, criticitaLgbt: 5, criticitaDisabilita: 0, criticitaRazzismo: 3, criticitaDonne: 2 },
      { name: "Svezia", coordinates: [2011302.35, 8251037.95], criticitaGenerale: 0, criticitaLgbt: 1, criticitaDisabilita: 2, criticitaRazzismo: 4, criticitaDonne: 3 },
      { name: "Norvegia", coordinates: [1195453.01, 8380446.92], criticitaGenerale: 2, criticitaLgbt: 0, criticitaDisabilita: 3, criticitaRazzismo: 1, criticitaDonne: 4 },
      { name: "Finlandia", coordinates: [2776567.57, 8437079.46], criticitaGenerale: 3, criticitaLgbt: 2, criticitaDisabilita: 1, criticitaRazzismo: 0, criticitaDonne: 4 },
      { name: "Danimarca", coordinates: [1399266.15, 7496299.91], criticitaGenerale: 0, criticitaLgbt: 5, criticitaDisabilita: 2, criticitaRazzismo: 3, criticitaDonne: 1 },
      { name: "Estonia", coordinates: [2754719.61, 8275405.92], criticitaGenerale: 1, criticitaLgbt: 0, criticitaDisabilita: 3, criticitaRazzismo: 2, criticitaDonne: 4 },
      { name: "Lettonia", coordinates: [2683346.93, 7749797.64], criticitaGenerale: 2, criticitaLgbt: 1, criticitaDisabilita: 4, criticitaRazzismo: 3, criticitaDonne: 2 },
      { name: "Lituania", coordinates: [2814477.07, 7301414.56], criticitaGenerale: 3, criticitaLgbt: 0, criticitaDisabilita: 2, criticitaRazzismo: 1, criticitaDonne: 4 },
      { name: "Lussemburgo", coordinates: [682345.66, 6379214.54], criticitaGenerale: 4, criticitaLgbt: 3, criticitaDisabilita: 2, criticitaRazzismo: 0, criticitaDonne: 1 },
      { name: "Malta", coordinates: [1615635.30, 4286733.78], criticitaGenerale: 0, criticitaLgbt: 5, criticitaDisabilita: 3, criticitaRazzismo: 4, criticitaDonne: 0 },
      { name: "Slovacchia", coordinates: [1904515.60, 6132156.30], criticitaGenerale: 3, criticitaLgbt: 1, criticitaDisabilita: 0, criticitaRazzismo: 4, criticitaDonne: 2 },
      { name: "Slovenia", coordinates: [1614909.70, 5788362.07], criticitaGenerale: 1, criticitaLgbt: 2, criticitaDisabilita: 3, criticitaRazzismo: 0, criticitaDonne: 4 },
      { name: "Croazia", coordinates: [1778627.45, 5750367.74], criticitaGenerale: 2, criticitaLgbt: 3, criticitaDisabilita: 0, criticitaRazzismo: 4, criticitaDonne: 1 },
      { name: "Serbia", coordinates: [2277379.96, 5592941.10], criticitaGenerale: 4, criticitaLgbt: 2, criticitaDisabilita: 0, criticitaRazzismo: 3, criticitaDonne: 1 },
      { name: "Bosnia ed Erzegovina", coordinates: [2046799.05, 5442545.32], criticitaGenerale: 3, criticitaLgbt: 4, criticitaDisabilita: 1, criticitaRazzismo: 0, criticitaDonne: 2 },
      { name: "Montenegro", coordinates: [2144201.41, 5227326.33], criticitaGenerale: 0, criticitaLgbt: 1, criticitaDisabilita: 4, criticitaRazzismo: 2, criticitaDonne: 3 },
      { name: "Albania", coordinates: [2206125.23, 5060868.63], criticitaGenerale: 5, criticitaLgbt: 0, criticitaDisabilita: 2, criticitaRazzismo: 3, criticitaDonne: 1 },
      { name: "Macedonia del Nord", coordinates: [2385789.09, 5160354.53], criticitaGenerale: 2, criticitaLgbt: 3, criticitaDisabilita: 4, criticitaRazzismo: 1, criticitaDonne: 0 },
      { name: "Kosovo", coordinates: [2355936.19, 5260961.26], criticitaGenerale: 1, criticitaLgbt: 4, criticitaDisabilita: 0, criticitaRazzismo: 2, criticitaDonne: 3 },
      { name: "Bulgaria", coordinates: [2595413.16, 5265924.33], criticitaGenerale: 2, criticitaLgbt: 0, criticitaDisabilita: 4, criticitaRazzismo: 3, criticitaDonne: 1 },
      { name: "Romania", coordinates: [2905627.16, 5533187.12], criticitaGenerale: 5, criticitaLgbt: 2, criticitaDisabilita: 1, criticitaRazzismo: 0, criticitaDonne: 3 },
      { name: "Ungheria", coordinates: [2119708.13, 6023674.28], criticitaGenerale: 0, criticitaLgbt: 5, criticitaDisabilita: 3, criticitaRazzismo: 2, criticitaDonne: 1 },
      { name: "Moldavia", coordinates: [3209372.07, 5945988.65], criticitaGenerale: 4, criticitaLgbt: 0, criticitaDisabilita: 2, criticitaRazzismo: 3, criticitaDonne: 0 },
      { name: "Cipro", coordinates: [3714061.19, 4187224.21], criticitaGenerale: 3, criticitaLgbt: 2, criticitaDisabilita: 0, criticitaRazzismo: 5, criticitaDonne: 1 },
      { name: "Turchia", coordinates: [3657294.42, 4854444.62], criticitaGenerale: 2, criticitaLgbt: 1, criticitaDisabilita: 4, criticitaRazzismo: 0, criticitaDonne: 3 },
      { name: "Ucraina", coordinates: [3397758.97, 6524454.58], criticitaGenerale: 1, criticitaLgbt: 0, criticitaDisabilita: 2, criticitaRazzismo: 4, criticitaDonne: 3 },
      { name: "Grecia", coordinates: [2642137.67, 4576076.42], criticitaGenerale: 1, criticitaLgbt: 3, criticitaDisabilita: 0, criticitaRazzismo: 2, criticitaDonne: 4 },
      { name: "Russia", coordinates: [4187505.32, 7509131.29], criticitaGenerale: 5, criticitaLgbt: 2, criticitaDisabilita: 1, criticitaRazzismo: 0, criticitaDonne: 3 },
      { name: "Bielorussia", coordinates: [3068143.88, 7151813.43], criticitaGenerale: 4, criticitaLgbt: 0, criticitaDisabilita: 3, criticitaRazzismo: 1, criticitaDonne: 2 },
      { name: "Repubblica Ceca", coordinates: [204005231.5, 645183456], criticitaGenerale: 0, criticitaLgbt: 1, criticitaDisabilita: 3, criticitaRazzismo: 2, criticitaDonne: 4 }
    ];
  }

  ngOnInit(): void {
    this.onCategoryChange({ target: { value: this.category } });
  }

  ngAfterViewInit(): void {
    this.initializeMap();
  }

  onCategoryChange(event: any): void {
    this.category = event.target.value;
    this.updatePins();
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

    this.updatePins();  // Aggiungi i pin iniziali
  }

  private updatePins(): void {
    const vectorSource = new VectorSource();
    
    // Rimuovi il vecchio layer se esiste
    if (this.vectorLayer) {
      this.map?.removeLayer(this.vectorLayer);
    }

    // Aggiungi i nuovi pin
    this.countries.forEach(country => {
      const firstPin = new Feature({
        geometry: new Point(country.coordinates),
        name: country.name
      });
    
      firstPin.setStyle(new Style({
        image: new Icon({
          src: "pin-mappa.png",
          scale: 0.3
        })
      }));
    
      vectorSource.addFeature(firstPin);
    
      // Aggiungi i pin dinamici in base alla categoria
      const criticita = country[this.category];
    
      // Se criticita è 0, non aggiungere il secondo pin
      if (criticita !== 0) {
        const secondPin = new Feature({
          geometry: new Point([
            country.coordinates[0] + 50000,
            country.coordinates[1] + 50000
          ]),
          name: `${country.name}`
        });
    
        let pinColor: string;
        switch (criticita) {
          case 1:
            pinColor = 'benchmark-verde.png';
            break;
          case 2:
            pinColor = 'benchmark-giallo.png';
            break;
          case 3:
            pinColor = 'benchmark-arancione.png';
            break;
          case 4:
            pinColor = 'benchmark-rosso.png';
            break;
          default:
            pinColor = 'benchmark-nero.png';
        }
    
        secondPin.setStyle(new Style({
          image: new Icon({
            src: `benchmark/${pinColor}`,
            scale: 0.3
          })
        }));
    
        vectorSource.addFeature(secondPin);
      }
    });

    this.vectorLayer = new VectorLayer({
      source: vectorSource
    });

    // Aggiungi il vectorLayer alla mappa
    this.map?.addLayer(this.vectorLayer);
  }
  
  // Funzione per mostrare il popup (aggiungila se necessario)
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
