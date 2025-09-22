const express = require('express');
const router = express.Router();
const Team = require('../models/Team');
const dataService = require('../services/dataService');
const emailService = require('../services/emailService');

// Get all teams
router.get('/', async (req, res) => {
  try {
    const teams = await dataService.getAllTeams();
    
    res.json({
      success: true,
      count: teams.length,
      teams: teams
    });

  } catch (error) {
    console.error('Error getting teams:', error.message);
    res.status(500).json({
      error: 'Failed to retrieve teams',
      message: error.message
    });
  }
});

// Get team by ID
router.get('/:id', async (req, res) => {
  try {
    const team = await dataService.getTeamById(req.params.id);
    
    if (!team) {
      return res.status(404).json({
        error: 'Team not found',
        message: `Team with ID "${req.params.id}" does not exist`
      });
    }

    res.json({
      success: true,
      team: team
    });

  } catch (error) {
    console.error('Error getting team:', error.message);
    res.status(500).json({
      error: 'Failed to retrieve team',
      message: error.message
    });
  }
});

// Create a new team
router.post('/', async (req, res) => {
  try {
    // Create new team instance
    const team = new Team(req.body);
    
    // Validate the team
    team.validate();

    // Check if a team with the same name already exists
    const existingTeam = await dataService.getTeamByName(team.name);
    if (existingTeam) {
      return res.status(400).json({
        error: 'Team already exists',
        message: `A team with the name "${team.name}" already exists`
      });
    }

    // Save the team
    const savedTeam = await dataService.saveTeam(team);

    console.log(`✅ New team created: ${team.name}`);

    res.status(201).json({
      success: true,
      message: 'Team created successfully',
      team: savedTeam.toJSON()
    });

  } catch (error) {
    console.error('Error creating team:', error.message);
    res.status(400).json({
      error: 'Failed to create team',
      message: error.message
    });
  }
});

// Update a team
router.put('/:id', async (req, res) => {
  try {
    // Check if team exists
    const existingTeam = await dataService.getTeamById(req.params.id);
    if (!existingTeam) {
      return res.status(404).json({
        error: 'Team not found',
        message: `Team with ID "${req.params.id}" does not exist`
      });
    }

    // Create updated team instance with existing ID
    const updatedData = { ...req.body, id: req.params.id };
    const team = new Team(updatedData);
    
    // Validate the team
    team.validate();

    // Check if another team with the same name exists (excluding current team)
    const teamWithSameName = await dataService.getTeamByName(team.name);
    if (teamWithSameName && teamWithSameName.id !== req.params.id) {
      return res.status(400).json({
        error: 'Team name already exists',
        message: `Another team with the name "${team.name}" already exists`
      });
    }

    // Save the updated team
    const savedTeam = await dataService.saveTeam(team);

    console.log(`✅ Team updated: ${team.name}`);

    res.json({
      success: true,
      message: 'Team updated successfully',
      team: savedTeam.toJSON()
    });

  } catch (error) {
    console.error('Error updating team:', error.message);
    res.status(400).json({
      error: 'Failed to update team',
      message: error.message
    });
  }
});

// Delete a team
router.delete('/:id', async (req, res) => {
  try {
    // Check if team exists
    const existingTeam = await dataService.getTeamById(req.params.id);
    if (!existingTeam) {
      return res.status(404).json({
        error: 'Team not found',
        message: `Team with ID "${req.params.id}" does not exist`
      });
    }

    // Delete the team
    await dataService.deleteTeam(req.params.id);

    console.log(`🗑️  Team deleted: ${existingTeam.name}`);

    res.json({
      success: true,
      message: 'Team deleted successfully'
    });

  } catch (error) {
    console.error('Error deleting team:', error.message);
    res.status(500).json({
      error: 'Failed to delete team',
      message: error.message
    });
  }
});

// Test email notifications for a team
router.post('/:id/test-email', async (req, res) => {
  try {
    const team = await dataService.getTeamById(req.params.id);
    
    if (!team) {
      return res.status(404).json({
        error: 'Team not found',
        message: `Team with ID "${req.params.id}" does not exist`
      });
    }

    // Create a test application for email testing
    const testApplication = {
      applicantName: 'Test Applicant',
      applicantEmail: 'test@example.com',
      position: 'Test Position',
      team: team.name,
      appliedAt: new Date().toISOString(),
      coverLetter: 'This is a test email notification to verify the email system is working correctly.',
      resumeUrl: 'https://example.com/test-resume.pdf'
    };

    // Get notification recipients
    const recipients = [...team.executives, ...team.projectLeads];

    // Send test email
    const emailResult = await emailService.sendApplicationNotification(
      testApplication, 
      recipients
    );

    console.log(`📧 Test email sent for team ${team.name}`);

    res.json({
      success: true,
      message: 'Test email sent successfully',
      recipients: recipients,
      emailResult: emailResult
    });

  } catch (error) {
    console.error('Error sending test email:', error.message);
    res.status(500).json({
      error: 'Failed to send test email',
      message: error.message
    });
  }
});

// Check email service status
router.get('/email/status', async (req, res) => {
  try {
    const connectionTest = await emailService.testConnection();
    
    res.json({
      success: true,
      emailService: connectionTest
    });

  } catch (error) {
    console.error('Error checking email status:', error.message);
    res.status(500).json({
      error: 'Failed to check email service status',
      message: error.message
    });
  }
});

module.exports = router;