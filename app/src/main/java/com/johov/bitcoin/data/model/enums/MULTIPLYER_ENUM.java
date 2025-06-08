package com.johov.bitcoin.data.model.enums;

public enum MULTIPLYER_ENUM {
    MULTYPLIER_1x(1),
    MULTYPLIER_2x(2),
    MULTYPLIER_3x(3),
    MULTYPLIER_5x(5),
    MULTYPLIER_8x(8),
    MULTYPLIER_13x(13),
    MULTYPLIER_21x(21),
    MULTYPLIER_34x(34),
    MULTYPLIER_55x(55);

    private final int value;
    private MULTIPLYER_ENUM(int v) { value = v; }
    public int getValue() { return value; }
    public static MULTIPLYER_ENUM getEnumValue(int v) {
        for (MULTIPLYER_ENUM enumValue : values()) {
            if (enumValue.getValue() == v) {
                return enumValue;
            }
        }
        return null; // or throw an exception if not found
    }
}
