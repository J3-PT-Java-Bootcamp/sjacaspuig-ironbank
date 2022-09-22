import { Role } from "../types/role";

export class User {
    _username: string;
    _role: Role;
    _id: string;


    constructor(username: string, role: Role, id: string) {
        this._username = username;
        this._role = role;
        this._id = id;
    }

    get username(): string {
        return this._username;
    }

    get role(): Role {
        return this._role;
    }

    get id(): string {
        return this._id;
    }
}