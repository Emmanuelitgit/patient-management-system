export type user = {
  name: string;
  email: string;
  username: string;
  image: string;
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
