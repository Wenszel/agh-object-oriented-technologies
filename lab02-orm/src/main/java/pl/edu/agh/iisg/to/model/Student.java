package pl.edu.agh.iisg.to.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name=Student.TABLE_NAME)
public class Student {

    public static final String TABLE_NAME = "student";
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "student_gen")
    @TableGenerator(
            name = "student_gen",
            table = "id_generator",
            pkColumnName = "gen_name",
            valueColumnName = "gen_value",
            pkColumnValue = "student_seq",
            allocationSize = 1
    )
    @Column(name = Columns.ID)
    private int id;
    @Column(name = Columns.FIRST_NAME, nullable = false, length = 50)
    private String firstName;
    @Column(name = Columns.LAST_NAME, nullable = false, length = 50)
    private String lastName;
    @Column(unique = true)
    private int indexNumber;

    @OneToMany
    private final Set<Grade> gradeSet = new HashSet<>();

    @ManyToMany(mappedBy = "studentSet", cascade = CascadeType.PERSIST)

    private final Set<Course> courseSet = new HashSet<>();

    public Student() {
    }

    public Student(final String firstName, final String lastName, final int indexNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.indexNumber = indexNumber;
    }

    public int id() {
        return id;
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public String fullName() {
        return firstName + " " + lastName;
    }

    public int indexNumber() {
        return indexNumber;
    }

    public Set<Course> courseSet() {
        return courseSet;
    }

    public Set<Grade> gradeSet() {
        return gradeSet;
    }

    public static class Columns {

        public static final String ID = "id";

        public static final String FIRST_NAME = "first_name";

        public static final String LAST_NAME = "last_name";

        public static final String INDEX_NUMBER = "index_number";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id && indexNumber == student.indexNumber && Objects.equals(firstName, student.firstName) && Objects.equals(lastName, student.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, indexNumber);
    }
}
