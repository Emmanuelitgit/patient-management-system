import mongoose, { Document, Schema } from "mongoose";

// interface for the document
export interface IPatient extends Document {
  name: string;
  email: string;
  username?: string;
  image?: string;
}

// the databse schema
const patientSchema = new Schema<IPatient>({
  name: {
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
  image: {
    type: String,
    required: false,
  },
});

// Create or get the model
const Patient = mongoose.models.Patient || mongoose.model<IPatient>("Patient", patientSchema);

export default Patient;
