CREATE TABLE app_users (
                           user_id BIGSERIAL PRIMARY KEY,
                           first_name VARCHAR(255),
                           last_name VARCHAR(255),
                           email_address VARCHAR(255),
                           user_name VARCHAR(255),
                           password VARCHAR(255)
);

CREATE TABLE app_users_user_type (
                                     app_users_user_id BIGINT NOT NULL,
                                     user_type VARCHAR(255) NOT NULL,
                                     CONSTRAINT fk_user_user_type_user
                                         FOREIGN KEY (app_users_user_id)
                                             REFERENCES app_users(user_id)
                                             ON DELETE CASCADE
);