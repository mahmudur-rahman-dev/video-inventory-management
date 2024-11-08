# Video Inventory Management

## Overview

This project is a Video Inventory Management system that consists of three main components: a MySQL database, a backend service built using Spring Boot, and a frontend built with Next.js. The application can be easily run using Docker Compose.

### Project Repository
The source code and Docker Compose file for this project can be found at the following GitHub repository:

[GitHub Repository](https://github.com/mahmudur-rahman-dev/video-inventory-management)

## Prerequisites
- Docker and Docker Compose should be installed on your system.

## Running the Project
To run the project locally using Docker Compose, follow these steps:

1. **Clone the Repository**
   ```sh
   git clone https://github.com/mahmudur-rahman-dev/video-inventory-management.git
   cd video-inventory-management
   ```

2. **Run Docker Compose**
   ```sh
   docker-compose up -d
   ```
   This command will start all the necessary services, including the MySQL database, the backend API, and the frontend UI. The `-d` flag runs the containers in detached mode.

3. **Access the Application**

   - **Backend API Swagger UI**: The backend provides a Swagger UI for easy interaction with the API. You can access it at:
     
     [Swagger UI](http://localhost:8080/swagger-ui/index.html#)
   
   - **Frontend Application**: The frontend of the application is accessible at:
     
     [Frontend UI](http://localhost:3000)

   - **Admin Access**: Admin Dashboard is accessible with the following credentials:
     
     ```sh
     username:admin password:pass
     ```

## Services Description

The Docker Compose file (`docker-compose.yml`) defines the following services:

1. **MySQL Database**
   - Uses the `mysql:latest` image.
   - Accessible on port `3306`.
   - Data is persisted in the `./mysql_data` directory, mapped to `/var/lib/mysql` inside the container.

2. **Backend (Spring Boot Application)**
   - Built from the `backend` directory.
   - Accessible on port `8080`.
   - Depends on the MySQL service and waits until it is healthy.

3. **Frontend (Next.js Application)**
   - Built from the `frontend` directory.
   - Accessible on port `3000`.
   - Depends on the backend service and connects to it for data.

## Environment Variables

The backend and frontend services are configured using environment variables specified in the `docker-compose.yml` file. Here are the most important ones:

- **Backend**:
  - `SPRING_DATASOURCE_URL`: URL for connecting to the MySQL database.
  - `SPRING_DATASOURCE_USERNAME`: Database username.
  - `SPRING_DATASOURCE_PASSWORD`: Database password.

- **Frontend**:
  - `NEXT_PUBLIC_API_BASE_URL`: Base URL for accessing the backend API.
  - `NEXT_PUBLIC_VIDEO_API_BASE_URL`: Base URL for accessing the video services.

## Stopping the Project
To stop the services, run:
```sh
docker-compose down
```
This will stop all running containers and remove any associated resources.

## Notes
- The `video_uploads` volume is used to persist uploaded video files.
- Please ensure that ports `3306`, `8080`, and `3000` are available on your local machine.

## Troubleshooting
- If you face any issues connecting to the MySQL database, ensure the `mysql_data` directory is empty before running `docker-compose up`.
- You can check the logs of a specific service to identify issues:
  ```sh
  docker-compose logs <service_name>
  ```
  For example, to check the logs of the backend service:
  ```sh
  docker-compose logs backend
  ```



