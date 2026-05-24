# Email Backend Microservice

```bash
# Run the frontend
npm run dev

# Start the server
docker compose up --build
```

## Localhost API Test

After the API is running on port 8080, run this smoke test:

```bash
bash scripts/test_localhost.sh
```

If your API is running on a different host or port:

```bash
bash scripts/test_localhost.sh http://localhost:9000
```

The script sends a valid request to `POST /api/submit/exec-form` and fails with a non-zero exit code if the endpoint does not return a successful response.
