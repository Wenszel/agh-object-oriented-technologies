package pl.edu.agh.iisg.to.dao;

import jakarta.persistence.NoResultException;
import pl.edu.agh.iisg.to.model.Student;
import pl.edu.agh.iisg.to.session.SessionService;

import javax.management.remote.NotificationResult;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StudentDao extends GenericDao<Student> {

    public StudentDao(SessionService sessionService) {
        super(sessionService, Student.class);
    }

    public Optional<Student> create(final String firstName, final String lastName, final int indexNumber) {
        Student student = new Student(firstName, lastName, indexNumber);
        return save(student);
    }

    public List<Student> findAll() {
        String jpql = "SELECT s FROM Student s ORDER BY s.lastName";
        return currentSession().createQuery(jpql, Student.class).getResultList();
    }

    public Optional<Student> findByIndexNumber(final int indexNumber) {
        String jpql = "SELECT s from Student s WHERE s.indexNumber = :indexNumber ";
        try {
            Student student = currentSession()

                    .createQuery(jpql, Student.class)
                    .setParameter("indexNumber", indexNumber)
                    .getSingleResult();
            return Optional.of(student);
        } catch (NoResultException e){
            return Optional.empty();
        }
    }
}
