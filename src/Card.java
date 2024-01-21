public class Card {
    private String value;
    private String type;

    public Card(String value, String type) {
        this.value = value;
        this.type = type;
    }

    public boolean isAce(){
        return value == "A";
    }

    public int getValue() {
        if("AJQK".contains(value)){ // A J Q K
            if(value == "A"){
                return 11;
            }
            return 10;
        }
        return Integer.parseInt(value);
    }

    public String getImagePath(){
        return "./cards/" + toString() + ".png";
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return value + "-" + type;
    }

}
