import mongoose from "mongoose";

const connect = async () => {
  try {
    await mongoose.connect("mongodb://localhost:27017/school");
    console.log("MongoDB connected");
  } catch (error) {
    console.log("erro occurs", error);
  }
};

export default connect;
