# Email-Backend

Spring Boot backend server to receive notifications via email for any applicants applying for executive and project lead positions.

## Features

- 📧 **Automated Email Notifications**: Sends email notifications to executives and project leads when new applications are submitted
- 🏢 **Team Management**: Organize teams with their respective executives and project leads
- 📊 **Application Tracking**: Track application status and view statistics
- 🔧 **Easy Configuration**: Environment-based configuration with Docker deployment
- 🚀 **RESTful API**: Clean REST API for integration with frontend applications
- 🐳 **Docker Ready**: Containerized application with Docker Compose for easy deployment
- 🔒 **Production Ready**: Includes nginx reverse proxy, monitoring, and security configurations

## Quick Start

### Option 1: Docker Deployment (Recommended)

#### Prerequisites
- Docker and Docker Compose installed
- Port 3000 available (or configure different port)

#### Basic Deployment
```bash
# Clone and navigate to repository
git clone <repository-url>
cd Email-Backend

# Deploy with Docker
./deploy.sh basic
```

#### Production Deployment with Nginx
```bash
# Deploy with reverse proxy
./deploy.sh production
```

#### Full Stack with Monitoring
```bash
# Deploy with Prometheus and Grafana monitoring
./deploy.sh monitoring
```

### Option 2: Manual Spring Boot Deployment

#### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

#### Build and Run
```bash
# Build the application
mvn clean package

# Run the application
java -jar target/email-backend-*.jar

# Or run in development mode
mvn spring-boot:run
```

## Configuration

### Environment Variables

Create a `.env` file (copy from `.env.docker`) and configure:

```env
# Server Configuration
PORT=3000
APP_NAME=Executive Hiring Notification System

# Email Configuration
EMAIL_HOST=smtp.gmail.com
EMAIL_PORT=587
EMAIL_USER=your-email@gmail.com
EMAIL_PASS=your-app-password
```

### Email Provider Examples

#### Gmail
```env
EMAIL_HOST=smtp.gmail.com
EMAIL_PORT=587
EMAIL_USER=your-email@gmail.com
EMAIL_PASS=your-app-password  # Use App Password, not regular password
```

#### Outlook/Hotmail
```env
EMAIL_HOST=smtp-mail.outlook.com
EMAIL_PORT=587
EMAIL_USER=your-email@outlook.com
EMAIL_PASS=your-password
```

#### Custom SMTP
```env
EMAIL_HOST=your-smtp-server.com
EMAIL_PORT=587
EMAIL_USER=your-email@domain.com
EMAIL_PASS=your-password
```

## API Endpoints

### Applications

- `POST /api/applications` - Submit a new application (triggers email notifications)
- `GET /api/applications` - Get all applications (with optional filtering by team/status)
- `GET /api/applications/{id}` - Get specific application
- `PATCH /api/applications/{id}/status` - Update application status
- `GET /api/applications/stats/summary` - Get application statistics

### Teams

- `GET /api/teams` - Get all teams
- `GET /api/teams/{id}` - Get specific team
- `POST /api/teams` - Create new team
- `PUT /api/teams/{id}` - Update team
- `DELETE /api/teams/{id}` - Delete team
- `POST /api/teams/{id}/test-email` - Send test email notification
- `GET /api/teams/email/status` - Check email service status

### System

- `GET /health` - Health check endpoint (Spring Boot Actuator)
- `GET /` - API information

## Usage Examples

### Submit an Application

```bash
curl -X POST http://localhost:3000/api/applications \
  -H "Content-Type: application/json" \
  -d '{
    "applicantName": "John Doe",
    "applicantEmail": "john@example.com",
    "position": "Senior Software Engineer",
    "team": "Engineering",
    "resumeUrl": "https://example.com/resume.pdf",
    "coverLetter": "I am excited to apply for this position..."
  }'
```

### Create a New Team

```bash
curl -X POST http://localhost:3000/api/teams \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Data Science",
    "description": "Data science and analytics team",
    "executives": ["cdo@company.com"],
    "projectLeads": ["data-lead@company.com"]
  }'
```

## Docker Deployment

### Basic Setup
```bash
# Start basic service
./deploy.sh basic

# Check status
./deploy.sh status

# View logs
./deploy.sh logs

# Stop services
./deploy.sh stop
```

### Production Setup
The production setup includes:
- Nginx reverse proxy with rate limiting
- SSL/HTTPS support (configure certificates)
- Security headers and gzip compression
- Health checks and monitoring

