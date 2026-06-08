# Silent Butler — Smart Home Management

Full-stack smart home application with Angular 17 frontend and Spring Boot 3.5 backend.

## Project Structure

```
merged/
├── src/                    # Angular 17 standalone frontend
├── backend/
│   ├── src/                # Spring Boot backend (Java 17, Maven)
│   ├── pom.xml
│   ├── mvnw / mvnw.cmd
│   └── .mvn/
├── proxy.conf.json         # Dev proxy: /api -> localhost:8080
├── package.json
├── angular.json
├── .env.example
└── README.md
```

## Prerequisites

- Node.js 18+
- Angular CLI 17+
- Java 17 (JDK)
- PostgreSQL
- Docker & Docker Compose (optional, for containerized run)

```bash
npm install -g @angular/cli
```

## Quick Start (Local Development)

### 1. Database

Create a PostgreSQL database:

```sql
CREATE DATABASE silentbutler;
```

### 2. Backend

```bash
cd backend
```

**Linux / macOS:**
```bash
./mvnw clean install
./mvnw spring-boot:run
```

**Windows PowerShell:**
```powershell
.\mvnw.cmd clean install
.\mvnw.cmd spring-boot:run
```

Backend runs on `http://localhost:8080`.

### 3. Frontend

```bash
cd merged
npm install
npm start
```

Frontend runs on `http://localhost:4200` and proxies `/api` to `http://localhost:8080`.

### 4. Build for Production

```bash
npm run build      # Output: dist/silent-butler-frontend
```

## Quick Start (Docker)

Run the entire stack with a single command:

```bash
docker compose up --build
```

This starts three containers:

| Service | Host Port | Description |
|---|---|---|
| `db` | 5432 | PostgreSQL 16 |
| `backend` | 8080 | Spring Boot API |
| `frontend` | 4200 | Angular app served by nginx |

Open `http://localhost:4200` in your browser.

Optional: copy `.env.docker.example` to `.env` to override defaults:

```bash
cp .env.docker.example .env
```

### Docker Services

- **db** — PostgreSQL with healthcheck, persistent volume `pgdata`
- **backend** — Spring Boot on Java 17, waits for db healthcheck
- **frontend** — Multi-stage build: Node 18 → nginx 1.25

### Docker Environment Variables

| Variable | Default | Description |
|---|---|---|
| `DB_PASSWORD` | `silentbutler` | PostgreSQL password |
| `JWT_SECRET` | `my_super_secret_token_...` | JWT signing secret |
| `JWT_EXPIRATION` | `86400000` | JWT expiry in ms (24h) |

### Stop Containers

```bash
docker compose down
```

To also remove the database volume:

```bash
docker compose down -v
```

## Environment Variables (Local Development)

Copy `.env.example` and set values for your environment.

| Variable | Description | Default |
|---|---|---|
| `SPRING_DATASOURCE_URL` | PostgreSQL JDBC URL | `jdbc:postgresql://localhost:5432/silentbutler` |
| `SPRING_DATASOURCE_USERNAME` | DB user | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | DB password | `postgres` |
| `JWT_SECRET` | JWT signing secret | (see `.env.example`) |
| `JWT_EXPIRATION` | JWT expiry in ms | `3600000` (1 hour) |

## API Endpoints

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/api/auth/register` | Public | Register new user |
| POST | `/api/auth/login` | Public | Login, returns JWT |
| POST | `/api/auth/logout` | JWT | Revoke current token |
| GET | `/api/users/me` | JWT | Current user profile |
| PUT | `/api/users/me` | JWT | Update own profile |
| GET | `/api/users` | Admin | List all users |
| GET/PUT/DELETE | `/api/users/{id}` | Admin | User CRUD |
| GET | `/api/houses/mine` | JWT | Current user's houses |
| POST | `/api/houses` | JWT | Create a house |
| GET | `/api/rooms/house/{houseId}` | JWT | Rooms in a house |
| GET | `/api/rooms/{id}` | JWT | Room by ID |
| POST | `/api/rooms` | JWT | Create room in a house |
| DELETE | `/api/rooms/{id}` | JWT | Delete room |
| GET | `/api/devices/room/{roomId}` | JWT | Devices in a room |
| POST | `/api/devices` | JWT | Create device |
| PATCH | `/api/devices/{id}/toggle` | JWT | Toggle device on/off |
| DELETE | `/api/devices/{id}` | JWT | Delete device |
| GET | `/api/device-categories` | JWT | List device categories |

## Auth Flow

- Login stores JWT in `localStorage` under key `sb_token`
- Every API request includes `Authorization: Bearer <token>`
- Logout calls `POST /api/auth/logout` then clears `sb_token`

## Known TODOs

- Dashboard device count returns `0` — no backend endpoint exists yet for aggregated device counts per user
- No CORS configuration in backend `SecurityConfig` — relies on Angular dev proxy
- No frontend test framework configured
