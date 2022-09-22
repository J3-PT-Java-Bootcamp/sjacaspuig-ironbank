export class ThirdParty {
    public id: string | undefined;
    public firstName: string;
    public lastName: string;
    public hashedKey: string;

    constructor(id: string | undefined, firstName: string, lastName: string, hashedKey: string) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hashedKey = hashedKey;
    }

    
}