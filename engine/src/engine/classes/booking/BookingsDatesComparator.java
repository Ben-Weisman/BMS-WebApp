package engine.classes.booking;

import java.util.Comparator;

public class BookingsDatesComparator implements Comparator<Booking> {
    @Override
    public int compare(Booking b1, Booking b2){
        return b1.getRequestedPracticeDate().compareTo(b2.getRequestedPracticeDate());
    }
}
