-- V2: Remaining domain tables and relationships for Oracle
-- Ensure constraint names <= 30 chars

-- PERMISSIONS
CREATE TABLE permissions (
  id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name VARCHAR2(100) NOT NULL,
  description VARCHAR2(255)
);
CREATE UNIQUE INDEX permissions_name_un ON permissions(name);

-- ROLE_PERMISSION join
CREATE TABLE role_permission (
  role_id NUMBER NOT NULL,
  permission_id NUMBER NOT NULL,
  CONSTRAINT role_perm_pk PRIMARY KEY (role_id, permission_id),
  CONSTRAINT role_perm_role_fk FOREIGN KEY (role_id) REFERENCES roles(id),
  CONSTRAINT role_perm_perm_fk FOREIGN KEY (permission_id) REFERENCES permissions(id)
);

-- LEVELS
CREATE TABLE levels (
  id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name VARCHAR2(100) NOT NULL,
  created_at TIMESTAMP
);

-- PERIODS
CREATE TABLE periods (
  id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  code VARCHAR2(50) NOT NULL,
  start_date DATE,
  end_date DATE
);
CREATE UNIQUE INDEX periods_code_un ON periods(code);

-- GROUPS
CREATE TABLE groups (
  id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name VARCHAR2(100) NOT NULL,
  semester VARCHAR2(50),
  period_id NUMBER,
  CONSTRAINT grp_period_fk FOREIGN KEY (period_id) REFERENCES periods(id)
);

-- ADMINISTRATORS
CREATE TABLE administrators (
  id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  department VARCHAR2(100),
  user_id NUMBER NOT NULL,
  CONSTRAINT admin_user_fk FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT admin_user_un UNIQUE (user_id)
);

-- PROFESSORS
CREATE TABLE professors (
  id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  registration_date DATE,
  status VARCHAR2(30),
  office_location VARCHAR2(100),
  specialty_area VARCHAR2(100),
  max_groups_allowed NUMBER,
  academic_rank VARCHAR2(60),
  rating_average NUMBER,
  user_id NUMBER NOT NULL,
  CONSTRAINT prof_user_fk FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT prof_user_un UNIQUE (user_id)
);

-- STUDENTS
CREATE TABLE students (
  id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  code VARCHAR2(50),
  gpa NUMBER,
  study_group_affiliation VARCHAR2(100),
  user_id NUMBER NOT NULL,
  level_id NUMBER,
  CONSTRAINT stud_user_fk FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT stud_level_fk FOREIGN KEY (level_id) REFERENCES levels(id),
  CONSTRAINT stud_user_un UNIQUE (user_id)
);

-- COURSES
CREATE TABLE courses (
  id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name VARCHAR2(150) NOT NULL,
  administrator_id NUMBER,
  CONSTRAINT course_admin_fk FOREIGN KEY (administrator_id) REFERENCES administrators(id)
);

-- TEACHING_ASSIGNMENTS
CREATE TABLE teaching_assignments (
  id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  assigned_date DATE,
  status VARCHAR2(30),
  evaluation_weight NUMBER,
  role_in_course VARCHAR2(60),
  classroom_location VARCHAR2(100),
  modality VARCHAR2(60),
  notes VARCHAR2(255),
  course_id NUMBER,
  professor_id NUMBER,
  group_id NUMBER,
  CONSTRAINT ta_course_fk FOREIGN KEY (course_id) REFERENCES courses(id),
  CONSTRAINT ta_prof_fk FOREIGN KEY (professor_id) REFERENCES professors(id),
  CONSTRAINT ta_group_fk FOREIGN KEY (group_id) REFERENCES groups(id)
);

-- EXERCISES
CREATE TABLE exercises (
  id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  description VARCHAR2(4000),
  difficulty VARCHAR2(50)
);

-- ACTIVITIES
CREATE TABLE activities (
  id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  title VARCHAR2(200) NOT NULL,
  description VARCHAR2(4000),
  start_date DATE,
  end_date DATE,
  professor_id NUMBER,
  CONSTRAINT act_prof_fk FOREIGN KEY (professor_id) REFERENCES professors(id)
);

-- ACTIVITY_EXERCISE (join with extra column)
CREATE TABLE activity_exercise (
  activity_id NUMBER NOT NULL,
  exercise_id NUMBER NOT NULL,
  position NUMBER,
  CONSTRAINT act_ex_pk PRIMARY KEY (activity_id, exercise_id),
  CONSTRAINT act_ex_act_fk FOREIGN KEY (activity_id) REFERENCES activities(id),
  CONSTRAINT act_ex_ex_fk FOREIGN KEY (exercise_id) REFERENCES exercises(id)
);

-- EXERCISE_LEVEL join
CREATE TABLE exercise_level (
  level_id NUMBER NOT NULL,
  exercise_id NUMBER NOT NULL,
  CONSTRAINT ex_lvl_pk PRIMARY KEY (level_id, exercise_id),
  CONSTRAINT ex_lvl_lvl_fk FOREIGN KEY (level_id) REFERENCES levels(id),
  CONSTRAINT ex_lvl_ex_fk FOREIGN KEY (exercise_id) REFERENCES exercises(id)
);

-- STUDENT_GROUP join
CREATE TABLE student_group (
  student_id NUMBER NOT NULL,
  group_id NUMBER NOT NULL,
  CONSTRAINT stud_grp_pk PRIMARY KEY (student_id, group_id),
  CONSTRAINT stud_grp_stud_fk FOREIGN KEY (student_id) REFERENCES students(id),
  CONSTRAINT stud_grp_grp_fk FOREIGN KEY (group_id) REFERENCES groups(id)
);

-- SUBMISSIONS
CREATE TABLE submissions (
  id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  link VARCHAR2(500),
  date TIMESTAMP,
  status VARCHAR2(30),
  created_at TIMESTAMP,
  student_id NUMBER,
  exercise_id NUMBER,
  CONSTRAINT subm_stud_fk FOREIGN KEY (student_id) REFERENCES students(id),
  CONSTRAINT subm_ex_fk FOREIGN KEY (exercise_id) REFERENCES exercises(id)
);

-- FILES
CREATE TABLE files (
  id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  file_name VARCHAR2(255),
  file_path VARCHAR2(1000),
  file_type VARCHAR2(100),
  file_size NUMBER,
  upload_date DATE,
  submission_id NUMBER,
  activity_id NUMBER,
  CONSTRAINT file_subm_fk FOREIGN KEY (submission_id) REFERENCES submissions(id),
  CONSTRAINT file_act_fk FOREIGN KEY (activity_id) REFERENCES activities(id)
);

-- SCORES
CREATE TABLE scores (
  id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  points_awarded NUMBER,
  assignment_date TIMESTAMP,
  submission_id NUMBER,
  CONSTRAINT score_subm_fk FOREIGN KEY (submission_id) REFERENCES submissions(id)
);

-- POINT_CODES
CREATE TABLE point_codes (
  code VARCHAR2(100) PRIMARY KEY,
  points NUMBER,
  redeemed_at TIMESTAMP,
  status VARCHAR2(30),
  usage_limit NUMBER,
  activity_id NUMBER,
  student_id NUMBER,
  CONSTRAINT pcode_act_fk FOREIGN KEY (activity_id) REFERENCES activities(id),
  CONSTRAINT pcode_stud_fk FOREIGN KEY (student_id) REFERENCES students(id)
);
