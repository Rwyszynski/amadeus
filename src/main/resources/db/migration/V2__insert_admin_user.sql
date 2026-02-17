INSERT INTO app_users (first_name, last_name, email_address, user_name, password)
VALUES (
           'Admin',
           'Admin',
           'admin@example.com',
           'admin',
           '$2a$10$4lv7mdfsRnlr4mSGp0FO1uw0jhbWTtywkP/XZXf6QmRvKIam.DN1K'
       );

INSERT INTO app_users_user_type (app_users_user_id, user_type)
SELECT user_id, 'ADMIN_ROLE'
FROM app_users
