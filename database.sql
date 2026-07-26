-- =============================================================================
--  OnlineExam  |  Database schema + seed data
--  Engine: MySQL 8.x   |   Run once:  mysql -u root -p < database.sql
-- =============================================================================

DROP DATABASE IF EXISTS college;
CREATE DATABASE college CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE college;

-- ---------------------------------------------------------------------------
--  Users
-- ---------------------------------------------------------------------------
CREATE TABLE admin (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE teacher (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(100) NOT NULL,
    email    VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE student (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(100) NOT NULL,
    email    VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------------
--  Academic content
-- ---------------------------------------------------------------------------
CREATE TABLE subject (
    code       VARCHAR(20)  PRIMARY KEY,
    name       VARCHAR(120) NOT NULL,
    teacher_id INT          NOT NULL,
    CONSTRAINT fk_subject_teacher FOREIGN KEY (teacher_id)
        REFERENCES teacher(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE question (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    subject_code   VARCHAR(20)  NOT NULL,
    question_text  VARCHAR(500) NOT NULL,
    option1        VARCHAR(255) NOT NULL,
    option2        VARCHAR(255) NOT NULL,
    option3        VARCHAR(255) NOT NULL,
    option4        VARCHAR(255) NOT NULL,
    correct_answer VARCHAR(255) NOT NULL,
    CONSTRAINT fk_question_subject FOREIGN KEY (subject_code)
        REFERENCES subject(code) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE result (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    student_id   INT         NOT NULL,
    subject_code VARCHAR(20) NOT NULL,
    score        INT         NOT NULL,
    total        INT         NOT NULL,
    attempted_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_result_student FOREIGN KEY (student_id)
        REFERENCES student(id) ON DELETE CASCADE,
    CONSTRAINT fk_result_subject FOREIGN KEY (subject_code)
        REFERENCES subject(code) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Helpful indexes for leaderboard / history queries
CREATE INDEX idx_result_subject ON result(subject_code);
CREATE INDEX idx_result_student ON result(student_id);

-- =============================================================================
--  SEED DATA  (demo accounts - passwords are salted SHA-256, 100k iterations)
--  Login credentials:
--     Admin    ->  admin@tiet.edu      /  Admin@123
--     Teacher  ->  prof.sharma@tiet.edu /  Teacher@123
--     Student  ->  adwitiya@tiet.edu       /  Student@123
-- =============================================================================
INSERT INTO admin (email, password) VALUES
    ('admin@tiet.edu', 'hfhpc6+l6qwojBvQrK+tjg==:6/8/NG3a+4/MIKvFOR5/H/1q8ZoOPTM4XOk2hW9mXgM=');

INSERT INTO teacher (name, email, password) VALUES
    ('Dr. R. Sharma', 'prof.sharma@tiet.edu', 'DyUbi6cDvQkqc/narDpBrA==:vTttDqIAW2qTlkkOrJvky4M+DQrTt5e+GWhyysUN9YM=');

INSERT INTO student (name, email, password) VALUES
    ('Adwitiya Shukla', 'adwitiya@tiet.edu', 'vxiON9NjnndegSydoKHalg==:2v+h+kzxPUyt70PZV+sok0ej4o6YdnxSo8dUQMb1Kkg=');

-- Subjects (owned by teacher id = 1)
INSERT INTO subject (code, name, teacher_id) VALUES
    ('CS101', 'Programming Fundamentals', 1),
    ('MA201', 'Discrete Mathematics',     1);

-- Questions for CS101
INSERT INTO question (subject_code, question_text, option1, option2, option3, option4, correct_answer) VALUES
    ('CS101', 'Which keyword is used to define a constant in Java?', 'final', 'const', 'static', 'constant', 'final'),
    ('CS101', 'What is the size of an int in Java?', '2 bytes', '4 bytes', '8 bytes', 'Depends on OS', '4 bytes'),
    ('CS101', 'Which method is the entry point of a Java application?', 'start()', 'main()', 'run()', 'init()', 'main()'),
    ('CS101', 'What does JVM stand for?', 'Java Virtual Machine', 'Java Verified Method', 'Just-In-Time VM', 'Java Variable Manager', 'Java Virtual Machine'),
    ('CS101', 'Which of these is NOT a primitive type in Java?', 'int', 'boolean', 'String', 'char', 'String');

-- Questions for MA201
INSERT INTO question (subject_code, question_text, option1, option2, option3, option4, correct_answer) VALUES
    ('MA201', 'How many elements are in the power set of a set with 3 elements?', '3', '6', '8', '9', '8'),
    ('MA201', 'By De Morgan''s law, the negation of "p AND q" equals?', 'NOT p AND NOT q', 'NOT p OR NOT q', 'p OR q', 'NOT p AND q', 'NOT p OR NOT q'),
    ('MA201', 'A connected graph with no cycles is called a?', 'Tree', 'Complete graph', 'Cyclic graph', 'Bipartite graph', 'Tree'),
    ('MA201', 'What is the value of 5! (5 factorial)?', '25', '120', '60', '20', '120'),
    ('MA201', 'Which logical connective does the symbol AND represent?', 'Disjunction', 'Conjunction', 'Negation', 'Implication', 'Conjunction');

-- A couple of sample attempts so dashboards / leaderboard are not empty
INSERT INTO result (student_id, subject_code, score, total, attempted_at) VALUES
    (1, 'CS101', 4, 5, '2026-07-20 10:15:00'),
    (1, 'MA201', 3, 5, '2026-07-21 14:30:00');
