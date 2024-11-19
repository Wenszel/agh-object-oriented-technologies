package pl.edu.agh.school.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import org.checkerframework.checker.fenum.qual.PolyFenum;
import pl.edu.agh.logger.ConsoleMessageSerializer;
import pl.edu.agh.logger.FileMessageSerializer;
import pl.edu.agh.logger.Logger;
import pl.edu.agh.school.School;
import pl.edu.agh.school.SchoolClass;
import pl.edu.agh.school.SchoolClassFactory;
import pl.edu.agh.school.SchoolDAO;
import pl.edu.agh.school.demo.SchoolDemo;
import pl.edu.agh.school.persistence.PersistenceManager;
import pl.edu.agh.school.persistence.SerializablePersistenceManager;

public class SchoolModule extends AbstractModule {

    public static final String TEACHERS_STORAGE_FILE_NAME = "guice-teachers.dat";
    public static final String CLASS_STORAGE_FILE_NAME = "guice-classes.dat";
    public static final String LOG_FILE_NAME = "persistence.log";

    @Override
    protected void configure() {
        bind(String.class).annotatedWith(Names.named("teachersStorageFileName"))
                .toInstance(TEACHERS_STORAGE_FILE_NAME);
        bind(String.class).annotatedWith(Names.named("classStorageFileName"))
                .toInstance(CLASS_STORAGE_FILE_NAME);
        bind(String.class).annotatedWith(Names.named("logfile")).toInstance(LOG_FILE_NAME);

    }
    @Provides
    public PersistenceManager providePersistenceManager(
            Logger logger,
            @Named("teachersStorageFileName") String teachersStorageFileName,
            @Named("classStorageFileName") String classStorageFileName) {
        return new SerializablePersistenceManager(teachersStorageFileName, classStorageFileName, logger);
    }

    @Provides
    public SchoolDAO provideSchoolDAO(PersistenceManager manager, Logger logger) {
        return new SchoolDAO(manager, logger);
    }

    @Provides
    @Singleton
    public Logger provideLogger(@Named ("logfile") String logFileName){
        Logger logger = new Logger();
        logger.registerSerializer(new ConsoleMessageSerializer());
        logger.registerSerializer(new FileMessageSerializer(logFileName));
        return logger;
    }

    @Provides
    public SchoolDemo provideSchoolDemo(School school, Logger logger) {
        return new SchoolDemo(school, logger);
    }

    @Provides
    public SchoolClassFactory provideSchoolClassFactory(Logger logger) {
        return new SchoolClassFactory(logger);
    }
}
