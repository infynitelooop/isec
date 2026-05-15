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
git clone git@github.com:infyniteloop/isec.git 
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

### Importing Test Data
1. In MySql workbench menu, go to 
    Server -> Data Import
2. Select the folder and select the schema and import
3. The test data is in *main/java/resources* folder


### Running MySQL in docker 
```bash
docker run -d --name db --network running-network -e MYSQL_ROOT_PASSWORD=96Cse@124 -e MYSQL_DATABASE=isecure -e MYSQL_USER=user -e MYSQL_PASSWORD=userpass -p 3306:3306 mysql:8
```

### Running backend in docker
```bash
docker run -e JWT_PRIVATE_KEY=MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDYzZjDAsJHRs08VEQKYpNCuYEQ6NgKaY73viYfVJZt/+B2IsVvppZhGWqi/x69cSNf/6O2DyCFDwKtcJYbEjDXLD4ly5somZSgYGWmMJNi+Yxa9kAzZwgp3/rgiPDrc8MGt6IbdhzQBlhbLm8gbr760kkDMSsu4aAG0utKuLQdMaBqe5y86M8Ce4Wj7c9WOkaJwgW+/aBF8tVkVaZ4MuuzDeiCW0L3XO6/LX3gloDaLC+qZaqMT9egzpKR1Ux3qlp+KxpF8G2GJ1HwgtUlOM9yaVpVw5qBWGubBLYS0MGKkShrj0oCF/xMZFzV78EjmO0+QHaAu0RmIRjfeClwLP83AgMBAAECggEBAIUJDB9GFx8TtCa78pCI0qJUIH3W05L4GTGurcf16OUQ6ZS0W4DasVZd+VCRyjQnM7TZsoSqHaNohZUWDBYKgIvY6z3YBZrROyF6ZYWoMxi91sGOb/Da3WiMt26GuYdRnrAFiOTfAmkoK5mwEaLOMkTJOuJJjp0S+FaXoKhWVLfANUMnSiuy0ut5VEwt5o1T55/VxmRBcXRhnX9FV3ZWtbPfEc7Y90VKeZAwKPWS5YJwkMkgmDI8x9GUJ0Ihq9Ud0xpCk6qgRbuyQpiWLORnUsgjo6YqB69AdaJt6qXpqyIBsZOswwj9JH1NHUkM924Ue2YEo49LDHf3DyxDq/eSnqkCgYEA/IaBKm2lck2fFX6Ey2R6wmtrBUOLYf9c76g+JCpArpok3554Q29rmR1nchXL9cD6/MjTjIRuG/sF7hKmgsBhKR1lV/9IzF7LjCxslZeBKz37KzRwt3zS9G6GphNKBixUezG9iWEk9rCMtX/hvVEMKOCgb6XEGlJGjsTli49783MCgYEA28lDkxfuiLyZrNjVvqUbhwXhRYm9DkkCrTuWNxuFkxBCuWuWasLSS73ceMasZp0saqdabwlVVDOJhuAAG56Wvu70TlQMfjmSL3VCx71Oe95F8OMEfJwITv5hrU3a8c90HnICCe0J09T1b3gPJR1+q4+63hu/Bqoj3bW+/bnK/C0CgYBNIgB+JzOr8E2xUQ3a2nwsuobb+r+uVUPb6DXi8g0DyA2PSGZPos7o2/RnAoVkMWf4opcLir/fSBONnqDBu2VyG4qDEHT6B9KApGkeYEKSJMJ9de0f/r6o7sGuJKIttIixgIX2Wnfh6hdwsPkQzJq8ObCHqch2ib8HiNjcgh1IRQKBgBivnXBrEra3AEfCvxc/sjzTMz89PkfZeXx9odyksbcX73amF+n74eTx+U5zei+4XaHJFEY+dSAU/dBxXnG7uVDOxA/8Lr8KtlmwQQTwRV1yVMa+qOVaHTDKL6xofzwDwhYdVg9JWX46gCq6AHF5w51/cky6LxWPjKG9ZStYb+0tAoGBAI8mbwsBUKOtMIS7k2H++McnJ4OdPwaYU4TxYj2z59N4G/T2ioHChhw+uCoi/T+Mo0FkiC+BujJe0H2YNOLbaowZrGEK5KDMxjDnRrCYun1i4dow6DVYoMAHqjaYednZGjIfH42JhHsmKtNxSaR9gXSLgnDOnOl5D7DEX6cRLEIA -e JWT_PUBLIC_KEY=MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2M2YwwLCR0bNPFRECmKTQrmBEOjYCmmO974mH1SWbf/gdiLFb6aWYRlqov8evXEjX/+jtg8ghQ8CrXCWGxIw1yw+JcubKJmUoGBlpjCTYvmMWvZAM2cIKd/64Ijw63PDBreiG3Yc0AZYWy5vIG6++tJJAzErLuGgBtLrSri0HTGganucvOjPAnuFo+3PVjpGicIFvv2gRfLVZFWmeDLrsw3ogltC91zuvy194JaA2iwvqmWqjE/XoM6SkdVMd6pafisaRfBthidR8ILVJTjPcmlaVcOagVhrmwS2EtDBipEoa49KAhf8TGRc1e/BI5jtPkB2gLtEZiEY33gpcCz/NwIDAQAB -e SPRING_DATASOURCE_USERNAME=root -e SPRING_DATASOURCE_PASSWORD=96Cse@124 --name=isec --network=running-network -p 8080:8080 infyniteloop/isec

```

### Passwords for testing

Put a breakpoint in AuthController signin method and use encoder.encode on any string to generate encrypted password

```aiignore
SET SQL_SAFE_UPDATES = 0;
UPDATE isecure.users SET `password`='$2a$10$mXa0HCAmv7C9eunESbWD/OL0oK.2lZdWAf.QSYGj.zwJkGc0mSsEu';
```



## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes
4. Push to the branch
5. Open a pull request

