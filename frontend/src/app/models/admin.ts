export class Admin {
    public id?: string | undefined;
    public username: string;
    public email: string;
    public firstName: string;
    public lastName: string;
    public password?: string | undefined;

    constructor(id: string | undefined, username: string, email: string, firstName: string, lastName: string, password: string) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }
}