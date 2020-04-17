package ro.utcluj.pt;

import java.time.Duration;
import java.time.LocalDateTime;

public class MonitoredData {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String activity;

    public MonitoredData(LocalDateTime startTime, LocalDateTime endTime, String activity) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.activity = activity;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getActivity() {
        return activity;
    }

    public Duration getDuration() {
        return Duration.between(this.getStartTime(), this.getEndTime());
    }

    @Override
    public String toString() {
        return "MonitoredData{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", activity='" + activity + '\'' +
                '}';
    }
}
