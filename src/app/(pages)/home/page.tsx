"use client";
import { user } from "@/types/type";
import React, { useEffect, useState } from "react";
import styles from "@/app/(pages)/home/page.module.css";
import axios from "axios";

const page = () => {
  const [data, setData] = useState<user>({
    name: "",
    email: "",
    age: null,
  });
  const [students, setStudents] = useState<user[]>();
  const [depValue, setDepValue] = useState<number>(0);

  useEffect(() => {
    const getStudents = async () => {
      try {
        const response = await axios.get("http://localhost:3000/api/students");
        if (response.status === 200) {
          setStudents(response.data.data);
        } else {
          console.log("no data from the backend");
        }
      } catch (error) {
        console.log(error);
      }
    };
    getStudents();
  }, [depValue]);

  const handleClick = async () => {
    try {
      setDepValue((prev) => prev + 1);
      const response = await axios.post(
        "http://localhost:3000/api/add-student",
        data
      );
      if (response.status === 200) {
        console.log("user added successfully from the frontend.");
      } else {
        console.log("could not add user from the frontend");
      }
    } catch (error) {
      console.log(error);
    }
  };

  console.log(depValue);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setData((prev) => ({
      ...prev,
      [name]: name === "age" ? Number(value) : value,
    }));
  };

  return (
    <div className={styles.container}>
      <h1>User Management</h1>
      <div className={styles.formContainer}>
        <input
          type="text"
          name="name"
          onChange={handleChange}
          className={styles.input}
          placeholder="name"
        />
        <input
          type="text"
          name="email"
          onChange={handleChange}
          className={styles.input}
          placeholder="email"
        />
        <input
          type="number"
          name="age"
          onChange={handleChange}
          className={styles.input}
          placeholder="age"
        />
        <button onClick={handleClick} className={styles.button}>
          submit
        </button>
      </div>
      <table className={styles.tableContainer}>
        <thead>
          <tr>
            <th>Name</th>
            <th>Email</th>
            <th>Age</th>
          </tr>
        </thead>
        <tbody>
          {students?.map((student) => (
            <tr key={student?.email}>
              <td>{student?.name}</td>
              <td>{student?.email}</td>
              <td>{student?.age}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default page;
