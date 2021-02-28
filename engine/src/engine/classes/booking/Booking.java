package engine.classes.booking;

import engine.classes.boat.Boat;
import engine.classes.boat.BoatType;
import engine.classes.member.Member;
import engine.customExceptions.InvalidInputException;
import engine.xmlAdapters.XmlLocalDateAdapter;
import engine.xmlAdapters.XmlLocalDateTimeAdapter;
import engine.classes.boat.BoatType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)


public class Booking implements Serializable {
    private static int count = 0;

    @XmlAttribute
    private int bookingID;

    @XmlJavaTypeAdapter(value = XmlLocalDateAdapter.class)
    private LocalDate requestedPracticeDate;

    @XmlJavaTypeAdapter(value = XmlLocalDateTimeAdapter.class)
    private LocalDateTime dateBookingCreated;

    @XmlElement
    private int requestedWindowID;


    @XmlElement
    private int memberOrderedID;

    @XmlElement
    private int assignedBoatID = -1;

    @XmlElement
    private int remainingSeats;

    @XmlElement
    private List<BoatType> requestedBoatTypes; // Can choose more than one boat type.

    @XmlElement
    private List<Integer> otherParticipatingRowersIDs = new ArrayList<>();

    public void setCounter(int max){
        count = max;
    }

    public Booking(){}

    public Booking (int memberOrderedID, List<BoatType> requestedBoatTypes, List<Integer> otherParticipatingRowersIDs,int requestedWindowID,
                    LocalDate requestedPracticeDate) {
        this.bookingID = ++count;
        this.requestedWindowID = requestedWindowID;
        this.assignedBoatID = 0;
        this.memberOrderedID = memberOrderedID;
        this.requestedBoatTypes = requestedBoatTypes;
        this.otherParticipatingRowersIDs = otherParticipatingRowersIDs;
        this.requestedPracticeDate = requestedPracticeDate;
        this.dateBookingCreated = LocalDateTime.now();

    }

    public LocalDate getRequestedPracticeDate(){
        return this.requestedPracticeDate;
    }
    public LocalDateTime getDateBookingCreated(){
        return this.dateBookingCreated;
    }
    public int getAssignedBoatID (){
        return this.assignedBoatID;
    }
    public int getMemberOrderedID(){
        return this.memberOrderedID;
    }
    public int getRequestedWindowID(){ return this.requestedWindowID; }
    public int getRemainingSeats(){
        return this.remainingSeats;
    }
    public List<BoatType> getRequestedBoatTypes(){
        return this.requestedBoatTypes;
    }
    public List<Integer> getOtherParticipatingRowersID(){
        return this.otherParticipatingRowersIDs;
    }

    public int calcRemainingSeats(List<Member> theRowers, Boat assignedBoat){
        return 0;
    }


    public int getBookingID(){
        return this.bookingID;
    }

    public void setRequestedPracticeDate(LocalDate theNewDate){
        this.requestedPracticeDate = theNewDate;
    }

    public void setWindowID(int theNewWindowID){
        this.requestedWindowID = theNewWindowID;
    }

    public void setBoatType(List<BoatType> newBoatTypes){
        this.requestedBoatTypes = newBoatTypes;
    }

    public boolean isApproved(){
        return this.assignedBoatID > 0;
    }
    
    public  boolean isInParticipatingRowers(int memberID) {
        for (Integer rowerID : otherParticipatingRowersIDs) {
            if (memberID == rowerID)
                return true;
        }
        return false;
    }

    public int getNumberParticipates(){
        return (otherParticipatingRowersIDs.size() + 1);
    }

    public void removeParticipatingRower(int participatingRowerID){
        otherParticipatingRowersIDs.removeIf(rowerID -> participatingRowerID == rowerID);
    }

    public void setAssignedBoatID(int boatID) {
        this.assignedBoatID = boatID;
    }

    public void setParticipatingRowers(List<Integer> participatingRowersIDs) {
        this.otherParticipatingRowersIDs = participatingRowersIDs;
    }

    public void setMemberOrderedID(int newIDVal) {
        this.memberOrderedID = newIDVal;
    }
}

