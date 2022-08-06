package com.bohui.common.qpcr;

import com.bohui.common.to.QPCRLineList;
import lombok.Data;

import java.util.List;

@Data
public class CtResult   {

    private List<QPCRLineList> qpcrLineListVos;

    //芯片1信息
    private List<TableData> tableData1list;
    //芯片2信息
    private List<TableData> tableData2list;
    //芯片3信息
    private List<TableData> tableData3list;

}


