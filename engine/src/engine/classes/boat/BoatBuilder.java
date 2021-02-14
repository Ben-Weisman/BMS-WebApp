package engine.classes.boat;

import engine.customExceptions.InvalidBoatNameException;
import engine.customExceptions.InvalidTypeException;

public class BoatBuilder {
    private int boatID = 0;
    private String boatName;
    private BoatType boatType;
    private boolean isPrivate;
    private boolean isWide = false;
    private boolean hasCoxswain;
    private boolean isCoastal = false;//isCostal
    private boolean isFunctioning = true;


    public void setBoatName(String boatName) {
        this.boatName = boatName;
    }

    public void setBoatType(BoatType boatType) {
        this.boatType = boatType;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public void setWide(boolean wide) {
        isWide = wide;
    }

    public void setHasCoxswain(boolean hasCoxswain) {
        this.hasCoxswain = hasCoxswain;
    }

    public void setCoastal(boolean coastal) {
        isCoastal = coastal;
    }

    public void setFunctioning(boolean functioning) {
        isFunctioning = functioning;
    }

    public void setBoatID(int id){
        this.boatID = id;
    }

    public Boat getResult() throws InvalidTypeException, InvalidBoatNameException {
        if (boatID == 0)
            return new Boat(boatName, boatType,isPrivate,isWide, isCoastal,isFunctioning);
        else return new Boat(boatName, boatType,isPrivate,isWide, isCoastal,isFunctioning, boatID);
    }
}
