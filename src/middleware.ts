import { withAuth } from "next-auth/middleware";
import { NextRequest, NextResponse } from "next/server";
import { log } from "@/logging-service";

export default withAuth(
  // `withAuth` augments your `Request` with the user's token.
  function middleware(req) {
    if (!req.nextauth.token) {
      return NextResponse.json("Not authenticated", { status: 401 });
    }
    console.log(`${req.nextauth.token.full_name} made a request to ${req.url}`);
  },
  {
    callbacks: {
      authorized: ({ token }) => (token ? true : false),
    },
    secret: process.env.AUTH_SECRET,
  }
);

// for purely next js without any framework
// export default function middleware(req: NextRequest) {
//   console.log("cookies in middleware:");
//   console.log(req.cookies.getAll());
//   NextResponse.next();
// }

export const config = { matcher: ["/dashboard", "/api/patients"] };
