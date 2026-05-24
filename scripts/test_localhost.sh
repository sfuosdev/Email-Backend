#!/usr/bin/env bash
set -euo pipefail

BASE_URL="${1:-http://localhost:8080}"
ENDPOINT="${BASE_URL%/}/api/submit/exec-form"

PAYLOAD='{
  "fullName": "Local Test User",
  "email": "local.test@example.com",
  "studentId": "12345678",
  "yearOfStudy": "3",
  "faculty": "Engineering",
  "major": "Software Engineering",
  "coopTerm": "no",
  "firstChoiceTeam": "TECHNOLOGY",
  "questions": {
    "tech_q1": "I enjoy building backend services.",
    "tech_q2": "I want to grow in API design.",
    "tech_q3": "Python and FastAPI.",
    "tech_q4": "I collaborate through pull requests and code reviews."
  }
}'

TMP_BODY="$(mktemp)"
trap 'rm -f "$TMP_BODY"' EXIT

HTTP_CODE="$(curl -sS -o "$TMP_BODY" -w "%{http_code}" \
  -X POST "$ENDPOINT" \
  -H "Content-Type: application/json" \
  -d "$PAYLOAD")"

BODY="$(cat "$TMP_BODY")"

if [[ "$HTTP_CODE" != "200" ]]; then
  echo "Localhost test failed: expected HTTP 200, got HTTP $HTTP_CODE"
  echo "Endpoint: $ENDPOINT"
  echo "Response body:"
  echo "$BODY"
  exit 1
fi

if [[ "$BODY" != *'"message":"Submission received successfully"'* ]]; then
  echo "Localhost test failed: success message not found in response"
  echo "Endpoint: $ENDPOINT"
  echo "Response body:"
  echo "$BODY"
  exit 1
fi

echo "Localhost test passed"
echo "Endpoint: $ENDPOINT"
echo "HTTP $HTTP_CODE"
echo "$BODY"
