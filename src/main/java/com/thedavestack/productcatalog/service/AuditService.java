package com.thedavestack.productcatalog.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thedavestack.productcatalog.model.AuditLog;
import com.thedavestack.productcatalog.repository.AuditLogRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    @Async
    public CompletableFuture<Void> logAction(String entityType, String entityId, AuditLog.AuditAction action, Object oldValue, Object newValue) {
        try {
            String username = getCurrentUsername();
            
            AuditLog auditLog = AuditLog.builder()
                .entityType(entityType)
                .entityId(entityId)
                .action(action)
                .username(username)
                .oldValues(oldValue != null ? objectMapper.writeValueAsString(oldValue) : null)
                .newValues(newValue != null ? objectMapper.writeValueAsString(newValue) : null)
                .changes(generateChanges(oldValue, newValue))
                .build();

            auditLogRepository.save(auditLog);
            log.info("Audit log created: {} {} by {}", action, entityType, username);
        } catch (Exception e) {
            log.error("Failed to create audit log for {} {}: {}", action, entityType, e.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }

    public Page<AuditLog> findAuditLogs(String entityType, String entityId, String username, AuditLog.AuditAction action, Pageable pageable) {
        return auditLogRepository.findWithFilters(entityType, entityId, username, action, pageable);
    }

    public Page<AuditLog> findByEntityId(String entityId, Pageable pageable) {
        return auditLogRepository.findByEntityIdOrderByCreatedAtDesc(entityId, pageable);
    }

    private String getCurrentUsername() {
        try {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        } catch (Exception e) {
            return "system";
        }
    }

    private String generateChanges(Object oldValue, Object newValue) {
        try {
            if (oldValue == null && newValue != null) {
                return "Created: " + objectMapper.writeValueAsString(newValue);
            } else if (oldValue != null && newValue == null) {
                return "Deleted: " + objectMapper.writeValueAsString(oldValue);
            } else if (oldValue != null && newValue != null) {
                // In a real implementation, you would do a field-by-field comparison
                return "Updated";
            }
            return "No changes";
        } catch (JsonProcessingException e) {
            return "Error generating changes: " + e.getMessage();
        }
    }
}