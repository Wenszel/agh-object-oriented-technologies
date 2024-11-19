package pl.edu.agh.school;

import java.io.Serializable;
import java.util.Collection;

public abstract class Person implements Serializable {

	private static final long serialVersionUID = 3564949349030026764L;

	protected String name;
	protected String surname;
	protected PersonType personType;

	public Person(String name, String surname) {
		this.name = name;
		this.surname = surname;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	boolean meetsSearchCriteria(String name, String surname) {
		return getName().equals(name) && getSurname().equals(surname);
	}

	public abstract Collection<Term> getSchedule();

	@Override
	public String toString() {
		return String.format("%s %s %s", name, surname, personType.toString());
	}

}