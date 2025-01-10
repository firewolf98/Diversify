import { Component, OnInit, AfterViewInit, ComponentRef, HostListener } from '@angular/core';
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
import { CountryService } from '../services/country.service';
import { SearchingCountryService } from '../services/searching-country.service'; // importa il servizio
 
 
 
const ueExtent = [
  -2763122.0902452143,  // Longitudine Ovest (Portogallo)
  3700000.044323073,   // Latitudine Sud (Grecia)
  6398696.460179701,   // Longitudine Est (Cipro)
  9053583.87463804    // Latitudine Nord (taglia parte della Scandinavia)
];
 
@Component({
  selector: 'app-map',
  standalone: true,
  templateUrl: './mappa.component.html',
  styleUrls: ['./mappa.component.css']
})
export class MapComponent implements OnInit, AfterViewInit {
  private map: Map | undefined;
  private countries: Array<any> | undefined;  // Lista dei paesi
  private category: string = 'criticitaGenerale'; // Categoria selezionata
  private vectorLayer: VectorLayer | undefined;
  private popupRef: ComponentRef<PopupGridComponent> | null = null;

  constructor(private viewContainerRef: ViewContainerRef,
    private countryService: CountryService,
    private searchingCountryService: SearchingCountryService
  ) {
    this.countryService.getCountries().subscribe((data) => {
      console.log('Dati ricevuti dal backend:', data); // Controlla se `idPaese` è presente
      this.countries = data.map((country) => ({
        id: country.idPaese, // Assicurati che l'oggetto abbia un campo ID
        name: country.nome,
        coordinates: this.getCoordinatesForCountry(country.nome),
        criticitaGenerale: country.benchmark.find((b: { tipo: string; }) => b.tipo === 'Criticità Generale')?.gravita,
        criticitaLgbt: country.benchmark.find((b: { tipo: string; }) => b.tipo === 'Criticità LGBTQ+')?.gravita,
        criticitaDisabilita: country.benchmark.find((b: { tipo: string; }) => b.tipo === 'Criticità Disabilità')?.gravita,
        criticitaRazzismo: country.benchmark.find((b: { tipo: string; }) => b.tipo === 'Criticità Razzismo')?.gravita,
        criticitaDonne: country.benchmark.find((b: { tipo: string; }) => b.tipo === 'Criticità Donne')?.gravita,
        tipoCriticita: country.benchmark[0].descrizione,
        bandiera: country.linkImmagineBandiera
      }));
      this.onCategoryChange({ target: { value: this.category } });
    });

    this.searchingCountryService.selectedCountry$.subscribe((country) => {
      this.updateMapWithCountry(country); // aggiorna la mappa con il paese selezionato
    });
  }

  ngOnInit(): void {
    
    this.onCategoryChange({ target: { value: this.category } });
    this.searchingCountryService.resetMap$.subscribe(() => {
      this.resetMap(); // Esegui il reset della mappa quando l'evento viene emesso
    });
  }
 
