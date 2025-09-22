const express = require('express');
const router = express.Router();
const Application = require('../models/Application');
const dataService = require('../services/dataService');
const emailService = require('../services/emailService');

// Submit a new application
router.post('/', async (req, res) => {
  try {
    // Create new application instance
    const application = new Application(req.body);
    
    // Validate the application
    application.validate();

    // Check if the team exists
    const team = await dataService.getTeamByName(application.team);
    if (!team) {
      return res.status(400).json({
        error: 'Invalid team',
        message: `Team "${application.team}" not found. Please use one of the existing teams.`,
        availableTeams: (await dataService.getAllTeams()).map(t => t.name)
      });
    }

    // Save the application
    const savedApplication = await dataService.saveApplication(application);

    // Get notification recipients from the team
    const recipients = [...team.executives, ...team.projectLeads];

    // Send email notifications
    const emailResult = await emailService.sendApplicationNotification(
      savedApplication, 
      recipients
    );

    // Log the notification attempt
    console.log(`📧 Application notification sent for ${application.applicantName} (${application.position}) to team ${team.name}`);
    console.log(`Recipients: ${recipients.join(', ')}`);

    res.status(201).json({
      success: true,
      message: 'Application submitted successfully',
      application: savedApplication.toJSON(),
      notification: {
        sent: emailResult.success,
        recipients: recipients,
        details: emailResult.message
      }
    });

  } catch (error) {
    console.error('Error submitting application:', error.message);
    res.status(400).json({
      error: 'Failed to submit application',
      message: error.message
    });
  }
});

// Get all applications
router.get('/', async (req, res) => {
  try {
    const applications = await dataService.getAllApplications();
    
    // Optional filtering by team or status
    let filteredApplications = applications;
    
    if (req.query.team) {
      filteredApplications = filteredApplications.filter(
        app => app.team.toLowerCase() === req.query.team.toLowerCase()
      );
    }
    
    if (req.query.status) {
      filteredApplications = filteredApplications.filter(
        app => app.status === req.query.status
      );
    }

    res.json({
      success: true,
      count: filteredApplications.length,
      applications: filteredApplications
    });

  } catch (error) {
    console.error('Error getting applications:', error.message);
    res.status(500).json({
      error: 'Failed to retrieve applications',
      message: error.message
    });
  }
});

// Get application by ID
router.get('/:id', async (req, res) => {
  try {
    const application = await dataService.getApplicationById(req.params.id);
    
    if (!application) {
      return res.status(404).json({
        error: 'Application not found',
        message: `Application with ID "${req.params.id}" does not exist`
      });
    }

    res.json({
      success: true,
      application: application
    });

  } catch (error) {
    console.error('Error getting application:', error.message);
    res.status(500).json({
      error: 'Failed to retrieve application',
      message: error.message
    });
  }
});

// Update application status
router.patch('/:id/status', async (req, res) => {
  try {
    const { status } = req.body;
    
    if (!status) {
      return res.status(400).json({
        error: 'Status is required',
        message: 'Please provide a status value'
      });
    }

    const validStatuses = ['pending', 'reviewing', 'interview', 'accepted', 'rejected'];
    if (!validStatuses.includes(status)) {
      return res.status(400).json({
        error: 'Invalid status',
        message: `Status must be one of: ${validStatuses.join(', ')}`
      });
    }

    const updatedApplication = await dataService.updateApplicationStatus(req.params.id, status);

    res.json({
      success: true,
      message: 'Application status updated successfully',
      application: updatedApplication
    });

  } catch (error) {
    console.error('Error updating application status:', error.message);
    res.status(error.message.includes('not found') ? 404 : 500).json({
      error: 'Failed to update application status',
      message: error.message
    });
  }
});

// Get application statistics
router.get('/stats/summary', async (req, res) => {
  try {
    const applications = await dataService.getAllApplications();
    
    const stats = {
      total: applications.length,
      byStatus: {},
      byTeam: {},
      recent: applications.filter(app => {
        const appDate = new Date(app.appliedAt);
        const weekAgo = new Date();
        weekAgo.setDate(weekAgo.getDate() - 7);
        return appDate >= weekAgo;
      }).length
    };

    // Count by status
    applications.forEach(app => {
      stats.byStatus[app.status] = (stats.byStatus[app.status] || 0) + 1;
    });

    // Count by team
    applications.forEach(app => {
      stats.byTeam[app.team] = (stats.byTeam[app.team] || 0) + 1;
    });

    res.json({
      success: true,
      stats: stats
    });

  } catch (error) {
    console.error('Error getting application stats:', error.message);
    res.status(500).json({
      error: 'Failed to retrieve application statistics',
      message: error.message
    });
  }
});

module.exports = router;