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
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss 'GMT'XXX");


        LocalDate localDate = LocalDate.parse(date, dateFormatter);
        LocalTime localTime = LocalTime.parse(time.split(" ")[0], timeFormatter); // Split to remove timezone
        String timezone = time.split(" ")[1]; // Extract timezone


        OffsetDateTime offsetDateTime = OffsetDateTime.parse(
                localDate.toString() + "T" + localTime.toString() + timezone,
                dateTimeFormatter
        );

        return offsetDateTime.toLocalDateTime();
    }
}

public class LogProcessor {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<LogEntry> logs = new ArrayList<>();

        System.out.println("Enter log entries. Type 'done' when you are finished.");

        while (true) {
            System.out.print("Enter service name: ");
            String serviceName = scanner.nextLine().trim();
            if ("done".equalsIgnoreCase(serviceName)) {
                break;
            }

            System.out.print("Enter log type (e.g., INFO, ERROR, DEBUG): ");
            String logType = scanner.nextLine().trim();

            System.out.print("Enter date (e.g., Wed Jul 25 2023): ");
            String date = scanner.nextLine().trim();

            System.out.print("Enter time with timezone (e.g., 14:00:00 GMT+0530): ");
            String time = scanner.nextLine().trim();

            logs.add(new LogEntry(serviceName, logType, date, time));
        }

        scanner.close();


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
