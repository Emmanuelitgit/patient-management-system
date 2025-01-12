import mongoose, { Document } from "mongoose";

export interface UserCredentials {
  email: String;
  password: String;
}

export interface ResponseData {
  message: string;
  status: number;
  data: object;
}

export type user = {
  full_name: string;
  email: string;
  password: string;
  image?: string;
  role?: string;
};
// interface for the document
export interface IPatient extends Document {
  full_name: string;
  email: string;
  username?: string;
  password: string;
  image?: string;
  role?: string;
}

export interface IDoctor extends Document {
  full_name: string;
  email: string;
  username?: string;
  password: string;
  image?: string;
  role?: string;
}
