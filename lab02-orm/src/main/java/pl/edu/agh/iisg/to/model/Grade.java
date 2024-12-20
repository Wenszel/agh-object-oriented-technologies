package pl.edu.agh.iisg.to.model;


import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = Grade.TABLE_NAME)
public class Grade {

    public static final String TABLE_NAME = "grade";

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "grade_gen")
    @TableGenerator(
            name = "grade_gen",
            table = "id_generator",
            pkColumnName = "gen_name",
            valueColumnName = "gen_value",
            pkColumnValue = "grade_seq",
            allocationSize = 1
    )
    @Column(name = Columns.ID)
    private int id;

    @Column(name = Columns.GRADE, nullable = false)
    private float grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = Columns.STUDENT_ID)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = Columns.COURSE_ID)
    private Course course;

    public Grade() {
    }

    public Grade(final Student student, final Course course, final float grade) {
        this.student = student;
        this.course = course;
        this.grade = grade;
    }

    public int id() {
        return id;
    }

    public float grade() {
        return grade;
    }

    public Student student() {
        return student;
    }

    public String getStudentFullName() {
        return student.fullName();
    }

    public Course course() {
        return course;
    }

    public static class Columns {

        public static final String ID = "id";

        public static final String GRADE = "grade";

        public static final String STUDENT_ID = "student_id";

        public static final String COURSE_ID = "course_id";

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grade grade1 = (Grade) o;
        return id == grade1.id && Float.compare(grade, grade1.grade) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, grade);
    }
}
