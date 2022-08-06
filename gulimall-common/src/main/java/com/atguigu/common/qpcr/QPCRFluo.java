package com.bohui.common.qpcr;

import java.awt.*;

public enum QPCRFluo {

    FAM(0, "ch1" , true, "FAM", (byte)0x04, Color.BLUE  , 1),
    VIC(1, "ch2" , true, "VIC", (byte)0x01, Color.GREEN , 3),
    ROX(2, "ch3" , true, "ROX", (byte)0x08, Color.ORANGE, 0),
    CY5(3, "ch4" , true, "CY5", (byte)0x02, Color.RED   , 2);

    private int		displayPosition;
    private String  channel;
    private boolean isEnabled;
    private String  name;
    private byte    ledPattern;
    private Color   color;
    private int     dataPosition;

    private QPCRFluo(int displayPosition, String channel, boolean isEnabled, String name, byte ledPattern,  Color color, int dataPosition) {
        this.displayPosition = displayPosition;
        this.channel = channel;
        this.isEnabled = isEnabled;
        this.name = name;
        this.ledPattern = ledPattern;
        this.color = color;
        this.dataPosition = dataPosition;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public int getDisplayPosition() {
        return displayPosition;
    }

    public void setDisplayPosition(int displayPosition) {
        this.displayPosition = displayPosition;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getLedPattern() {
        return ledPattern;
    }

    public void setLedPattern(byte ledPattern) {
        this.ledPattern = ledPattern;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getDataPosition() {
        return dataPosition;
    }

    public void setDataPosition(int dataPosition) {
        this.dataPosition = dataPosition;
    }

    public static QPCRFluo fromDisplayPosition(int p) {
        for (QPCRFluo fluo : QPCRFluo.values()) {
            if (fluo.getDisplayPosition() == p) {
                return fluo;
            }
        }
        return null;
    }

    public static QPCRFluo fromDataPosition(int p) {
        for (QPCRFluo fluo : QPCRFluo.values()) {
            if (fluo.getDataPosition() == p) {
                return fluo;
            }
        }
        return null;
    }
}
