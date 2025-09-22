class Application {
  constructor(data) {
    this.id = data.id || this.generateId();
    this.applicantName = data.applicantName;
    this.applicantEmail = data.applicantEmail;
    this.position = data.position;
    this.team = data.team;
    this.resumeUrl = data.resumeUrl;
    this.coverLetter = data.coverLetter;
    this.appliedAt = data.appliedAt || new Date().toISOString();
    this.status = data.status || 'pending';
  }

  generateId() {
    return Date.now().toString(36) + Math.random().toString(36).substr(2);
  }

  validate() {
    const required = ['applicantName', 'applicantEmail', 'position', 'team'];
    const missing = required.filter(field => !this[field]);
    
    if (missing.length > 0) {
      throw new Error(`Missing required fields: ${missing.join(', ')}`);
    }

    // Basic email validation
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(this.applicantEmail)) {
      throw new Error('Invalid email format');
    }

    return true;
  }

  toJSON() {
    return {
      id: this.id,
      applicantName: this.applicantName,
      applicantEmail: this.applicantEmail,
      position: this.position,
      team: this.team,
      resumeUrl: this.resumeUrl,
      coverLetter: this.coverLetter,
      appliedAt: this.appliedAt,
      status: this.status
    };
  }
}

module.exports = Application;