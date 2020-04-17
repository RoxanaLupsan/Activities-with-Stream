package ro.utcluj.pt;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

public class Activities {

    private static final Duration TEN_HOURS = Duration.ofHours(10);
    private final List<MonitoredData> monitoredData;

    public Activities(List<MonitoredData> monitoredData) {
        this.monitoredData = monitoredData;
    }

    public long countDays() {
        return monitoredData.stream()
                .flatMap(data -> Stream.of(data.getStartTime(), data.getEndTime()))
                .map(LocalDateTime::toLocalDate)
                .distinct()
                .count();
    }

    public Map<String, Integer> countActivityOccurrences() {
        return countOccurrences(monitoredData);
    }

    private Map<String, Integer> countOccurrences(List<MonitoredData> input) {
        return input.stream()
                .map(MonitoredData::getActivity)
                .collect(Collectors.toMap(identity(), a -> 1, (a, b) -> a + b));
    }

    public Map<Integer, Map<String, Integer>> countActivityOccurrencesForEachDay() {
        Map<LocalDate, List<MonitoredData>> dataByDay = monitoredData.stream()
                .collect(groupingBy(data -> data.getStartTime().toLocalDate()));

        AtomicInteger index = new AtomicInteger();
        return dataByDay.entrySet().stream()
                .sorted((entry1, entry2) -> entry1.getKey().compareTo(entry2.getKey()))
                .collect(toMap(entry -> index.getAndIncrement(), entry -> countOccurrences(entry.getValue())));
    }

    public Map<String, Duration> mapActivityDuration() {
        Map<String, Duration> activityAndDurationInMillis = monitoredData.stream()
                .collect(toMap(MonitoredData::getActivity, MonitoredData::getDuration, Duration::plus));

        return activityAndDurationInMillis.entrySet()
                .stream()
                .filter(this::isShorterThanTenHours)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private boolean isShorterThanTenHours(Map.Entry<String, Duration> entry) {
        return entry.getValue().compareTo(TEN_HOURS) < 0;
    }

    public List<String> getActivitiesWithDurationGreaterThanFiveMinutes() {
        Map<String, Integer> totalNumberOfOccurrences = countActivityOccurrences();
        Map<String, Integer> occurrencesWithDurationLessThanFiveMinutes = monitoredData.stream()
                .filter(data -> data.getDuration().compareTo(Duration.ofMinutes(5)) < 0)
                .map(MonitoredData::getActivity)
                .collect(toMap(identity(), a -> 1, (a, b) -> a + b));

        return totalNumberOfOccurrences.entrySet()
                .stream()
                .filter(entry -> calculateFrequency(occurrencesWithDurationLessThanFiveMinutes, entry) > 0.9)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }



    private double calculateFrequency(Map<String, Integer> occurrencesWithDurationLessThanFiveMinutes, Map.Entry<String, Integer> entry) {
        int total = entry.getValue();
        int shorterThanFive = occurrencesWithDurationLessThanFiveMinutes.getOrDefault(entry.getKey(), 0);
        return (double) shorterThanFive / total;
    }
}
