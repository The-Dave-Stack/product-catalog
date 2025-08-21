---
id: task-2
title: Core JPA Entity Classes - User Management Domain
status: todo
assignee: []
created_date: '2025-08-21 11:30'
labels:
  - jpa
  - entities
  - domain
  - security
dependencies:
  - task-1
priority: high
---

## Description

Implement JPA entity classes for the normalized user management domain including Role, User, ApiKey, and UserRoleHistory entities with proper relationships, validation, and audit support.

## Acceptance Criteria

- [ ] RoleEntity with permissions array and user relationships
- [ ] UserEntity with role foreign key and authentication fields
- [ ] ApiKeyEntity with user relationship and scope management
- [ ] UserRoleHistoryEntity for audit trail tracking
- [ ] Proper JPA annotations and relationships configured
- [ ] Validation annotations for data integrity
- [ ] Lombok integration for boilerplate reduction
- [ ] Soft delete support where applicable
- [ ] Audit fields (created_at updated_at) properly mapped

## Implementation Plan

1. Create feature/jpa-entities-user-role-apikey branch from develop,2. Implement RoleEntity with @Entity @Table annotations,3. Add role permissions as List<String> with proper converters,4. Implement UserEntity with @ManyToOne relationship to Role,5. Add authentication fields with proper validation,6. Implement ApiKeyEntity with @ManyToOne to User,7. Add scope management and expiration logic,8. Implement UserRoleHistoryEntity for audit trail,9. Configure JPA relationships and cascade options,10. Add validation annotations for business rules,11. Test entity mappings and relationships,12. Commit and create PR
