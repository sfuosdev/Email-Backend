class Team {
  constructor(data) {
    this.id = data.id || this.generateId();
    this.name = data.name;
    this.description = data.description;
    this.executives = data.executives || []; // Array of executive email addresses
    this.projectLeads = data.projectLeads || []; // Array of project lead email addresses
    this.createdAt = data.createdAt || new Date().toISOString();
  }

  generateId() {
    return Date.now().toString(36) + Math.random().toString(36).substr(2);
  }

  validate() {
    if (!this.name) {
      throw new Error('Team name is required');
    }

    if (this.executives.length === 0 && this.projectLeads.length === 0) {
      throw new Error('At least one executive or project lead email is required');
    }

    // Validate email formats
    const allEmails = [...this.executives, ...this.projectLeads];
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    
    for (const email of allEmails) {
      if (!emailRegex.test(email)) {
        throw new Error(`Invalid email format: ${email}`);
      }
    }

    return true;
  }

  getAllNotificationEmails() {
    return [...this.executives, ...this.projectLeads];
  }

  toJSON() {
    return {
      id: this.id,
      name: this.name,
      description: this.description,
      executives: this.executives,
      projectLeads: this.projectLeads,
      createdAt: this.createdAt
    };
  }
}

module.exports = Team;