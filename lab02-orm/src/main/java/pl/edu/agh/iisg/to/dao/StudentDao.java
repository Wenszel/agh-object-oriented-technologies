package pl.edu.agh.iisg.to.dao;

import pl.edu.agh.iisg.to.model.Student;
import pl.edu.agh.iisg.to.session.SessionService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StudentDao extends GenericDao<Student> {

    public StudentDao(SessionService sessionService) {
        super(sessionService, Student.class);
    }

    public Optional<Student> create(final String firstName, final String lastName, final int indexNumber) {
        // TODO - implement
        return Optional.empty();
    }

    public List<Student> findAll() {
        // TODO - implement
        return Collections.emptyList();
    }

    public Optional<Student> findByIndexNumber(final int indexNumber) {
        // TODO - implement
        return Optional.empty();
    }
}
