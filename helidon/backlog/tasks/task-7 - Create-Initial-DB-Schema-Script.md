---
id: task-7
title: Create Initial DB Schema Script
status: To Do
assignee: []
created_date: '2025-07-30'
labels:
  - db
  - sql
  - schema
  - helidon
dependencies:
  - task-5
  - task-6
priority: high
---

## Description

## Acceptance Criteria

- [ ] "'schema.sql' exists and is correct.
- [ ] 'docker-compose.yml' is updated to mount the script.
- [ ] On a clean start
- [ ] the 'products' table is created in the database.
- [ ] Human review and approval."

## Implementation Plan

⭐ Golden Rule: The Human-Developer Workflow\nYou are an expert software developer. For this task, you MUST follow this exact lifecycle:\n1. Start Task: Announce you are starting. Change the task status to 'In Progress'. Start the timer using the 'time-mcp' tool.\n2. Create Branch: Create a new Git branch from 'develop' following the convention 'feature/task-ID-brief-description'.\n3. Execute & Journal: Execute the Implementation Plan below. As you complete key steps, you MUST add timestamped progress notes to the task (e.g., '[YYYY-MM-DD HH:MM:SS] - Note content.').\n4. Stop & Log Time: When the work is done, stop the timer. Add a final timestamped note with the total time spent.\n5. Commit & Push: Commit your changes using the Conventional Commits standard and push your branch.\n6. Prepare PR: Generate the title and body for a Pull Request.\n7. Finish Task: Change the task status to 'Done'.\n\nUniversal Mandates:\n- You MUST strictly adhere to all rules in 'global.general.rules.pdf' and 'global.backend.rules.pdf'.\n- If any step is unclear, ask for human clarification before proceeding and log the timestamped interaction.\n\nImplementation Plan:\n1. Create a directory 'db-init' in the project root.\n2. Inside, create a 'schema.sql' file.\n3. Write the 'CREATE TABLE products (...)' SQL statement, matching the 'Product' record.\n4. Modify 'docker-compose.yml' to mount './db-init' to '/docker-entrypoint-initdb.d' on the postgres container.
