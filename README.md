# Research Group Backend API

A Spring Boot REST API for the Prof. Dr. Yunus Research Group website.

## Tech Stack

- **Java 17**
- **Spring Boot 4.0.2**
- **PostgreSQL** (Database)
- **Cloudinary** (Image uploads)
- **Maven** (Build tool)

## Features

- Member management
- Publication management
- Project management
- News & Notices
- Admin authentication
- Google Scholar integration
- Image upload via Cloudinary

## Local Development

### Prerequisites

- Java 17+
- Maven 3.8+
- PostgreSQL database

### Environment Variables

Create the following environment variables for local development:

```properties
DATABASE_URL=jdbc:postgresql://localhost:5432/research
DATABASE_USERNAME=your_username
DATABASE_PASSWORD=your_password
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret
```

### Running Locally

```bash
# Build the project
./mvnw clean package -DskipTests

# Run the application
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`

## Deployment on Railway

### Environment Variables Required

Set these environment variables in Railway:

| Variable                | Description               |
| ----------------------- | ------------------------- |
| `DATABASE_URL`          | PostgreSQL connection URL |
| `DATABASE_USERNAME`     | Database username         |
| `DATABASE_PASSWORD`     | Database password         |
| `CLOUDINARY_CLOUD_NAME` | Cloudinary cloud name     |
| `CLOUDINARY_API_KEY`    | Cloudinary API key        |
| `CLOUDINARY_API_SECRET` | Cloudinary API secret     |

Railway will automatically:

- Detect this as a Java/Maven project
- Build using `mvn clean package`
- Use the `Procfile` for starting the application

## API Endpoints

Base URL: `/api`

- `/api/members` - Member management
- `/api/publications` - Publication management
- `/api/projects` - Project management
- `/api/news` - News articles
- `/api/notices` - Notices
- `/api/auth` - Authentication
- `/api/upload` - File uploads
- `/api/settings` - Site settings
- `/api/scholar` - Google Scholar integration

## Frontend

The frontend is deployed at: https://prof-dr-yunus-researchgroup.netlify.app/

## License

This project is private and proprietary.
