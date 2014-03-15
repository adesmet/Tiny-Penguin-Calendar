package net.tiny.penguin.calendar.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Acl;
import com.google.api.services.calendar.model.AclRule;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

@Service
public class GoogleCalendarServiceImpl implements GoogleCalendarService {

    private static final String APPLICATION_NAME = "Tiny Penguin Calendar";
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final String CLIENT_ID = "423282905385-lilu2sonssvpunec450fsss9krd9a2dr.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "6ow3ngVDE8C7BXzZFdA559CS";


    @Override
    public void addMeeting(DateTime date, Duration duration) {
        try {
            GoogleCredential credentials = new GoogleCredential.Builder().setClientSecrets(CLIENT_ID, CLIENT_SECRET).build();
            Calendar client = new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(), new GsonFactory(), credentials).build();
            CalendarList calList = client.calendarList().list().execute();
            for (CalendarListEntry calendarListEntry : calList.getItems()) {
                System.out.println(calendarListEntry.getEtag());
            }
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            GoogleCredential credentials = new GoogleCredential.Builder().setTransport(HTTP_TRANSPORT)
                    .setJsonFactory(new GsonFactory())
                    .setServiceAccountId("423282905385-5d9ne92rjisv92ti061q8dep8q4b0e1j@developer.gserviceaccount.com")
                    .setServiceAccountScopes(Arrays.asList(CalendarScopes.CALENDAR))
                    .setServiceAccountPrivateKeyFromP12File(new ClassPathResource("f75867de2b6c4ef58ee1c89322b2987f81b6e738-privatekey.p12").getFile())
                    .build();
            Calendar service = new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(), new GsonFactory(), credentials).setApplicationName(APPLICATION_NAME).build();

//            com.google.api.services.calendar.model.Calendar calendar = service.calendars().get("secundary").execute();
//
//            calendar.setSummary("New Summary");
//
//            com.google.api.services.calendar.model.Calendar updatedCalendar = service.calendars().update(calendar.getId(), calendar).execute();
//
//            System.out.println(updatedCalendar.getEtag());

            String pageToken = null;
            String latestCalendarId="";
            do {
                CalendarList calendarList = service.calendarList().list().setPageToken(pageToken).execute();
                List<CalendarListEntry> items = calendarList.getItems();

                for (CalendarListEntry calendarListEntry : items) {
                    System.out.println("SUM: " + calendarListEntry.getSummary());
                    System.out.println("DESCR: " + calendarListEntry.getDescription());
                    System.out.println("ACCESSROLE: " + calendarListEntry.getAccessRole());
                    System.out.println("ETAG: " + calendarListEntry.getEtag());
                    System.out.println("ID: " + calendarListEntry.getId());
                    System.out.println("PRIMARY: " + calendarListEntry.getPrimary());
                    System.out.println("KIND: " + calendarListEntry.getKind());
                    System.out.println("ID: " + calendarListEntry.getId());
                    latestCalendarId = calendarListEntry.getId();
                }

                pageToken = calendarList.getNextPageToken();
            } while (pageToken != null);

            AclRule rule = new AclRule();
            AclRule.Scope scope = new AclRule.Scope();

            scope.setType("user");
            scope.setValue("tinypenguintest@gmail.com");

            rule.setScope(scope);
            rule.setRole("reader");

            AclRule createdRule = service.acl().insert(latestCalendarId, rule).execute();
            System.out.println(createdRule.getId());



        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
