INSERT INTO roles (name) VALUES ('USER'), ('MODERATOR'), ('ADMIN') ON CONFLICT DO NOTHING;