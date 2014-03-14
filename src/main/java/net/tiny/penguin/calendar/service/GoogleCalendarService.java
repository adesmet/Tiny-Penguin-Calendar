package net.tiny.penguin.calendar.service;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public interface GoogleCalendarService {

    public void addMeeting(DateTime date, Duration duration);
}
