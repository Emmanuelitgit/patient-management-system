import { DefaultUser } from "next-auth";

// Augment the NextAuth module to add types to the user, account, and profile
declare module "next-auth" {
  interface Session {
    user: {
      id: string; // Adding custom `id` to session
      email: string;
      name: string;
      image?: string;
    };
  }

  interface User extends DefaultUser {
    id: string; // Adding custom `id` to user
    name:string;
    email: string;
    username?: string;
    image?: string;
  }

  interface Profile {
    email: string;
    name: string;
    image?: string;
  }

  interface Account {
    provider: string;
    type: string;
    access_token?: string;
    refresh_token?: string;
    id_token?: string;
    scope?: string;
  }
}