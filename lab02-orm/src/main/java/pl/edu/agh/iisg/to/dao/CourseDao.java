package pl.edu.agh.iisg.to.dao;

import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import pl.edu.agh.iisg.to.model.Course;
import pl.edu.agh.iisg.to.session.SessionService;

import java.util.Optional;

public class CourseDao extends GenericDao<Course> {

    public CourseDao(SessionService sessionService) {
        super(sessionService, Course.class);
    }

    public Optional<Course> create(final String name) {
        return save(new Course(name));
    }

    public Optional<Course> findByName(final String name) {
        try {
            Session session = currentSession();
            return session.createQuery("SELECT c FROM Course c WHERE c.name = :name", Course.class)
                    .setParameter("name", name)
                    .uniqueResultOptional();
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
