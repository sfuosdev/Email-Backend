# OSDev Email Backend

Receives executive applications from the club website and emails them to executives.

## Plan: Render Free + Resend HTTPS API

Keep the existing **Python/FastAPI backend and Docker setup**. Replace only the SMTP email delivery implementation with Resend's HTTPS API. No Cloudflare Worker or backend rewrite is planned.

This is a roadmap; updating this README does not implement the Resend integration or deploy the service.

```text
sfuosdev.com (GitHub Pages)
    -> FastAPI backend (Render Free)
    -> Resend HTTPS API
    -> Executive inbox
```

## Roadmap

### 1. Set up Resend

- [ ] Create a club-managed Resend account.
- [ ] Add and verify a sending domain, such as `sfuosdev.com`, using the DNS records provided by Resend.
- [ ] Create a sending API key and choose the sender and recipient addresses.

### 2. Update email delivery

- [ ] Add Resend's Python SDK to `requirements.txt`.
- [ ] Read the configuration below in `core/config.py` and update `.env.example` with placeholders.
- [ ] Adapt `services/email_sender.py` to send through the Resend HTTPS API instead of `smtplib`.
- [ ] Preserve the `send_application_email(...)` interface where practical, the existing API route, and the website's request format.
- [ ] Include personal information and all team questions and answers; keep the applicant's email as Reply-To.
- [ ] Handle provider failures and return success only after Resend accepts the request.
- [ ] Update tests to mock Resend and verify the complete email content.

Planned local `.env` settings:

```dotenv
RESEND_API_KEY=YOUR_RESEND_API_KEY
EMAIL_FROM=applications@sfuosdev.com
EMAIL_TO=executive@example.com
```

These settings must be implemented in the Python code; adding them alone does not activate Resend. Use a verified-domain address for `EMAIL_FROM`. Recipients can continue using Gmail. SMTP settings and Gmail app passwords are not needed for this sending method.

Do not commit `.env` or API keys. Keep the Resend key only in the backend environment.

### 3. Test locally with Docker

After implementing the sender and configuration:

```powershell
docker compose up --build -d
docker compose exec -T api python -m pytest -q
docker compose logs --tail=30 api
```

- [ ] Submit a test application from the local website.
- [ ] Confirm the email includes personal information and all team questions and answers.
- [ ] Confirm replies go to the applicant.
- [ ] Check invalid input and Resend failure responses.

For the local website, set `.env.development.local` and restart Vite:

```dotenv
VITE_LOCAL_ENDPOINT=http://localhost:8080
```

If `VITE_ENDPOINT` is also set, it takes priority. Automated tests should mock delivery; a real submission with real credentials sends a real email.

### 4. Deploy to Render Free

- [ ] Push the backend changes to GitHub, excluding secrets.
- [ ] Create a Render **Web Service**, connect this repository, and choose **Docker** and the **Free** instance type.
- [ ] Add `RESEND_API_KEY`, `EMAIL_FROM`, and `EMAIL_TO` in Render's Environment settings.
- [ ] Set `PORT=8080` to match the existing Dockerfile.
- [ ] Deploy and check `https://YOUR-SERVICE.onrender.com/docs`.
- [ ] Send a test application through the deployed API and confirm delivery.
- [ ] Configure allowed website origins and submission rate limits/bot protection before public launch. CORS alone does not prevent abuse.

Render builds the Dockerfile; Docker Compose remains the local development tool. Local `.env` values are not automatically copied to Render.

The HTTPS API avoids Render Free's SMTP port restrictions. No SMTP port connectivity test is needed for this plan.

### 5. Connect the live website

- [ ] Set the website's production build variable:

```dotenv
VITE_ENDPOINT=https://YOUR-SERVICE.onrender.com
```

- [ ] Make sure the website build workflow passes that variable to Vite.
- [ ] Rebuild and deploy the website to GitHub Pages.
- [ ] Submit from `sfuosdev.com` and confirm the complete email reaches the executive inbox.

Use the backend base URL only; the website appends `/api/submit/exec-form`. Never put the Resend key in the frontend or a `VITE_*` variable.

## Expected behavior

- Render Free sleeps after 15 minutes without inbound requests. The next request can take about one minute to wake the service before the application is processed.
- Test that the website handles the wait and prevents duplicate submissions.
- Success means Resend accepted the message; inbox delivery can still be delayed or fail.
- Free operation depends on Render and Resend usage limits. Existing domain renewal costs are separate.

## References

- [Send emails with Python and Resend](https://resend.com/docs/send-with-python)
- [Resend domain verification](https://resend.com/docs/dashboard/domains/introduction)
- [Render Docker deployment](https://render.com/docs/docker)
- [Render Free limitations](https://render.com/docs/free)
- [Resend free limits](https://resend.com/pricing)
