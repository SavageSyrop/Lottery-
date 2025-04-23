package ru.lot.enums;

public enum LotteryType {
    FIVE_OUT_OF_36("5 из 36"),
    SIX_OUT_OF_45("6 из 45");

    private final String label;

    LotteryType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }

    public static LotteryType fromLabel(String label) {
        for (LotteryType type : values()) {
            if (type.getLabel().equalsIgnoreCase(label)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown lottery type: " + label);
    }
}