  private updateMapWithCountry(country: string): void {
    // Trova il paese selezionato e aggiorna la mappa
    const countryCoordinates = this.getCoordinatesForCountry(country);
    if (this.map) {
      this.map.getView().setCenter(countryCoordinates); // centrare la mappa sul paese selezionato
      this.map.getView().setZoom(6); // Zoom per il paese selezionato
    }
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
      center: [1000000, 5000000],  // Centro approssimativo per focalizzare l'UE
      zoom: 2,                 // Zoom bilanciato per vedere bene le capitali
      extent: ueExtent
    });
 
    this.map = new Map({
      target: 'map',
      layers: [osmLayer],
      view: view,
      interactions: []
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
    this.countries?.forEach(country => {
      const firstPin = new Feature({
        geometry: new Point(country.coordinates),
        name: country.name
      });
   
      firstPin.setStyle(new Style({
        image: new Icon({
          src: "pin-mappa.png",
          scale: 0.4
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
          case '1':
            pinColor = 'benchmark-verde.png';
            break;
          case '2':
            pinColor = 'benchmark-giallo.png';
            break;
          case '3':
            pinColor = 'benchmark-arancione.png';
            break;
          case '4':
            pinColor = 'benchmark-rosso.png';
            break;
          default:
            pinColor = 'benchmark-nero.png';
        }
   
        secondPin.setStyle(new Style({
          image: new Icon({
            src: `benchmark/${pinColor}`,
            scale: 0.4
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
 
  private openPopupGrid(countryName: string, coordinate: any): void {
    const country = this.countries?.find(c => c.name === countryName);
    console.log('Oggetto Paese:', country); // Stampa i dettagli del paese selezionato
    const tipoCriticita = country ? country.tipoCriticita : '';  // Assegna una stringa vuota se non c'è
    const countryId = country?.id || null;
  
    console.log('ID del Paese selezionato:', countryId); // Stampa l'ID del paese nella console
  
    this.closePopupGrid();
  
    const componentRef = this.viewContainerRef.createComponent(PopupGridComponent);
    componentRef.instance.countryName = countryName;
    componentRef.instance.tipoCriticita = tipoCriticita;  // Passa la stringa vuota se non c'è
    componentRef.instance.flag = country?.bandiera || '';
    componentRef.instance.idPaese = countryId || ''; // Passa l'ID del paese al popup
  
    this.popupRef = componentRef;
  
    const popupElement = document.createElement('div');
    popupElement.classList.add('popup');
    popupElement.appendChild(componentRef.location.nativeElement);
    document.body.appendChild(popupElement);
  
    componentRef.instance.closePopup.subscribe(() => this.closePopupGrid());
  }

  private getCoordinatesForCountry(name: string): [number, number] {
    const staticCoordinates: { [key: string]: [number, number] } = {
      "italia": [1398226.38, 5161471.19],
      "francia": [261800, 6270564],
      "germania": [1491538.66, 6893050.21],
      "spagna": [-412323.77, 4926736.21],
      "portogallo": [-1017076.81, 4679891.74],
      "regno unito": [-14243.57, 6711459.82],
      "irlanda": [-696920.12, 7047883.07],
      "belgio": [484416.76, 6594201.06],
      "paesi bassi": [544603.57, 6867871.26],
      "svizzera": [829527.62, 5933730.25],
      "austria": [1660097.5474459173, 6046474.734704574],
      "polonia": [2338451.85, 6842165.97],
      "svezia": [2011302.35, 8251037.95],
      "norvegia": [1195453.01, 8380446.92],
      "finlandia": [2776567.57, 8437079.46],
      "danimarca": [1399266.15, 7496299.91],
      "estonia": [2754719.61, 8275405.92],
      "lettonia": [2683346.93, 7749797.64],
      "lituania": [2814477.07, 7301414.56],
      "lussemburgo": [680012.4877201075, 6381813.565947726],
      "malta": [1615635.30, 4286733.78],
      "slovacchia": [2163244.391310441, 6223655.777785426],
      "slovenia": [1614909.70, 5788362.07],
      "croazia": [1778627.45, 5750367.74],
      "serbia": [2277379.96, 5592941.10],
      "bosnia ed erzegovina": [2046799.05, 5442545.32],
      "montenegro": [2144201.41, 5227326.33],
      "albania": [2206125.23, 5060868.63],
      "macedonia del nord": [2385789.09, 5160354.53],
      "kosovo": [2355936.19, 5260961.26],
      "bulgaria": [2595413.16, 5265924.33],
      "romania": [2905627.16, 5533187.12],
      "ungheria": [2119708.13, 6023674.28],
      "moldavia": [3209372.07, 5945988.65],
      "cipro": [3714061.19, 4187224.21],
      "turchia": [3657294.42, 4854444.62],
      "ucraina": [3397758.97, 6524454.58],
      "grecia": [2642137.67, 4576076.42],
      "russia": [4187505.32, 7509131.29],
      "bielorussia": [3068143.88, 7151813.43],
      "repubblica ceca": [1721973.3732084506, 6389141.895904409],
    };
 
    // Normalizza l'input
    const normalizedCountry = name.toLowerCase().trim();
    return staticCoordinates[normalizedCountry] || [0, 0];
  }
 
  resetMap(): void {
    const view = this.map?.getView();
    if (view) {
      view.setCenter([1000000, 5000000]);  // Posizione iniziale
      view.setZoom(1);  // Zoom iniziale
    }
  }
 
  private closePopupGrid(): void {
    if (this.popupRef) {
      this.popupRef.destroy();
      this.popupRef = null;
 
      // Rimuovi eventuali elementi DOM residui
      const existingPopups = document.querySelectorAll('.popup');
      existingPopups.forEach(popup => popup.remove());
    }
  }

  @HostListener('document:click', ['$event'])
  onClickOutside(event: MouseEvent): void {
    const target = event.target as HTMLElement;
    const clickedInsidePopup = target.closest('.popup');
    const clickedOnMap = target.closest('#map');

    if (!clickedInsidePopup && !clickedOnMap) {
      this.closePopupGrid();
    }
  }
 
}
 