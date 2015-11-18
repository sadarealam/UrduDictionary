package seatech.alam.urdudictionary.util;

/**
 * Created by yesalam on 18-11-2015.
 */
public class DModel {
    String roman ;
    String urdu ;

    public DModel(){

    }
    public DModel(String urdu,String roman){
        this.roman = roman ;
        this.urdu = urdu ;
    }

    public String getUrdu() {
        return urdu;
    }

    public void setUrdu(String urdu) {
        this.urdu = urdu;
    }

    public String getRoman() {
        return roman;
    }

    public void setRoman(String roman) {
        this.roman = roman;
    }
}
