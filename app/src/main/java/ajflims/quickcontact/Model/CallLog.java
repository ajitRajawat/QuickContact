package ajflims.quickcontact.Model;

/**
 * Created by ajit on 10/13/2018.
 */

public class CallLog {
    String name,number,calldate,calltime,calltype,callduration;

    public CallLog(String name, String number, String calldate, String calltype, String callduration) {
        this.name = name;
        this.number = number;
        this.calldate = calldate;
        this.calltype = calltype;
        this.callduration = callduration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCalldate() {
        return calldate;
    }

    public void setCalldate(String calldate) {
        this.calldate = calldate;
    }

    public String getCalltype() {
        return calltype;
    }

    public void setCalltype(String calltype) {
        this.calltype = calltype;
    }

    public String getCallduration() {
        return callduration;
    }

    public void setCallduration(String callduration) {
        this.callduration = callduration;
    }
}
