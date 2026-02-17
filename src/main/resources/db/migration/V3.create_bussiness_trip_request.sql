CREATE TABLE bussiness_trip_requests (
                                         btrid SERIAL PRIMARY KEY,
                                         title VARCHAR(255) NOT NULL,
                                         trip_reason VARCHAR(255) NOT NULL,
                                         start_date DATE NOT NULL,
                                         end_date DATE NOT NULL,
                                         start_location VARCHAR(255) NOT NULL,
                                         destination VARCHAR(255) NOT NULL,
                                         anticipated_expense_amount NUMERIC(15,2),
                                         comments TEXT,
                                         status VARCHAR(50) DEFAULT 'PENDING',
                                         user_id INT NOT NULL,
                                         CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES app_users(user_id)
);