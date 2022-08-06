package com.bohui.common.qpcr;

public enum QPCRProjectType {
    呼吸道("呼吸道",(byte) 0x00),
    腹泻("腹泻",(byte) 0x01),
    其它("其它",(byte) 0x02),
    初始("初始",(byte) 0x03);


    String name;
    public byte getValue() {
        return value;
    }
    public void setValue(byte value) {
        this.value = value;
    }
    public void setName(String name) {
        this.name = name;
    }
    byte value;

    private QPCRProjectType(String name, byte value) {
        this.name = name;
        this.value = value;
    }
    public String getName() {
        return name;
    }
    public byte getByte() {
        return value;
    }
}
