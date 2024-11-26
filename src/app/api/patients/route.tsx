import Patient from "@/models/Patient";
import { IPatient, user } from "@/types/type";
import connectToDatabase from "@/utils/db";
import { NextResponse } from "next/server";

export const GET = async (req: Request) => {
  try {
    await connectToDatabase();
    const patient: user[] = await Patient.find();
    if (patient) {
      return new NextResponse(JSON.stringify(patient));
    }
    if (!patient) {
      return new NextResponse(JSON.stringify("user not found"));
    }
  } catch (error) {
    console.log("internal server error");
  }
};
