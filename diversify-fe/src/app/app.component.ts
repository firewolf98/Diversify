import { Component, AfterViewInit } from '@angular/core';
import Map from 'ol/Map';  // Correct import for Map
import View from 'ol/View';  // Correct import for View
import TileLayer from 'ol/layer/Tile';  // Correct import for TileLayer
import OSM from 'ol/source/OSM';  // Correct import for OSM source
import { fromLonLat } from 'ol/proj';  // Correct import for fromLonLat
import 'ol/ol.css';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']  // Corrected to styleUrls
})
export class AppComponent implements AfterViewInit {
  title = 'diversify-fe'; // Title of the app

  ngAfterViewInit(): void {
    // Initialize the OpenLayers map after the view is rendered
    console.log("ngAfterViewInit is triggered");
    const map = new Map({
      target: 'map', // This is the id of the div in your HTML
      layers: [
        new TileLayer({
          source: new OSM() // OpenStreetMap layer
        })
      ],
      view: new View({
        center: fromLonLat([0, 0]), // Starting coordinates (longitude, latitude)
        zoom: 2 // Starting zoom level
      })
    });
  }
}