INSERT INTO users(id, email, password, first_name, last_name, is_deleted)
VALUES (3, 'test@gmail.com', '$2a$10$fyPrzqYDmqbqyelQ/szVgOhwp//KSaboOyDinvdwc41GJIRpFozFy',
        'TestUserName', 'TestUserLastName', false);
INSERT INTO users_roles(user_id, role_id)
VALUES (3, 1);
