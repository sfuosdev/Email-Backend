#!/usr/bin/env node

/**
 * Demo script for the Email Backend System
 * This script demonstrates how to use the API endpoints
 */

const axios = require('axios');

const BASE_URL = 'http://localhost:3000';

async function demo() {
  console.log('🚀 Email Backend Demo\n');

  try {
    // 1. Check system health
    console.log('1. Checking system health...');
    const health = await axios.get(`${BASE_URL}/health`);
    console.log(`✅ System Status: ${health.data.status}\n`);

    // 2. Get existing teams
    console.log('2. Getting available teams...');
    const teams = await axios.get(`${BASE_URL}/api/teams`);
    console.log(`📋 Available Teams (${teams.data.count}):`);
    teams.data.teams.forEach(team => {
      console.log(`   - ${team.name}: ${team.executives.length} executives, ${team.projectLeads.length} project leads`);
    });
    console.log();

    // 3. Submit sample applications
    console.log('3. Submitting sample applications...\n');

    const applications = [
      {
        applicantName: 'Sarah Chen',
        applicantEmail: 'sarah.chen@example.com',
        position: 'Senior Full Stack Developer',
        team: 'Engineering',
        resumeUrl: 'https://example.com/sarah-resume.pdf',
        coverLetter: 'I have 6 years of experience in React, Node.js, and cloud technologies. I am passionate about building scalable web applications and leading technical initiatives.'
      },
      {
        applicantName: 'Michael Rodriguez',
        applicantEmail: 'michael.r@example.com',
        position: 'Senior Product Manager',
        team: 'Product',
        coverLetter: 'With 8 years in product management at high-growth startups, I specialize in user research, roadmap planning, and cross-functional team leadership.'
      },
      {
        applicantName: 'Jennifer Kim',
        applicantEmail: 'jennifer.kim@example.com',
        position: 'Digital Marketing Manager',
        team: 'Marketing',
        resumeUrl: 'https://example.com/jennifer-resume.pdf',
        coverLetter: 'I have successfully led digital marketing campaigns that increased user acquisition by 300% and managed marketing budgets of $2M+.'
      }
    ];

    for (const app of applications) {
      console.log(`📝 Submitting application from ${app.applicantName} for ${app.position}...`);
      const response = await axios.post(`${BASE_URL}/api/applications`, app);
      
      if (response.data.success) {
        console.log(`   ✅ Application submitted (ID: ${response.data.application.id})`);
        console.log(`   📧 Email sent to: ${response.data.notification.recipients.join(', ')}`);
      }
      console.log();
    }

    // 4. View application statistics
    console.log('4. Viewing application statistics...');
    const stats = await axios.get(`${BASE_URL}/api/applications/stats/summary`);
    console.log(`📊 Application Statistics:`);
    console.log(`   Total Applications: ${stats.data.stats.total}`);
    console.log(`   Recent (last 7 days): ${stats.data.stats.recent}`);
    console.log(`   By Status:`);
    Object.entries(stats.data.stats.byStatus).forEach(([status, count]) => {
      console.log(`     - ${status}: ${count}`);
    });
    console.log(`   By Team:`);
    Object.entries(stats.data.stats.byTeam).forEach(([team, count]) => {
      console.log(`     - ${team}: ${count}`);
    });
    console.log();

    // 5. Create a new team
    console.log('5. Creating a new team...');
    const newTeam = {
      name: 'DevOps',
      description: 'Infrastructure and deployment team',
      executives: ['cto@company.com'],
      projectLeads: ['devops-lead@company.com', 'infrastructure-lead@company.com']
    };

    const teamResponse = await axios.post(`${BASE_URL}/api/teams`, newTeam);
    if (teamResponse.data.success) {
      console.log(`✅ Created team: ${teamResponse.data.team.name}`);
      console.log(`   ID: ${teamResponse.data.team.id}`);
      console.log(`   Notification emails: ${[...teamResponse.data.team.executives, ...teamResponse.data.team.projectLeads].join(', ')}`);
    }
    console.log();

    // 6. Test email notification for the new team
    console.log('6. Testing email notification for new team...');
    const emailTest = await axios.post(`${BASE_URL}/api/teams/${teamResponse.data.team.id}/test-email`);
    if (emailTest.data.success) {
      console.log(`✅ Test email sent to: ${emailTest.data.recipients.join(', ')}`);
    }
    console.log();

    console.log('🎉 Demo completed successfully!');
    console.log('\n📝 To configure real email sending:');
    console.log('   1. Copy .env.example to .env');
    console.log('   2. Configure your SMTP settings');
    console.log('   3. Restart the server');

  } catch (error) {
    console.error('❌ Demo failed:', error.response?.data || error.message);
    console.log('\n💡 Make sure the server is running: npm start');
  }
}

// Install axios if not already installed
async function ensureAxios() {
  try {
    require('axios');
  } catch (error) {
    console.log('Installing axios for demo...');
    const { execSync } = require('child_process');
    execSync('npm install axios', { stdio: 'inherit' });
  }
}

// Run the demo
if (require.main === module) {
  ensureAxios().then(() => {
    demo();
  });
}

module.exports = { demo };