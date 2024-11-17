import mongoose, { Document, Schema } from "mongoose";

// interface for the document
export interface IPatient extends Document {
  full_name: string;
  email: string;
  username?: string;
  password: string;
  image?: string;
  access_token: string;
  refresh_token: string;
}

// the databse schema
const patientSchema = new Schema<IPatient>({
  full_name: {
    type: String,
    required: true,
  },
  email: {
    type: String,
    required: true,
  },
  username: {
    type: String,
    required: false,
  },
  password: {
    type: String,
    required: false,
  },
  image: {
    type: String,
    required: false,
  },
});

// Create or get the model
const Patient =
  mongoose.models.Patient || mongoose.model<IPatient>("Patient", patientSchema);

export default Patient;
