import Student from "@/models/Student";
import { user } from "@/types/type";
import { NextResponse } from "next/server";
import connect from "@/utils/db";
import { request } from "http";

export const POST = async (request: Request) => {
  const { name, email, age }: user = await request.json();

  try {
    await connect();

    const data = { name, email, age };
    const neStudent = new Student(data);

    if (neStudent) {
      await neStudent.save();

      return new NextResponse("data", { status: 200 });
    } else {
      return new NextResponse("invalid data", { status: 400 });
    }
  } catch (error) {
    console.log(error);
    return new NextResponse("error occured", { status: 500 });
  }
};
