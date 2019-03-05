package Data;

import java.text.SimpleDateFormat;
import java.util.Date;

import spark.loop.callblocker.R;

public class UserData {

    String Name, Number,date,Type;
    int type;

    public UserData(String Name, String Number) {
        this.Name = Name;
        this.Number = Number;
    }

    public UserData(String name, String number, int type,long date) {
        if (name==null){
            this.Name ="Unknown";
        }else {
            this.Name=name;
        }
        this.Number = number;
        this.type = type;
        switch (type){
            case 1:
                 Type="Incoming";
                 break;
            case 2:
                 Type="Outgoing";
                 break;
            default:
                 Type="Missed";
                    break;
        }
        Date calldaytime = new Date(date);
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MMMM-yy\nhh:mm aa");
        this.date=dateFormat.format(calldaytime);


    }

    public String getName() {
        if (Name==null){
            return "Unknown";
        }else{
            return Name;
        }

    }

    public String getNumber() {
        return Number;
    }

    public String getType() {
    return Type;
    }

    public String getDate() {

        return date;
    }
}
