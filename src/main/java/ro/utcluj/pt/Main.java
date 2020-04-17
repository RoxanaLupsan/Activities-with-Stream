package ro.utcluj.pt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static final String FILE_NAME = "Activities.txt";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public static void main(String[] args) {

        try (Stream<String> stream = Files.lines(Paths.get(FILE_NAME))) {

            List<MonitoredData> list = stream
                    .map(line -> createMonitoredData(line))
                    .collect(Collectors.toList());
            Activities activities = new Activities(list);
            FileWriter fileWriter = new FileWriter();
            fileWriter.openFile();
            fileWriter.addRecords("Count distinct days: " + activities.countDays() + "\n");
            fileWriter.addRecords("Count activity occurrences: " + activities.countActivityOccurrences() + "\n");
            fileWriter.addRecords("Count activity occurrences for each day: " + activities.countActivityOccurrencesForEachDay() + "\n");
            fileWriter.addRecords("Map activity duration:" + activities.mapActivityDuration() + "\n");
            fileWriter.addRecords("Activities with duration less than 5 minutes: " + activities.getActivitiesWithDurationGreaterThanFiveMinutes() + "\n");
            fileWriter.closeFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static MonitoredData createMonitoredData(String line) {
        String[] parts = line.split("\t\t");

        LocalDateTime startTime = LocalDateTime.parse(parts[0], FORMATTER);
        LocalDateTime endTime = LocalDateTime.parse(parts[1], FORMATTER);

        return new MonitoredData(startTime, endTime, parts[2]);
    }

}
