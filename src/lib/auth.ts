// src/lib/auth.ts
import { NextAuthOptions, User } from "next-auth";
import GoogleProvider from "next-auth/providers/google";
import GithubProvider from "next-auth/providers/github";
import CredentialsProvider from "next-auth/providers/credentials";
import connectToDatabase from "@/utils/db";
import Patient from "@/models/Patient";
import { IPatient } from "@/types/type";
import { text } from "stream/consumers";
import { use } from "react";
import { compareSync } from "bcrypt-ts";
import jwt, { Secret, JwtPayload } from "jsonwebtoken";

export const authConfig: NextAuthOptions = {
  providers: [
    CredentialsProvider({
      name: "credentials",

      credentials: {
        email: { label: "email", type: "text", placeholder: "email" },
        username: { label: "username", type: "text", placeholder: "username" },
        password: { label: "password", type: "text", placeholder: "password" },
      },

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
              const token = jwt.sign({ id: patientExist.id }, "my_secret", {
                expiresIn: "1h",
              });

              return {
                id: patientExist.id,
                full_name: patientExist.full_name,
                username: patientExist.username,
                email: patientExist.email,
                image: patientExist.image,
                access_token: token,
                refresh_token: token,
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

  callbacks: {
    // return type is token
    async jwt({ token, user }) {
      if (user) {
        (token.id = user?.id),
          (token.username = user?.username ?? ""),
          (token.email = user?.email),
          (token.full_name = user?.full_name),
          (token.image = user?.image),
          (token.access_token = user?.access_token),
          (token.refresh_token = user?.refresh_token);
      }
      return token;
    },

    // return type for session is session
    async session({ session, token }) {
      if (token) {
        session.user.id = token.id;
        (session.user.email = token?.email),
          (session.user.username = token?.username);
        (session.user.full_name = token?.full_name),
          (session.user.access_token = token?.access_token ?? ""),
          (session.user.refresh_token = token?.refresh_token ?? "");
      }

      return session;
    },

    // return type for signIn is boolean
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
};
