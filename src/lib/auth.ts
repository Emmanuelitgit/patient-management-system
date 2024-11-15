// src/lib/auth.ts
import { NextAuthOptions, User } from "next-auth";
import GoogleProvider from "next-auth/providers/google";
import GithubProvider from "next-auth/providers/github";
import CredentialsProvider from "next-auth/providers/credentials";
import connectToDatabase  from "@/utils/db";
import Patient from "@/models/Patient";
import { IPatient } from "@/models/Patient";
import { user } from "@/types/type";
import { text } from "stream/consumers";
import { use } from "react";



export const authConfig:NextAuthOptions={
  providers:[
    CredentialsProvider({
      name:"credentials",

      credentials:{
        email:{label:"email", type:"text", placeholder:"email"},
        password:{label:"password", type:"text", placeholder:"password"}
      },

     async authorize(credentials) {

      await connectToDatabase();

      try{

      const patientExist:IPatient | null = await Patient.findOne({email:credentials?.email});

      if(patientExist){
        return patientExist as User
      }else{
        return null;
      }
      }catch(err){
        console.log(err)
        return null
      }
       
     },
    })
  ],

  callbacks:{
    async session({session, user}){
      if(session.user){
        session.user.id = user.id
      }
      return session;
    },

    async signIn({profile, account, credentials}){

      await connectToDatabase();
      
      if(credentials){
        const userCredentials:IPatient | null = await Patient.findOne({email:credentials?.email})
        if(userCredentials) return true;
      }

      if(profile){
        const userProfile: IPatient | null = await Patient.findOne({email:profile.email});
        if(userProfile) return true

        if(!userProfile){
            await Patient.create({
            email:profile.email,
            name:profile.name,
            image:profile.image
          });

          return true
        }
      }

      return true;
    }
  }
}