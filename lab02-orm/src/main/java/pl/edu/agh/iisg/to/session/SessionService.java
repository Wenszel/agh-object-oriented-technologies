package pl.edu.agh.iisg.to.session;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Optional;
import java.util.function.Supplier;

public class SessionService implements TransactionService {

    private final SessionFactory sessionFactory =
            new Configuration().configure() // configures settings from hibernate.cfg.xml
                    .buildSessionFactory();

    private Session session;

    public void openSession() {
        session = sessionFactory.openSession();
    }

    public Session getSession() {
        return session;
    }

    public void closeSession() {
        session.close();
    }

    public void clearSessionObjects() {
        session.clear();
    }

    public <T> Optional<T> doAsTransaction(Supplier<T> task) {
        if (session.getTransaction().isActive()) {
            return Optional.ofNullable(task.get());
        }
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            T result = task.get();
            transaction.commit();
            return Optional.ofNullable(result);
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return Optional.empty();
    }
}
