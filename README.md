# FoodCal — Backend

Spring Boot REST API behind [FoodCal](https://foodcal-fn-three.vercel.app/), an AI nutrition
and fitness tracker. It owns authentication, the user's plan, every food log, weigh-ins, and
image storage.

**Frontend:** https://github.com/krixen-org/foodCal_fe

---

## What it's responsible for

The frontend talks to Gemini for *estimates* — what's in this photo. This service owns
everything that must be **true**: who the user is, what they're allowed to see, and what was
actually logged.

```
Next.js client
     │  Authorization: Bearer <JWT>
     ▼
┌──────────────────────────────────────────┐
│  Spring Boot                             │
│  ┌────────────────────────────────────┐  │
│  │ JwtAuthFilter                      │  │  validates signature + expiry,
│  │  → UserPrincipal in SecurityContext│  │  builds the principal
│  └──────────────┬─────────────────────┘  │
│   auth · user · fitness · food · weight  │  controllers check row ownership
│                 │                        │
└─────────────────┼────────────────────────┘
                  ├──────────────► PostgreSQL   (schema owned by Flyway)
                  └──────────────► Supabase Storage (avatars, meal photos)
```

There are no sessions. Every request carries its own proof, and the server keeps no state
between them.

---

## Stack

| | |
|---|---|
| Runtime | Java 17 |
| Framework | Spring Boot 4 (Web MVC, Data JPA, Security) |
| Database | PostgreSQL |
| Migrations | Flyway |
| Auth | JJWT (HS256) + BCrypt |
| Storage | Supabase Storage over `RestClient` |
| Build | Maven |
| Deploy | Docker → Render |

---

## Quick start

**Prerequisites:** JDK 17, a PostgreSQL database, and a Supabase project (for image buckets).

```bash
cp .env.example .env     # then fill in the values below
./mvnw spring-boot:run   # http://localhost:8080
```

Verify it's alive — this is the one endpoint that needs no token:

```bash
curl http://localhost:8080/api/v1/auth/test     # → Hello World
```

Flyway runs every pending migration on boot, so an empty database becomes a correct one with
no extra step.

### Environment variables

`application.properties` imports `.env` if present, so local dev needs no exported shell vars.

| Variable | What it's for |
|---|---|
| `DB_HOST` `DB_PORT` `DB_NAME` `DB_USER` `DB_PASSWORD` | PostgreSQL connection |
| `JWT_SECRET` | HMAC signing key. Must be ≥ 32 bytes for HS256 |
| `JWT_ACCESS_EXPIRATION` | Access token lifetime, **milliseconds** |
| `JWT_REFRESH_EXPIRATION` | Refresh token lifetime, **milliseconds** |
| `FRONTEND_URL` | Allowed CORS origin(s), comma-separated |
| `SUPABASE_URL` | Supabase project URL |
| `SUPABASE_SERVICE_ROLE_KEY` | Service-role key — server-side only, never ship it to a client |
| `SUPABASE_AVATAR_BUCKET` | Bucket for profile pictures |
| `SUPABASE_MEAL_IMAGE_BUCKET` | Bucket for meal photos |
| `PORT` | Optional, defaults to 8080 |

The connection string sets `sslmode=require` and `prepareThreshold=0` — the latter disables
server-side prepared statements, which is what a transaction-pooling connection pooler
(Supabase/PgBouncer) requires.

---

## API reference

Base path `/api/v1`. Everything except the four public endpoints needs
`Authorization: Bearer <accessToken>`; anything unmatched is denied by default.

### Auth — `/api/v1/auth`

| Method | Path | Auth | Purpose |
|---|---|---|---|
| POST | `/signup` | public | Create an account, returns tokens |
| POST | `/login` | public | Exchange credentials for tokens |
| POST | `/refresh-token` | public | Exchange a refresh token for a new pair |
| GET | `/test` | public | Health check |
| DELETE | `/{userID}` | required | Delete an account |

```http
POST /api/v1/auth/signup
{ "fullName": "Alex Doe", "email": "alex@example.com", "password": "..." , "gender": "male" }

201 Created
{
  "accessToken": "eyJhbGciOi...",
  "refreshToken": "eyJhbGciOi...",
  "tokenType": "Bearer",
  "user": { "id": "uuid", "fullName": "Alex Doe", "email": "alex@example.com",
            "gender": "male", "avatarUrl": null, "role": "USER" }
}
```

Email is normalised to lowercase and checked against a unique index, so casing can't create a
duplicate account. Passwords are BCrypt-hashed and the hash is never returned. Login answers a
missing user and a wrong password with the same `Invalid email or password`, so the endpoint
can't be used to enumerate registered emails.

### User — `/api/v1/user`

| Method | Path | Purpose |
|---|---|---|
| PUT | `/update` | Update the caller's profile (acts on the token's user) |
| GET | `/{userID}` | Fetch a profile |
| POST | `/avatar/{userID}` | Upload a profile picture — `multipart/form-data`, field `file` |

### Fitness plan — `/api/v1/fitness`

| Method | Path | Purpose |
|---|---|---|
| GET | `/get` | The caller's plan: body stats, goal, daily calorie and macro targets |
| PUT | `/update` | Replace it |

Both derive the user from the JWT — there is no ID in the path to tamper with.

### Food logs — `/api/v1/food`

| Method | Path | Purpose |
|---|---|---|
| POST | `/{userID}` | Log a meal — `multipart/form-data`: `file` (photo) + `foodData` (JSON) |
| GET | `/stats/{date}` | Totals for one day: calories, protein, carbs, fat |
| GET | `/logs/{date}` | Every meal logged on that day |
| GET | `/history/allLogs` | Daily totals across all time, keyed by date |

