package com.bohui.common.qpcr;

import com.bohui.common.to.QPCRLineList;
import lombok.Data;

import java.util.List;

@Data
public class QPCRProjectInfoVo {

    /**
     * 项目类型（呼吸道，腹泻）
     */
    private String ProjectType;


    /**
     * 基线起位置
     */
    private Integer baseLineStart;


    /**
     * 基线终止位置
     */
    private Integer baseLineEnd;

    /**
     * 96条线的详细信息
     */
    private List<QPCRLineList> qpcrlineList;

}
