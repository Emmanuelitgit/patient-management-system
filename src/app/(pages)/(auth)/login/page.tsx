"use client";
import React from "react";
import styles from "@/app/(pages)/home/page.module.css";

const page = () => {
  const handleChange = () => {};
  const handleClick = () => {};
  return (
    <div className={styles.container}>
      <h1>Login Form</h1>
      <div className={styles.formContainer}>
        <input
          type="text"
          name="email"
          onChange={handleChange}
          className={styles.input}
          placeholder="email"
        />
        <input
          type="password"
          name="password"
          onChange={handleChange}
          className={styles.input}
          placeholder="password"
        />
        <button onClick={handleClick} className={styles.button}>
          Login
        </button>
        <button onClick={handleClick} className={styles.button}>
          Login with Github
        </button>
      </div>
    </div>
  );
};

export default page;
