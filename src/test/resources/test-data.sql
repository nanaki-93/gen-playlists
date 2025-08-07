-- Clear any existing data
DELETE
FROM users;

-- Insert test users for repository tests
INSERT INTO users (id, email, password_hash, role, created_at, updated_at)
VALUES ('550e8400-e29b-41d4-a716-446655440001', 'test@example.com', '$2a$10$hashedpassword1', 'USER', CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       ('550e8400-e29b-41d4-a716-446655440002', 'admin@example.com', '$2a$10$hashedpassword2', 'ADMIN',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('550e8400-e29b-41d4-a716-446655440003', 'user2@example.com', '$2a$10$hashedpassword3', 'USER',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);