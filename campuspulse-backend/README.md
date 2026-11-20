# CampusPulse Backend
Spring Boot 3.5.7 · Java 25 · Spring Security · JWT · JPA · MySQL / TiDB.

## Local development (Eclipse)
1. Install JDK 25, Maven 3.9+ and MySQL 8+.
2. Create database: `CREATE DATABASE campuspulse CHARACTER SET utf8mb4;`
3. Configure environment variables from `.env.example` in Eclipse Run Configuration → Environment (the `.env` file is **not** auto-loaded by Spring).
4. Generate JWT secret with `openssl rand -base64 32`; set `JWT_SECRET` to its output.
5. Set `ADMIN_EMAIL` and a unique 12+ character `ADMIN_PASSWORD` only for first startup. Remove these environment variables after the admin is created.
6. Eclipse → File → Import → Existing Maven Projects; select backend folder; select JDK 25; Maven Update Project.
7. Run `CampusPulseApplication` as Java Application, or `mvn spring-boot:run`.
8. Check `http://localhost:8080/actuator/health`.
9. `mvn test` and `mvn package` run automated checks. Database schema is created/updated by Hibernate for MVP only.

## TiDB Cloud production
1. Create a TiDB Cloud cluster and database `campuspulse`.
2. Copy the cluster's **public connection endpoint**, port, username and password.
3. Obtain TiDB Cloud's required CA certificate and TLS connection instructions for your cluster; Render deployment may require packaging a truststore / using the JVM default truststore depending on the issuing CA.
4. Set `DB_URL=jdbc:mysql://HOST:4000/campuspulse?sslMode=VERIFY_IDENTITY` (replace host/port and verify provider-specific settings), `DB_USER`, `DB_PASSWORD`. Do **not** use `useSSL=false` or `trustServerCertificate=true`.
5. Validate TLS certificate and hostname before production use. TiDB Cloud MySQL-compatible JDBC protocol is supported by MySQL Connector/J.
6. Set pool max 5 and avoid exceeding your cluster connection limits.

## Render Docker Web Service
1. Push backend folder to GitHub (or set repository root directory to backend).
2. Render → New → Web Service → connect repository → Runtime Docker → Dockerfile `./Dockerfile`.
3. Configure `DB_URL`, `DB_USER`, `DB_PASSWORD`, `JWT_SECRET`, `FRONTEND_URL=https://YOUR-FRONTEND.onrender.com`, and initial admin bootstrap vars.
4. Health check path `/actuator/health`; Docker exposes 8080; Spring binds `PORT` supplied by Render.
5. Deploy and check `/actuator/health`. After admin creation, remove `ADMIN_PASSWORD`/`ADMIN_EMAIL` from Render environment to prevent further bootstrap.
6. Configure Render Static Site separately: build `npm install && npm run build`, publish `dist`, `VITE_API_URL=https://YOUR-BACKEND.onrender.com/api`, rewrite `/*` → `/index.html`.
7. Update backend `FRONTEND_URL` to the exact frontend origin (no path/trailing slash). Redeploy backend and frontend after environment changes.

## Security model
Authorization header: `Bearer <JWT>`. JWTs expire after 8 hours and are stored in browser `sessionStorage`, not cookies. This avoids ambient-cookie CSRF but leaves tokens accessible to injected JavaScript (XSS). Use strict CSP, no untrusted scripts, no dangerouslySetInnerHTML, and HTTPS. For production, consider same-site reverse proxy + Secure HttpOnly SameSite cookies **with CSRF tokens**. Logout clears local token; server-side revocation is not implemented. Login rate limiting and account lockout are recommended before public launch. Passwords use BCrypt; role is never accepted in signup. Admin is bootstrap-only, staff created by admins. Access and ownership are enforced on backend. CORS allows exact configured origin and OPTIONS preflight.

## REST endpoints
All paths start `/api`; authenticated endpoints require Authorization header except signup/login.
| Method | Path | Role | Purpose |
|---|---|---|---|
| POST | /auth/signup | Public | Student signup |
| POST | /auth/login | Public | Get JWT |
| GET | /auth/me | Any authenticated | Current user |
| PUT | /auth/profile | Any authenticated | Update name |
| POST | /ai/analyze | Any authenticated | Rule-based category/priority |
| GET | /departments | Any authenticated | Departments |
| POST | /complaints | STUDENT | Create issue |
| GET | /complaints/mine | STUDENT | Paginated own issues |
| GET | /complaints/{id} | Owner/assigned staff/admin | Details |
| GET | /complaints/{id}/history | Owner/assigned staff/admin | History |
| GET | /admin/complaints | ADMIN | Paginated all issues |
| PATCH | /admin/complaints/{id}/review | ADMIN | Review submitted issue |
| PATCH | /admin/complaints/{id}/reject | ADMIN | Reject submitted/review issue |
| POST | /admin/complaints/{id}/assign | ADMIN | Assign staff |
| PATCH | /admin/complaints/{id}/priority | ADMIN | Set priority |
| GET | /admin/users | ADMIN | List users |
| GET | /admin/staff | ADMIN | List staff |
| POST | /admin/staff | ADMIN | Create staff |
| GET | /admin/stats | ADMIN | Summary counts |
| GET | /staff/complaints | STAFF | Assigned issues |
| PATCH | /staff/complaints/{id}/status | STAFF | Progress/resolve |

Example requests:
`POST /api/auth/signup` `{"name":"Student Demo","email":"student@example.edu","password":"StrongDemo123!"}`
`POST /api/auth/login` `{"email":"student@example.edu","password":"StrongDemo123!"}`
Response: `{"token":"<JWT>","tokenType":"Bearer","user":{"id":1,"name":"Student Demo","email":"student@example.edu","role":"STUDENT"}}`
`POST /api/complaints` `{"title":"Wi-Fi outage in library","description":"The Wi-Fi network is unavailable on the second floor.","location":"Library second floor"}`
`POST /api/ai/analyze` `{"title":"Water leaking","description":"Water is leaking from the hostel tap"}`
`POST /api/admin/complaints/1/assign` `{"staffId":3}`
`PATCH /api/staff/complaints/1/status` `{"status":"IN_PROGRESS","note":"Inspection started"}`
Error: `{"message":"Access denied"}` with HTTP 403.
List query params: `page` (0-based), `size` (1–100), `search`, `status`.

## MVP scope / limitations
No external AI API; classification is explicitly keyword-based. No image uploads (persistent object storage required). No password reset, JWT revocation, email notifications, or user disable/delete workflow. Department catalog is seeded, but complaints are not auto-linked to departments. Pagination filtering currently loads records into memory and should be replaced by database queries for larger deployments. Hibernate `ddl-auto=update` should be replaced by Flyway migrations before production scale. Demo accounts should be created through signup and the protected admin staff-creation screen; never commit credentials.
