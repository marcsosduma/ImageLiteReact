'use client'
import { useState, useEffect } from "react";
import Login from "@/app/login/page";
import { useAuthService } from "@/resources";

interface AutenticatePageProps {
    children: React.ReactNode;
}

export const AuthenticatePage: React.FC<AutenticatePageProps> = ({ children }) => {
    const authService = useAuthService();
    const [isAuthenticated, setIsAuthenticated] = useState<boolean | null>(null);

    useEffect(() => {
        // This only runs on the client, so localStorage is safe here
        setIsAuthenticated(authService.isSessionValid());
    }, [authService]);

    // Show nothing (or a loading spinner) while determining the state
    if (isAuthenticated === null) {
        return null; 
    }

    if (!isAuthenticated) {
        return <Login />;
    }

    return <>{children}</>;
};