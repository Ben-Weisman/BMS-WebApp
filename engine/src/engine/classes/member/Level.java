package engine.classes.member;

import java.io.Serializable;

public enum Level  implements Serializable {
    BEGINNER("Beginner"), INTERMEDIATE("Intermediate"), ADVANCED("Advanced");

    private final String symbol;

    Level (String symbol){
        this.symbol = symbol;
    }

    public String getSymbol(){
        return this.symbol;
    }
}
