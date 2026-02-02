package hms;

public class Country {
    private final String name;
    private final String code;
    private final String flag;
    private final int length;

    public Country(String name, String code, String flag, int length) {
        this.name = name;
        this.code = code;
        this.flag = flag;
        this.length = length;
    }

    public String getName() { return name; }
    public String getCode() { return code; }
    public String getFlag() { return flag; }
    public int getLength() { return length; }

    @Override
    public String toString() {
        return flag + " " + code; // This is shown in the ComboBox editor
    }
}