CREATE TABLE users_tbl (
   id VARCHAR(36) NOT NULL PRIMARY KEY,
   name VARCHAR(100) NOT NULL UNIQUE,
   email VARCHAR(100) NOT NULL,
   phone VARCHAR(10) NOT NULL,
   password VARCHAR(255) NOT NULL,
   username VARCHAR(50) NOT NULL,
   role VARCHAR(50) NOT NULL,
   enable VARCHAR(10) NOT NULL,
   created_by VARCHAR(50),
   created_at DATE,
   updated_by VARCHAR(50),
   updated_at DATE
);
