export class Address {
    public street: string;
    public number: string;
    public extraInformation?: string | undefined;
    public postalCode: string;
    public city: string;
    public country: string;
    public province?: string | undefined;

    constructor(street: string, number: string, extraInformation: string, postalCode: string, city: string, country: string, province: string) {
        this.street = street;
        this.number = number;
        this.extraInformation = extraInformation;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
        this.province = province;
    }
}