import {
  AfterViewInit,
  Component,
  EventEmitter,
  Output,
  Input,
} from '@angular/core';
import * as L from 'leaflet';
import { Airport } from '../../../../../model/airport';

@Component({
  selector: 'app-airport-map',
  imports: [],
  template: `
    <div
      class="fixed z-30 inset-0 flex items-center justify-center bg-black/50 transition-opacity duration-300"
      [class.opacity-0]="!showing"
      [class.opacity-100]="showing"
      (click)="onBackdropClick()"
    >
      <div
        class="relative z-30 bg-white rounded-lg shadow-lg max-w-3xl w-full p-4 transform transition-transform duration-300"
        [class.scale-95]="!showing"
        [class.scale-100]="showing"
        (click)="$event.stopPropagation()"
      >
        <h2
          class="absolute text-xl font-semibold top-2 left-4 text-custom-nav-via hover:text-gray-700"
        >
          Find your airport on the map!
        </h2>

        <button
          class="absolute text-2xl top-2 right-4 text-gray-500 hover:text-gray-700"
          (click)="onClose()"
          aria-label="Close"
        >
          ✕
        </button>

        <!-- Loading overlay -->
        @if(isLoading){
        <div
          class="absolute inset-0 bg-white/80 flex items-center justify-center rounded-lg z-40"
        >
          <div class="flex flex-col items-center">
            <div
              class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"
            ></div>
            <p class="mt-2 text-gray-600">Loading airports...</p>
          </div>
        </div>
        }
        <div id="map" class="w-full h-[80vh] rounded-md mt-8"></div>
      </div>
    </div>
  `,
})
export class AirportMapComponent implements AfterViewInit {
  @Input() airports: Airport[] = [];

  @Output() closed = new EventEmitter<void>();
  @Output() selectedAirport = new EventEmitter<Airport>();

  showing!: boolean;
  private map!: L.Map;
  private markers: L.CircleMarker[] = [];
  public isLoading = false;

  ngAfterViewInit(): void {
    this.showing = false;
    this.initMap();
    queueMicrotask(() => {
      this.map.invalidateSize();
      this.addAirportMarkers();
      requestAnimationFrame(() => {
        this.showing = true;
      });
    });
  }

  private initMap() {
    this.map = L.map('map').setView([48.8566, 2.3522], 5);
    L.tileLayer(
      'https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}{r}.png',
      {
        attribution:
          '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors &copy; <a href="https://carto.com/">CARTO</a>',
      }
    ).addTo(this.map);
  }

  private addAirportMarkers() {
    if (!this.airports || this.airports.length === 0) {
      return;
    }

    this.isLoading = true;

    setTimeout(() => {
      this.clearMarkers();

      const validAirports = this.airports.filter(
        (airport) =>
          airport.latitude &&
          airport.longitude &&
          !isNaN(parseFloat(airport.latitude)) &&
          !isNaN(parseFloat(airport.longitude))
      );

      validAirports.forEach((airport) => {
        const lat = parseFloat(airport.latitude);
        const lng = parseFloat(airport.longitude);

        const circleMarker = L.circleMarker([lat, lng], {
          radius: this.getRadiusForZoom(this.map.getZoom()),
          color: 'blue',
          fillColor: '#3b82f6',
          fillOpacity: 0.8,
          weight: 1,
        }).addTo(this.map);

        const popupContent = `
          <div class="airport-popup my-0">
            <h3 class="font-bold text-lg">${airport.city} (${airport.code})</h3>
            <h5 class="text-gray-700 -mt-1 text-sm">${airport.name}</h5>
            <h5 class="text-sm text-gray-500 mt-1 mb-1.5">${airport.country}</h5>
            <button 
              class="rounded-md py-2 px-4 bg-emerald-600 hover:bg-emerald-700 text-slate-50"
              data-airport-code="${airport.code}"
            >
              Select
            </button>
          </div>
        `;

        circleMarker.on('popupopen', (e) => {
          const popupEl = e.popup.getElement();
          if (popupEl) {
            const btn = popupEl.querySelector(`button`);
            if (btn) {
              btn.addEventListener('click', () => this.select(airport));
            }
          }
        });

        circleMarker.bindPopup(popupContent);

        this.markers.push(circleMarker);
      });

      if (validAirports.length > 0) {
        const group = L.featureGroup(this.markers);
        this.map.fitBounds(group.getBounds().pad(0.1));
      }

      this.map.on('zoomend', () => {
        const zoom = this.map.getZoom();
        const newRadius = this.getRadiusForZoom(zoom);
        this.markers.forEach((marker) => {
          if (marker instanceof L.CircleMarker) {
            marker.setRadius(newRadius);
          }
        });
      });

      this.isLoading = false;
    }, 0);
  }

  private getRadiusForZoom(zoom: number): number {
    if (zoom >= 10) return 13;
    if (zoom >= 7) return 10;
    if (zoom >= 5) return 7;
    if (zoom == 4) return 4.5;
    if (zoom == 3) return 3;
    if (zoom == 2) return 1;
    if (zoom == 1) return 0.5;
    if (zoom == 0) return 0.25;
    return 3;
  }

  select(airport: Airport) {
    this.selectedAirport.emit(airport);
    this.onClose();
  }

  private clearMarkers() {
    this.markers.forEach((marker) => {
      this.map.removeLayer(marker);
    });
    this.markers = [];
  }

  onClose() {
    this.showing = false;
    setTimeout(() => {
      this.clearMarkers();
      this.closed.emit();
    }, 300); // durata tranziției
  }

  onBackdropClick() {
    this.onClose();
  }
}
