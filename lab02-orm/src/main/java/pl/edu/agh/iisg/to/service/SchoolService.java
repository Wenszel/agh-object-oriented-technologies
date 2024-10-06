package pl.edu.agh.iisg.to.service;

import pl.edu.agh.iisg.to.dao.CourseDao;
import pl.edu.agh.iisg.to.dao.GradeDao;
import pl.edu.agh.iisg.to.dao.StudentDao;
import pl.edu.agh.iisg.to.model.Course;
import pl.edu.agh.iisg.to.model.Grade;
import pl.edu.agh.iisg.to.model.Student;
import pl.edu.agh.iisg.to.repository.StudentRepository;
import pl.edu.agh.iisg.to.session.TransactionService;

import java.util.*;
import java.util.stream.Collectors;

public class SchoolService {

    private final TransactionService transactionService;
    private final StudentRepository studentRepository;
    private final CourseDao courseDao;
    private final StudentDao studentDao;
    private final GradeDao gradeDao;

    public SchoolService(TransactionService transactionService, StudentDao studentDao, CourseDao courseDao, StudentRepository studentRepository, GradeDao gradeDao) {
        this.transactionService = transactionService;
        this.courseDao = courseDao;
        this.studentDao = studentDao;
        this.studentRepository = studentRepository;
        this.gradeDao = gradeDao;
    }

    public boolean enrollStudent(final Course course, final Student student) {
        return transactionService.doAsTransaction(()-> {
            Set<Student> courseStudentSet = course.studentSet();
            if (courseStudentSet.contains(student))
                return false;
            courseStudentSet.add(student);
            student.courseSet().add(course);
            courseDao.save(course);
            return true;
        }).orElseThrow();
    }

    public void removeStudent(int indexNumber) {
        transactionService.doAsTransaction(() -> {
            Student student = studentDao.findByIndexNumber(indexNumber).orElseThrow();
            studentRepository.remove(student);
            return null;
        });
    }

    public boolean gradeStudent(final Student student, final Course course, final float gradeValue) {
        return transactionService.doAsTransaction(() -> {
            Grade grade = new Grade(student, course, gradeValue);
            student.gradeSet().add(grade);
            course.gradeSet().add(grade);
            gradeDao.save(grade);
            return true;
        }).orElseThrow();
    }

    public Map<String, List<Float>> getStudentGrades(String courseName) {
        Course course = courseDao.findByName(courseName).orElseThrow();

        return course.gradeSet().stream()
                .collect(Collectors.groupingBy(
                        Grade::getStudentFullName,
                        Collectors.mapping(Grade::grade, Collectors.toList())
                ));
    }
}
