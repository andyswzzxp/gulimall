package com.bohui.common.qpcr;

import java.io.Serializable;
import java.util.List;

public class QpcrLineViewModel  implements Serializable {
    // Fluorescence name
    private String name;

    public Integer seriesNumber;

    public List<Double> ylist;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSeriesNumber() {
        return seriesNumber;
    }

    public void setSeriesNumber(Integer seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    public List<Double> getY() {
        return ylist;
    }

    public void setY(List<Double> y) {
        this.ylist = y;
    }
}