```bash
# Deploy production setup
./deploy.sh production

# Service available at http://localhost (port 80)
# Backend API at http://localhost:3000
```

### Monitoring Stack
Includes Prometheus and Grafana for monitoring:

```bash
# Deploy with monitoring
./deploy.sh monitoring

# Access points:
# - Application: http://localhost
# - Prometheus: http://localhost:9090
# - Grafana: http://localhost:3001 (admin/admin)
```

## Data Models

### Application
```json
{
  "id": "unique-id",
  "applicantName": "John Doe",
  "applicantEmail": "john@example.com",
  "position": "Senior Software Engineer",
  "team": "Engineering",
  "resumeUrl": "https://example.com/resume.pdf",
  "coverLetter": "Cover letter text...",
  "appliedAt": "2024-01-01T12:00:00",
  "status": "pending"
}
```

### Team
```json
{
  "id": "unique-id",
  "name": "Engineering",
  "description": "Software development team",
  "executives": ["cto@company.com"],
  "projectLeads": ["eng-lead@company.com"],
  "createdAt": "2024-01-01T12:00:00"
}
```

## Deployment Platforms

### NAS Servers
The Docker Compose setup is perfect for NAS servers like:
- **Synology NAS**: Use Container Manager
- **QNAP NAS**: Use Container Station
- **TrueNAS**: Use TrueCharts or custom deployment

```bash
# On NAS, clone repository and run:
./deploy.sh production
```

### Cloud Platforms

#### Firebase Hosting + Cloud Run
```bash
# Build and push to Google Container Registry
docker build -t gcr.io/your-project/email-backend .
docker push gcr.io/your-project/email-backend

# Deploy to Cloud Run
gcloud run deploy email-backend \
  --image gcr.io/your-project/email-backend \
  --platform managed \
  --allow-unauthenticated
```

#### AWS ECS/Fargate
Use the provided Dockerfile with AWS ECS task definitions.

#### DigitalOcean Apps
Connect your repository and use the Dockerfile for automatic deployment.

## Development

### Project Structure
```
src/
├── main/
│   ├── java/com/sfuosdev/emailbackend/
│   │   ├── controller/          # REST controllers
│   │   ├── service/             # Business logic
│   │   ├── model/               # Data models
│   │   ├── dto/                 # Data transfer objects
│   │   ├── config/              # Configuration classes
│   │   └── EmailBackendApplication.java
│   └── resources/
│       └── application.properties
├── test/                        # Unit and integration tests
├── Dockerfile                   # Container definition
├── docker-compose.yml          # Multi-container setup
├── deploy.sh                   # Deployment script
└── pom.xml                     # Maven dependencies
```

### Default Teams
The system comes with three pre-configured teams:
1. **Engineering** - Software development and technical roles
2. **Product** - Product management and design roles  
3. **Marketing** - Marketing and growth roles

### Building from Source
```bash
# Clone repository
git clone <repository-url>
cd Email-Backend

# Build with Maven
mvn clean package

# Run tests
mvn test

# Run application
mvn spring-boot:run
```

## Monitoring and Health Checks

### Health Endpoints
- `/health` - Application health status
- `/actuator/health` - Detailed health information
- `/actuator/info` - Application information

### Docker Health Checks
The Docker containers include built-in health checks that monitor application availability.

### Prometheus Metrics
When deployed with monitoring, metrics are available at `/actuator/prometheus`.

## Security

### Production Security Features
- CORS configuration for API access
- Rate limiting via nginx
- Security headers (X-Frame-Options, X-Content-Type-Options, etc.)
- Non-root user in Docker container
- Input validation and sanitization

### SSL/HTTPS Setup
Uncomment SSL configuration in `nginx.conf` and provide certificates:
```bash
# Create SSL directory and add certificates
mkdir ssl
# Add cert.pem and key.pem to ssl/ directory
# Update docker-compose.yml to mount SSL directory
```

## Troubleshooting

### Common Issues

1. **Email not sending**: Check email configuration in `.env` file
2. **Port conflicts**: Change PORT in `.env` or docker-compose.yml
3. **Permission issues**: Ensure Docker has proper permissions
4. **Memory issues**: Adjust JVM settings in Dockerfile if needed

### Logs and Debugging
```bash
# View application logs
./deploy.sh logs

# View all container logs
docker-compose logs

# Debug email configuration
curl http://localhost:3000/api/teams/email/status
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

MIT License
