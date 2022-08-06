package com.bohui.common.to;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

@Data
public class QPCRLineList {


    private String lineName;
    private List<Double > ylist;

    public Integer seriesNumber;



}
