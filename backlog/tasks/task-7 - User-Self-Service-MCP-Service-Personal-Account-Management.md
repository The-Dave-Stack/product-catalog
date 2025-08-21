---
id: task-7
title: User Self-Service MCP Service - Personal Account Management
status: todo
assignee: []
created_date: '2025-08-21 11:31'
labels:
  - mcp
  - tools
  - self-service
  - user-profile
dependencies:
  - task-4
priority: medium
---

## Description

Implement MCP service for user self-service operations allowing users to manage their own profiles, API keys, and account settings without requiring admin privileges.

## Acceptance Criteria

- [ ] UserSelfServiceService with @Tool annotations
- [ ] Generate personal API keys with limited scopes
- [ ] List and manage own API keys tool
- [ ] Update own profile information tool
- [ ] Change own password securely tool
- [ ] View own account activity and audit log
- [ ] Revoke own API keys tool
- [ ] Scope validation based on user role
- [ ] USER and ADMIN role access control
- [ ] Self-service validation and security checks
- [ ] Integration with existing authentication flow

## Implementation Plan

1. Create feature/mcp-user-self-service branch,2. Implement UserSelfServiceService with @Service,3. Add generateMyApiKey @Tool with scope limitations,4. Add listMyApiKeys @Tool for personal keys,5. Add updateMyProfile @Tool for profile changes,6. Add changeMyPassword @Tool with validation,7. Add getMyActivity @Tool for account history,8. Add revokeMyApiKey @Tool for key management,9. Implement scope validation based on user role,10. Add self-service security validations,11. Create user permission tests,12. Verify MCP tool integration,13. Commit and create PR
