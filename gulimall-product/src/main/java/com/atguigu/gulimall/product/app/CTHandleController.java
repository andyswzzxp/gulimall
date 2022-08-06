package com.atguigu.gulimall.product.app;

import com.bohui.common.qpcr.CtResult;
import com.bohui.common.qpcr.PointXY;
import com.bohui.common.qpcr.SmoothLine;
import com.bohui.common.qpcr.TableData;
import com.bohui.common.qpcr.calculate.CalCt;
import com.bohui.common.qpcr.calculate.FilterData;
import com.bohui.common.to.QPCRLineList;
import com.bohui.common.utils.R;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("product/cthandle")
public class CTHandleController {

//
//    /**
//     * 保存
//     */
//    @PostMapping("/CalculateCTAuto/{QPCRProjectType}")
//    public R CalculateCTAuto(@PathVariable("QPCRProjectType") String QPCRProjectType,
//                             @RequestBody List<QPCRLineList> entities) {
//        TableData tableData = new TableData();
//        String[] ProjectTypelist = QPCRProjectType.split("-");
//        List<QPCRLineList> list = new ArrayList<QPCRLineList>();
//        List<TableData> tableData1list = new ArrayList<>();
//        TableData tdFAM1 = new TableData();
//        tdFAM1.setDatatitle("1FAM");
//        TableData tdVIC1 = new TableData();
//        tdVIC1.setDatatitle("VIC");
//        TableData tdROX1 = new TableData();
//        tdROX1.setDatatitle("ROX");
//        TableData tdCY51 = new TableData();
//        tdCY51.setDatatitle("CY5");
//        List<TableData> tableData2list = new ArrayList<>();
//        TableData tdFAM2 = new TableData();
//        tdFAM2.setDatatitle("2FAM");
//        TableData tdVIC2 = new TableData();
//        tdVIC2.setDatatitle("VIC");
//        TableData tdROX2 = new TableData();
//        tdROX2.setDatatitle("ROX");
//        TableData tdCY52 = new TableData();
//        tdCY52.setDatatitle("CY5");
//        List<TableData> tableData3list = new ArrayList<>();
//        TableData tdFAM3 = new TableData();
//        tdFAM3.setDatatitle("3FAM");
//        TableData tdVIC3 = new TableData();
//        tdVIC3.setDatatitle("VIC");
//        TableData tdROX3 = new TableData();
//        tdROX3.setDatatitle("ROX");
//        TableData tdCY53 = new TableData();
//        tdCY53.setDatatitle("CY5");
//
//        for (int i = 0; i < entities.size(); i++) {
//            String[] lineNamelist = entities.get(i).getLineName().split("-");
//            int LaneNum = Integer.parseInt(lineNamelist[1]);
//            QPCRLineList listVo = new QPCRLineList();
//            int minIndexfinal = 0;
//           // System.out.print( entities.get(i).getLineName());
//            if (entities.get(i).getLineName().equals("Lane-2-Tube-1-FAM")) {
//                int m = 0;
//                m++;
//            }
//
//
//            List<Double> currentDatasmooth0 = FilterData.smoothlow(entities.get(i).getYlist(), ProjectTypelist[0], entities.get(i).getLineName());// 去异常
//            Double CtValue = CalCt.GetCTValue(currentDatasmooth0, Integer.valueOf(ProjectTypelist[1]), Integer.valueOf(ProjectTypelist[2]),
//                    entities.get(i).getLineName().split("-")[2]);// 如果无CT，直接归0
//            if (CtValue == 0 || CtValue.isNaN() || currentDatasmooth0.get(currentDatasmooth0.size() - 1) < 5) {
//                for (int j = 0; j < currentDatasmooth0.size(); j++) {
//                    currentDatasmooth0.set(j, 0.0);
//                }
//                CtValue = 0.0;
//
//                SetCtValue(LaneNum, lineNamelist, tdFAM1, tdVIC1, tdROX1, tdCY51
//                        , tdFAM2, tdVIC2, tdROX2, tdCY52, tdFAM3, tdVIC3, tdROX3, tdCY53, i, CtValue);
//                listVo.setLineName(entities.get(i).getLineName());
//                listVo.setYlist(currentDatasmooth0);
//                list.add(listVo);
//                continue;
//
//            }
//            Double ValFinalCT = currentDatasmooth0.get(CtValue.intValue() - 5);// 减去本底
//            try {
//                for (int k = 0; k < currentDatasmooth0.size(); k++) {
//                    currentDatasmooth0.set(k, currentDatasmooth0.get(k) - ValFinalCT);
//                    if (currentDatasmooth0.get(k) < 0) {
//                        currentDatasmooth0.set(k, 0.0);
//                    }
//                }
//                if (currentDatasmooth0.get(currentDatasmooth0.size() - 1) < 5) {// 如果最后一个数小于5，则都置为0
//                    for (int l = 0; l < currentDatasmooth0.size(); l++) {
//                        currentDatasmooth0.set(l, 0.0);
//                    }
//                    CtValue = 0.0;
//                    SetCtValue(LaneNum, lineNamelist, tdFAM1, tdVIC1, tdROX1, tdCY51
//                            , tdFAM2, tdVIC2, tdROX2, tdCY52, tdFAM3, tdVIC3, tdROX3, tdCY53, i, CtValue);
//                    listVo.setLineName(entities.get(i).getLineName());
//                    listVo.setYlist(currentDatasmooth0);
//                    list.add(listVo);
//                    continue;
//                }
//            } catch (Exception ex) {
//                System.out.printf(ex.toString());
//            }
//            List<PointXY> smoothData0 = SmoothLine.GetPointListAfterSmooth(0, null,
//                    currentDatasmooth0, 9, 8, 10, 250, minIndexfinal);// 平滑
//            List<Double> smoothData1 = new ArrayList<Double>();
//            smoothData0.remove(0);
//            for (int j = 0; j < smoothData0.size(); j++) {
//                if (j % 5 == 0) {
//                    smoothData1.add(smoothData0.get(j).getY());
//                }
//            }
//
//            Double minVal = GetminVal(smoothData1);// 找最小值
//            List<Double> ReducedValue = reduceMin(smoothData1, minVal);// 所有数减最小值
//            List<Double> smoothDatafinal = equalminVal(ReducedValue);// 最终S曲线数据
//            List<Double> listdb = FilterData.TopDataHandle(smoothDatafinal, CtValue);
//            SetCtValue(LaneNum, lineNamelist, tdFAM1, tdVIC1, tdROX1, tdCY51
//                    , tdFAM2, tdVIC2, tdROX2, tdCY52, tdFAM3, tdVIC3, tdROX3, tdCY53, i, CtValue);
//            listVo.setLineName(entities.get(i).getLineName());
//            listVo.setYlist(listdb);
//            list.add(listVo);
//        }
//        tableData1list.add(tdFAM1);
//      //  System.out.print(  tdFAM1.getChannel11().toString());
//
//
//        tableData1list.add(tdVIC1);
//        tableData1list.add(tdROX1);
//        tableData1list.add(tdCY51);
//        tableData2list.add(tdFAM2);
//        tableData2list.add(tdVIC2);
//        tableData2list.add(tdROX2);
//        tableData2list.add(tdCY52);
//        tableData3list.add(tdFAM3);
//        tableData3list.add(tdVIC3);
//        tableData3list.add(tdROX3);
//        tableData3list.add(tdCY53);
//        CtResult ctResult = new CtResult();
//        ctResult.setQpcrLineListVos(list);
//        ctResult.setTableData1list(tableData1list);
//        ctResult.setTableData2list(tableData2list);
//        ctResult.setTableData3list(tableData3list);
//        //  return R.ok().put("list", list);
//        return R.ok().put("data", ctResult);
//    }
//
//
//    public List<Double> equalminVal(List<Double> in) {
//        double dmin = 0;
//        int minIndex = 0;
//        List<Double> out = new ArrayList<Double>();
//        try {
//            if (in.size() > 5) {
//                dmin = in.get(5);
//                for (int i = 5; i < in.size(); i++) {// 找出最小值
//                    if (dmin >= in.get(i)) {
//                        dmin = in.get(i);
//                        minIndex = i;
//                    }
//                }
//            }
//
//            for (int i = 0; i < in.size(); i++) {
//                if (minIndex > i) {
//                    out.add(0.0);
//                } else {
//                    out.add(in.get(i));
//                }
//            }
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        //minIndexfinal = minIndex;
//        return out;
//
//    }
//
//
//    public boolean SetCtValue(int LaneNum, String[] lineNamelist, TableData tdFAM1, TableData tdVIC1, TableData tdROX1, TableData tdCY51
//            , TableData tdFAM2, TableData tdVIC2, TableData tdROX2, TableData tdCY52
//            , TableData tdFAM3, TableData tdVIC3, TableData tdROX3, TableData tdCY53, int i, Double CtValue1) {
//        String result = String.valueOf((new BigDecimal(CtValue1)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//        if (LaneNum <= 4)//芯片1
//        {
//            switch (lineNamelist[4]) {
//                case "FAM":
//                    if (lineNamelist[1].equals("1") && lineNamelist[3].equals("1"))
//                        tdFAM1.setChannel11(result); //11
//                    if (lineNamelist[1].equals("1") && lineNamelist[3].equals("2"))
//                        tdFAM1.setChannel12(result);//12
//                    if (lineNamelist[1].equals("2") && lineNamelist[3].equals("1"))
//                        tdFAM1.setChannel21(result); //21
//                    if (lineNamelist[1].equals("2") && lineNamelist[3].equals("2"))
//                        tdFAM1.setChannel22(result);  //22
//                    if (lineNamelist[1].equals("3") && lineNamelist[3].equals("1"))
//                        tdFAM1.setChannel31(result);//31
//                    if (lineNamelist[1].equals("3") && lineNamelist[3].equals("2"))
//                        tdFAM1.setChannel32(result);  //32
//                    if (lineNamelist[1].equals("4") && lineNamelist[3].equals("1"))
//                        tdFAM1.setChannel41(result);   //41
//                    if (lineNamelist[1].equals("4") && lineNamelist[3].equals("2"))
//                        tdFAM1.setChannel42(result);//42
//                    break;
//                case "VIC":
//                    if (lineNamelist[1].equals("1") && lineNamelist[3].equals("1"))
//                        tdVIC1.setChannel11(result); //11
//                    if (lineNamelist[1].equals("1") && lineNamelist[3].equals("2"))
//                        tdVIC1.setChannel12(result);//12
//                    if (lineNamelist[1].equals("2") && lineNamelist[3].equals("1"))
//                        tdVIC1.setChannel21(result); //21
//                    if (lineNamelist[1].equals("2") && lineNamelist[3].equals("2"))
//                        tdVIC1.setChannel22(result);  //22
//                    if (lineNamelist[1].equals("3") && lineNamelist[3].equals("1"))
//                        tdVIC1.setChannel31(result);//31
//                    if (lineNamelist[1].equals("3") && lineNamelist[3].equals("2"))
//                        tdVIC1.setChannel32(result);  //32
//                    if (lineNamelist[1].equals("4") && lineNamelist[3].equals("1"))
//                        tdVIC1.setChannel41(result);  //41
//                    if (lineNamelist[1].equals("4") && lineNamelist[3].equals("2"))
//                        tdVIC1.setChannel42(result);//42
//                    break;
//                case "ROX":
//                    if (lineNamelist[1].equals("1") && lineNamelist[3].equals("1"))
//                        tdROX1.setChannel11(result); //11
//                    if (lineNamelist[1].equals("1") && lineNamelist[3].equals("2"))
//                        tdROX1.setChannel12(result);//12
//                    if (lineNamelist[1].equals("2") && lineNamelist[3].equals("1"))
//                        tdROX1.setChannel21(result); //21
//                    if (lineNamelist[1].equals("2") && lineNamelist[3].equals("2"))
//                        tdROX1.setChannel22(result);  //22
//                    if (lineNamelist[1].equals("3") && lineNamelist[3].equals("1"))
//                        tdROX1.setChannel31(result);//31
//                    if (lineNamelist[1].equals("3") && lineNamelist[3].equals("2"))
//                        tdROX1.setChannel32(result);  //32
//                    if (lineNamelist[1].equals("4") && lineNamelist[3].equals("1"))
//                        tdROX1.setChannel41(result);   //41
//                    if (lineNamelist[1].equals("4") && lineNamelist[3].equals("2"))
//                        tdROX1.setChannel42(result);//42
//                    break;
//                case "CY5":
//                    if (lineNamelist[1].equals("1") && lineNamelist[3].equals("1"))
//                        tdCY51.setChannel11(result); //11
//                    if (lineNamelist[1].equals("1") && lineNamelist[3].equals("2"))
//                        tdCY51.setChannel12(result);//12
//                    if (lineNamelist[1].equals("2") && lineNamelist[3].equals("1"))
//                        tdCY51.setChannel21(result); //21
//                    if (lineNamelist[1].equals("2") && lineNamelist[3].equals("2"))
//                        tdCY51.setChannel22(result);  //22
//                    if (lineNamelist[1].equals("3") && lineNamelist[3].equals("1"))
//                        tdCY51.setChannel31(result);//31
//                    if (lineNamelist[1].equals("3") && lineNamelist[3].equals("2"))
//                        tdCY51.setChannel32(result);  //32
//                    if (lineNamelist[1].equals("4") && lineNamelist[3].equals("1"))
//                        tdCY51.setChannel41(result);   //41
//                    if (lineNamelist[1].equals("4") && lineNamelist[3].equals("2"))
//                        tdCY51.setChannel42(result);//42
//                    break;
//            }
//
//        } else if (LaneNum > 4 && LaneNum <= 8)//芯片2
//        {
//            switch (lineNamelist[4]) {
//                case "FAM":
//                    if (lineNamelist[1].equals("5") && lineNamelist[3].equals("1"))
//                        tdFAM2.setChannel11(result); //11
//                    if (lineNamelist[1].equals("5") && lineNamelist[3].equals("2"))
//                        tdFAM2.setChannel12(result);//12
//                    if (lineNamelist[1].equals("6") && lineNamelist[3].equals("1"))
//                        tdFAM2.setChannel21(result); //21
//                    if (lineNamelist[1].equals("6") && lineNamelist[3].equals("2"))
//                        tdFAM2.setChannel22(result);  //22
//                    if (lineNamelist[1].equals("7") && lineNamelist[3].equals("1"))
//                        tdFAM2.setChannel31(result);//31
//                    if (lineNamelist[1].equals("7") && lineNamelist[3].equals("2"))
//                        tdFAM2.setChannel32(result);  //32
//                    if (lineNamelist[1].equals("8") && lineNamelist[3].equals("1"))
//                        tdFAM2.setChannel41(result);   //41
//                    if (lineNamelist[1].equals("8") && lineNamelist[3].equals("2"))
//                        tdFAM2.setChannel42(result);//42
//                    break;
//                case "VIC":
//                    if (lineNamelist[1].equals("5") && lineNamelist[3].equals("1"))
//                        tdVIC2.setChannel11(result); //11
//                    if (lineNamelist[1].equals("5") && lineNamelist[3].equals("2"))
//                        tdVIC2.setChannel12(result);//12
//                    if (lineNamelist[1].equals("6") && lineNamelist[3].equals("1"))
//                        tdVIC2.setChannel21(result); //21
//                    if (lineNamelist[1].equals("6") && lineNamelist[3].equals("2"))
//                        tdVIC2.setChannel22(result);  //22
//                    if (lineNamelist[1].equals("7") && lineNamelist[3].equals("1"))
//                        tdVIC2.setChannel31(result);//31
//                    if (lineNamelist[1].equals("7") && lineNamelist[3].equals("2"))
//                        tdVIC2.setChannel32(result);  //32
//                    if (lineNamelist[1].equals("8") && lineNamelist[3].equals("1"))
//                        tdVIC2.setChannel41(result);  //41
//                    if (lineNamelist[1].equals("8") && lineNamelist[3].equals("2"))
//                        tdVIC2.setChannel42(result);//42
//                    break;
//                case "ROX":
//                    if (lineNamelist[1].equals("5") && lineNamelist[3].equals("1"))
//                        tdROX2.setChannel11(result); //11
//                    if (lineNamelist[1].equals("5") && lineNamelist[3].equals("2"))
//                        tdROX2.setChannel12(result);//12
//                    if (lineNamelist[1].equals("6") && lineNamelist[3].equals("1"))
//                        tdROX2.setChannel21(result); //21
//                    if (lineNamelist[1].equals("6") && lineNamelist[3].equals("2"))
//                        tdROX2.setChannel22(result);  //22
//                    if (lineNamelist[1].equals("7") && lineNamelist[3].equals("1"))
//                        tdROX2.setChannel31(result);//31
//                    if (lineNamelist[1].equals("7") && lineNamelist[3].equals("2"))
//                        tdROX2.setChannel32(result);  //32
//                    if (lineNamelist[1].equals("8") && lineNamelist[3].equals("1"))
//                        tdROX2.setChannel41(result);   //41
//                    if (lineNamelist[1].equals("8") && lineNamelist[3].equals("2"))
//                        tdROX2.setChannel42(result);//42
//                    break;
//                case "CY5":
//                    if (lineNamelist[1].equals("5") && lineNamelist[3].equals("1"))
//                        tdCY52.setChannel11(result); //11
//                    if (lineNamelist[1].equals("5") && lineNamelist[3].equals("2"))
//                        tdCY52.setChannel12(result);//12
//                    if (lineNamelist[1].equals("6") && lineNamelist[3].equals("2"))
//                        tdCY52.setChannel21(result); //21
//                    if (lineNamelist[1].equals("6") && lineNamelist[3].equals("2"))
//                        tdCY52.setChannel22(result);  //22
//                    if (lineNamelist[1].equals("7") && lineNamelist[3].equals("1"))
//                        tdCY52.setChannel31(result);//31
//                    if (lineNamelist[1].equals("7") && lineNamelist[3].equals("2"))
//                        tdCY52.setChannel32(result);  //32
//                    if (lineNamelist[1].equals("8") && lineNamelist[3].equals("1"))
//                        tdCY52.setChannel41(result);   //41
//                    if (lineNamelist[1].equals("8") && lineNamelist[3].equals("2"))
//                        tdCY52.setChannel42(result);//42
//                    break;
//            }
//        } else//芯片3
//        {
//            switch (lineNamelist[4]) {
//                case "FAM":
//                    if (lineNamelist[1].equals("9") && lineNamelist[3].equals("1"))
//                        tdFAM3.setChannel11(result); //11
//                    if (lineNamelist[1].equals("9") && lineNamelist[3].equals("2"))
//                        tdFAM3.setChannel12(result);//12
//                    if (lineNamelist[1].equals("10") && lineNamelist[3].equals("1"))
//                        tdFAM3.setChannel21(result); //21
//                    if (lineNamelist[1].equals("10") && lineNamelist[3].equals("2"))
//                        tdFAM3.setChannel22(result);  //22
//                    if (lineNamelist[1].equals("11") && lineNamelist[3].equals("1"))
//                        tdFAM3.setChannel31(result);//31
//                    if (lineNamelist[1].equals("11") && lineNamelist[3].equals("2"))
//                        tdFAM3.setChannel32(result);  //32
//                    if (lineNamelist[1].equals("12") && lineNamelist[3].equals("1"))
//                        tdFAM3.setChannel41(result);   //41
//                    if (lineNamelist[1].equals("12") && lineNamelist[3].equals("2"))
//                        tdFAM3.setChannel42(result);//42
//                    break;
//                case "VIC":
//                    if (lineNamelist[1].equals("9") && lineNamelist[3].equals("1"))
//                        tdVIC3.setChannel11(result); //11
//                    if (lineNamelist[1].equals("9") && lineNamelist[3].equals("2"))
//                        tdVIC3.setChannel12(result);//12
//                    if (lineNamelist[1].equals("10") && lineNamelist[3].equals("1"))
//                        tdVIC3.setChannel21(result); //21
//                    if (lineNamelist[1].equals("10") && lineNamelist[3].equals("2"))
//                        tdVIC3.setChannel22(result);  //22
//                    if (lineNamelist[1].equals("11") && lineNamelist[3].equals("1"))
//                        tdVIC3.setChannel31(result);//31
//                    if (lineNamelist[1].equals("11") && lineNamelist[3].equals("2"))
//                        tdVIC3.setChannel32(result);  //32
//                    if (lineNamelist[1].equals("12") && lineNamelist[3].equals("1"))
//                        tdVIC3.setChannel41(result);  //41
//                    if (lineNamelist[1].equals("12") && lineNamelist[3].equals("2"))
//                        tdVIC3.setChannel42(result);//42
//                    break;
//                case "ROX":
//                    if (lineNamelist[1].equals("9") && lineNamelist[3].equals("1"))
//                        tdROX3.setChannel11(result); //11
//                    if (lineNamelist[1].equals("9") && lineNamelist[3].equals("2"))
//                        tdROX3.setChannel12(result);//12
//                    if (lineNamelist[1].equals("10") && lineNamelist[3].equals("1"))
//                        tdROX3.setChannel21(result); //21
//                    if (lineNamelist[1].equals("10") && lineNamelist[3].equals("2"))
//                        tdROX3.setChannel22(result);  //22
//                    if (lineNamelist[1].equals("11") && lineNamelist[3].equals("1"))
//                        tdROX3.setChannel31(result);//31
//                    if (lineNamelist[1].equals("11") && lineNamelist[3].equals("2"))
//                        tdROX3.setChannel32(result);  //32
//                    if (lineNamelist[1].equals("12") && lineNamelist[3].equals("1"))
//                        tdROX3.setChannel41(result);   //41
//                    if (lineNamelist[1].equals("12") && lineNamelist[3].equals("2"))
//                        tdROX3.setChannel42(result);//42
//                    break;
//                case "CY5":
//                    if (lineNamelist[1].equals("9") && lineNamelist[3].equals("1"))
//                        tdCY53.setChannel11(result); //11
//                    if (lineNamelist[1].equals("9") && lineNamelist[3].equals("2"))
//                        tdCY53.setChannel12(result);//12
//                    if (lineNamelist[1].equals("10") && lineNamelist[3].equals("1"))
//                        tdCY53.setChannel21(result); //21
//                    if (lineNamelist[1].equals("10") && lineNamelist[3].equals("2"))
//                        tdCY53.setChannel22(result);  //22
//                    if (lineNamelist[1].equals("11") && lineNamelist[3].equals("1"))
//                        tdCY53.setChannel31(result);//31
//                    if (lineNamelist[1].equals("11") && lineNamelist[3].equals("2"))
//                        tdCY53.setChannel32(result);  //32
//                    if (lineNamelist[1].equals("12") && lineNamelist[3].equals("1"))
//                        tdCY53.setChannel41(result);   //41
//                    if (lineNamelist[1].equals("12") && lineNamelist[3].equals("2"))
//                        tdCY53.setChannel42(result);//42
//                    break;
//            }
//        }
//        return true;
//    }
//
//
//    // 获取最小值
//    public double GetminVal(List<Double> in) {
//        try {
//            double dmin = 0;
//            double dmin2 = 0;
//            int minIndex2 = 0;
//            if (in.size() > 25) {
//                dmin = in.get(10);
//                for (int i = 10; i < 36; i++) {// 找出最小值
//                    if (dmin > in.get(i)) {
//                        dmin = in.get(i);
//                    }
//                }
//            }
//            return minIndex2 > 0 ? dmin2 : dmin;
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return 0;
//        }
//    }
//
//
//    public List<Double> reduceMin(List<Double> in, Double minVal) {
//        double dmax = 0;
//        List<Double> out = new ArrayList<Double>();
//        for (int i = 0; i < in.size(); i++) {
//            if (dmax < in.get(i)) {
//                dmax = in.get(i);
//            }
//            if (i < 5) {
//                out.add(0.0);
//            } else {
//                double valpre = in.get(i) - minVal;
//                out.add(valpre < 0 ? 0 : valpre);
//            }
//        }
//        return out;
//    }
}
