**# isec

A Spring Boot application for managing notes with audit logging.

## Features

- Notes management (CRUD)
- Audit logging for actions
- RESTful API
- Configurable frontend URL

## Technologies

- Java
- Spring Boot
- Maven

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- Node.js (for frontend, if applicable)

### Setup

1. Clone the repository:

```bash
git clone git@github.com:infynitelooop/isec.git 
cd isec
```

2. Configure application properties in `src/main/resources/application.properties`:

```bash
   frontend.url=http://localhost:3000
   ```

3. Build and run the backend:
```bash
   mvn clean install mvn spring-boot:run
   ```
4. (Optional) Start the frontend:
```bash
   cd frontend npm install npm start
```

## API Endpoints

### Notes Management
- `GET /notes` - List notes
- `POST /notes` - Create note
- `PUT /notes/{id}` - Update note
- `DELETE /notes/{id}` - Delete note

### Authentication
- `POST /auth/signin` - User login
- `POST /auth/signup` - User registration
- `POST /auth/user` - Retrieve details of the authenticated user
- `POST /auth/username` - Retrieve the username of the authenticated user

### Admin Management
- `GET /admin/users` - List all users (Admin only)
- `DELETE /admin/users/{id}` - Delete user (Admin only)
- `GET /admin/roles` - List all roles (Admin only)
- `PUT /admin/update-loc-status` - Update location status (Admin only)
- `PUT /admin/update-expiry-status` - Update expiry status (Admin only)
- `PUT /admin/update-enabled-status` - Update enabled status (Admin only)
- `PUT /admin/update-credentials-expiry-status` - Update credentials expiry status (Admin only)
- `PUT /admin/update-password` - Update user password (Admin only)


### Audit Logs
- `GET /audit-logs` - View audit logs
- `GET /audit-logs/{id}` - View specific audit log

## Testing the API
You can test the API using **Swagger UI**, **Postman**, or **curl**.
### Using Swagger UI
1. Start your Spring Boot application:
```bash
mvn spring-boot:run
```
2. Open your browser:
```bash
http://localhost:8080/swagger-ui/index.html
```
3. Use the Swagger UI to interact with the API endpoints.

### Using Postman
1. Start your Spring Boot application:
```bash
mvn spring-boot:run
```
2. Download the swagger apecification from:
```bash
curl http://localhost:8080/v3/api-docs.yaml -o swagger.yaml
```
3. Import the specification into Postman and use it to interact with the API endpoints.
4. Use the provided endpoints to test various functionalities.
5. Make sure to include the JWT token in the `Authorization` header for protected endpoints.

   

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes
4. Push to the branch
5. Open a pull request

