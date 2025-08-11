---
id: task-10
title: Implement REST Endpoints in 'ProductResource'
status: To Do
assignee: []
created_date: '2025-07-30'
labels:
  - api
  - resource
  - jax-rs
  - helidon
dependencies:
  - task-9
priority: high
---

## Description

## Acceptance Criteria

- [ ] "'ProductResource.java' and DTOs have been created from scratch.
- [ ] All MVP endpoints are implemented using JAX-RS annotations.
- [ ] Custom 'ExceptionMapper' classes are implemented.
- [ ] Human review and approval."

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
1. Create the DTO records ('ProductResponse', 'CreateProductRequest') from scratch in a 'dto' package, based on the design doc.
2. Create the 'ProductResource.java' class as a JAX-RS resource (@Path(\"/products\"), @ApplicationScoped).
3. Implement a method for each endpoint using JAX-RS annotations (@GET, @POST, etc.).
4. Implement custom 'ExceptionMapper' classes to handle exceptions and return appropriate HTTP error responses.
