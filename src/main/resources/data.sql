INSERT INTO `role` (`id`, `code`) VALUES
(1, 'ROLE_USER');

INSERT INTO `user` (`id`, `username`, `password`, `firstname`, `lastname`) VALUES
(1, 'username', '$2a$10$P5mDLmh00MuKZi3qNKJUf.ep4B4amTbGoX7DY8uDlPuLXUfUSxATG', 'firstname', 'lastname');

INSERT INTO `user_roles` (`user_id`, `roles_id`) VALUES
(1, 1);