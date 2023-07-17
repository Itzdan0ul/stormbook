CREATE DATABASE stormbook CHARACTER SET utf8 COLLATE utf8_spanish2_ci;

CREATE TABLE user (
    `id` SERIAL,
    `username` NVARCHAR(32) NOT NULL,
    `password` VARCHAR(512) NOT NULL,
    `created_at` DATE DEFAULT CURRENT_TIMESTAMP(),
    `updated_at` DATE NULL,
    CONSTRAINT `user_id_pk` PRIMARY KEY (`id`),
    CONSTRAINT `user_username_uq` UNIQUE (`username`)
);

CREATE TABLE category (
    `name` VARCHAR(128) NOT NULL,
    `created_at` DATE DEFAULT CURRENT_TIMESTAMP(),
    `updated_at` DATE NULL,
    CONSTRAINT `category_name_pk` PRIMARY KEY (`name`)
)ENGINE = INNODB;

CREATE TABLE book (
    `isbn` VARCHAR(17) NOT NULL,
    `name` VARCHAR(256) NOT NULL,
    `author` VARCHAR(256) NOT NULL,
    `publisher` VARCHAR(128) NOT NULL,
    `pages` INT NOT NULL,
    `category_name` VARCHAR(128) NOT NULL,
    `created_at` DATE DEFAULT CURRENT_TIMESTAMP(),
    `updated_at` DATE NULL,
    CONSTRAINT `book_isbn_pk` PRIMARY KEY (`isbn`),
    CONSTRAINT `book_category_name_fk` FOREIGN KEY(`category_name`) 
        REFERENCES `category`(`name`) ON UPDATE CASCADE ON DELETE CASCADE
)ENGINE = INNODB;

CREATE TABLE student (
    `enrollment` VARCHAR(12) NOT NULL,
    `name` VARCHAR(256) NOT NULL,
    `grade` VARCHAR(5) NOT NULL,
    `group` VARCHAR(5) NOT NULL,
    `created_at` DATE DEFAULT CURRENT_TIMESTAMP(),
    `updated_at` DATE NULL,
    CONSTRAINT `student_id_pk` PRIMARY KEY (`enrollment`)
)ENGINE = INNODB;

CREATE TABLE loan (
    `folio` VARCHAR(12) NOT NULL,
    `return_date` DATE NOT NULL,
    `state` VARCHAR(32) DEFAULT 'Prestado' NULL,
    `student_enrollment` VARCHAR(12) NOT NULL,
    `book_isbn` VARCHAR(13) NOT NULL,
    `created_at` DATE DEFAULT CURRENT_TIMESTAMP(),
    `updated_at` DATE NULL,
    CONSTRAINT `loan_id_pk` PRIMARY KEY (`folio`),
    CONSTRAINT `loan_student_enrollment_fk` FOREIGN KEY(`student_enrollment`)
        REFERENCES `student`(`enrollment`) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT `loan_book_isbn_fk` FOREIGN KEY(`book_isbn`)
        REFERENCES `book`(`isbn`) ON UPDATE CASCADE ON DELETE CASCADE
)ENGINE = INNODB;
