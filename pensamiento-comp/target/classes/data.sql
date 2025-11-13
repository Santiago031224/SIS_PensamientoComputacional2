-- Limpiar tablas (en orden correcto por dependencias de claves foráneas)
DELETE FROM activity_exercise;
DELETE FROM point_codes;
DELETE FROM scores;
DELETE FROM submissions;
DELETE FROM student_group;
DELETE FROM teaching_assignments;
DELETE FROM exercise_level;
DELETE FROM role_permission;
DELETE FROM user_role;
DELETE FROM activities;
DELETE FROM exercises;
DELETE FROM students;
DELETE FROM professors;
DELETE FROM administrators;
DELETE FROM users;
DELETE FROM groups;
DELETE FROM periods;
DELETE FROM courses;
DELETE FROM levels;
DELETE FROM roles;
DELETE FROM permissions;

-- Insertar Roles
INSERT INTO roles (name) VALUES 
('ADMIN'),
('PROFESSOR'),
('STUDENT');

-- Insertar Permisos
INSERT INTO permissions (name, description) VALUES 
('READ_USER', 'Permite leer usuarios'),
('WRITE_USER', 'Permite crear o modificar usuarios'),
('DELETE_USER', 'Permite eliminar usuarios'),
('READ_COURSE', 'Permite leer cursos'),
('WRITE_COURSE', 'Permite crear o modificar cursos'),
('READ_ACTIVITY', 'Permite leer actividades'),
('WRITE_ACTIVITY', 'Permite crear o modificar actividades'),
('READ_EXERCISE', 'Permite leer ejercicios'),
('WRITE_EXERCISE', 'Permite crear o modificar ejercicios');

-- Asignar Permisos a Roles
INSERT INTO role_permission (role_id, permission_id) VALUES 
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9),  -- ADMIN todos los permisos
(2, 1), (2, 4), (2, 6), (2, 7), (2, 8), (2, 9),  -- PROFESSOR permisos de lectura/escritura académicos
(3, 1), (3, 4), (3, 6), (3, 8);  -- STUDENT permisos básicos de lectura

-- Insertar Usuarios
INSERT INTO users (name, last_name, email, password, status, created_at, profile_picture) VALUES 
('Francisco', 'Lozano', 'admin@example.com', '$2a$10$ui2bR5yqWgggdxDviaZBROdD1sa4NjAMel6j91tT.gH1G8q7JeC1S', 'ACTIVE', NOW(), 'admin.jpg'),
('María', 'García', 'maria@example.com', '$2a$10$ui2bR5yqWgggdxDviaZBROdD1sa4NjAMel6j91tT.gH1G8q7JeC1S', 'ACTIVE', NOW(), 'maria.jpg'),
('Carlos', 'López', 'carlos@example.com', '$2a$10$ui2bR5yqWgggdxDviaZBROdD1sa4NjAMel6j91tT.gH1G8q7JeC1S', 'ACTIVE', NOW(), 'carlos.jpg'),
('Ana', 'Rodríguez', 'ana@example.com', '$2a$10$ui2bR5yqWgggdxDviaZBROdD1sa4NjAMel6j91tT.gH1G8q7JeC1S', 'ACTIVE', NOW(), 'ana.jpg'),
('Pedro', 'Martínez', 'pedro@example.com', '$2a$10$ui2bR5yqWgggdxDviaZBROdD1sa4NjAMel6j91tT.gH1G8q7JeC1S', 'ACTIVE', NOW(), 'pedro.jpg'),
('Laura', 'Hernández', 'laura@example.com', '$2a$10$ui2bR5yqWgggdxDviaZBROdD1sa4NjAMel6j91tT.gH1G8q7JeC1S', 'ACTIVE', NOW(), 'laura.jpg'),
('Miguel', 'Díaz', 'miguel@example.com', '$2a$10$ui2bR5yqWgggdxDviaZBROdD1sa4NjAMel6j91tT.gH1G8q7JeC1S', 'ACTIVE', NOW(), 'miguel.jpg'),
('Elena', 'Torres', 'elena@example.com', '$2a$10$ui2bR5yqWgggdxDviaZBROdD1sa4NjAMel6j91tT.gH1G8q7JeC1S', 'ACTIVE', NOW(), 'elena.jpg');

-- Asignar Roles a Usuarios
INSERT INTO user_role (user_id, role_id) VALUES 
(1, 1),  -- Francisco como ADMIN
(2, 2),  -- María como PROFESSOR
(3, 3),  -- Carlos como STUDENT
(4, 2),  -- Ana como PROFESSOR
(5, 3),  -- Pedro como STUDENT
(6, 3),  -- Laura como STUDENT
(7, 3),  -- Miguel como STUDENT
(8, 3);  -- Elena como STUDENT

-- Insertar Administradores
INSERT INTO administrators (department, user_id) VALUES 
('Tecnología', 1);

-- Insertar Cursos
INSERT INTO courses (name, administrator_id) VALUES 
('Programación I', 1),
('Base de Datos', 1),
('Estructuras de Datos', 1),
('Inteligencia Artificial', 1);

-- Insertar Periodos
INSERT INTO periods (code, start_date, end_date) VALUES 
('2024-01', '2024-01-15', '2024-06-15'),
('2024-02', '2024-07-01', '2024-12-15');

-- Insertar Niveles
INSERT INTO levels (name, created_at) VALUES 
('Principiante', NOW()),
('Intermedio', NOW()),
('Avanzado', NOW());

-- Insertar Grupos
INSERT INTO groups (name, semester, period_id) VALUES 
('Grupo A', '2024-1', 1),
('Grupo B', '2024-1', 1),
('Grupo C', '2024-2', 2),
('Grupo D', '2024-2', 2);

