import { Address } from "./address";

export class AccountHolder {
    public id?: string | undefined;
    public username: string;
    public email: string;
    public firstName: string;
    public lastName: string;
    public birthDate: Date;
    public primaryAddress: Address;
    public secondaryAddress?: Address | null | undefined;
    public password?: string | undefined;

    constructor(
        id: string | undefined,
        username: string,
        email: string,
        firstName: string,
        lastName: string,
        birthDate: Date,
        primaryAddress: Address,
        secondaryAddress: Address | null,
        password: string
    ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.primaryAddress = primaryAddress;
        this.secondaryAddress = secondaryAddress;
        this.password = password;
    }
}