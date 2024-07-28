<h1 align="center">Welcome to File-Manager 👋</h1>
<p align="center"><img src="https://socialify.git.ci/LeonudMD/File-Manager/image?language=1&name=1&owner=1&stargazers=1&theme=Dark"></p>
<p>
  <img alt="Version" src="https://img.shields.io/badge/version-1.0-blue.svg?cacheSeconds=2592000" />
  <a href="http://localhost:8080/swagger-ui/index.html" target="_blank">
    <img alt="Documentation" src="https://img.shields.io/badge/documentation-yes-brightgreen.svg" />
  </a>
</p>

> RESTful backend applications in Java for file management: upload, download, delete, get file information and create one-time links. PostgreSQL is used for storing metadata and Minio for storing files. The project is deployed using Docker Compose.

# ⚙ Features

- CRUD operations for managing files
- RESTful APIs
- OpenAPI documentation
- Database integration with PostgreSQL
- Liquibase for database migrations
- File storage with Minio

## 💻 Used technologies

- **Java 17**
- **Spring Boot 3.3.1**
- **Spring Data JPA**
- **Spring Actuator**
- **Spring HATEOAS**
- **Spring Web**
- **SpringDoc OpenAPI**
- **Log4j 1.2.17**
- **PostgreSQL**
- **Liquibase**
- **Lombok**
- **MapStruct**
- **Minio**
- **Jackson Dataformat XML**
- **WebJars Locator**
- **Classgraph**
- **Commons Codec**

## 🛠️ Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/LeonudMD/File-Manager.git
   
   cd File-Manager

2. Build the project:
   ```bash
   ./mvnw clean install

4. Update the src/main/resources/application.properties file with your database credentials


## Environment Variables

To run this project, you will need to add the following environment variables to your .env file

PostgreSQL settings

`POSTGRES_USER`

`POSTGRES_PASSWORD`

`POSTGRES_DB`

`POSTGRES_PORT`

MinIO settings

`MINIO_ACCESS_KEY`

`MINIO_SECRET_KEY`

`MINIO_CONSOLE_PORT`

`MINIO_BUCKET_NAME`

Timezone and language

`TZ`

`LANG`

Docker image versions

`POSTGRES_VERSION`

`MINIO_VERSION`

Encryption salt

`SALT_SECRET_KEY`


## 🏃🏼‍♀️ Running the app

1. Run the application using Maven:
    ```bash
   ./mvnw spring-boot:run
    
2. The application will start on http://localhost:8080

## 🐋 Running with Docker Compose

1. Ensure Docker and Docker Compose are installed.

2. Build and start the containers:
   ```bash
   docker-compose up --build

3. The application will start on http://localhost:8080

## 📝 API Documentation

- Available on:
   ```bash
   http://localhost:8080/swagger-ui/index.html

## Author

👤 **Leonud**

* Github: [@LeonudMD](https://github.com/LeonudMD)

## Show your support

Give a ⭐️ if this project helped you!
