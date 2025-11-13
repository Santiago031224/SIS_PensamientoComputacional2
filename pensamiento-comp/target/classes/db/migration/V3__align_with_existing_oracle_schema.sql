-- V3: Align JPA table names with existing Oracle schema via synonyms
-- Also drop accidental tables created by earlier migrations if present

-- Helper: drop table if exists
BEGIN
  FOR t IN (
    SELECT table_name FROM user_tables WHERE table_name IN (
      'USERS','ROLES','USER_ROLE','PERMISSIONS','COURSES','ADMINISTRATORS','PROFESSORS','STUDENTS','PERIODS','GROUPS','LEVELS','TEACHING_ASSIGNMENTS',
      'ACTIVITIES','EXERCISES','SUBMISSIONS','FILES','SCORES','POINT_CODES','ACTIVITY_EXERCISE','EXERCISE_LEVEL'
    )
  ) LOOP
    BEGIN
      EXECUTE IMMEDIATE 'DROP TABLE ' || t.table_name || ' CASCADE CONSTRAINTS';
    EXCEPTION WHEN OTHERS THEN NULL; END;
  END LOOP;
END;
/

-- Create synonyms mapping plural JPA names to legacy singular tables
-- Note: Unquoted identifiers are folded to uppercase by Oracle

CREATE OR REPLACE SYNONYM users FOR "USER";
CREATE OR REPLACE SYNONYM roles FOR ROLE;
CREATE OR REPLACE SYNONYM administrators FOR ADMINISTRATOR;
CREATE OR REPLACE SYNONYM professors FOR PROFESSOR;
CREATE OR REPLACE SYNONYM students FOR STUDENT;
CREATE OR REPLACE SYNONYM periods FOR PERIOD;
CREATE OR REPLACE SYNONYM groups FOR "GROUP";
CREATE OR REPLACE SYNONYM levels FOR "LEVEL";
CREATE OR REPLACE SYNONYM courses FOR COURSE;
CREATE OR REPLACE SYNONYM permissions FOR PERMISSION;
CREATE OR REPLACE SYNONYM teaching_assignments FOR TEACHING_ASSIGNMENT;

-- Join tables used by JPA already match legacy names (USER_ROLE, ROLE_PERMISSION, STUDENT_GROUP)
-- No synonyms required for these; JPA will refer to them unquoted and Oracle resolves to uppercase.
