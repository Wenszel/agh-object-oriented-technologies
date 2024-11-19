package pl.edu.agh.school.demo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;

import pl.edu.agh.logger.ConsoleMessageSerializer;
import pl.edu.agh.logger.FileMessageSerializer;
import pl.edu.agh.logger.Logger;
import pl.edu.agh.school.DayOfWeek;
import pl.edu.agh.school.Person;
import pl.edu.agh.school.School;
import pl.edu.agh.school.SchoolClass;
import pl.edu.agh.school.Student;
import pl.edu.agh.school.Subject;
import pl.edu.agh.school.Teacher;
import pl.edu.agh.school.Term;

public class SchoolDemo {

    private final School school;

    private final DateFormat timeFormat = new SimpleDateFormat("hh:mm");

    public SchoolDemo() {
        school = new School();
    }

    public static void main(String[] args) throws Exception {

        Logger.getInstance().registerSerializer(new ConsoleMessageSerializer());
        Logger.getInstance().registerSerializer(
                new FileMessageSerializer("logfile.log"));

        SchoolDemo schoolDemo = new SchoolDemo();
        schoolDemo.initTeachers();
        schoolDemo.initClass();
        schoolDemo.showClass();
        schoolDemo.showScheduleForClass();
        schoolDemo.showScheduleForTeacher();
    }

    public void initTeachers() {
        if (school.findPerson("Thomas", "Anderson").isEmpty()) {
            school.addTeacher(new Teacher("Thomas", "Anderson"));
            school.addTeacher(new Teacher("Han", "Solo"));
            school.addTeacher(new Teacher("Princess", "Leia"));
            school.addTeacher(new Teacher("Severus", "Snape"));
            school.addTeacher(new Teacher("Dolores", "Umbridge"));
        } else {
            System.out.println("School teachers data is already initialized");
        }
    }

    public void initClass() throws ParseException {
        if (school.findClass("1A", "humane").isEmpty()) {
            SchoolClass schoolClass = new SchoolClass("1A", "humane");
            schoolClass.addStudent(new Student("Peter", "Pan"));
            schoolClass.addStudent(new Student("Anna", "Shirley"));
            schoolClass.addStudent(new Student("Harry", "Potter"));
            schoolClass.addStudent(new Student("Ron", "Weasley"));

            Subject subject;
            subject = new Subject("Math");
            subject.addTerm(new Term(DayOfWeek.MONDAY, timeFormat
                    .parse("10:30"), 45));
            subject.addTerm(new Term(DayOfWeek.THURSDAY, timeFormat
                    .parse("12:50"), 45));
            subject.setTeacher((Teacher) school
                    .findPerson("Thomas", "Anderson").iterator().next());
            schoolClass.addSubject(subject);

            subject = new Subject("English");
            subject.addTerm(new Term(DayOfWeek.MONDAY, timeFormat
                    .parse("12:00"), 45));
            subject.addTerm(new Term(DayOfWeek.TUESDAY, timeFormat
                    .parse("11:00"), 45));
            subject.addTerm(new Term(DayOfWeek.WEDNESDAY, timeFormat
                    .parse("10:30"), 45));
            subject.addTerm(new Term(DayOfWeek.THURSDAY, timeFormat
                    .parse("11:00"), 45));
            subject.setTeacher((Teacher) school.findPerson("Han", "Solo")
                    .iterator().next());
            schoolClass.addSubject(subject);

            subject = new Subject("Computer Science");
            subject.addTerm(new Term(DayOfWeek.TUESDAY, timeFormat
                    .parse("09:00"), 45));
            subject.setTeacher((Teacher) school
                    .findPerson("Thomas", "Anderson").iterator().next());
            schoolClass.addSubject(subject);

            subject = new Subject("Chemistry");
            subject.addTerm(new Term(DayOfWeek.WEDNESDAY, timeFormat
                    .parse("11:30"), 45));
            subject.setTeacher((Teacher) school.findPerson("Princess", "Leia")
                    .iterator().next());
            schoolClass.addSubject(subject);

            subject = new Subject("Potions");
            subject.addTerm(new Term(DayOfWeek.FRIDAY, timeFormat
                    .parse("09:00"), 90));
            subject.setTeacher((Teacher) school.findPerson("Severus", "Snape")
                    .iterator().next());
            schoolClass.addSubject(subject);

            subject = new Subject("Black Magic Defense");
            subject.addTerm(new Term(DayOfWeek.FRIDAY, timeFormat
                    .parse("12:30"), 90));
            subject.setTeacher((Teacher) school.findPerson("Dolores",
                    "Umbridge").iterator().next());
            schoolClass.addSubject(subject);

            school.addClass(schoolClass);
        } else {
            System.out.println("School classes data is already initialized");
        }
    }

    public void showClass() {
        SchoolClass schoolClass = school.findClass("1A", "humane").iterator()
                .next();

        System.out.println("---------- CLASS ----------");
        System.out.format("Class name: %s \n", schoolClass.getName());
        System.out.format("Class profile: %s \n", schoolClass.getProfile());

        System.out.println();
        System.out.println("---------- CLASS STUDENTS ----------");
        for (Person student : schoolClass.getStudents()) {
            System.out.format("%s %s \n", student.getName(), student
                    .getSurname());
        }

        System.out.println();
        System.out.println("---------- CLASS SUBJECTS ----------");
        for (Subject subject : schoolClass.getSubjects()) {
            System.out.format("Subject name: %s \n", subject.getName());
            System.out.format("Subject teacher: %s %s\n", subject.getTeacher()
                    .getName(), subject.getTeacher().getSurname());
        }
        System.out.println();
    }

    public void showScheduleForClass() {
        SchoolClass schoolClass = school.findClass("1A", "humane").iterator()
                .next();
        renderSchedule(schoolClass.getSchedule(), "Class 1A");
    }

    public void showScheduleForTeacher() {
        Teacher teacher = (Teacher) school.findPerson("Han", "Solo").iterator()
                .next();
        renderSchedule(teacher);
    }

    private void renderSchedule(Person person) {
        renderSchedule(person.getSchedule(), person.toString());
    }

    private void renderSchedule(Collection<Term> terms, String description) {
        System.out.println("---------- SCHEDULE for " + description
                + " ----------");
        for (Term term : terms) {
            System.out.format("\tDay of week: %s \n", term.getDayOfWeek());
            System.out.format("\tStart time: %s \n", timeFormat.format(term
                    .getStartTime()));
            System.out.format("\tDuration (min): %d \n", term.getDuration());
            System.out.println("----------------------------------------");
        }
        System.out.println();
    }
}
