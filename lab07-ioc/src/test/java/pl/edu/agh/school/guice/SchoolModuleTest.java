package pl.edu.agh.school.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.Test;
import pl.edu.agh.logger.Logger;
import pl.edu.agh.school.SchoolDAO;
import pl.edu.agh.school.demo.SchoolDemo;
import pl.edu.agh.school.persistence.PersistenceManager;

import static org.junit.jupiter.api.Assertions.assertSame;

public class SchoolModuleTest {
    @Test
    public void checkIsLoggerSingleton() {
        // given
        SchoolModule schoolModule = new SchoolModule();
        String logFileName = "test.log";
        // when

        Injector injector = Guice.createInjector(new SchoolModule());
        SchoolDemo schoolDemo = injector.getInstance(SchoolDemo.class);
        SchoolDAO schoolDAO = injector.getInstance(SchoolDAO.class);
        PersistenceManager persistenceManager = injector.getInstance(PersistenceManager.class);
        Logger logger1 = schoolDAO.log;
        Logger logger2 = schoolDAO.log;

        // then
        assertSame(logger1, logger2);
    }
}
