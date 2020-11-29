CREATE TABLE IF NOT EXISTS `roles`(
   `id` INT UNSIGNED AUTO_INCREMENT,
   `role` VARCHAR(100) NOT NULL,
   PRIMARY KEY (`id`),
   UNIQUE KEY `role_unique` (`role`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `users`(
   `id` INT UNSIGNED AUTO_INCREMENT,
   `user_id` VARCHAR(100) NOT NULL,
   `username` VARCHAR(100) NOT NULL,
   `password` VARCHAR(1000) NOT NULL,
   `email` VARCHAR(100),
   `phone` VARCHAR(100),
   `avatar` TEXT,
   `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
   `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
   `enable` TINYINT(1) NOT NULL DEFAULT 1,
   `role_id` INT UNSIGNED,
   `version` INT UNSIGNED NOT NULL,
   PRIMARY KEY (`id`),
   UNIQUE KEY `user_id_unique` (`user_id`),
   UNIQUE KEY `username_unique` (`username`),
   UNIQUE KEY `email_unique` (`email`),
   UNIQUE KEY `phone_unique` (`phone`),
   FOREIGN KEY (`role_id`) REFERENCES roles(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;