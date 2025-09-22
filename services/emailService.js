const nodemailer = require('nodemailer');

class EmailService {
  constructor() {
    this.transporter = null;
    this.initializeTransporter();
  }

  initializeTransporter() {
    // Check if email configuration is available
    if (!process.env.EMAIL_HOST || !process.env.EMAIL_USER || !process.env.EMAIL_PASS) {
      console.warn('⚠️  Email configuration not found. Email notifications will be logged only.');
      return;
    }

    try {
      this.transporter = nodemailer.createTransporter({
        host: process.env.EMAIL_HOST,
        port: parseInt(process.env.EMAIL_PORT) || 587,
        secure: false, // true for 465, false for other ports
        auth: {
          user: process.env.EMAIL_USER,
          pass: process.env.EMAIL_PASS,
        },
      });

      console.log('✅ Email service initialized successfully');
    } catch (error) {
      console.error('❌ Failed to initialize email service:', error.message);
    }
  }

  async sendApplicationNotification(application, recipients) {
    const subject = `New Application: ${application.position} - ${application.applicantName}`;
    
    const htmlContent = this.generateApplicationNotificationHTML(application);
    const textContent = this.generateApplicationNotificationText(application);

    const emailOptions = {
      from: process.env.EMAIL_FROM || process.env.EMAIL_USER,
      to: recipients.join(', '),
      subject: subject,
      text: textContent,
      html: htmlContent,
    };

    return this.sendEmail(emailOptions);
  }

  generateApplicationNotificationHTML(application) {
    return `
      <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
        <h2 style="color: #333; border-bottom: 2px solid #007bff; padding-bottom: 10px;">
          New Job Application Received
        </h2>
        
        <div style="background-color: #f8f9fa; padding: 20px; border-radius: 5px; margin: 20px 0;">
          <h3 style="color: #007bff; margin-top: 0;">Applicant Information</h3>
          <p><strong>Name:</strong> ${application.applicantName}</p>
          <p><strong>Email:</strong> <a href="mailto:${application.applicantEmail}">${application.applicantEmail}</a></p>
          <p><strong>Position:</strong> ${application.position}</p>
          <p><strong>Team:</strong> ${application.team}</p>
          <p><strong>Applied At:</strong> ${new Date(application.appliedAt).toLocaleString()}</p>
        </div>

        ${application.coverLetter ? `
          <div style="background-color: #fff; padding: 20px; border-left: 4px solid #007bff; margin: 20px 0;">
            <h3 style="color: #333; margin-top: 0;">Cover Letter</h3>
            <p style="white-space: pre-wrap;">${application.coverLetter}</p>
          </div>
        ` : ''}

        ${application.resumeUrl ? `
          <div style="margin: 20px 0;">
            <h3 style="color: #333;">Resume</h3>
            <p><a href="${application.resumeUrl}" style="color: #007bff; text-decoration: none;">📄 View Resume</a></p>
          </div>
        ` : ''}

        <div style="background-color: #e9ecef; padding: 15px; border-radius: 5px; margin-top: 30px; text-align: center;">
          <p style="margin: 0; color: #6c757d; font-size: 12px;">
            This is an automated notification from the ${process.env.APP_NAME || 'Executive Hiring Notification System'}
          </p>
        </div>
      </div>
    `;
  }

  generateApplicationNotificationText(application) {
    let text = `New Job Application Received\n\n`;
    text += `Applicant Information:\n`;
    text += `Name: ${application.applicantName}\n`;
    text += `Email: ${application.applicantEmail}\n`;
    text += `Position: ${application.position}\n`;
    text += `Team: ${application.team}\n`;
    text += `Applied At: ${new Date(application.appliedAt).toLocaleString()}\n\n`;

    if (application.coverLetter) {
      text += `Cover Letter:\n${application.coverLetter}\n\n`;
    }

    if (application.resumeUrl) {
      text += `Resume: ${application.resumeUrl}\n\n`;
    }

    text += `---\nThis is an automated notification from the ${process.env.APP_NAME || 'Executive Hiring Notification System'}`;

    return text;
  }

  async sendEmail(emailOptions) {
    if (!this.transporter) {
      // Log the email instead of sending it
      console.log('📧 EMAIL NOTIFICATION (not sent - no transporter configured):');
      console.log('To:', emailOptions.to);
      console.log('Subject:', emailOptions.subject);
      console.log('Content:', emailOptions.text);
      console.log('---');
      
      return {
        success: true,
        message: 'Email logged (transporter not configured)',
        messageId: 'mock-' + Date.now()
      };
    }

    try {
      const info = await this.transporter.sendMail(emailOptions);
      console.log('✅ Email sent successfully:', info.messageId);
      
      return {
        success: true,
        message: 'Email sent successfully',
        messageId: info.messageId
      };
    } catch (error) {
      console.error('❌ Failed to send email:', error.message);
      
      return {
        success: false,
        message: error.message,
        error: error
      };
    }
  }

  async testConnection() {
    if (!this.transporter) {
      return {
        success: false,
        message: 'Email transporter not configured'
      };
    }

    try {
      await this.transporter.verify();
      return {
        success: true,
        message: 'Email service connection successful'
      };
    } catch (error) {
      return {
        success: false,
        message: error.message
      };
    }
  }
}

module.exports = new EmailService();