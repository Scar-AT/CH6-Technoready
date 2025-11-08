# 🧩 CH6 – Collectibles Store API



---

## 🚀 Project Overview

The goal is to build an **API service** that supports basic user operations and demonstrates good backend practices using Spark, Gson, and Logback.

| Sprint | Focus | Technologies |
|:--|:--|:--|
| **1** | Backend API (Users CRUD) | Java 17+, Maven, Spark, Gson, Logback |
| **2** | Templates + Views + Forms | Mustache, HTML, CSS, JS |
| **3** | Filters + Real-time updates | WebSockets, JavaScript |

---

## ⚙️ Installation and Setup

### 🧱 Requirements
- Java 17 or later
- Maven 3.x
- IntelliJ IDEA / VS Code with Java plugin

### 🧩 Technologies Used

- Java 17 + Spark 2.9.4

- Maven

- Gson 2.11.0

- Logback 1.4.14

- Postman (API testing)

---

### 🧩 Steps

1. **Clone the repository**
    ````bash
      git clone https://github.com/Scar-AT/CH6-Technoready.git
      cd CH6-Technoready
    ````

2. **Compile the project**
    ````bash
      mvn clean install
    ````
    
3. **Run the server**

- In IntelliJ → right-click  `Main.java` → Run  `Main.main()`
- Or via terminal:
    ````bash
      mvn exec:java -Dexec.mainClass="com.techready.Main"
    ````

4. **Access the API**
- Base URL: http://localhost:4567
- Health check: http://localhost:4567/hello

---
### API Endpoints
| Method      | Route        | Description          |
| :---------- | :----------- | :------------------- |
| **GET**     | `/users`     | Retrieve all users   |
| **GET**     | `/users/:id` | Retrieve user by ID  |
| **POST**    | `/users/:id` | Add new user         |
| **PUT**     | `/users/:id` | Edit existing user   |
| **OPTIONS** | `/users/:id` | Check if user exists |
| **DELETE**  | `/users/:id` | Delete user          |

### Example Request – POST /users/:id

**Request body**
````json
  {
  "name": "Ana",
  "email": "ana@tts.com"
  }
````

**Response**
    
````json
  "USER ADDED!"
````

---

### Testing the API
Use **postman** or **curl** to verify each route



| Test | Method  | Endpoint   | Example Body                                       | Expected Response     |
| :--- | :------ | :--------- | :------------------------------------------------- | :-------------------- |
| 1    | GET     | `/users`   | —                                                  | Returns list of users |
| 2    | GET     | `/users/1` | —                                                  | Returns user by ID    |
| 3    | POST    | `/users/3` | `{"name":"Charlie","email":"charlie@mail.com"}`    | “USER ADDED!”         |
| 4    | PUT     | `/users/2` | `{"name":"Bob Updated","email":"bob@newmail.com"}` | “USER UPDATED!”       |
| 5    | OPTIONS | `/users/2` | —                                                  | “USER EXISTS true”    |
| 6    | DELETE  | `/users/3` | —                                                  | “USER DELETED!”       |

---

## Project Structure

````
CH6-Technoready/
├── src/
│   ├── main/
│   │   ├── java/com/techready/
│   │   │   ├── Main.java
│   │   │   └── user/
│   │   │       ├── User.java
│   │   │       └── UserService.java
│   │   └── resources/
│   │       └── logback.xml
│   └── test/        # optional
├── pom.xml
└── README.md
````