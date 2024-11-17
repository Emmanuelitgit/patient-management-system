"use client";
import React, { useEffect, useState } from "react";
import styles from "@/app/(pages)/dashboard/page.module.css";
import axios from "axios";
import ScheduleIcon from "@mui/icons-material/Schedule";
import HourglassEmptyIcon from "@mui/icons-material/HourglassEmpty";
import WarningAmberIcon from "@mui/icons-material/WarningAmber";
import DataTable from "@/components/DataTable";
import { useSession } from "next-auth/react";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";

const page = () => {
  const session = useSession();

  return (
    <div className={styles.container}>
      <div className={styles.headerContainer}>
        <div>
          <h1>Welcome 🤝</h1>
          <p>Start day with managing new appointments</p>
        </div>
        <div className={styles.profileContainer}>
          <AccountCircleIcon
            style={{
              fontSize: "45px",
              color: "white",
              cursor: "pointer",
            }}
          />
        </div>
      </div>
      <div className={styles.cardsContainer}>
        <div className={styles.cardItem}>
          <div className={styles.cardIconContainer}>
            <ScheduleIcon
              className={styles.scheduleIcon}
              style={{
                fontSize: "40px",
              }}
            />
            <span>4</span>
          </div>
          <p>Schedulled appointments</p>
        </div>

        <div className={styles.cardItem}>
          <div className={styles.cardIconContainer}>
            <HourglassEmptyIcon
              className={styles.pendingIcon}
              style={{
                fontSize: "40px",
              }}
            />
            <span>4</span>
          </div>
          <p>Pending appointments</p>
        </div>

        <div className={styles.cardItem}>
          <div className={styles.cardIconContainer}>
            <WarningAmberIcon
              className={styles.warningIcon}
              style={{
                fontSize: "40px",
              }}
            />
            <span>4</span>
          </div>
          <p>Cancelled appointments</p>
        </div>
      </div>
      <div></div>
      <div className={styles.tableComponent}>
        <DataTable />
      </div>
    </div>
  );
};

export default page;
