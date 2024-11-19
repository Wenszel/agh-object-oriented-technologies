package pl.edu.agh.school;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Term implements Serializable {

	private static final long serialVersionUID = -5531282222177760431L;

	private final DayOfWeek dayOfWeek;
	private final Date startTime;
	private final int durationInMinutes;
	private Subject subject;

	public Term(DayOfWeek dayOfWeek, Date hour, int durationInMinutes) {
		this.dayOfWeek = dayOfWeek;
		startTime = hour;
		this.durationInMinutes = durationInMinutes;
	}

	public DayOfWeek getDayOfWeek() {
		return dayOfWeek;
	}

	public Date getStartTime() {
		return startTime;
	}

	public long getDuration() {
		return durationInMinutes;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Subject getSubject() {
		return subject;
	}

	@Override
	public String toString() {
		DateFormat timeFormat = new SimpleDateFormat("hh:mm");
		return "term of " + subject + " on " + dayOfWeek + " at "
				+ timeFormat.format(startTime) + ", " + durationInMinutes
				+ " min";
	}
}
