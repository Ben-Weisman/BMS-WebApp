package engine.classes.boat;
import engine.customExceptions.*;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

//test
public class Boat implements Serializable {
    private static volatile int count = 0; // Static field. Increment in each c'tor triggered.
    @XmlAttribute
    private int boatID;
    @XmlElement
    private String boatName;
    private BoatType boatType;
    @XmlElement
    private boolean isPrivate;
    @XmlElement
    private boolean isWide;
    @XmlElement
    private boolean hasCoxswain;
    @XmlElement
    private boolean isCoastal;//isCoastal
    @XmlElement
    private boolean isFunctioning;

    public Boat(){}

    public Boat (String name, BoatType type, boolean isPrivate, boolean isWide, boolean isCoastal, boolean isFunctioning, int id) throws InvalidBoatNameException,InvalidTypeException {

        this.boatID = id;

        if (name != null && name.length()>0)
            this.boatName = name;
        else throw new InvalidBoatNameException();

        if (type != null)
            this.boatType = type;
        else throw new InvalidTypeException();

        this.isCoastal = isCoastal;
        this.isFunctioning = isFunctioning;
        this.isWide = isWide;
        this.isPrivate = isPrivate;
        this.hasCoxswain = type.hasCoxswain();

    }



    public Boat (String name, BoatType type, boolean isPrivate, boolean isWide, boolean isCoastal, boolean isFunctioning) throws InvalidBoatNameException,InvalidTypeException {

        this.boatID = ++count;

        if (name != null && name.length()>0)
            this.boatName = name;
        else throw new InvalidBoatNameException();

        if (type != null)
            this.boatType = type;
        else throw new InvalidTypeException();

        this.isCoastal = isCoastal;
        this.isFunctioning = isFunctioning;
        this.isWide = isWide;
        this.isPrivate = isPrivate;
        this.hasCoxswain = type.hasCoxswain();

    }
    //setters
    public void setCounter(int max){
        count = max;
    }

    public void setName(String nameFromUser) {
        this.boatName = nameFromUser;
    }

    public void setBoatType(BoatType boatTypeFromUser) {
        this.boatType = boatTypeFromUser;
    }

    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public void setIsWide(boolean isWide) {
        this.isWide = isWide;
    }

    public void setHasCoxswain(boolean hasCoxswain) {
        this.hasCoxswain = hasCoxswain;
    }

    public void setIsCoastal(boolean isCoastal) {
        this.isCoastal = isCoastal;
    }

    public void setIsFunctioning(boolean isFunctioning) {
        this.isFunctioning = isFunctioning;
    }

    public void setBoatID(int id) {
        this.boatID = id;
    }

    // Get methods
    public String getBoatName(){

        return this.boatName;
    }
    @XmlElement
    public BoatType getBoatType(){
        return this.boatType;
    }

    public boolean isPrivateBoat (){

        return this.isPrivate;
    }

    public boolean isWideBoat(){

        return this.isWide;
    }

    public boolean isCoastal(){

        return this.isCoastal;
    }

    public  boolean isFunctioningBoat(){

        return this.isFunctioning;
    }

    public boolean hasCoxswain() {
        return hasCoxswain;
    }


    public int getID() {
        return this.boatID;
    }


}
