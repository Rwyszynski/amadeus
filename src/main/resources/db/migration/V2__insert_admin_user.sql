INSERT INTO app_users (first_name, last_name, email_address, user_name, password)
VALUES (
           'Admin',
           'Admin',
           'admin@example.com',
           'admin',
           '$2a$10$N9qo8uLOickgx2ZMRZo5i.Uf36YZV/Oz7slYt1H/9Kq4X8q3jKqZC'
       );

INSERT INTO app_users_user_type (app_users_user_id, user_type)
SELECT user_id, 'ADMIN_ROLE'
FROM app_users
         WHER