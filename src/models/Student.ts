import mongoose from "mongoose";

const userScema = new mongoose.Schema({
  name: {
    type: String,
    require: true,
  },

  email: {
    type: String,
    require: true,
  },

  age: {
    type: Number,
    require: true,
  },
});

export default mongoose.models.Student || mongoose.model("Student", userScema);
