package com.bohui.common.utils;

import com.bohui.common.qpcr.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtils {


    //读取excel数据
    //1.新老文件都能读
    public static List<QpcrLineViewModel> load(String filePath) throws IOException {
        List<QpcrLineViewModel> lineViewModelList = new ArrayList<QpcrLineViewModel>();
        Workbook wb = null;
        Sheet sheet;
        FileInputStream fileIn = new FileInputStream(filePath);
        try {
            wb = new XSSFWorkbook(fileIn);
            fileIn.close();
        } catch (Exception ex) {
            FileInputStream fileIn1 = new FileInputStream(filePath);
            wb = new HSSFWorkbook(fileIn1);
            fileIn1.close();
        } finally {
            sheet = wb.getSheetAt(0);
            // read data by row
            int rowNumber = 1;
            Row rtitle = sheet.getRow(0);
            if (rtitle.getCell(0).toString().equals("CT")) {
                CurrentData.getInstance().ProjectType = QPCRProjectType.呼吸道;
            } else {
                //CurrentData.getInstance().ProjectType=rtitle.getCell(1).toString().equals(QPCRProjectType.呼吸道.toString())?QPCRProjectType.呼吸道:QPCRProjectType.腹泻;
                rowNumber += 1;
            }


            for (QPCRFluo fluo : QPCRFluo.values()) {
                System.out.println(fluo.getName());
                for (int m = 0; m < DefinitionsQPCR.MODULES; m++) {
                    for (int l = 0; l < DefinitionsQPCR.LANES_PER_MODULE; l++) {
                        for (int v = 0; v < DefinitionsQPCR.VIALS_PER_LANE; v++) {

  //                          QPCRLane lane = chart.getLanes().get(m*DefinitionsQPCR.LANES_PER_MODULE + l);
                           QpcrLineViewModel line = new QpcrLineViewModel();
//                            line.setY()=new ArrayList<Double>();
                            line.setName(fluo.getName());
//                            line.clear();
                            // save by row
                            Row r = sheet.getRow(rowNumber++);

                            // read data
                            List<Double> yvallist=new ArrayList<Double>();
                            for (int i = 2; i < r.getLastCellNum(); i++) {
                                try {
                                    double a = r.getCell(i).getNumericCellValue();
                                    yvallist.add(a);
                                } catch (Exception e) {
                                    System.out.print(e.toString());
                                }
                            }
                            line.setY(yvallist);
                            lineViewModelList.add(line);
                        }
                    }
                }
            }
            //setFilePath(filePath);
        }
        return lineViewModelList;

//      } catch (Exception e) {
//          e.printStackTrace();
//      }
    }


    public void setFilePath(String filePath) {
//        this.filePath.set(filePath);
//        CurrentData.getInstance().filUrl = filePath;
    }

}
