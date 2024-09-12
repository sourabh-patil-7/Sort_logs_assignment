package org.example;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

class LogEntry {
    private String serviceName;
    private String logType;
    private LocalDateTime dateTime;

    public LogEntry(String serviceName, String logType, String date, String time) {
        this.serviceName = serviceName;
        this.logType = logType;
        this.dateTime = parseDateTime(date, time);
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String toJSON() {
        return String.format(
                "{\"date\": \"%s\", \"time\": \"%s\", \"service_name\": \"%s\", \"log_type\": \"%s\"}",
                this.dateTime.toLocalDate().toString(),
                this.dateTime.toLocalTime().toString(),
                this.serviceName,
                this.logType
        );
    }

    private LocalDateTime parseDateTime(String date, String time) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

        LocalDate localDate = LocalDate.parse(date, dateFormatter);
        LocalTime localTime = LocalTime.parse(time.split(" ")[0], timeFormatter);
        String timezone = time.split(" ")[1];

        OffsetDateTime offsetDateTime = OffsetDateTime.parse(
                localDate.toString() + "T" + localTime.toString() + timezone,
                dateTimeFormatter
        );

        return offsetDateTime.toLocalDateTime();
    }
}

public class LogProcessor {

    public static void main(String[] args) {
        List<LogEntry> logs = Arrays.asList(
                new LogEntry("micro-service-m1", "INFO", "Wed Jul 25 2023", "14:00:00 GMT+0530"),
                new LogEntry("micro-service-n1", "ERROR", "Wed Jul 25 2023", "14:10:00 GMT+0530"),
                new LogEntry("micro-service-o1", "DEBUG", "Wed Jul 25 2023", "14:20:00 GMT+0530"),
                new LogEntry("micro-service-n1", "ERROR", "Wed Jul 25 2023", "14:30:00 GMT+0530"),
                new LogEntry("micro-service-o1", "INFO", "Wed Jul 25 2023", "14:40:00 GMT+0530"),
                new LogEntry("micro-service-p1", "DEBUG", "Wed Jul 25 2023", "14:50:00 GMT+0530")
        );

        logs.sort(Comparator.comparing(LogEntry::getDateTime).reversed());

        System.out.println("\nSorted Logs:");
        System.out.println("[");
        for (int i = 0; i < logs.size(); i++) {
            System.out.print(logs.get(i).toJSON());
            if (i < logs.size() - 1) {
                System.out.println(",");
            }
        }
        System.out.println("\n]");
    }
}
