# Email-Backend

Backend server to receive notification via email for any applicants applying for executive and project lead positions.

## Features

- 📧 **Automated Email Notifications**: Sends email notifications to executives and project leads when new applications are submitted
- 🏢 **Team Management**: Organize teams with their respective executives and project leads
- 📊 **Application Tracking**: Track application status and view statistics
- 🔧 **Easy Configuration**: Simple environment-based configuration
- 🚀 **RESTful API**: Clean REST API for integration with frontend applications

## Quick Start

### 1. Installation

```bash
npm install
```

### 2. Configuration

Copy the example environment file and configure your settings:

```bash
cp .env.example .env
```

Edit `.env` with your email configuration:

```env
# Server Configuration
PORT=3000
NODE_ENV=development

# Email Configuration (Gmail example)
EMAIL_HOST=smtp.gmail.com
EMAIL_PORT=587
EMAIL_USER=your-email@gmail.com
EMAIL_PASS=your-app-password
EMAIL_FROM=noreply@yourcompany.com

# Application Configuration
APP_NAME=Executive Hiring Notification System
```

### 3. Start the Server

```bash
# Development mode with auto-reload
npm run dev

# Production mode
npm start
```

The server will start on `http://localhost:3000` (or your configured PORT).

## API Endpoints

### Applications

- `POST /api/applications` - Submit a new application
- `GET /api/applications` - Get all applications (with optional filtering)
- `GET /api/applications/:id` - Get specific application
- `PATCH /api/applications/:id/status` - Update application status
- `GET /api/applications/stats/summary` - Get application statistics

### Teams

- `GET /api/teams` - Get all teams
- `GET /api/teams/:id` - Get specific team
- `POST /api/teams` - Create new team
- `PUT /api/teams/:id` - Update team
- `DELETE /api/teams/:id` - Delete team
- `POST /api/teams/:id/test-email` - Send test email notification
- `GET /api/teams/email/status` - Check email service status

### System

- `GET /health` - Health check endpoint
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

### Get All Applications for a Team

```bash
curl "http://localhost:3000/api/applications?team=Engineering"
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
  "appliedAt": "2024-01-01T12:00:00.000Z",
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
  "createdAt": "2024-01-01T12:00:00.000Z"
}
```

## Email Configuration

The system supports various email providers. Here are some common configurations:

### Gmail

```env
EMAIL_HOST=smtp.gmail.com
EMAIL_PORT=587
EMAIL_USER=your-email@gmail.com
EMAIL_PASS=your-app-password  # Use App Password, not regular password
```

### Outlook/Hotmail

```env
EMAIL_HOST=smtp-mail.outlook.com
EMAIL_PORT=587
EMAIL_USER=your-email@outlook.com
EMAIL_PASS=your-password
```

### Custom SMTP

```env
EMAIL_HOST=your-smtp-server.com
EMAIL_PORT=587
EMAIL_USER=your-email@domain.com
EMAIL_PASS=your-password
```

## Development

### Project Structure

```
├── index.js              # Main server file
├── models/               # Data models
│   ├── Application.js
│   └── Team.js
├── routes/               # API routes
│   ├── applications.js
│   └── teams.js
├── services/             # Business logic
│   ├── emailService.js
│   └── dataService.js
└── data/                 # JSON data storage
    ├── applications.json
    └── teams.json
```

### Default Teams

The system comes with three pre-configured teams:

1. **Engineering** - Software development and technical roles
2. **Product** - Product management and design roles  
3. **Marketing** - Marketing and growth roles

You can modify these or add new teams via the API.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

MIT License
