package lk.hd192.getsafedriver.Utils;

public class MsgPoJo{
    private String message, from;
    private long time;
    private boolean seen;

    public MsgPoJo() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MsgPoJo(String message, String from, long time, boolean seen) {
        this.message = message;
        this.from = from;
        this.time = time;
        this.seen = seen;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}