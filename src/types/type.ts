import mongoose, { Document } from "mongoose";

export type user = {
  full_name: string;
  email: string;
  username?: string;
  password: string;
  image?: string;
};

export interface UserCredentials {
  username: String;
  email: String;
  password: String;
}

export interface ResponseData {
  message: string;
  status: number;
  data: object;
}

// interface for the document
export interface IPatient extends Document {
  full_name: string;
  email: string;
  username?: string;
  password: string;
  image?: string;
}
