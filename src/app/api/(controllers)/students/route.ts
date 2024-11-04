import Student from "@/models/Student";
import { user } from "@/types/type";
import { NextResponse } from "next/server";
import connect from "@/utils/db";
import { request } from "http";

type responseData = {
  status: number;
  data: user[];
  message: String;
};

export const GET = async (request: Request) => {
  try {
    await connect();

    const students: user[] = await Student.find();

    const data: responseData = {
      status: 200,
      message: students.length > 0 ? "detais here" : "no student found",
      data: students,
    };

    return new NextResponse(JSON.stringify(data));
  } catch (error) {
    console.log(error);
    return new NextResponse("internal error");
  }
};
