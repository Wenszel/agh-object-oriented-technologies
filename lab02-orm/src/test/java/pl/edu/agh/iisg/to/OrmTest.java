package pl.edu.agh.iisg.to;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.agh.iisg.to.dao.CourseDao;
import pl.edu.agh.iisg.to.dao.GradeDao;
import pl.edu.agh.iisg.to.dao.StudentDao;
import pl.edu.agh.iisg.to.model.Course;
import pl.edu.agh.iisg.to.model.Student;
import pl.edu.agh.iisg.to.service.SchoolService;
import pl.edu.agh.iisg.to.session.SessionService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrmTest {

    private final SessionService sessionService = new SessionService();

    private final StudentDao studentDao = new StudentDao(sessionService);

    private final CourseDao courseDao = new CourseDao(sessionService);

    private final GradeDao gradeDao = new GradeDao(sessionService);

    private final SchoolService schoolService = new SchoolService(sessionService, studentDao, courseDao, gradeDao);

    @BeforeEach
    public void before() {
        sessionService.openSession();
    }

    @AfterEach
    public void after() {
        sessionService.closeSession();
    }

    @Test
    public void studentWithUniqueIndexNumberCanBeCreated() {
        // When
        var student1 = studentDao.create("Adam", "Kowalski", 100122);
        var student2 = studentDao.create("Jan", "Nowak", 100123);
        var redundantStudent = studentDao.create("Kasia", "Kowalska", 100123);

        // Then
        checkStudent(student1);
        checkStudent(student2);

        assertNotEquals(student1.orElseThrow().id(), student2.orElseThrow().id());
        assertTrue(redundantStudent.isEmpty());
    }

    @Test
    public void allStudentsCanBeListed() {
        // given
        var student1 = studentDao.create("Adam", "Kowalski", 100122).orElseThrow();
        var student2 = studentDao.create("Jan", "Nowak", 100123).orElseThrow();
        var student3 = studentDao.create("Piotr", "Budynek", 100124).orElseThrow();
        sessionService.clearSessionObjects();

        var expectedStudentsOrder = List.of(student3, student1, student2);

        // when
        List<Student> allStudents = studentDao.findAll();

        // then
        assertEquals(3, allStudents.size());
        assertEquals(expectedStudentsOrder, allStudents);
    }

    @Test
    public void studentCanBeFoundByIndexNumber() {
        // Given
        var student = studentDao.create("Kasia", "Kowalska", 300124);
        sessionService.clearSessionObjects();

        // When
        var foundStudent = studentDao.findByIndexNumber(student.orElseThrow().indexNumber());

        // Then
        checkStudent(foundStudent);
        assertEquals(student.orElseThrow(), foundStudent.orElseThrow());
    }

    @Test
    public void courseWithUniqueNameCanBeCreated() {
        // When
        var course1 = courseDao.create("TO");
        var course2 = courseDao.create("TO2");
        var redundantCourse = courseDao.create("TO2");

        // Then
        checkCourse(course1);
        checkCourse(course2);

        assertNotEquals(course1.orElseThrow().id(), course2.orElseThrow().id());
        assertFalse(redundantCourse.isPresent());
    }

    @Test
    public void courseCanBeFoundById() {
        // Given
        var course = courseDao.create("TK");
        sessionService.clearSessionObjects();

        // When
        var foundCourse = courseDao.findById(course.orElseThrow().id());

        // Then
        checkCourse(course);
        assertEquals(course.orElseThrow(), foundCourse.orElseThrow());
    }

    @Test
    public void studentCanBeEnrolledInCourseOnce() {
        // Given
        var student = studentDao.create("Kasia", "Kowalska", 700124).orElseThrow();
        var course = courseDao.create("MOWNIT").orElseThrow();

        // When
        boolean studentEnrolled = schoolService.enrollStudent(course, student);
        boolean reundantStudentEnroll = schoolService.enrollStudent(course, student);

        // Then
        sessionService.clearSessionObjects();

        Course updatedCourse = courseDao.findById(course.id()).orElseThrow();
        Student updatedStudent = studentDao.findByIndexNumber(student.indexNumber()).orElseThrow();
        var courseStudents = updatedCourse.studentSet();
        var studentCourses = updatedStudent.courseSet();

        checkStudent(updatedStudent);
        checkCourse(updatedCourse);

        assertTrue(studentEnrolled);
        assertFalse(reundantStudentEnroll);

        assertTrue(courseStudents.contains(updatedStudent));
        assertTrue(studentCourses.contains(updatedCourse));
    }

    @Test
    public void courseConsistsOfEnrolledStudents() {
        // Given
        var student1 = studentDao.create("Adam", "Paciaciak", 800125).orElseThrow();
        var student2 = studentDao.create("Jan", "Paciaciak", 800126).orElseThrow();
        var course = courseDao.create("WDI").orElseThrow();

        schoolService.enrollStudent(course, student1);
        schoolService.enrollStudent(course, student2);
        sessionService.clearSessionObjects();

        // When
        var students = courseDao.findByName("WDI").orElseThrow().studentSet();

        // Then
        checkStudent(student1);
        checkStudent(student2);
        checkCourse(course);

        assertEquals(2, students.size());
        assertTrue(students.contains(student1));
        assertTrue(students.contains(student2));
    }

    @Test
    public void studentCanBeGraded() {
        // Given
        var student = studentDao.create("Kasia", "Kowalska", 900124).orElseThrow();
        var course = courseDao.create("MOWNIT 2").orElseThrow();

        var initialStudentGradesSize = student.gradeSet().size();

        // When
        boolean studentGraded = schoolService.gradeStudent(student, course, 5.0f);

        // Then
        sessionService.clearSessionObjects();
        var resultStudentGradesSize = studentDao.findByIndexNumber(900124)
                .orElseThrow()
                .gradeSet()
                .size();

        checkStudent(student);
        checkCourse(course);

        assertTrue(studentGraded);
        assertEquals(0, initialStudentGradesSize);
        assertEquals(1, resultStudentGradesSize);
    }

    @Test
    public void courseReportCanBeObtained() {
        // Given
        var student1 = studentDao.create("Kasia", "Kowalska", 1000124).orElseThrow();
        var student2 = studentDao.create("Piotr", "Budynek", 100123).orElseThrow();
        var course1 = courseDao.create("Bazy").orElseThrow();

        schoolService.enrollStudent(course1, student1);
        schoolService.enrollStudent(course1, student2);

        schoolService.gradeStudent(student1, course1, 5.0f);
        schoolService.gradeStudent(student1, course1, 4.0f);

        schoolService.gradeStudent(student2, course1, 5.0f);
        schoolService.gradeStudent(student2, course1, 3.0f);
        schoolService.gradeStudent(student2, course1, 3.0f);

        sessionService.clearSessionObjects();

        Map<String, List<Float>> expectedReport = Map.of(
                "Kasia Kowalska", List.of(4.0f, 5.0f),
                "Piotr Budynek", List.of(3.0f, 3.0f, 5.0f)
        );

        // When
        Map<String, List<Float>> actualReport = schoolService.getStudentGrades(course1.name());

        // Then
        checkStudent(student1);
        checkStudent(student2);
        checkCourse(course1);

        assertEquals(expectedReport, actualReport);
    }

    @Test
    public void studentCanBeRemovedFromSchool() {
        // Given
        var student = studentDao.create("Kasia", "Kowalska", 700124).orElseThrow();
        var course = courseDao.create("MOWNIT").orElseThrow();
        schoolService.enrollStudent(course, student);

        // When
        schoolService.removeStudent(student.indexNumber());

        // then
        sessionService.clearSessionObjects();

        var updatedCourse = courseDao.findById(course.id()).orElseThrow();
        var notExistingStudent = studentDao.findByIndexNumber(student.indexNumber());
        var courseStudents = updatedCourse.studentSet();

        // Then
        checkCourse(updatedCourse);

        assertTrue(notExistingStudent.isEmpty());
        assertFalse(courseStudents.contains(student));
    }

    private void checkStudent(final Student student) {
        checkStudent(Optional.ofNullable(student));
    }

    private void checkStudent(final Optional<Student> student) {
        assertTrue(student.isPresent());
        student.ifPresent(s -> {
            assertTrue(s.id() > 0);
            Assertions.assertNotNull(s.firstName());
            Assertions.assertNotNull(s.lastName());
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
            Assertions.assertNotNull(c.name());
        });
    }
}
