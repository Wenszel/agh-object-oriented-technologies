package pl.edu.agh.iisg.to.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = Course.TABLE_NAME)
public class Course {

    public static final String TABLE_NAME = "course";

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = Columns.ID)
    private int id;

    @Column(name = Columns.NAME, nullable = false, length = 50, unique = true)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "student_id", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "ID"))
    private Set<Student> studentSet = new HashSet<>();

    @OneToMany(mappedBy = "course")
    private Set<Grade> gradeSet = new HashSet<>();

    Course() {
    }

    public Course(final String name) {
        this.name = name;
    }

    public int id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Set<Student> studentSet() {
        return studentSet;
    }

    public Set<Grade> gradeSet() {
        return gradeSet;
    }

    public static class Columns {

        public static final String ID = "id";

        public static final String NAME = "name";

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id == course.id && Objects.equals(name, course.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
