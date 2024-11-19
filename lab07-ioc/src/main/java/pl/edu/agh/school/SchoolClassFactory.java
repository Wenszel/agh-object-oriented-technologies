package pl.edu.agh.school;

import com.google.inject.Inject;
import pl.edu.agh.logger.Logger;

public class SchoolClassFactory {
    private final Logger logger;

    @Inject
    public SchoolClassFactory(Logger logger) {
        this.logger = logger;
    }

    public SchoolClass createSchoolClass(String name, String profile) {
        return new SchoolClass(name, profile, logger);
    }
}
