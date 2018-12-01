package dk.ikas.lcd.examproject;

import java.util.UUID;

public class ReportExtension {

    private String uid;
    private Long timeStamp;
    private final String uuid = UUID.randomUUID().toString();

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUuid(){
        return this.uuid;
    }
}
