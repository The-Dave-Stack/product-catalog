-- Create audit logs table for tracking changes
CREATE TABLE audit_logs (
    id VARCHAR(36) PRIMARY KEY,
    entity_type VARCHAR(255) NOT NULL,
    entity_id VARCHAR(255) NOT NULL,
    action VARCHAR(50) NOT NULL,
    username VARCHAR(255),
    old_values TEXT,
    new_values TEXT,
    changes TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Create indexes for efficient querying
CREATE INDEX idx_audit_logs_entity_type ON audit_logs(entity_type);
CREATE INDEX idx_audit_logs_entity_id ON audit_logs(entity_id);
CREATE INDEX idx_audit_logs_username ON audit_logs(username);
CREATE INDEX idx_audit_logs_action ON audit_logs(action);
CREATE INDEX idx_audit_logs_created_at ON audit_logs(created_at DESC);
CREATE INDEX idx_audit_logs_composite ON audit_logs(entity_type, entity_id, created_at DESC);