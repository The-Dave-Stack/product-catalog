---
id: task-2
title: Set Up Initial Helidon MP Project using a Non-Interactive Command
status: To Do
assignee: []
created_date: '2025-07-30'
labels:
  - setup
  - helidon
  - cli
  - automation
dependencies:
  - task-1
priority: high
---

## Description

## Acceptance Criteria

- [ ] "The Helidon MP project has been generated using the non-interactive 'helidon init' command.
- [ ] The 'pom.xml' contains 'helidon-mp-db-client' and 'helidon-mp-health'.
- [ ] 'mvn clean package' completes without errors.
- [ ] Human review and approval."

## Implementation Plan

⭐ Golden Rule: The Human-Developer Workflow\nYou are an expert software developer. For this task, you MUST follow this exact lifecycle:\n1. Start Task: Announce you are starting. Change the task status to 'In Progress'. Start the timer using the 'time-mcp' tool.\n2. Create Branch: Create a new Git branch from 'develop' following the convention 'feature/task-ID-brief-description'.\n3. Execute & Journal: Execute the Implementation Plan below. As you complete key steps, you MUST add timestamped progress notes to the task (e.g., '[YYYY-MM-DD HH:MM:SS] - Note content.').\n4. Stop & Log Time: When the work is done, stop the timer. Add a final timestamped note with the total time spent.\n5. Commit & Push: Commit your changes using the Conventional Commits standard and push your branch.\n6. Prepare PR: Generate the title and body for a Pull Request.\n7. Finish Task: Change the task status to 'Done'.\n\nUniversal Mandates:\n- You MUST strictly adhere to all rules in 'global.general.rules.pdf' and 'global.backend.rules.pdf'.\n- If any step is unclear, ask for human clarification before proceeding and log the timestamped interaction.\n\nImplementation Plan:\n1. Ensure the Helidon CLI is installed and up-to-date.\n2. Execute the single, non-interactive command to generate the project skeleton: 'helidon init --flavor mp --group com.thedavestack --artifact product-catalog-helidon --name product-catalog-helidon --features db-client,health'.\n3. Once generated, navigate into its root directory.\n4. Run 'mvn clean package' to verify the setup.
