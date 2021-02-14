package engine.classes.windows;

import engine.classes.boat.BoatType;
import engine.customExceptions.InvalidInputException;

import java.time.LocalTime;

public class ScheduleWindowBuilder {
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private BoatType type;
    private int id;

    public ScheduleWindowBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ScheduleWindowBuilder setStartTime(LocalTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public ScheduleWindowBuilder setEndTime(LocalTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public ScheduleWindowBuilder setType(BoatType type) {
        this.type = type;
        return this;
    }

    public ScheduleWindowBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public ScheduleWindow createScheduleWindow() throws InvalidInputException {
        return new ScheduleWindow(name, startTime, endTime, type);
    }
}