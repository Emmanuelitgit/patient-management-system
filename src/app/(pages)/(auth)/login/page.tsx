"use client";
import React from "react";
import styles from "@/app/(pages)/(auth)/login/page.module.css";
import Image from "next/image";
import PermIdentityIcon from '@mui/icons-material/PermIdentity';


const page = () => {
  const handleChange = () => {};
  const handleClick = () => {};
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
          <PermIdentityIcon className={styles.inputUserIcon}/>
          <input type="text" placeholder="Full Name" />
        </div>
        <div className={styles.inputItem}>
          <PermIdentityIcon className={styles.inputEmailIcon}/>
          <input type="email" placeholder="Email" />
        </div>
        <div className={styles.inputItem}>
          <PermIdentityIcon className={styles.inputPasswordIcon}/>
          <input type="password" placeholder="Password" />
        </div>
        <div className={styles.inputBtnContainer}>
          <button className={styles.inputBtn}>Login</button>
        </div>
      </div>
      <div className={styles.imageContainer}>
        <img src="https://s3-eu-west-1.amazonaws.com/intercare-web-public/wysiwyg-uploads%2F1698752331464-pexels-tessy-agbonome-18828741-min.jpg" alt="" />
      </div>
    </div>
  );
};

export default page;
