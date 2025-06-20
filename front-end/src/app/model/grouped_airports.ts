import { Airport } from './airport';

export interface GroupedAirports {
  country: string;
  airports: Airport[];
}

export function groupAirportsByCountry(airports: Airport[]): GroupedAirports[] {
  const grouped = airports.reduce((acc, airport) => {
    const country = airport.country;
    if (!acc[country]) {
      acc[country] = [];
    }
    acc[country].push(airport);
    return acc;
  }, {} as Record<string, Airport[]>);

  return Object.entries(grouped)
    .map(([country, airports]) => ({
      country,
      airports: airports.sort((a, b) => a.city.localeCompare(b.city)),
    }))
    .sort((a, b) => a.country.localeCompare(b.country));
}
