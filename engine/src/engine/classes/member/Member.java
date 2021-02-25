package engine.classes.member;

import engine.alerts.MemberNotification;
import engine.alerts.MemberNotificationManager;
import engine.xmlAdapters.XmlLocalDateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)

public class Member implements Serializable {
    @XmlElement
    private String name;
    private static volatile int count = 0;

    @XmlAttribute
    private int memberID;

    @XmlElement
    private int age;

    @XmlElement
    private String comments;

    @XmlElement
    private Level level;

    @XmlJavaTypeAdapter(value = XmlLocalDateAdapter.class)
    private LocalDate joiningDate;

    @XmlJavaTypeAdapter(value = XmlLocalDateAdapter.class)
    private LocalDate expirationDate;

    @XmlElement
    private boolean hasPrivateBoat;

    @XmlElement
    private String privateBoatSerialNumber;

    @XmlElement
    private String phoneNumber;

    @XmlElement
    private String emailAddress;

    @XmlElement
    private String password;

    @XmlElement
    private boolean isManager;

    private MemberNotificationManager notificationManager = new MemberNotificationManager();;

    public Member(){}
    public Member(String name, int age, String comments, Level level, LocalDate joiningDate, LocalDate expirationDate, boolean hasPrivateBoat, String privateBoatSerialNumber, String phoneNumber, String emailAddress, String password, boolean isManager) {
        this.name = name;
        this.memberID = ++count;
        this.age = age;
        this.comments = comments;
        this.level = level;
        this.joiningDate = joiningDate;
        this.expirationDate = expirationDate;
        this.hasPrivateBoat = hasPrivateBoat;
        this.privateBoatSerialNumber = privateBoatSerialNumber;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.password = password;
        this.isManager = isManager;


    }

    public Member(String name, int age, String comments, Level level, LocalDate joiningDate, LocalDate expirationDate, boolean hasPrivateBoat, String privateBoatSerialNumber, String phoneNumber, String emailAddress, String password, boolean isManager, int id) {
        this.name = name;
        this.memberID = id;
        this.age = age;
        this.comments = comments;
        this.level = level;
        this.joiningDate = joiningDate;
        this.expirationDate = expirationDate;
        this.hasPrivateBoat = hasPrivateBoat;
        this.privateBoatSerialNumber = privateBoatSerialNumber;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.password = password;
        this.isManager = isManager;

    }

    public void addAutoNotification(MemberNotification mn){
        this.notificationManager.addAutoNotification(mn);
    }
    public List<MemberNotification> getAutoNotification(){
        return this.notificationManager.getAutoNotifications();
    }


    public void addAdminNotification(MemberNotification newNot){
        this.notificationManager.addAdminNotification(newNot);
    }
    public List<MemberNotification> getAdminNotifications(int chatVersion){
        return this.notificationManager.getAdminNotifications(chatVersion);
    }


    public void setCounter(int num){
        count = num;
    }




  /*  public Member (String name, String emailAddress, String phoneNumber, String password){
        this.name = name;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.password = password;

         int age = 26;
         String[] comments = null;
         Level level = null;
         LocalDate joiningDate = null;
         LocalDate expirationDate = null;
         hasPrivateBoat = false;
         String privateBoatSerialNumber = null;
    }*/


    public boolean isAdmin(){
        return this.isManager;
    }
    public String getPassword(){
        return this.password;
    }
    public String getEmailAddress(){
        return this.emailAddress;
    }
    public String getName(){return this.name;}
    public int getAge() {return this.age;}
    public String getPhoneNumber(){return this.phoneNumber;}
    public int getID(){
        return this.memberID;
    }

    public void setName(String newName){
        this.name = newName;
    }
    public void setEmailAddress(String newEmail){
        this.emailAddress = newEmail;
    }
    public void setPhoneNumber(String newPhone){
        this.phoneNumber = newPhone;
    }
    public void setPassword(String newPass){
        this.password = newPass;
    }


    public String getComments() {
        return comments;
    }

    public Level getLevel() {

        return  level;
    }

    public LocalDate getJoiningDate() {
            return joiningDate;

    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public boolean hasPrivateBoat() {
        return hasPrivateBoat;
    }

    public String getPrivateBoatSerialNumber() {
        return  privateBoatSerialNumber;

    }
    public  boolean isManager(){
        return isManager;
    }

    public void setAge(int ageFromUser) {
        if (ageFromUser < 0)
            this.age = ageFromUser;
    }

    public void replaceComments(String commentsFromUser) {
        this.comments = commentsFromUser;
    }

    public void setExpirationDate(LocalDate expirationDateFromUser) {
        if (this.joiningDate.isBefore(expirationDate))
            this.expirationDate = expirationDateFromUser;
    }

    public void setPrivateBoatSerialNumber(String privateBoatSerialNumber) {
        if (privateBoatSerialNumber.isEmpty() || privateBoatSerialNumber == null) {
            this.hasPrivateBoat = false;
        } else {
            this.hasPrivateBoat = true;
            this.privateBoatSerialNumber = privateBoatSerialNumber;
        }
    }

    public void setHasPrivateBoat(boolean fromUserIfHasPrivateBoat) {
        if (!fromUserIfHasPrivateBoat) {
            this.privateBoatSerialNumber = null;
        }
        this.hasPrivateBoat = fromUserIfHasPrivateBoat;
    }

    public void setAdmin(boolean boolFromUser) {
        this.isManager = boolFromUser;
    }

    public void setID(int newIDVal) {
        this.memberID = newIDVal;
    }

    public void removePrivateBoat() {
        setHasPrivateBoat(false);
        this.privateBoatSerialNumber = "";
    }

    public MemberNotificationManager getNotificationsManager() {
        return this.notificationManager;
    }
}
