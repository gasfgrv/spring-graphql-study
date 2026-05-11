CREATE TABLE enrollments
(
    id          BIGSERIAL PRIMARY KEY,
    student_id  BIGINT NOT NULL,
    course_id   BIGINT NOT NULL,
    progress    INTEGER DEFAULT 0 CHECK (progress >= 0 AND progress <= 100),
    enrolled_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    -- Chaves Estrangeiras
    CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE,
    CONSTRAINT fk_course FOREIGN KEY (course_id) REFERENCES courses (id) ON DELETE CASCADE,

    -- Garante que um aluno não se matricule duas vezes no mesmo curso
    CONSTRAINT unique_student_course UNIQUE (student_id, course_id)
);