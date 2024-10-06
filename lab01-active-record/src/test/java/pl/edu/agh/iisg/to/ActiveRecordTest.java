package pl.edu.agh.iisg.to;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.agh.iisg.to.connection.ConnectionProvider;
import pl.edu.agh.iisg.to.executor.QueryExecutor;
import pl.edu.agh.iisg.to.model.Course;
import pl.edu.agh.iisg.to.model.Grade;
import pl.edu.agh.iisg.to.model.Student;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ActiveRecordTest {

    @BeforeAll
    public static void init() {
        ConnectionProvider.init("jdbc:sqlite:active_record_test.db");
    }

    @BeforeEach
    public void setUp() throws SQLException {
        QueryExecutor.delete("DELETE FROM STUDENT_COURSE");
        QueryExecutor.delete("DELETE FROM STUDENT");
        QueryExecutor.delete("DELETE FROM COURSE");
        QueryExecutor.delete("DELETE FROM GRADE");
    }

    @AfterAll
    public static void cleanUp() throws SQLException {
        ConnectionProvider.close();
    }

    @Test
    public void studentWithUniqueIndexNumberCanBeCreated() {
        // When
        var student1 = Student.create("Adam", "Kowalski", 100122);
        var student2 = Student.create("Jan", "Nowak", 100123);
        var redundantStudent = Student.create("Kasia", "Kowalska", 100123);

        // Then
        checkStudent(student1);
        checkStudent(student2);

        assertNotEquals(student1.orElseThrow().id(), student2.orElseThrow().id());
        assertFalse(redundantStudent.isPresent());
    }

    @Test
    public void studentCanBeFoundById() {
        // Given
        var student = Student.create("Kasia", "Kowalska", 200124);

        // When
        var foundStudent = Student.findById(student.orElseThrow().id());
        var nonExistingStudent = Student.findById(Integer.MAX_VALUE);

        // Then
        checkStudent(foundStudent);
        Assertions.assertEquals(student.orElseThrow(), foundStudent.orElseThrow());
        Assertions.assertFalse(nonExistingStudent.isPresent());
    }

    @Test
    public void studentCanBeFoundByIndexNumber() {
        // Given
        var student = Student.create("Kasia", "Kowalska", 300124);

        // When
        var foundStudent = Student.findByIndexNumber(student.orElseThrow().indexNumber());

        // Then
        checkStudent(student);
        Assertions.assertEquals(student.orElseThrow(), foundStudent.orElseThrow());
    }

    @Test
    public void courseWithUniqueNameCanBeCreated() {
        // When
        var course1 = Course.create("TO");
        var course2 = Course.create("TO2");
        var redundantCourse = Course.create("TO2");

        // Then
        checkCourse(course1);
        checkCourse(course2);

        assertNotEquals(course1.orElseThrow().id(), course2.orElseThrow().id());
        assertFalse(redundantCourse.isPresent());
    }

    @Test
    public void courseCanBeFoundById() {
        // Given
        var course = Course.create("TK");

        // When
        var foundCourse = Course.findById(course.orElseThrow().id());

        // Then
        checkCourse(course);
        assertEquals(course.orElseThrow(), foundCourse.orElseThrow());
    }

    @Test
    public void studentCanBeEnrolledInCourseOnce() {
        // Given
        var student = Student.create("Kasia", "Kowalska", 700124);
        var course = Course.create("MOWNIT");

        // When
        boolean studentEnrolled = course.orElseThrow().enrollStudent(student.orElseThrow());
        boolean reundantStudentEnroll = course.orElseThrow().enrollStudent(student.orElseThrow());

        // Then
        checkStudent(student);
        checkCourse(course);

        assertTrue(studentEnrolled);
        assertFalse(reundantStudentEnroll);
    }

    @Test
    public void courseConsistsOfEnrolledStudents() {
        // Given
        var student1 = Student.create("Adam", "Paciaciak", 800125).orElseThrow();
        var student2 = Student.create("Jan", "Paciaciak", 800126).orElseThrow();
        var course = Course.create("WDI").orElseThrow();

        course.enrollStudent(student1);
        course.enrollStudent(student2);

        // When
        var students = course.studentList();

        // Then
        checkStudent(student1);
        checkStudent(student2);
        checkCourse(course);

        assertEquals(2, students.size());
        assertTrue(students.contains(student1));
        assertTrue(students.contains(student2));
    }

    @Test
    public void courseStudentListIsCached() {
        // Given
        var student1 = Student.create("Adam", "Paciaciak", 800125).orElseThrow();
        var student2 = Student.create("Jan", "Paciaciak", 800126).orElseThrow();
        var course = Course.create("WDI").orElseThrow();

        course.enrollStudent(student1);
        course.enrollStudent(student2);

        // When
        List<Student> students = course.cachedStudentsList();
        List<Student> cachedStudents = course.cachedStudentsList();

        // Then
        checkStudent(student1);
        checkStudent(student2);
        checkCourse(course);

        assertEquals(2, students.size());
        assertTrue(students.contains(student1));
        assertTrue(students.contains(student2));

        assertEquals(cachedStudents, students);
    }

    @Test
    public void studentCanBeGraded() {
        // Given
        var student = Student.create("Kasia", "Kowalska", 900124).orElseThrow();
        var course = Course.create("MOWNIT 2").orElseThrow();

        // When
        boolean studentGraded = Grade.gradeStudent(student, course, 5.0f);

        // Then
        checkStudent(student);
        checkCourse(course);

        assertTrue(studentGraded);
    }

    @Test
    public void studentReportCanBeObtained() {
        // Given
        var student = Student.create("Kasia", "Kowalska", 1000124).orElseThrow();
        var course1 = Course.create("Bazy").orElseThrow();
        var course2 = Course.create("Bazy 2").orElseThrow();

        // When
        Grade.gradeStudent(student, course1, 5.0f);
        Grade.gradeStudent(student, course1, 4.0f);
        Grade.gradeStudent(student, course2, 5.0f);
        Grade.gradeStudent(student, course2, 3.0f);

        Map<Course, Float> report = student.createReport();

        // Then
        checkStudent(student);
        checkCourse(course1);
        checkCourse(course2);

        assertEquals(Float.compare(4.5f, report.get(course1)), 0);
        assertEquals(Float.compare(4.0f, report.get(course2)), 0);
    }

    private void checkStudent(final Student student) {
        checkStudent(Optional.ofNullable(student));
    }

    private void checkStudent(final Optional<Student> student) {
        assertTrue(student.isPresent());
        student.ifPresent(s -> {
            assertTrue(s.id() > 0);
            assertNotNull(s.firstName());
            assertNotNull(s.lastName());
            assertTrue(s.indexNumber() > 0);
        });
    }

    private void checkCourse(final Course course) {
        checkCourse(Optional.ofNullable(course));
    }

    private void checkCourse(final Optional<Course> course) {
        assertTrue(course.isPresent());
        course.ifPresent(c -> {
            assertTrue(c.id() > 0);
            assertNotNull(c.name());
        });
    }
}
