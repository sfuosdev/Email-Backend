const fs = require('fs').promises;
const path = require('path');

class DataService {
  constructor() {
    this.dataDir = path.join(__dirname, '../data');
    this.applicationsFile = path.join(this.dataDir, 'applications.json');
    this.teamsFile = path.join(this.dataDir, 'teams.json');
    this.initializeDataFiles();
  }

  async initializeDataFiles() {
    try {
      // Create data directory if it doesn't exist
      await fs.mkdir(this.dataDir, { recursive: true });

      // Initialize applications file
      try {
        await fs.access(this.applicationsFile);
      } catch {
        await fs.writeFile(this.applicationsFile, JSON.stringify([], null, 2));
      }

      // Initialize teams file with default teams
      try {
        await fs.access(this.teamsFile);
      } catch {
        const defaultTeams = [
          {
            id: 'engineering',
            name: 'Engineering',
            description: 'Software development and technical roles',
            executives: ['cto@company.com'],
            projectLeads: ['eng-lead@company.com'],
            createdAt: new Date().toISOString()
          },
          {
            id: 'product',
            name: 'Product',
            description: 'Product management and design roles',
            executives: ['cpo@company.com'],
            projectLeads: ['product-lead@company.com'],
            createdAt: new Date().toISOString()
          },
          {
            id: 'marketing',
            name: 'Marketing',
            description: 'Marketing and growth roles',
            executives: ['cmo@company.com'],
            projectLeads: ['marketing-lead@company.com'],
            createdAt: new Date().toISOString()
          }
        ];
        await fs.writeFile(this.teamsFile, JSON.stringify(defaultTeams, null, 2));
      }

      console.log('✅ Data service initialized successfully');
    } catch (error) {
      console.error('❌ Failed to initialize data service:', error.message);
    }
  }

  // Applications methods
  async saveApplication(application) {
    try {
      const applications = await this.getAllApplications();
      applications.push(application.toJSON());
      await fs.writeFile(this.applicationsFile, JSON.stringify(applications, null, 2));
      return application;
    } catch (error) {
      throw new Error(`Failed to save application: ${error.message}`);
    }
  }

  async getAllApplications() {
    try {
      const data = await fs.readFile(this.applicationsFile, 'utf8');
      return JSON.parse(data);
    } catch (error) {
      console.error('Error reading applications:', error.message);
      return [];
    }
  }

  async getApplicationById(id) {
    try {
      const applications = await this.getAllApplications();
      return applications.find(app => app.id === id);
    } catch (error) {
      throw new Error(`Failed to get application: ${error.message}`);
    }
  }

  async updateApplicationStatus(id, status) {
    try {
      const applications = await this.getAllApplications();
      const appIndex = applications.findIndex(app => app.id === id);
      
      if (appIndex === -1) {
        throw new Error('Application not found');
      }

      applications[appIndex].status = status;
      await fs.writeFile(this.applicationsFile, JSON.stringify(applications, null, 2));
      
      return applications[appIndex];
    } catch (error) {
      throw new Error(`Failed to update application status: ${error.message}`);
    }
  }

  // Teams methods
  async saveTeam(team) {
    try {
      const teams = await this.getAllTeams();
      const existingIndex = teams.findIndex(t => t.id === team.id);
      
      if (existingIndex !== -1) {
        teams[existingIndex] = team.toJSON();
      } else {
        teams.push(team.toJSON());
      }
      
      await fs.writeFile(this.teamsFile, JSON.stringify(teams, null, 2));
      return team;
    } catch (error) {
      throw new Error(`Failed to save team: ${error.message}`);
    }
  }

  async getAllTeams() {
    try {
      const data = await fs.readFile(this.teamsFile, 'utf8');
      return JSON.parse(data);
    } catch (error) {
      console.error('Error reading teams:', error.message);
      return [];
    }
  }

  async getTeamById(id) {
    try {
      const teams = await this.getAllTeams();
      return teams.find(team => team.id === id);
    } catch (error) {
      throw new Error(`Failed to get team: ${error.message}`);
    }
  }

  async getTeamByName(name) {
    try {
      const teams = await this.getAllTeams();
      return teams.find(team => team.name.toLowerCase() === name.toLowerCase());
    } catch (error) {
      throw new Error(`Failed to get team by name: ${error.message}`);
    }
  }

  async deleteTeam(id) {
    try {
      const teams = await this.getAllTeams();
      const filteredTeams = teams.filter(team => team.id !== id);
      
      if (teams.length === filteredTeams.length) {
        throw new Error('Team not found');
      }
      
      await fs.writeFile(this.teamsFile, JSON.stringify(filteredTeams, null, 2));
      return true;
    } catch (error) {
      throw new Error(`Failed to delete team: ${error.message}`);
    }
  }
}

module.exports = new DataService();