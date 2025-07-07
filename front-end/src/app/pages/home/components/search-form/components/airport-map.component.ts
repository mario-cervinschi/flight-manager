import { AfterViewInit, Component, EventEmitter, Output } from '@angular/core';
import * as L from 'leaflet';

@Component({
  selector: 'app-airport-map',
  imports: [],
  template: `
    <div
      class="fixed z-30 inset-0 flex items-center justify-center bg-black/50"
      (click)="onBackdropClick()"
    >
      <div
        class="relative z-30 bg-white rounded-lg shadow-lg max-w-3xl w-full p-4"
        (click)="$event.stopPropagation()"
      >
        <button
          class="absolute text-2xl top-2 right-4 text-gray-500 hover:text-gray-700"
          (click)="onClose()"
          aria-label="Close"
        >
          ✕
        </button>
        <div id="map" class="w-full h-[80vh] rounded-md"></div>
      </div>
    </div>
  `,
})
export class AirportMapComponent implements AfterViewInit {
  @Output() closed = new EventEmitter<void>();
  private map!: L.Map;

  ngAfterViewInit(): void {
    this.initMap();

    // Defer resize to next event loop tick (safer than timeout)
    queueMicrotask(() => {
      this.map.invalidateSize();
    });
  }

  private initMap() {
    const iconRetinaUrl = 'assets/marker-icon-2x.png';
    const iconUrl = 'assets/marker-icon.png';
    const shadowUrl = 'assets/marker-shadow.png';
    const iconDefault = L.icon({
      iconRetinaUrl,
      iconUrl,
      shadowUrl,
      iconSize: [20, 32],
      iconAnchor: [10, 32],
      popupAnchor: [1, -30],
      tooltipAnchor: [16, -28],
      shadowSize: [32, 32],
    });
    L.Marker.prototype.options.icon = iconDefault;

    this.map = L.map('map').setView([48.8566, 2.3522], 5);

    L.tileLayer(
      'https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}{r}.png',
      {
        attribution:
          '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors &copy; <a href="https://carto.com/">CARTO</a>',
      }
    ).addTo(this.map);

    let marker = L.marker([51.5, -0.09]).addTo(this.map);
    marker.bindPopup("<b>Hello world!</b><br>I am a popup.");

  }


  onClose() {
    this.closed.emit();
  }

  onBackdropClick() {
    this.onClose();
  }
}
