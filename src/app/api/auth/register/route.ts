import connectToDatabase from "@/utils/db";
import { NextRequest, NextResponse } from "next/server";
import Patient from "@/models/Patient";
import { IPatient } from "@/types/type";
import { UserCredentials } from "@/types/type";
import { NextApiRequest } from "next";
import { ResponseData } from "@/types/type";
import { genSaltSync, hashSync } from "bcrypt-ts";

export const POST = async (req: NextRequest) => {
  const { username, email, password, image, full_name }: IPatient =
    await req.json();
  try {
    const salt = genSaltSync(10);
    const hashedPassword = hashSync(password, 10);
    await connectToDatabase();
    const newUser: IPatient = await new Patient({
      username,
      email,
      password: hashedPassword,
      image,
      full_name,
    });
    const response: ResponseData = {
      message: "user added successfully",
      status: 200,
      data: newUser,
    };
    if (newUser) {
      await newUser.save();
      return new NextResponse(JSON.stringify(response));
    }
  } catch (err) {
    console.log(err);
    return new NextResponse(JSON.stringify("an error ocurs"));
  }
};
