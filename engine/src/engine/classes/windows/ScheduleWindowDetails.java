package engine.classes.windows;
import engine.classes.boat.BoatType;

import java.io.Serializable;
import java.time.LocalTime;

public class ScheduleWindowDetails implements Serializable {
    String name;
    LocalTime startTime;
    LocalTime EndTime;
    BoatType boatType;

    public ScheduleWindowDetails(String name, LocalTime startTime, LocalTime endTime, BoatType boatType) {
        this.name = name;
        this.startTime = startTime;
        EndTime = endTime;
        this.boatType = boatType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return EndTime;
    }

    public void setEndTime(LocalTime endTime) {
        EndTime = endTime;
    }

    public BoatType getBoatType() {
        return boatType;
    }

    public void setBoatType(BoatType boatType) {
        this.boatType = boatType;
    }
}
