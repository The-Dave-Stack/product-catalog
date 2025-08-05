---
id: task-14
title: Create Java-based E2E Tests for Helidon Stack
status: To Do
assignee: []
created_date: '2025-07-30'
updated_date: '2025-08-05 13:48'
labels:
  - test
  - e2e
  - docker-compose
  - testcontainers
  - helidon
dependencies:
  - task-13
priority: medium
---

## Description

Create comprehensive Java-based E2E tests for the Helidon application, integrated directly into the main project structure, using Testcontainers with PostgreSQL and RestAssured for API validation.
## Implementation Plan

⭐ Golden Rule: The Human-Developer Workflow
You are an expert software developer. For this task, you MUST follow this exact lifecycle:
1. Start Task: Announce you are starting. Change the task status to 'In Progress'. Start the timer using the 'time-mcp' tool.
2. Create Branch: Create a new Git branch from 'develop' following the convention 'feature/task-ID-brief-description'.
3. Execute & Journal: Execute the Implementation Plan below. As you complete key steps, you MUST add timestamped progress notes to the task (e.g., '[YYYY-MM-DD HH:MM:SS] - Note content.').
4. Stop & Log Time: When the work is done, stop the timer. Add a final timestamped note with the total time spent.
5. Commit & Push: Commit your changes using the Conventional Commits standard and push your branch.
6. Prepare PR: Generate the title and body for a Pull Request.
7. Finish Task: Change the task status to 'Done'.

Universal Mandates:
- You MUST strictly adhere to all rules in 'global.general.rules.pdf' and 'global.backend.rules.pdf'.
- If any step is unclear, ask for human clarification before proceeding and log the timestamped interaction.

Implementation Plan:
1. Create a new Maven module named 'e2e-tests'.
2. Add dependencies for Testcontainers, RestAssured, and JUnit 5.
3. Create a test class that uses 'DockerComposeContainer' to manage the root 'docker-compose.yml'.
4. Implement @Test methods for each user story defined in the design document.

## Acceptance Criteria

- [ ] E2E tests are integrated into the main project (not a separate module); Tests use PostgreSQL Testcontainer for database; RestAssured is used for API testing; Comprehensive test scenarios cover all CRUD operations, error handling, and business logic validation; All E2E tests pass successfully.
