package engine.classes.windows;

import engine.classes.boat.BoatType;
import engine.customExceptions.*;
import engine.xmlAdapters.XmlLocalTimeAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalTime;


@XmlAccessorType(XmlAccessType.FIELD)

public class ScheduleWindow implements Serializable {
    private static int count = 0;
    private int scheduleWindowID;

    @XmlElement
    private String name;

    @XmlJavaTypeAdapter(value = XmlLocalTimeAdapter.class)
    private LocalTime startTime;

    @XmlJavaTypeAdapter(value = XmlLocalTimeAdapter.class)
    private LocalTime endTime;


    @XmlElement
    private BoatType boatType; // optional


    public ScheduleWindow(){}
    public ScheduleWindow (String name,LocalTime startTime,LocalTime endTime,BoatType type) throws
            InvalidInputException {
        this.scheduleWindowID = ++count;
        if (name == null || name.length() == 0)
            throw new InvalidInputException("Illegal name entered. Please try again. ");
        else this.name = name;

        this.startTime = startTime;
        this.endTime = endTime;
        this.boatType = type;

    }
    public ScheduleWindow(String name, LocalTime startTime, LocalTime endTime,BoatType type, int id) throws InvalidInputException
    {
        if (id <= 0)
            throw new InvalidInputException("ID cannot be 0 or negative");
        else this.scheduleWindowID = id;
        if (name == null || name.length() == 0)
            throw new InvalidInputException("Illegal name entered. Please try again. ");
        else this.name = name;

        this.startTime = startTime;
        this.endTime = endTime;
        this.boatType = type;
    }

    public String getName(){
        return this.name;
    }
    public BoatType getBoatType(){
        return this.boatType;
    }

    public LocalTime getStartTime(){
        return this.startTime;
    }

    public LocalTime getEndTime(){
        return this.endTime;
    }

    public int getID(){
        return this.scheduleWindowID;
    }

    public void setName(String scannedParam) {
        if (!scannedParam.equals(name))
            name = scannedParam;
    }

    public void setBoatType(BoatType boatType) {
        if (!boatType.equals(this.boatType))
            this.boatType = boatType;
    }

    public void setStartTime(LocalTime startTime) {
        if (this.endTime.isAfter(startTime))
            this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        if (this.startTime.isBefore(endTime))
            this.endTime = endTime;
    }

    public void setCounter(int max){
        count = max;
    }
}
