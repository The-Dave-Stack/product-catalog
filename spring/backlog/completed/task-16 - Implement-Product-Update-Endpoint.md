---
id: task-16
title: Implement Product Update Endpoint
status: Done
assignee: []
created_date: '2025-08-05 13:19'
updated_date: '2025-08-05 13:35'
labels: []
dependencies: []
---

## Description

Add a PUT /api/v1/products/{id} endpoint to allow updating existing product details.

## Acceptance Criteria

- [x] API endpoint PUT /api/v1/products/{id} is implemented and functional.
- [x] Product details (name, description, price) can be updated.
- [x] SKU remains immutable during update.
- [x] Proper error handling for product not found (404).
- [x] Input validation is applied to the update request payload.
- [x] Swagger documentation for the update endpoint is accurate and complete.
- [x] E2E tests for product update (happy path and 404) are passing.

## Implementation Notes

Implemented PUT /api/v1/products/{id} endpoint, including UpdateProductRequest DTO, updated ProductMapper, and added comprehensive E2E tests. Swagger documentation is also updated and verified.
