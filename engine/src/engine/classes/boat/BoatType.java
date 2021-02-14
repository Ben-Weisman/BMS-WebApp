package engine.classes.boat;

import java.io.Serializable;

public enum BoatType implements Serializable {
    SINGLE("1X"), DOUBLE("2X"), COXED_DOUBLE("2X+"), PAIR("2-"), COXED_PAIR("2+"),
    FOUR("4-"), COXED_FOUR("4+"), QUAD("4X-"), COXED_QUAD("4X+"), OCTUPLE("8X+"),
    EIGHT("8+");

    private final String symbol;

    BoatType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean hasCoxswain(){
        return this.name().contains("COXED");
    }

    public int getRowersAmount() {
        String symbol = this.getSymbol();
        if (symbol.length() > 0) {
            if (this.getSymbol().contains("+")){
                return Integer.parseInt(String.valueOf(symbol.charAt(0))) + 1 ;
            }
            return Integer.parseInt(String.valueOf(symbol.charAt(0)));
        }
        return 0;
    }


}