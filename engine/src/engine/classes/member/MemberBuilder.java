package engine.classes.member;

import java.time.LocalDate;

public class MemberBuilder {
    private int id = -1;
    private String name;
    private int age;
    private String comments;
    private Level level;
    private LocalDate joiningDate;
    private LocalDate expirationDate;
    private boolean hasPrivateBoat;
    private String privateBoatSerialNumber;
    private String phoneNumber;
    private String emailAddress;
    private String password;
    private boolean isManager;

    public MemberBuilder setName(String name) {
        this.name = name;
        return this;
    }
    public MemberBuilder setID(int id){
        this.id = id;
        return this;
    }

    public MemberBuilder setAge(int age) {
        this.age = age;
        return this;
    }

    public MemberBuilder setComments(String comments) {
        this.comments = comments;
        return this;
    }

    public MemberBuilder setLevel(Level level) {
        this.level = level;
        return this;
    }

    public MemberBuilder setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
        return this;
    }

    public MemberBuilder setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public MemberBuilder setHasPrivateBoat(boolean hasPrivateBoat) {
        this.hasPrivateBoat = hasPrivateBoat;
        return this;
    }

    public MemberBuilder setPrivateBoatSerialNumber(String privateBoatSerialNumber) {
        this.privateBoatSerialNumber = privateBoatSerialNumber;
        return this;
    }
    public MemberBuilder setMemberID(int id){
        this.id = id;
        return this;
    }

    public MemberBuilder setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public MemberBuilder setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public MemberBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public MemberBuilder setIsManager(boolean isManager) {
        this.isManager = isManager;
        return this;
    }

    public Member createMember() {
        if (this.id > 0)
            return new Member(name, age, comments, level, joiningDate, expirationDate, hasPrivateBoat, privateBoatSerialNumber, phoneNumber, emailAddress, password, isManager,id);
        else return new Member(name, age, comments, level, joiningDate, expirationDate, hasPrivateBoat, privateBoatSerialNumber, phoneNumber, emailAddress, password, isManager);
    }
}