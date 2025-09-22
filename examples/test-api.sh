#!/bin/bash

# Test script for Email Backend API
# Make sure the server is running: npm start

BASE_URL="http://localhost:3000"

echo "🚀 Testing Email Backend API"
echo "============================="
echo

# Test 1: Health check
echo "1. Health Check"
echo "curl -s $BASE_URL/health"
curl -s $BASE_URL/health | jq .
echo
echo

# Test 2: Get teams
echo "2. Get Teams"
echo "curl -s $BASE_URL/api/teams"
curl -s $BASE_URL/api/teams | jq .
echo
echo

# Test 3: Submit application
echo "3. Submit Application"
echo "curl -s -X POST $BASE_URL/api/applications \\"
echo "  -H 'Content-Type: application/json' \\"
echo "  -d '{ ... }'"

curl -s -X POST $BASE_URL/api/applications \
  -H "Content-Type: application/json" \
  -d '{
    "applicantName": "Test User",
    "applicantEmail": "test@example.com",
    "position": "Software Engineer",
    "team": "Engineering",
    "coverLetter": "This is a test application submission."
  }' | jq .
echo
echo

# Test 4: Get applications
echo "4. Get Applications"
echo "curl -s $BASE_URL/api/applications"
curl -s $BASE_URL/api/applications | jq .
echo
echo

# Test 5: Get statistics
echo "5. Get Statistics"
echo "curl -s $BASE_URL/api/applications/stats/summary"
curl -s $BASE_URL/api/applications/stats/summary | jq .
echo
echo

# Test 6: Create new team
echo "6. Create New Team"
echo "curl -s -X POST $BASE_URL/api/teams \\"
echo "  -H 'Content-Type: application/json' \\"
echo "  -d '{ ... }'"

curl -s -X POST $BASE_URL/api/teams \
  -H "Content-Type: application/json" \
  -d '{
    "name": "QA",
    "description": "Quality Assurance team",
    "executives": ["cqo@company.com"],
    "projectLeads": ["qa-lead@company.com"]
  }' | jq .
echo
echo

# Test 7: Error handling (invalid team)
echo "7. Error Handling (Invalid Team)"
echo "curl -s -X POST $BASE_URL/api/applications \\"
echo "  -H 'Content-Type: application/json' \\"
echo "  -d '{ \"team\": \"InvalidTeam\" }'"

curl -s -X POST $BASE_URL/api/applications \
  -H "Content-Type: application/json" \
  -d '{
    "applicantName": "Error Test",
    "applicantEmail": "error@example.com",
    "position": "Test Position",
    "team": "InvalidTeam"
  }' | jq .
echo
echo

echo "✅ API tests completed!"
echo
echo "💡 Tips:"
echo "  - Use 'npm run demo' for a complete interactive demo"
echo "  - Configure .env file for real email sending"
echo "  - Check server logs for email notifications"