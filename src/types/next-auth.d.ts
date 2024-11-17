import { DefaultUser } from "next-auth";
import { JWT } from "next-auth/jwt";

// Augment the NextAuth module to add types to the user, account, profile, and session
declare module "next-auth" {
  interface Session {
    user: {
      id: string;
      email: string;
      full_name: string;
      username: string;
      image?: string;
      access_token: string;
      refresh_token: string;
    };
  }

  interface User extends DefaultUser {
    id: string;
    full_name: string;
    email: string;
    username?: string;
    image?: string;
    access_token: string;
    refresh_token: string;
    password: string;
  }

  interface Profile {
    email: string;
    full_name: string;
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

// Augmenting the JWT module to include custom fields
declare module "next-auth/jwt" {
  interface JWT {
    id: string;
    email: string;
    full_name: string;
    username: string;
    image?: string;
    access_token?: string;
    refresh_token?: string;
  }
}
