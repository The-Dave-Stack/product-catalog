-- Create users, roles, and API keys tables for authentication and authorization system

-- Create roles table for normalized role management
CREATE TABLE roles (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    permissions TEXT[] NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Create users table for database-managed users
CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role_id VARCHAR(36) NOT NULL REFERENCES roles(id),
    enabled BOOLEAN NOT NULL DEFAULT true,
    mcp_access_enabled BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Create API keys table for API key management
CREATE TABLE api_keys (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    key_name VARCHAR(100) NOT NULL,
    key_hash VARCHAR(255) NOT NULL,
    key_prefix VARCHAR(10) NOT NULL,
    scopes TEXT[] NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT true,
    expires_at TIMESTAMP WITH TIME ZONE,
    last_used_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    UNIQUE(user_id, key_name)
);

-- Create user role history table for audit trail of role changes
CREATE TABLE user_role_history (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL REFERENCES users(id),
    old_role_id VARCHAR(36) REFERENCES roles(id),
    new_role_id VARCHAR(36) NOT NULL REFERENCES roles(id),
    changed_by VARCHAR(36) REFERENCES users(id),
    change_reason TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Create indexes for performance optimization
CREATE INDEX idx_users_role_id ON users(role_id);
CREATE INDEX idx_users_username_enabled ON users(username, enabled);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_api_keys_user_enabled ON api_keys(user_id, enabled);
CREATE INDEX idx_api_keys_prefix ON api_keys(key_prefix);
CREATE INDEX idx_api_keys_expires_at ON api_keys(expires_at);
CREATE INDEX idx_user_role_history_user_id ON user_role_history(user_id);
CREATE INDEX idx_user_role_history_created_at ON user_role_history(created_at DESC);

-- NOTE: Data seeding is handled by application bootstrap service, not migrations
-- This follows enterprise best practices by separating schema evolution from business data

-- Add check constraints for data integrity
ALTER TABLE roles ADD CONSTRAINT check_role_name 
CHECK (name IN ('ADMIN', 'USER', 'MANAGER', 'READONLY'));

ALTER TABLE api_keys ADD CONSTRAINT check_key_prefix_length 
CHECK (LENGTH(key_prefix) >= 3 AND LENGTH(key_prefix) <= 10);

ALTER TABLE api_keys ADD CONSTRAINT check_expires_at_future 
CHECK (expires_at IS NULL OR expires_at > created_at);

-- Add comments for documentation
COMMENT ON TABLE roles IS 'Normalized role management with granular permissions';
COMMENT ON TABLE users IS 'Database-managed users with role-based access control';
COMMENT ON TABLE api_keys IS 'API key management with scoped access and expiration';
COMMENT ON TABLE user_role_history IS 'Audit trail for all user role changes';

COMMENT ON COLUMN roles.permissions IS 'Array of permission strings: READ, WRITE, DELETE, USER_MANAGEMENT, etc.';
COMMENT ON COLUMN users.mcp_access_enabled IS 'Controls access to MCP (Model Context Protocol) server endpoints';
COMMENT ON COLUMN api_keys.scopes IS 'Array of permission scopes for this API key';
COMMENT ON COLUMN api_keys.key_prefix IS 'First few characters of API key for identification (not sensitive)';
COMMENT ON COLUMN user_role_history.change_reason IS 'Human-readable reason for role change';