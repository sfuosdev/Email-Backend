#!/bin/bash

# Email Backend Deployment Script
# This script helps deploy the Spring Boot email backend using Docker Compose

set -e

echo "🚀 Email Backend Deployment Script"
echo "=================================="

# Check if Docker and Docker Compose are installed
if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed. Please install Docker first."
    exit 1
fi

if ! command -v docker-compose &> /dev/null && ! docker compose version &> /dev/null; then
    echo "❌ Docker Compose is not installed. Please install Docker Compose first."
    exit 1
fi

# Function to deploy basic setup
deploy_basic() {
    echo "📦 Deploying basic email backend..."
    
    # Create data directory
    mkdir -p data
    
    # Copy environment template if .env doesn't exist
    if [ ! -f .env ]; then
        echo "📝 Creating .env file from template..."
        cp .env.docker .env
        echo "⚠️  Please edit .env file with your email configuration before starting the service"
    fi
    
    # Build and start the service
    docker-compose up -d --build
    
    echo "✅ Basic deployment completed!"
    echo "🌐 Service will be available at: http://localhost:3000"
    echo "📧 Configure email settings in .env file and restart if needed"
}

# Function to deploy with nginx proxy
deploy_production() {
    echo "🏭 Deploying production setup with nginx..."
    
    # Create data directory
    mkdir -p data
    
    # Copy environment template if .env doesn't exist
    if [ ! -f .env ]; then
        cp .env.docker .env
        echo "⚠️  Please edit .env file with your configuration"
    fi
    
    # Deploy with production profile
    docker-compose --profile production up -d --build
    
    echo "✅ Production deployment completed!"
    echo "🌐 Service available at: http://localhost (port 80)"
    echo "🌐 Direct backend access: http://localhost:3000"
}

# Function to deploy with monitoring
deploy_monitoring() {
    echo "📊 Deploying with monitoring (Prometheus + Grafana)..."
    
    mkdir -p data monitoring
    
    # Create Prometheus config if it doesn't exist
    if [ ! -f monitoring/prometheus.yml ]; then
        cat > monitoring/prometheus.yml << EOF
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'email-backend'
    static_configs:
      - targets: ['email-backend:3000']
    metrics_path: '/actuator/prometheus'
    scrape_interval: 5s
EOF
    fi
    
    if [ ! -f .env ]; then
        cp .env.docker .env
    fi
    
    # Deploy with monitoring profile
    docker-compose --profile production --profile monitoring up -d --build
    
    echo "✅ Monitoring deployment completed!"
    echo "🌐 Application: http://localhost"
    echo "📊 Prometheus: http://localhost:9090"
    echo "📈 Grafana: http://localhost:3001 (admin/admin)"
}

# Function to stop services
stop_services() {
    echo "🛑 Stopping email backend services..."
    docker-compose --profile production --profile monitoring down
    echo "✅ Services stopped"
}

# Function to show logs
show_logs() {
    echo "📋 Showing email backend logs..."
    docker-compose logs -f email-backend
}

# Function to show status
show_status() {
    echo "📊 Email Backend Status"
    echo "======================"
    docker-compose ps
    echo ""
    echo "🔍 Health Check:"
    curl -s http://localhost:3000/health | jq . || echo "Service not responding"
}

# Main menu
case "${1:-menu}" in
    "basic")
        deploy_basic
        ;;
    "production")
        deploy_production
        ;;
    "monitoring")
        deploy_monitoring
        ;;
    "stop")
        stop_services
        ;;
    "logs")
        show_logs
        ;;
    "status")
        show_status
        ;;
    "menu"|*)
        echo ""
        echo "Usage: $0 [command]"
        echo ""
        echo "Commands:"
        echo "  basic       - Deploy basic setup (backend only)"
        echo "  production  - Deploy with nginx reverse proxy"
        echo "  monitoring  - Deploy with nginx + monitoring stack"
        echo "  stop        - Stop all services"
        echo "  logs        - Show application logs"
        echo "  status      - Show service status"
        echo ""
        echo "Examples:"
        echo "  $0 basic      # Simple deployment for development"
        echo "  $0 production # Production deployment with nginx"
        echo "  $0 monitoring # Full stack with monitoring"
        echo ""
        echo "📝 Configuration:"
        echo "  - Edit .env file for email settings"
        echo "  - Edit docker-compose.yml for advanced configuration"
        echo "  - Edit nginx.conf for proxy settings"
        ;;
esac