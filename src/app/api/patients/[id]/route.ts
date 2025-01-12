import { getToken } from "next-auth/jwt";
import { NextRequest, NextResponse } from "next/server";
const secret = process.env.AUTH_SECRET;

const params = {
  id: String,
};
export const GET = async (
  req: NextRequest,
  {
    params,
  }: {
    params: Promise<{ id: string }>;
  }
) => {
  const value = (await params).id;
  console.log(value);
  const token = await getToken({ req, secret });
  if (!token) return NextResponse.json("No patient found");
  return NextResponse.json("patient");
};
