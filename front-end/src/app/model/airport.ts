export class Airport{
    code!: string;
    name!: string;
    city!: string;
    country!: string;

    constructor(code: string, name: string, city: string, country: string){
        this.code = code;
        this.name = name;
        this.city = city;
        this.country = country;
    }
}