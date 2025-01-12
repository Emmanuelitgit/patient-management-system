import Patient from "@/models/Patient";
import { IPatient, user } from "@/types/type";
import connectToDatabase from "@/utils/db";
import { NextRequest, NextResponse } from "next/server";
import { cookies } from "next/headers";
import { getSession } from "next-auth/react";
import { getServerSession } from "next-auth";
import { getToken } from "next-auth/jwt";
import { authConfig } from "@/lib/auth";
import { redirect } from "next/navigation";
import { log } from "@/logging-service";

const secret = process.env.AUTH_SECRET;

export const GET = async (req: NextRequest, res: NextResponse) => {
  try {
    // console.log("cookies in get patient route:", req.cookies.getAll());
    const token = await getToken({ req, secret });
    if (!token) {
      return new NextResponse(JSON.stringify("Unauthorized access"), {
        status: 401,
      });
    }
    // log.info(`${token.full_name} made a request to ${req.url}`);
    const session = await getServerSession(authConfig);

    await connectToDatabase();
    const patients: user[] = await Patient.find();
    if (patients) {
      return NextResponse.json(
        {
          data: patients,
          meassage: "Patients list fetched successfully",
          status: 200,
        },
        { status: 200 }
      );
    }
    if (!patients) {
      return NextResponse.json("Patient data not found", { status: 404 });
    }
  } catch (error) {
    return NextResponse.json("Internal Server Error", {
      status: 500,
    });
  }
};

export const POST = async (req: NextRequest) => {
  try {
    await connectToDatabase();
    const body = await req.json();
    const newUser: IPatient = new Patient({
      full_name: body.full_name,
      email: body.email,
      username: body.username,
      password: body.password,
      image: body.image,
      role: body.role,
    });
    if (newUser !== null) {
      const savedUser: user = await newUser.save();
      if (!savedUser) {
        return NextResponse.json("Error occured while saving user", {
          status: 404,
        });
      }
      return NextResponse.json("Patient saved successfully", { status: 201 });
    }
  } catch (error) {
    console.log(error);
    return NextResponse.json("Internal server error", { status: 500 });
  }
};
