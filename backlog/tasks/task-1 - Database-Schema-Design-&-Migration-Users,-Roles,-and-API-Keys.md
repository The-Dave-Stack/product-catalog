---
id: task-1
title: 'Database Schema Design & Migration - Users, Roles, and API Keys'
status: todo
assignee: []
created_date: '2025-08-21 11:30'
labels:
  - database
  - migration
  - security
  - foundation
dependencies: []
priority: high
---

## Description

Create normalized database schema with separate tables for users, roles, and API keys to replace hardcoded authentication system. Implement Flyway migration V4 with proper indexes, constraints, and initial data seeding.

## Acceptance Criteria

- [ ] V4 migration file created and tested locally
- [ ] Roles table with permissions array created
- [ ] Users table with role foreign key created
- [ ] API keys table with scoping created
- [ ] User role history table for audit trail created
- [ ] Default roles seeded (ADMIN USER MANAGER READONLY)
- [ ] Default users migrated from hardcoded system
- [ ] All indexes and constraints properly implemented
- [ ] Migration successfully runs with mvn flyway:migrate

## Implementation Plan

1. Checkout develop branch and create feature/database-schema-users-roles-apikeys branch,2. Create V4__Create_users_roles_and_api_keys_tables.sql migration file,3. Implement roles table with id name description permissions timestamps,4. Implement users table with role_id foreign key and authentication fields,5. Implement api_keys table with user relationship and scoping,6. Implement user_role_history for audit trail,7. Add strategic indexes for performance optimization,8. Insert default roles with appropriate permissions,9. Migrate existing hardcoded users to database,10. Test migration locally with flyway,11. Commit and push changes,12. Create PR to develop branch
