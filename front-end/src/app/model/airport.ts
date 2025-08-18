export class Airport{
    code!: string;
    name!: string;
    city!: string;
    country!: string;
    latitude!: string;
    longitude!: string;

    constructor(code: string, name: string, city: string, country: string, lat: string, long: string){
        this.code = code;
        this.name = name;
        this.city = city;
        this.country = country;
        this.latitude = lat;
        this.longitude = long;
    }
}