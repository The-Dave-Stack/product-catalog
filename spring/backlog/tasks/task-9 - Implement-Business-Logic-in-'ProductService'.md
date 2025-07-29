---
id: task-9
title: Implement Business Logic in 'ProductService'
status: To Do
assignee: []
created_date: '2025-07-28'
labels:
  - service
  - business-logic
dependencies:
  - task-8
priority: high
---

## Description

Create the service layer to orchestrate product management logic.

## Acceptance Criteria

- [ ] 'ProductService.java' exists and is annotated with @Service.
- [ ] All CRUD and batch-create methods are implemented.
- [ ] SKU logic is correctly handled.
- [ ] Batch creation is @Transactional.
- [ ] Human review and approval.

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
1. Create package 'com.thedavestack.productcatalog.service' and class 'ProductService.java'.
2. Annotate with @Service and inject 'ProductRepository'.
3. Implement 'createProduct', including SKU generation and duplicate validation logic.
4. Implement 'createMultipleProducts' with @Transactional annotation.
5. Implement all other CRUD methods (findById, findAll, update, delete), handling not-found scenarios with exceptions.
