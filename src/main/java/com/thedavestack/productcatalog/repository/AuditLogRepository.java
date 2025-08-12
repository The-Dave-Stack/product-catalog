package com.thedavestack.productcatalog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.thedavestack.productcatalog.model.AuditLog;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, String> {
    
    /**
     * Finds audit logs by entity ID with pagination.
     *
     * @param entityId The entity ID to search for.
     * @param pageable Pagination information.
     * @return A page of audit logs.
     */
    Page<AuditLog> findByEntityIdOrderByCreatedAtDesc(String entityId, Pageable pageable);
    
    /**
     * Finds audit logs by entity type with pagination.
     *
     * @param entityType The entity type to search for.
     * @param pageable Pagination information.
     * @return A page of audit logs.
     */
    Page<AuditLog> findByEntityTypeOrderByCreatedAtDesc(String entityType, Pageable pageable);
    
    /**
     * Finds audit logs by username with pagination.
     *
     * @param username The username to search for.
     * @param pageable Pagination information.
     * @return A page of audit logs.
     */
    Page<AuditLog> findByUsernameOrderByCreatedAtDesc(String username, Pageable pageable);
    
    /**
     * Finds audit logs with filters.
     *
     * @param entityType Optional entity type filter.
     * @param entityId Optional entity ID filter.
     * @param username Optional username filter.
     * @param action Optional action filter.
     * @param pageable Pagination information.
     * @return A page of audit logs.
     */
    @Query("SELECT a FROM AuditLog a WHERE " +
           "(:entityType IS NULL OR a.entityType = :entityType) AND " +
           "(:entityId IS NULL OR a.entityId = :entityId) AND " +
           "(:username IS NULL OR a.username = :username) AND " +
           "(:action IS NULL OR a.action = :action) " +
           "ORDER BY a.createdAt DESC")
    Page<AuditLog> findWithFilters(@Param("entityType") String entityType,
                                   @Param("entityId") String entityId,
                                   @Param("username") String username,
                                   @Param("action") AuditLog.AuditAction action,
                                   Pageable pageable);
}