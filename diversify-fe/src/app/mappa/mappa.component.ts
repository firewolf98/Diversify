import { Component, OnInit, AfterViewInit } from '@angular/core';
import * as ol from 'ol';
import { OSM } from 'ol/source';
import { View } from 'ol';
import { Tile as TileLayer } from 'ol/layer';
import Map from 'ol/Map';
import { Overlay } from 'ol';

@Component({
	selector: 'app-map',
	standalone: true,
	templateUrl: './mappa.component.html',
	styleUrls: ['./mappa.component.css']
})
export class MapComponent implements OnInit, AfterViewInit {
	private map: Map | undefined;

	ngOnInit(): void {
	}

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

			const popupOverlay = new Overlay({
				element: document.createElement('div'),
				position: coordinate,
				positioning: 'center-center',
				stopEvent: false
			});

			const popupElement = popupOverlay.getElement();
			if (popupElement) {
				popupElement.classList.add('popup');  // 
				popupElement.innerHTML = `<p>Clicked at coordinates: [${coordinate[0].toFixed(2)}, ${coordinate[1].toFixed(2)}]</p>`;


				this.map?.addOverlay(popupOverlay);
			} else {
				console.error('Popup element is not defined');
			}
		});
	}
}