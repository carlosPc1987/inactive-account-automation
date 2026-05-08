CREATE TABLE accounts (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    last_activity_at TIMESTAMP WITH TIME ZONE NOT NULL,
    inactive BOOLEAN NOT NULL
);

CREATE TABLE account_audit_events (
    id UUID PRIMARY KEY,
    account_id UUID NOT NULL,
    event_type VARCHAR(128) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    occurred_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_account_audit_events_account_id ON account_audit_events(account_id);