```http
POST /api/v1/food/{userID}
Content-Type: multipart/form-data
  file=@meal.jpg
  foodData={"foodName":"Chicken biryani","quantity":"1 bowl (~320 g)","calories":610,
            "proteinG":32,"carbohydrateG":68,"fatG":24,"aiConfidence":0.78,
            "mealType":"lunch","isManual":false,"date":"2026-09-23"}
```

The photo goes to Supabase Storage and the row stores the returned public URL. `{userID}` must
match the authenticated user or the request is rejected with **403**. Dates use `LocalDate`,
so the client sends the user's *local* day rather than a UTC timestamp — otherwise a late
dinner lands on tomorrow.

### Weight — `/api/v1/weight`

| Method | Path | Purpose |
|---|---|---|
| POST | `/log` | Record a weigh-in |
| GET | `/{userId}` | Full weigh-in history, newest first |

A unique index on `(user_id, logged_on)` enforces one weigh-in per day, so re-weighing updates
rather than duplicating.

### Errors

`GlobalExceptionHandler` returns a consistent `{ "message": "..." }` body:

| Status | When |
|---|---|
| 400 | `InvalidRequestException` — bad input, failed login |
| 401 | Missing, malformed or expired token |
| 403 | Authenticated, but the resource belongs to someone else |
| 409 | `DuplicateResourceException` or a unique-constraint violation |

---

## Data model

All primary keys are UUIDs (migrated from bigserial in `V4`), so an ID in a URL reveals nothing
about how many users exist and can't be walked by incrementing.

| Table | Holds | Notes |
|---|---|---|
| `user_details` | identity, email, password hash, avatar, role | unique on `LOWER(email)` and `LOWER(user_name)` |
| `fitness_details` | body stats, goal, daily calorie + macro targets | one row per user (`UNIQUE user_id`) |
| `food_log` | one meal: name, quantity, macros, AI confidence, notes, photo URL, `is_manual` | `date` is the user's local day |
| `weight` | one weigh-in | unique `(user_id, logged_on)`; index on `(user_id, logged_on DESC)` |
| `notification` | in-app notifications | entity exists, delivery not wired up yet |
| `subscription` | billing plan placeholder | not in use |

`ai_confidence` and `is_manual` are stored per log deliberately: an AI estimate and a
hand-entered meal are different kinds of data, and the UI needs to tell them apart.

---

## Security

- **BCrypt** password hashing, with an adaptive work factor.
- **Stateless JWT.** `JwtAuthFilter` runs before Spring's username/password filter, validates
  the signature and expiry, and puts a `UserPrincipal` (id, email, name, role) in the security
  context. Access and refresh tokens carry a `type` claim so one can't be used as the other.
- **Deny by default.** Four endpoints are public; `/api/v1/**` requires authentication;
  `/api/v1/admin/**` requires the `ADMIN` authority; `anyRequest().denyAll()`. A new controller
  is protected the moment it exists, without anyone remembering to guard it.
- **Ownership checks.** Authentication answers *who are you*; these answer *is this yours*.
  Routes with a user ID in the path compare it to the token's subject and return 403 on a
  mismatch. Most endpoints avoid the question entirely by taking the ID from the token.
- **CORS** is restricted to `FRONTEND_URL`, comma-split and trailing-slash trimmed so multiple
  deploy origins work. `allowCredentials` is on, which is why the origin list cannot be `*`.
- **Uploads** are capped at 2 MB and restricted to JPEG, PNG and WebP by MIME type.

---

## Migrations

Flyway owns the schema; Hibernate is set to `ddl-auto=validate`, so the app refuses to start if
the entities and the tables disagree. That turns a schema drift into a boot failure instead of
a mystery at runtime.

```bash
src/main/resources/db/migration/V{n}__{description}.sql
```

Add the next number, never edit a migration that has run. The 14 files there are the honest
history of the project — bigserial to UUID, adding password credentials, splitting gender out
of `user_details`, adding a local `date` to food logs.

---

## Project structure

```
src/main/java/com/foodcal/foodcal_backend/
├─ config/        Spring Security + CORS
├─ controller/    HTTP layer: auth, user, fitness, food, weight
├─ service/       business logic + Supabase storage client
├─ repository/    Spring Data JPA interfaces
├─ entity/        JPA entities mapped to the Flyway schema
├─ dto/           request/response shapes (entities aren't exposed raw for auth)
├─ security/      JwtUtil, JwtAuthFilter, UserPrincipal
└─ exception/     typed exceptions + @RestControllerAdvice
```

---

## Docker and deployment

```bash
docker build -t foodcal-backend .
docker run -p 8080:8080 --env-file .env foodcal-backend
```

Multi-stage build: Maven image compiles, a JRE image runs, so the shipped image carries no
build toolchain.

The JVM flags in the Dockerfile are tuned for Render's free tier (512 MB, a fraction of a CPU),
where cold-start time is what users actually feel:

```
-XX:MaxRAMPercentage=75   # default heap there is ~128 MB, which causes GC thrashing on boot
-XX:+UseSerialGC          # parallel GC costs more than it returns on a fractional CPU
-XX:TieredStopAtLevel=1   # C1-only JIT: slower peak, much faster start
-Xss512k
```

The trade is deliberate — a little peak throughput for a cold start that doesn't stretch past a
minute. The frontend also holds requests during a cold start rather than failing them.

---

## Known gaps

- `Notification` and `Subscription` entities and tables exist, but nothing writes to them yet —
  push delivery and billing aren't implemented.
- `GET /api/v1/user/{userID}` and `DELETE /api/v1/auth/{userID}` don't compare the path ID to the
  authenticated user the way the food, weight and avatar routes do.
- `WeightController` throws a bare `RuntimeException` on an ownership mismatch, which surfaces
  as 500 rather than 403.
- Test coverage is a context-load test only.
