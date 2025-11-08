# 🧩 CH6 – Collectibles Store API

A lightweight web application built with **Java**, **Spark**, and **Mustache** for managing users and item offers.  
It features dynamic data filtering, real-time updates via WebSockets, form validation, and robust exception handling — all structured with modular Java classes and integrated logging through SLF4J + Logback.

---

## 🚀 Project Overview

The goal is to build an **API service** that supports basic user operations and demonstrates good backend practices using Spark, Gson, and Logback.
It offers an interactive and reliable platform to manage users and product offers in real time.


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

- **Java 17** + **Spark 2.9.4**
- **Mustache 2.7.1**
- **Gson 2.11.0**
- **Logback 1.4.14**
- **WebSockets (Jetty)**
- **Postman** (for API testing)


---

### 🧩 Steps

1. **Clone the repository**
    ```bash
    git clone https://github.com/Scar-AT/CH6-Technoready.git
    cd CH6-Technoready
    ```

2. **Compile the project**
    ```bash
    mvn clean install
    ```
    
3. **Run the server**
    ```bash
    mvn exec:java -Dexec.mainClass="com.techready.Main"
    ```

4. **Access the app**
   - Base URL → [http://localhost:4567](http://localhost:4567)
   - Health check → [http://localhost:4567/hello](http://localhost:4567/hello)

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

## Views and Offer Management
The project includes a Mustache-based frontend for managing collectible item offers.

### Routes
| Method   | Endpoint           | Description                                         |
|:---------| :------------------|:----------------------------------------------------|
| **GET**  |`/offers`           | Displays form and list of offers                    |
| **POST** |`/offers`           | Add a new offer                                     |
| **Error**| (handled globally) | Renders a friendly error page when exceptions ocurr |

#### Filters
Supports query parameters:
```
/offers?item=Keyboard&seller=Amazon&min=100&max=300
```

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
## 🧠 Form Behavior and Validation

**Client-side**

- HTML required fields prevent blank submissions.

**Server-side**

- Invalid price format triggers a custom validation exception.

- Duplicate offers are rejected with a clear message.

- Manual error simulation (simulateError=true) can be used for testing.

**Error Rendering**

- All exceptions are handled through Spark’s global exception() method.

- A Mustache template (error.mustache) provides a user-friendly display.


---

## Logging

Implemented with SLF4J + Logback, logging key actions and validation errors for better visibility.

### Sample output
````bash
15:27:41.113 [qtp181812446-14] INFO  com.techready.Main - Received offer submission: item=Laptop, price=1200, seller=Scarlett
15:27:42.002 [qtp181812446-14] ERROR com.techready.Main - Invalid price format received: abc
15:27:43.409 [qtp181812446-14] WARN  com.techready.Main - Simulated backend error triggered by user input.
````
Logs record successful actions, user input, validation errors and server exceptions.

---
## Modules and File structure
| Path                        | Description                                |
|:----------------------------|:-------------------------------------------|
| `com.techready.Main`        | Entry point and route configuration        |
| `com.techready.user.*`      | User model and CRUD service                |
| `com.techready.offer.*`     | Offer model and service logic              |
| `com.techready.exception.*` | Custom exception classes                   |
| `templates/`                | Mustache views (`index`, `offers`, `error`)|
| `logback.xml`               | Logging configuration                      |



---
## 🧩 Project Structure

```plaintext
CH6-Technoready/
├── src/
│   ├── main/
│   │   ├── java/com/techready/
│   │   │   ├── Main.java
│   │   │   ├── user/
│   │   │   │   ├── User.java
│   │   │   │   └── UserService.java
│   │   │   ├── offer/
│   │   │   │   ├── Offer.java
│   │   │   │   └── OfferService.java
│   │   │   ├── exception/
│   │   │   │   ├── AppException.java
│   │   │   │   ├── InvalidFormDataException.java
│   │   │   │   └── UserNotFoundException.java
│   │   │   └── websocket/
│   │   │       └── PriceWebSocket.java
│   │   └── resources/
│   │       ├── templates/
│   │       │   ├── index.mustache
│   │       │   ├── offers.mustache
│   │       │   ├── users-view.mustache
│   │       │   └── error.mustache
│   │       ├── public/css/users.css
│   │       └── logback.xml
│   └── test/
├── pom.xml
└── README.md
```