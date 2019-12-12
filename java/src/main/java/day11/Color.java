package day11;

public enum Color {
    Black(0, "."), White(1, "#");

    private long value;
    private String displayValue;

    Color(long value, String displayValue) {
        this.value = value;
        this.displayValue = displayValue;
    }

    public long getValue() {
        return value;
    }

    @Override
    public String toString() {
        return displayValue;
    }

    public static Color valueOf(long val) {
        if(val == 0L){
            return Black;
        }

        return White;
    }
}
