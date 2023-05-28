export interface RegisterRequest {
	email: string;
	password: string;
    firstName: string;
    lastName: string;
	// roles?: string[];
}

export interface AuthenticationRequest {
	email: string;
	password: string
}