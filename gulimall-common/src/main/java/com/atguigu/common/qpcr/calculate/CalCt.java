package com.bohui.common.qpcr.calculate;

import java.util.List;

public class CalCt {

    /**
     * 计算
     * @param in 传入的45个数
     * @param baseLineStart 基线的起始位置
     * @param baseLineEnd  基线的终止位置
     * @param LineType
     * @return
     */
    public static Double GetCTValue(List<Double> in, int baseLineStart, int baseLineEnd, String LineType) {
        double background =0.0;
        Double val=0.0;
        //找出最大，最小值，从第六个数算起
        double dmax = 0;
        double dmin = 99999;
        int minIndex = 0;
        for (int i = 0; i < in.size(); i++) {// 找出最大，最小值
            if (dmax < in.get(i)) {
                dmax = in.get(i);
            }
            if (dmin > in.get(i)) {
                dmin = in.get(i);
                minIndex = i;
            }
        }
        if( GetFilterCTVal((dmax-dmin)*0.1, LineType, baseLineEnd,in.size())==0)
        {
            return 0.0;
        }
        background = getBackground(in, baseLineStart, baseLineEnd);
        if(background>dmin&&minIndex>baseLineEnd)
        {
            background=dmin;
        }
        Double LineValue=(dmax-background)*0.18;
        val=	calculateCtValue(in, background,LineValue);
        return val;
    }

    //如果最大值减去最小值小于100，ROX的小于50，那么无CT
    public static Double GetFilterCTVal(double  LineValue,String LineType,int baseLineEnd,int datasize)
    {
        if(LineValue<10&&!LineType.equals("ROX"))
        {
            return 0.0;
        }
        if(LineValue<5&&!LineType.equals("ROX"))
        {
            return 0.0;
        }
        return baseLineEnd>=datasize?0.0:1.0;

    }


    //求方差
    public static double CalSqrVal(double background ,List<Double> in,int baseLineStart, int baseLineEnd)
    {
        double sumVariance = 0.0;
        for (int i = baseLineStart - 1; i < baseLineEnd; i++) {
            sumVariance += Math.pow(in.get(i) - background, 2);
        }
        sumVariance /= (double) (baseLineEnd - baseLineStart + 1);
        sumVariance = Math.sqrt(sumVariance);
        return sumVariance;
    }

    // 计算基线平均值
    public static double getBackground(List<Double> in, int baseLineStart, int baseLineEnd) {
        double sum = 0.0;
        double count = 0.0;
        for (int i = baseLineStart; i <= baseLineEnd; i++) {
            sum += in.get(i);
            count++;
        }
        return sum / count;
    }


    //获取CT值
    public static Double calculateCtValue(List<Double> in, double background, double threshold) {
        Double y1 = 0.0, y2 = 0.0;
        Double ctValue = 0.0;
        boolean founded = false;
        for (int index = 9; index < in.size() - 1; index++) {
            try {
                y1 = in.get(index) - background;
                y2 = in.get(index + 1) - background;
                if (y1 != null && y2 != null) {
                    if (threshold >= y1 && threshold <= y2) {
                        ctValue = index + (threshold - y1) / (y2 - y1);
                        ctValue += 1;
                        if (ctValue < 1.0) {
                            ctValue = 0.0;
                        }
                        if (ctValue < 10) {
                            continue;
                        } else {
                            founded = true;
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                continue;
            }
        }
        if (!founded) {
            ctValue = 0.0;
        }
        return ctValue;
    }

}
