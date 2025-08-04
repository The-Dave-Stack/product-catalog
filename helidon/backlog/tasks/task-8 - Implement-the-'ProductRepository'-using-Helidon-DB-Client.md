---
id: task-8
title: Implement the 'ProductRepository' using Helidon DB Client
status: To Do
assignee: []
created_date: '2025-07-30'
labels:
  - db
  - repository
  - helidon
  - db-client
dependencies:
  - task-7
priority: high
---

## Description

## Acceptance Criteria

- [ ] "'ProductRepository.java' is a correct @ApplicationScoped CDI bean.
- [ ] The implementation follows current best practices from Helidon documentation.
- [ ] All CRUD methods are implemented with manual SQL.
- [ ] Batch operations are transactional.
- [ ] Human review and approval."

## Implementation Plan

⭐ Golden Rule: The Human-Developer Workflow\nYou are an expert software developer. For this task, you MUST follow this exact lifecycle:\n1. Start Task: Announce you are starting. Change the task status to 'In Progress'. Start the timer using the 'time-mcp' tool.\n2. Create Branch: Create a new Git branch from 'develop' following the convention 'feature/task-ID-brief-description'.\n3. Execute & Journal: Execute the Implementation Plan below. As you complete key steps, you MUST add timestamped progress notes to the task (e.g., '[YYYY-MM-DD HH:MM:SS] - Note content.').\n4. Stop & Log Time: When the work is done, stop the timer. Add a final timestamped note with the total time spent.\n5. Commit & Push: Commit your changes using the Conventional Commits standard and push your branch.\n6. Prepare PR: Generate the title and body for a Pull Request.\n7. Finish Task: Change the task status to 'Done'.\n\nUniversal Mandates:\n- You MUST strictly adhere to all rules in 'global.general.rules.pdf' and 'global.backend.rules.pdf'.\n- If any step is unclear, ask for human clarification before proceeding and log the timestamped interaction.\n\nImplementation Plan:\n1. Consult the official Helidon 4.x documentation for 'DbClient' best practices using your MCP tools.\n2. Create the 'ProductRepository.java' class, annotate with @ApplicationScoped, and inject the 'DbClient'.\n3. Implement methods for all CRUD operations by manually building and executing SQL queries.\n4. Pay close attention to the 'Transactional Integrity' rule for batch operations, using 'dbClient.inTransaction(...)'.\n5. Create a private helper method to map from 'DbRow' to the 'Product' record.
