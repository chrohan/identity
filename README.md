# Bitespeed Identity Reconciliation – Backend Assignment

This is my submission for the **Bitespeed Backend Intern Task: Identity Reconciliation**.  
The goal is to consolidate user identities across multiple purchases, even if the customer uses different combinations of email and phone numbers.

---

## Hosted API Endpoint

**POST** [`https://identity-1pnk.onrender.com/identity`](https://identity-1pnk.onrender.com/identity)

### Sample Request (JSON)

```json
{
  "email": "roha99500@gmail.com",
  "phoneNumber": "123456"
}
```

### Sample Response

```json
{
  "contact": {
    "primaryContactId": 1,
    "emails": ["roha99500@gmail.com"],
    "phoneNumbers": ["123456"],
    "secondaryContactIds": []
  }
}
```

---

## Identity Reconciliation Logic

- If a new email/phone doesn't exist in the DB → a new **primary** contact is created.
- If either email or phone exists → a new **secondary** contact is created and linked.
- If both email and phone match different primaries → the **older one remains primary**, others are demoted.
- The `/identity` endpoint always returns a **consolidated view** of the identity group:
    - Primary contact ID
    - All emails and phone numbers in the group
    - Secondary contact IDs

---

## Tech Stack

| Layer         | Technology                      |
|---------------|---------------------------------|
| Language      | Java 21                          |
| Framework     | Spring Boot                     |
| Build Tool    | Maven                           |
| Database      | PostgreSQL (hosted on NeonDB)   |
| Container     | Docker (multi-stage build)      |
| Hosting       | Render.com (Docker Web Service) |

---

## Dockerization

This project uses a **multi-stage Docker build**:

1. **Stage 1 (Build)** – Uses Maven image to compile and package the app.
2. **Stage 2 (Run)** – Uses OpenJDK slim image to run the JAR.



### Run Locally with Docker

```bash
docker build -t identity .
docker run -p 8080:8080 identity
```

Then access:
```
POST http://localhost:8080/identity
```

---

## Database

- PostgreSQL hosted online using [NeonDB](https://neon.tech/)
- Secure SSL-enabled connection
- Connected using environment variables in `application.properties`:



---

## Project Structure

```
src/
├── controller/      # API Controller
├── dto/             # Request & Response DTOs
├── model/           # JPA Entity
├── repository/      # Spring Data JPA
└── service/         # Core Business Logic
```

---

## Testing the API

### Using `curl`:

```bash
curl --location 'https://identity-1pnk.onrender.com/identity' \
--header 'Content-Type: application/json' \
--data-raw '{
  "email": "roha99500@gmail.com",
  "phoneNumber": "123456"
}'
```

### Using Postman:

- Method: `POST`
- URL: `https://identity-1pnk.onrender.com/identity`
- Headers: `Content-Type: application/json`
- Body:
```json
{
  "email": "roha99500@gmail.com",
  "phoneNumber": "123456"
}
```

---

## Submission Checklist

- [x] Code pushed to GitHub
- [x] Dockerized build with multi-stage Dockerfile
- [x] App hosted on Render.com
- [x] Database hosted on NeonDB (Postgres)
- [x] `/identity` endpoint tested and documented
- [x] Professional `README.md` included

---

##Author

**Rohan Chauhan**  
🔗 [GitHub](https://github.com/chrohan)  
🔗 [LinkedIn](https://www.linkedin.com/in/rohan-chauhan2003/)  
📫 chrohan007@gmail.com

---

> This project was submitted as part of the BiteSpeed Backend Intern Task. Built with ❤️ using Spring Boot and Docker.