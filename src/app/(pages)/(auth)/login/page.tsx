"use client";
import React, { useState } from "react";
import styles from "@/app/(pages)/(auth)/login/page.module.css";
import Image from "next/image";
import PermIdentityIcon from "@mui/icons-material/PermIdentity";
import { signIn, signOut, useSession, getProviders } from "next-auth/react";
import { UserCredentials } from "@/types/type";
import { useRouter } from "next/navigation";

const page = () => {
  const router = useRouter();
  const [credentials, setCredentials] = useState<UserCredentials>({
    username: "",
    email: "",
    password: "",
  });

  const handleLogin = async (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    const rsesponse = await signIn("credentials", {
      redirect: false,
      username: credentials.username,
      email: credentials.email,
      password: credentials.password,
    });

    if (rsesponse?.status === 200) {
      router.push("/dashboard");
    } else if (rsesponse?.status === 401) {
      alert("invalid credentials");
    } else if (rsesponse?.status === 500) {
      alert("internal server error");
    } else {
      alert("unexpected error");
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setCredentials((prev) => {
      return {
        ...prev,
        [name]: value,
      };
    });
  };

  return (
    <div className={styles.container}>
      <div className={styles.formContainer}>
        <div className={styles.titleLogo}>
          <img
            className={styles.logoImg}
            alt=""
            src="https://img.freepik.com/premium-vector/health-care-medical-logo-vector-design_990473-2554.jpg"
          />
          <h2 className={styles.titleText}>ManuelCare</h2>
        </div>
        <div className={styles.subTitleContainer}>
          <h1>Hi there 🤝</h1>
          <p>Login to get started with your appointments</p>
        </div>
        <div className={styles.inputItem}>
          <PermIdentityIcon className={styles.inputUserIcon} />
          <input
            type="text"
            placeholder="Username"
            name="username"
            onChange={handleChange}
          />
        </div>
        <div className={styles.inputItem}>
          <PermIdentityIcon className={styles.inputEmailIcon} />
          <input
            type="email"
            placeholder="Email"
            name="email"
            onChange={handleChange}
          />
        </div>
        <div className={styles.inputItem}>
          <PermIdentityIcon className={styles.inputPasswordIcon} />
          <input
            type="password"
            placeholder="Password"
            name="password"
            onChange={handleChange}
          />
        </div>
        <div className={styles.inputBtnContainer}>
          <button className={styles.inputBtn} onClick={handleLogin}>
            Login
          </button>
        </div>
      </div>
      <div className={styles.imageContainer}>
        <img
          src="https://s3-eu-west-1.amazonaws.com/intercare-web-public/wysiwyg-uploads%2F1698752331464-pexels-tessy-agbonome-18828741-min.jpg"
          alt=""
        />
      </div>
    </div>
  );
};

export default page;
