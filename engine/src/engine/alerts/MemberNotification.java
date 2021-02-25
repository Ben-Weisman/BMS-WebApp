package engine.alerts;

public class MemberNotification implements Comparable<MemberNotification>{
    private final String message;
    private final String sender;
    private final long timestamp;

    public MemberNotification (String message){
        this.message = message;
        this.timestamp = System.currentTimeMillis();
        this.sender = "system";
    }

    public MemberNotification (String message, String sender){
        this.message = message;
        this.timestamp = System.currentTimeMillis();
        this.sender = sender;
    }

    @Override
    public int compareTo(MemberNotification o) {
        if (o == null)
            return 1;
        return (int)(this.getTimestamp() - o.getTimestamp());
    }

    public long getTimestamp() {
        return timestamp;
    }


}
