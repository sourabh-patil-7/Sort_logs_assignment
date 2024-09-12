package org.example;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

class LogEntry {
    private String serviceName;
    private String logType;
    private LocalDateTime dateTime;

    public LogEntry(String serviceName, String logType, String date, String time, String timezone) {
        this.serviceName = serviceName;
        this.logType = logType;
        this.dateTime = parseDateTime(date, time, timezone);
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

    private LocalDateTime parseDateTime(String date, String time, String timezone) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

        LocalDate localDate = LocalDate.parse(date, dateFormatter);
        LocalTime localTime = LocalTime.parse(time, timeFormatter);

        OffsetDateTime offsetDateTime = OffsetDateTime.of(
                localDate, localTime, ZoneOffset.UTC
        );

        return offsetDateTime.toLocalDateTime();
    }
}

public class LogProcessor {

    public static void main(String[] args) {
        List<LogEntry> logs = Arrays.asList(
                new LogEntry("micro-service-m1", "INFO", "2023-07-25", "14:00:00", "Z"),
                new LogEntry("micro-service-n1", "ERROR", "2023-07-25", "14:10:00", "Z"),
                new LogEntry("micro-service-o1", "DEBUG", "2023-07-25", "14:20:00", "Z"),
                new LogEntry("micro-service-n1", "ERROR", "2023-07-25", "14:30:00", "Z"),
                new LogEntry("micro-service-o1", "INFO", "2023-07-25", "14:40:00", "Z"),
                new LogEntry("micro-service-p1", "DEBUG", "2023-07-25", "14:50:00", "Z")
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
