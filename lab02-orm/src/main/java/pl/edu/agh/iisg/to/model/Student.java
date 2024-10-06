package pl.edu.agh.iisg.to.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Student {

    public static final String TABLE_NAME = "student";

    private int id;

    private String firstName;

    private String lastName;

    private int indexNumber;

    private Set<Grade> gradeSet = new HashSet<>();

    private Set<Course> courseSet = new HashSet<>();

    Student() {
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
