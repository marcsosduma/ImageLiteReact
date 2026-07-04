import { User, AccessToken, UserSessionToken, Credentials } from "./user.resource";
import jwt from "jwt-decode";

class AuthService {
    baseURL: string = process.env.NEXT_PUBLIC_API_URL + "/v1/users";
    static AUTH_PARAM: string = "_auth";

    async authenticate(credentials: Credentials): Promise<AccessToken> {
        const response = await fetch(`${this.baseURL}/auth`, {
            method: 'POST',
            body: JSON.stringify(credentials),
            headers: {
                'Content-Type': 'application/json'
            }
        });
        if (response.status === 401) {
            throw new Error('User or password invalid!');
        }
        if (!response.ok) {
            throw new Error('Authentication failed');
        }
        const data: AccessToken = await response.json();
        return data;
    }

    async save(user: User): Promise<void> {
        const response = await fetch(`${this.baseURL}`, {
            method: 'POST',
            body: JSON.stringify(user),
            headers: {
                'Content-Type': 'application/json'
            }
        });
        
        console.log('Response Auth.save:', response);

        if (response.status === 409) {
            throw new Error('User already exists!');
        }
        if (!response.ok) {
            throw new Error('User registration failed');
        }
        return;
    }

    initSession(token: AccessToken): void {
        if(token && token.accessToken) {
            const decodedToken: any = jwt(token.accessToken);
            //onsole.log('Decoded Token:', decodedToken);
            const userSession: UserSessionToken = {
                name: decodedToken.name,
                email: decodedToken.sub,  
                accessToken: token.accessToken,
                expiration: decodedToken.exp
            };
            this.setUserSession(userSession);
        }
    }

    setUserSession(userSession: UserSessionToken): void {
        try{
            localStorage.setItem(AuthService.AUTH_PARAM, JSON.stringify(userSession));
        } catch (error) {
           
        }
    }

    getUserSession(): UserSessionToken | null {
        try {
            if (typeof window === 'undefined') {
                return null;
            }
            const userSessionString = localStorage.getItem(AuthService.AUTH_PARAM);
            if (userSessionString) {
                return JSON.parse(userSessionString) as UserSessionToken;
            }
        } catch (error) {
        }
        return null;
    }

    isSessionValid(): boolean {
        const userSession = this.getUserSession();
        if (userSession && userSession.expiration) {
            const expiration: number | undefined = userSession.expiration;
            //console.log('Session Expiration:', new Date(expiration * 1000));
            return new Date() < new Date(expiration * 1000);
        }   
        return false;
    }

    invalidateSession(): void {
        try{
            localStorage.removeItem(AuthService.AUTH_PARAM);
        } catch (error) {
        }
    }
}
    
export const useAuthService = () => new AuthService();