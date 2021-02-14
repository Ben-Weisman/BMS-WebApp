package engine.classes.member;

import java.io.Serializable;
import java.time.LocalDate;

public class MemberDetails implements Serializable {
    String emailAddress;
    String comments;
    String name;
    String password;
    String phoneNumber;
    int age;
    LocalDate JoiningDate;
    LocalDate expirationDate;
    boolean hasPrivateBoat;
    boolean isManager;
    Level level;
    String privateBoatSerialNumber;

    public MemberDetails(String emailAddress, String comments, String name, String password, String phoneNumber, int age, LocalDate joiningDate, LocalDate expirationDate, boolean hasPrivateBoat, Boolean isManager, Level level, String privateBoatSerialNumber) {
        this.emailAddress = emailAddress;
        this.comments = comments;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.age = age;
        JoiningDate = joiningDate;
        this.expirationDate = expirationDate;
        this.hasPrivateBoat = hasPrivateBoat;
        this.isManager = isManager;
        this.level = level;
        this.privateBoatSerialNumber = privateBoatSerialNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDate getJoiningDate() {
        return JoiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        JoiningDate = joiningDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isHasPrivateBoat() {
        return hasPrivateBoat;
    }

    public void setHasPrivateBoat(boolean hasPrivateBoat) {
        this.hasPrivateBoat = hasPrivateBoat;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setIsManager(Boolean manager) {
        isManager = manager;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getPrivateBoatSerialNumber() {
        return privateBoatSerialNumber;
    }

    public void setPrivateBoatSerialNumber(String privateBoatSerialNumber) {
        this.privateBoatSerialNumber = privateBoatSerialNumber;
    }
}