-- Insertar Profesores
INSERT INTO professors (registration_date, status, office_location, specialty_area, max_groups_allowed, academic_rank, rating_average, user_id) VALUES 
('2020-03-15', 'ACTIVE', 'Edificio A-201', 'Programación', 3, 'Profesor Asociado', 4.5, 2),
('2018-08-20', 'ACTIVE', 'Edificio B-105', 'Base de Datos', 4, 'Profesor Titular', 4.7, 4);

-- Insertar Estudiantes
INSERT INTO students (code, gpa, study_group_affiliation, user_id, level_id) VALUES 
('S001', 3.8, 'Grupo A', 3, 1),
('S002', 4.0, 'Grupo A', 5, 2),
('S003', 3.5, 'Grupo B', 6, 1),
('S004', 3.9, 'Grupo C', 7, 3),
('S005', 3.7, 'Grupo D', 8, 2);

-- Insertar Asignaciones de Enseñanza
INSERT INTO teaching_assignments (assigned_date, status, evaluation_weight, role_in_course, classroom_location, modality, notes, course_id, professor_id, group_id) VALUES 
('2024-01-15', 'ACTIVE', 0.4, 'Profesor Titular', 'Aula 101', 'PRESENCIAL', 'Grupo matutino', 1, 1, 1),
('2024-01-15', 'ACTIVE', 0.6, 'Profesor Asistente', 'Aula 102', 'PRESENCIAL', 'Grupo vespertino', 1, 1, 2),
('2024-01-15', 'ACTIVE', 0.5, 'Profesor Titular', 'Laboratorio B', 'HIBRIDO', 'Laboratorio de BD', 2, 2, 3),
('2024-07-01', 'ACTIVE', 0.5, 'Profesor Titular', 'Aula 201', 'VIRTUAL', 'Clases virtuales', 3, 1, 4);

-- Insertar Ejercicios
INSERT INTO exercises (description, difficulty) VALUES 
('Implementar una función que calcule el factorial de un número', 'FACIL'),
('Crear una base de datos para un sistema de biblioteca', 'MEDIO'),
('Implementar un árbol binario de búsqueda', 'DIFICIL'),
('Resolver el problema de las N-reinas', 'DIFICIL'),
('Crear consultas SQL complejas con JOINs', 'MEDIO'),
('Implementar un algoritmo de ordenamiento rápido', 'MEDIO');

-- Insertar Relación Ejercicio-Nivel
INSERT INTO exercise_level (exercise_id, level_id) VALUES 
(1, 1), (1, 2),  -- Factorial para principiantes e intermedios
(2, 2), (2, 3),  -- BD para intermedios y avanzados
(3, 3),          -- Árbol binario para avanzados
(4, 3),          -- N-reinas para avanzados
(5, 2), (5, 3),  -- SQL para intermedios y avanzados
(6, 2), (6, 3);  -- Ordenamiento para intermedios y avanzados

-- Insertar Actividades
INSERT INTO activities (title, start_date, end_date, description, professor_id) VALUES 
('Introducción a la Programación', '2024-02-01', '2024-02-28', 'Conceptos básicos de programación', 1),
('Bases de Datos Relacionales', '2024-03-01', '2024-03-31', 'Diseño e implementación de BD', 2),
('Estructuras de Datos Avanzadas', '2024-04-01', '2024-04-30', 'Árboles y grafos', 1),
('Proyecto Final de Programación', '2024-05-01', '2024-05-31', 'Desarrollo de proyecto integrador', 1);

-- Insertar Relación Actividad-Ejercicio
INSERT INTO activity_exercise (activity_id, exercise_id, position) VALUES 
(1, 1, 1),
(1, 6, 2),
(2, 2, 1),
(2, 5, 2),
(3, 3, 1),
(3, 4, 2),
(4, 1, 1),
(4, 3, 2),
(4, 6, 3);

-- Insertar Códigos de Puntos
INSERT INTO point_codes (code, points, redeemed_at, status, usage_limit, activity_id, student_id) VALUES 
('CODE001', 10, NULL, 'AVAILABLE', 1, 1, NULL),
('CODE002', 15, '2024-02-15 10:30:00', 'REDEEMED', 1, 1, 1),
('CODE003', 20, NULL, 'AVAILABLE', 2, 2, NULL),
('CODE004', 25, '2024-03-20 14:15:00', 'REDEEMED', 1, 2, 2),
('CODE005', 30, NULL, 'EXPIRED', 1, 3, NULL);

-- Insertar Envíos
INSERT INTO submissions (link, date, status, created_at, student_id, exercise_id) VALUES 
('http://example.com/file1.java', '2024-02-10 09:00:00', 'SUBMITTED', NOW(), 1, 1),
('http://example.com/file2.sql', '2024-03-05 11:30:00', 'GRADED', NOW(), 2, 2),
('http://github.com/project', '2024-04-12 16:45:00', 'UNDER_REVIEW', NOW(), 3, 3),
('http://example.com/file4.java', '2024-05-20 08:15:00', 'SUBMITTED', NOW(), 4, 6);

-- Insertar Puntuaciones
INSERT INTO scores (points_awarded, assignment_date, submission_id) VALUES 
(9.5, '2024-02-12 10:00:00', 1),
(8.0, '2024-03-07 14:20:00', 2),
(10.0, '2024-04-15 09:30:00', 3);

-- Insertar Relación Estudiante-Grupo
INSERT INTO student_group (student_id, group_id) VALUES 
(1, 1), (2, 1), (3, 2), (4, 3), (5, 4);

-- Actualizar últimos logins
UPDATE users SET last_login = NOW() WHERE id IN (1, 2, 3);