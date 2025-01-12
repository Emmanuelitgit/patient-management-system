// src/lib/auth.ts
import { NextAuthOptions, User } from "next-auth";
import GoogleProvider from "next-auth/providers/google";
import GithubProvider from "next-auth/providers/github";
import CredentialsProvider from "next-auth/providers/credentials";
import connectToDatabase from "@/utils/db";
import Patient from "@/models/Patient";
import { compareSync } from "bcrypt-ts";
import { MongoDBAdapter } from "@auth/mongodb-adapter";
import client from "@/utils/client-db";
import { log } from "@/logging-service";

export const authConfig: NextAuthOptions = {
  // adapter: MongoDBAdapter(client),
  providers: [
    CredentialsProvider({
      name: "credentials",
      id: "credentials",
      credentials: {
        email: { label: "email", type: "text", placeholder: "email" },
        username: { label: "username", type: "text", placeholder: "username" },
        password: { label: "password", type: "text", placeholder: "password" },
      },

      // full_name, access_token, refresh_token, password

      // return type must be User
      async authorize(credentials) {
        await connectToDatabase();

        try {
          const patientExist: User | null = await Patient.findOne({
            email: credentials?.email,
          });

          if (patientExist) {
            const isPasswordCorrect = compareSync(
              credentials?.password ?? "",
              patientExist.password
            );
            if (isPasswordCorrect) {
              return {
                id: patientExist.id,
                full_name: patientExist.full_name,
                email: patientExist.email,
                image: patientExist.image,
              } as User;
            } else {
              return null;
            }
          } else {
            return null;
          }
        } catch (err) {
          console.log(err);
          return null;
        }
      },
    }),
  ],

  session: {
    strategy: "jwt", // Use JWT for stateless session
    maxAge: 6000 * 6000, // Session expiration time in seconds (1 hour)
  },

  callbacks: {
    // return type is token
    // this token is sent to the users browser on their first login or visit to the application
    // this token automatically append to each request by the browser automatically.
    // the token is stored in the browser as a cookie.
    async jwt({ token, user }) {
      if (user) {
        (token.id = user?.id),
          (token.email = user?.email),
          (token.full_name = user?.full_name),
          (token.image = user?.image);
      }
      return token;
    },

    // return type for session is session
    // this is what is accessible to the frontend via useSession() hook
    async session({ session, token }) {
      if (token) {
        session.user.id = token.id;
        (session.user.email = token?.email),
          (session.user.full_name = token?.full_name);
      }

      return session;
    },

    // return type for signIn is boolean
    // it returns true when login is success otherwise false
    // it also allow us to check the type of authentication user is using example credentials, third party provider etc.
    async signIn({ profile, account, credentials }) {
      await connectToDatabase();

      // check if user is logging in with custom credentials like email and password
      if (credentials) {
        const userCredentials: User | null = await Patient.findOne({
          email: credentials?.email,
        });
        if (userCredentials) return true;
      }

      // check if user is loggin in with provider like google or github
      if (profile) {
        const userProfile: User | null = await Patient.findOne({
          email: profile.email,
        });
        if (userProfile) return true;

        // create one if user details does not exist in the database
        if (!userProfile) {
          await Patient.create({
            email: profile.email,
            full_name: profile.name,
            image: profile?.image,
          });

          return true;
        }
      }

      return true;
    },
  },

  // secret key for encoding and decoding of user authentication details eg is jwt.
  secret: process.env.AUTH_SECRET,

  //log levels
  logger: {
    error(code, metadata) {
      log.error(`NextAuth Error - Code: ${code}`, metadata);
    },
    warn(code) {
      log.warn(`NextAuth Warning - Code: ${code}`);
    },
    debug(code, metadata) {
      log.debug(`NextAuth Debug - Code: ${code}`, metadata);
    },
  },

  pages: {
    signIn: "/login",
  },
};
