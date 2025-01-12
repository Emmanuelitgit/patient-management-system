import winston from "winston";
const { combine, timestamp, json } = winston.format;

const log = winston.createLogger({
  level: "info",
  format: combine(timestamp(), json()),
  transports: [
    new winston.transports.File({
      filename: "app.log",
    }),
  ],
});

export { log };
