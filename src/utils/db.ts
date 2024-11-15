import mongoose from 'mongoose';

// Replace <db_password> with your actual MongoDB password
const uri: string = "mongodb+srv://eyidana001:ldJrzhX33PCKdSBL@pharmasys.lc0nsyk.mongodb.net/pharmasys";
// CONNECTION_STRING="mongodb+srv://eyidana001:ldJrzhX33PCKdSBL@pharmasys.lc0nsyk.mongodb.net/pharmasys"

export default async function connectToDatabase() {
  try {
    // Connect to MongoDB Atlas, specifying the database name in the URI
    await mongoose.connect(uri);

    console.log("Connected to MongoDB Atlas!");
    
  } catch (error) {
    console.error("Error connecting to MongoDB:", error);
  }
}
