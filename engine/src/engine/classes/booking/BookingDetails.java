package engine.classes.booking;

import engine.classes.boat.BoatType;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class BookingDetails implements Serializable {
    int userID;
    List<BoatType> requestedBoatType;
    List<Integer> otherParticipatingRowersIDs;
    int windowID;
    LocalDate requestedPracticeDate;

    public BookingDetails(int userID, List<BoatType> requestedBoatType, List<Integer> otherParticipatingRowersIDs, int windowID, LocalDate requestedPracticeDate) {
        this.userID = userID;
        this.requestedBoatType = requestedBoatType;
        this.otherParticipatingRowersIDs = otherParticipatingRowersIDs;
        this.windowID = windowID;
        this.requestedPracticeDate = requestedPracticeDate;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public List<BoatType> getRequestedBoatType() {
        return requestedBoatType;
    }

    public void setRequestedBoatType(List<BoatType> requestedBoatType) {
        this.requestedBoatType = requestedBoatType;
    }

    public List<Integer> getOtherParticipatingRowersIDs() {
        return otherParticipatingRowersIDs;
    }

    public void setOtherParticipatingRowersIDs(List<Integer> otherParticipatingRowersIDs) {
        this.otherParticipatingRowersIDs = otherParticipatingRowersIDs;
    }

    public int getWindowID() {
        return windowID;
    }

    public void setWindowID(int windowID) {
        this.windowID = windowID;
    }

    public LocalDate getRequestedPracticeDate() {
        return requestedPracticeDate;
    }

    public void setRequestedPracticeDate(LocalDate requestedPracticeDate) {
        this.requestedPracticeDate = requestedPracticeDate;
    }
}
