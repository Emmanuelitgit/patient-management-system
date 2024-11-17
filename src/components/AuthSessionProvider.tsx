"use client";
import React from "react";
import { SessionProvider } from "next-auth/react";

interface AuthSessionProviderProps {
  children: React.ReactNode; // Type for children prop
}

export const AuthSessionProvider: React.FC<AuthSessionProviderProps> = ({
  children,
}) => {
  return <SessionProvider>{children}</SessionProvider>;
};
