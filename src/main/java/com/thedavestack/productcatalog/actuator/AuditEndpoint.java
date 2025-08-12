package com.thedavestack.productcatalog.actuator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.actuator.endpoint.annotation.Endpoint;
import org.springframework.boot.actuator.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuator.endpoint.annotation.Selector;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.thedavestack.productcatalog.model.AuditLog;
import com.thedavestack.productcatalog.service.AuditService;

import lombok.RequiredArgsConstructor;

@Component
@Endpoint(id = "audit")
@RequiredArgsConstructor
public class AuditEndpoint {

    private final AuditService auditService;

    @ReadOperation
    public Map<String, Object> auditSummary() {
        Map<String, Object> summary = new HashMap<>();
        
        Pageable pageable = PageRequest.of(0, 10);
        Page<AuditLog> recentAudits = auditService.findAuditLogs(null, null, null, null, pageable);
        
        summary.put("totalAuditLogs", recentAudits.getTotalElements());
        summary.put("recentAudits", recentAudits.getContent());
        
        return summary;
    }

    @ReadOperation
    public Map<String, Object> auditByEntity(@Selector String entityId) {
        Map<String, Object> result = new HashMap<>();
        
        Pageable pageable = PageRequest.of(0, 20);
        Page<AuditLog> audits = auditService.findByEntityId(entityId, pageable);
        
        result.put("entityId", entityId);
        result.put("totalLogs", audits.getTotalElements());
        result.put("auditLogs", audits.getContent());
        
        return result;
    }
}