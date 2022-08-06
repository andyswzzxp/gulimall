package com.bohui.common.qpcr.calculate;

import com.bohui.common.qpcr.QPCRProjectType;

import java.util.ArrayList;
import java.util.List;

public class FilterData {

    // 找最大值
    public static Double GetMaxVal(List<Double> in) {
        double dmax = 0;
        int maxIndex = 0;
        for (int i = 0; i < in.size(); i++) {// 找出最小值

            if (dmax < in.get(i)) {
                dmax = in.get(i);
                maxIndex = i;
            }
        }
        return in.get(maxIndex);
    }

    /// 归线，最终最大值
    public static List<Double> TopDataHandle(List<Double> in, Double CtValue) {
        Double maxvalue = 0.0;
        Double RatioVal = 0.0;
        maxvalue = GetMaxVal(in);
        int k = 0;
        int b = 0;

        if (CtValue <= 20) {
            k = -6;
            b = 1140;
        } else if (CtValue > 20 && CtValue <= 25) {
            k = -4;
            b = 1100;
        } else if (CtValue > 25 && CtValue <= 26) {
            k = -100;
            b = 3500;
        } else if (CtValue > 26 && CtValue <= 27) {
            k = -90;
            b = 3240;
        } else if (CtValue > 27 && CtValue <= 28) {
            k = -80;
            b = 2970;
        } else if (CtValue > 28 && CtValue <= 29) {
            k = -70;
            b = 2690;
        } else if (CtValue > 29 && CtValue <= 30) {
            k = -60;
            b = 2400;
        } else if (CtValue > 30 && CtValue <= 31) {
            k = -50;
            b = 2100;
        } else if (CtValue > 31 && CtValue <= 32) {
            k = -40;
            b = 1790;
        }

        else if (CtValue > 32 && CtValue <= 33) {
            k = -30;
            b = 1470;
        }

        else if (CtValue > 33 && CtValue <= 34) {
            k = -20;
            b = 1140;
        } else if (CtValue > 34 && CtValue <= 35) {
            k = -10;
            b = 800;
        }

        else if (CtValue > 35) {
            k = -5;
            b = 625;
        }

        RatioVal = (CtValue * k + b) / maxvalue;
        for (int i = 0; i < in.size(); i++) {
            Double value = in.get(i) * RatioVal;
            in.set(i, value);
        }
        return in;
    }

    // 处理异常数据
    public static List<Double> smoothlow(List<Double> in, String ProjectType, String LineType)// 突然调下去，又上来
    {
        int minindex=0;
        try {
            List<Double> out = new ArrayList<Double>(in);
            // 1.1.1: 5-15向上跳,又下来了
            for (int i = 4; i < in.size() - 5; i++) {
                if ((in.get(i) < (in.get(i + 1) - 53)) && (in.get(i + 1) - 50 > in.get(i + 2))) {
                    out.set(i + 1, (in.get(i + 2) + in.get(i)) / 2);
                    break;
                }
            }
//			// 1.1.2: 有气泡，跳了几个，又下来了
            for (int i = 5; i < in.size() - 9; i++) {
                double avgbefore = 0;
                double avgafter = 0;
                if ((in.get(i) < (in.get(i + 1) - 53)) && (in.get(i + 2) - 50 > in.get(i + 3)))// 跳两个
                {
                    avgbefore = (in.get(i - 1) + in.get(i - 2) + in.get(i - 3) + in.get(i - 4) + in.get(i - 5)) / 5;
                    avgafter = (in.get(i + 3) + in.get(i + 4) + in.get(i + 5) + in.get(i + 6) + in.get(i + 7)) / 5;
                    if (avgafter - avgbefore <= 50) {
                        out.set(i + 1, (in.get(i + 3) + in.get(i)) / 2);
                        out.set(i + 2, (in.get(i + 3) + in.get(i)) / 2);
                        break;
                    }
                }

                if ((in.get(i) < (in.get(i + 1) - 53)) && (in.get(i + 3) - 50 > in.get(i + 4)))// 跳三个
                {
                    avgbefore = (in.get(i - 1) + in.get(i - 2) + in.get(i - 3) + in.get(i - 4) + in.get(i - 5)) / 5;
                    avgafter = (in.get(i + 8) + in.get(i + 4) + in.get(i + 5) + in.get(i + 6) + in.get(i + 7)) / 5;
                    if (avgafter - avgbefore <= 50) {
                        out.set(i + 1, (in.get(i + 4) + in.get(i)) / 2);
                        out.set(i + 2, (in.get(i + 4) + in.get(i)) / 2);
                        out.set(i + 3, (in.get(i + 4) + in.get(i)) / 2);
                        break;
                    }
                }
                if ((in.get(i) < (in.get(i + 1) - 53)) && (in.get(i + 4) - 50 > in.get(i + 5)))// 跳四个
                {
                    avgbefore = (in.get(i - 1) + in.get(i - 2) + in.get(i - 3) + in.get(i - 4) + in.get(i - 5)) / 5;
                    try {
                        avgafter = (in.get(i + 8) + in.get(i + 9) + in.get(i + 5) + in.get(i + 6) + in.get(i + 7)) / 5;
                    }
                    catch (Exception ex){
                        String s=ex.toString();
                    }

                    if (avgafter - avgbefore <= 50) {
                        out.set(i + 1, (in.get(i + 5) + in.get(i)) / 2);
                        out.set(i + 2, (in.get(i + 5) + in.get(i)) / 2);
                        out.set(i + 3, (in.get(i + 5) + in.get(i)) / 2);
                        out.set(i + 4, (in.get(i + 5) + in.get(i)) / 2);
                        break;
                    }
                }
            }

            // 1.1.3:前15个，有连续下降的，那么都等于不再下降的那个值
            int descNum = 5;
            for (int i = 5; i < out.size() - 5; i++) {
                try {
                    if (out.get(i) >= out.get(i + 1)) {
                        descNum++;
                    } else {
                        break;
                    }
                } catch (Exception ex) {
                    System.out.print(ex.toString());
                }

            }
            if (descNum > 5) {
                for (int i = 5; i < descNum; i++) {
                    out.set(i, out.get(descNum));
                }
            }
            // 1.1.4前五个取值，取平均值
            Double AvgValue = 0.0;
            for (int i = 5; i < 15; i++) {
                AvgValue += out.get(i);
            }
            AvgValue = AvgValue / 10;
            for (int i = 0; i < 5; i++) {
                out.set(i, AvgValue);
            }
            if (out.size() > 25) {
                for (int i = 5; i < 35; i++) {// 1.1.5找出最小值,突然调下去后又突然上来，取两边的平均值
                    if (i > 5 && i < 15 & (out.get(i) < Math.abs(out.get(i - 1) - 30))
                            && (out.get(i) < Math.abs(out.get(i + 1) - 30))) {
                        out.set(i, (out.get(i - 1) + out.get(i + 1)) / 2);
                        break;
                    } else if (i >= 15 && (out.get(i) < Math.abs(out.get(i - 1) - 40))
                            && (out.get(i) < Math.abs(out.get(i + 1) - 40))) {
                        out.set(i, (out.get(i - 1) + out.get(i + 1)) / 2);
                        break;
                    } else if (i >= 15 && (out.get(i) - 40 > Math.abs(out.get(i - 1)))
                            && (out.get(i) - 40 > Math.abs(out.get(i + 1)))) {
                        out.set(i, (out.get(i - 1) + out.get(i + 1)) / 2);
                        break;
                    } else if (i >= 20 && ((out.get(i) - out.get(i + 1)) > 40 || (out.get(i) > out.get(i + 1)))
                            && ((out.get(i + 2) - out.get(i + 1)) > 40 || (out.get(i + 2) > out.get(i + 1)))// 起峰了，但是第一个数比第二个数大，第三个数比第二个数大
                            && (out.get(i) > out.get(i - 1)) && (out.get(i - 1) > out.get(i - 2))
                            && (out.get(i + 3) > out.get(i + 2)) && (out.get(i + 4) > out.get(i + 3))
                            && (out.get(i + 5) > out.get(i + 4))) {
                        out.set(i + 1, (out.get(i) + out.get(i + 2)) / 2);
                        break;
                    }
                }

                for (int i = 10; i < out.size() - 5; i++) // 1.1.6 整个基线跳了一级//向上调
                {
                    if ((out.get(i) < (out.get(i + 1) - 120))) {
                        int beforNum = 0;
                        if (Math.abs(out.get(i) - out.get(i - 1)) < 100)
                            beforNum++;
                        if (Math.abs(out.get(i - 1) - out.get(i - 2)) < 100)
                            beforNum++;
                        if (Math.abs(out.get(i - 2) - out.get(i - 3)) < 100)
                            beforNum++;
                        if (Math.abs(out.get(i - 3) - out.get(i - 4)) < 100)
                            beforNum++;
                        if (Math.abs(out.get(i - 4) - out.get(i - 5)) < 100)
                            beforNum++;
                        if (Math.abs(out.get(i - 5) - out.get(i - 6)) < 100)
                            beforNum++;
                        if (beforNum >= 3) {
                            int suceesNum = 0;
                            if (Math.abs(out.get(i + 1) - out.get(i + 2)) < 50)
                                suceesNum++;
                            if (Math.abs(out.get(i + 2) - out.get(i + 3)) < 50)
                                suceesNum++;
                            if (Math.abs(out.get(i + 3) - out.get(i + 4)) < 50)
                                suceesNum++;
                            if (Math.abs(out.get(i + 4) - out.get(i + 5)) < 50)
                                suceesNum++;
                            if (out.size() > i + 6) {
                                if (Math.abs(out.get(i + 5) - out.get(i + 6)) < 50)
                                    suceesNum++;
                            }
                            if (out.size() > i + 7) {
                                if (Math.abs(out.get(i + 6) - out.get(i + 7)) < 50)
                                    suceesNum++;
                            }
                            if (suceesNum >= 4) {
                                minindex = GetminValindex(out, i);
                                boolean pearkhave = false;
                                for (int k = minindex + 1; k < i; k++) {
                                    if (out.get(minindex) + 100 <= out.get(k)) {
                                        pearkhave = true;
                                        break;
                                    }
                                }
                                if (pearkhave)// 如果峰后有上跳
                                {
                                    // 1.找出调了多少
                                    // 2.跳后的每个数都减去这个值
                                    for (int m = i + 1; m < out.size(); m++) {
                                        out.set(m, out.get(m) - ((out.get(i + 1) - out.get(i))));
                                    }
                                }
                                if (!pearkhave) {
                                    for (int j = 0; j <= i; j++) {
                                        out.set(j, out.get(i + 1));
                                    }
                                }
                            }
                        }
                    }
                }

                for (int i = 10; i < out.size() - 5; i++) // 整个基线跳了一级//向下调
                {

                    if ((out.get(i) >= (out.get(i + 1) + 50)) && (out.get(i) > (out.get(i + 1) + 30))
                            && (Math.abs(out.get(i + 1) - out.get(i + 2)) < 30)
                            && (Math.abs(out.get(i + 2) - out.get(i + 3)) < 30)
                            && (Math.abs(out.get(i + 3) - out.get(i + 4)) < 30)
                            && (Math.abs(out.get(i + 4) - out.get(i + 5)) < 30)) {
                        boolean pearkhave = false;
                        for (int k = minindex + 1; k < i; k++) {

                            if (out.get(minindex) + 100 <= out.get(k)) {
                                pearkhave = true;
                                break;
                            }
                        }
                        if (pearkhave)// 如果峰后有上跳
                        {
                            // 1.找出调了多少
                            Double addValue = Math.abs(out.get(i + 1) - out.get(i));
                            // 2.跳后的每个数都加上这个值
                            for (int m = i + 1; m < out.size(); m++) {

                                out.set(m, out.get(m) + addValue);
                            }
                        }
                    }
                }
            }
            // ???????????????????????是否还要找出最小值，然后比较末尾的几个数与这个最小值的比较
            Double maxvalue = GetMaxVal(out);

            if (maxvalue != out.get(out.size() - 1) && maxvalue != out.get(out.size() - 2)
                    && maxvalue != out.get(out.size() - 3) && maxvalue != out.get(out.size() - 4)
                    && maxvalue != out.get(out.size() - 5)) {
                Double AvgValuefinal = 0.0;
                for (int i = out.size() - 3; i < out.size(); i++) {
                    AvgValuefinal += out.get(i);
                }

                AvgValuefinal = AvgValuefinal / 3;
                if (AvgValuefinal + 45 < maxvalue) {
                    for (int i = 0; i < out.size(); i++) {

                        out.set(i, 0.0);
                    }
                }
            }
            for (int i = 4; i < 15; i++) {
                if ((out.get(i) < (out.get(i + 1) - 53)) && (out.get(i + 1) - 50 > out.get(i + 2))) {
                    out.set(i + 1, (out.get(i + 2) + out.get(i)) / 2);
                    break;
                }
            }

            // 取最后5个数的平均数，如果前面的数超出这个平均数，那么超出的都等于它？？？？？？？？？

            // 没有峰的都归0;
//			if (LineType.equals("ROX"))
//			{
//				return out;
//			}
            List<Integer> peakIndex = HavePeak(out, LineType, maxvalue,ProjectType);
            if (peakIndex.size() <= 0) {
                for (int i = 0; i < out.size(); i++) {
                    out.set(i, 0.0);
                }
            }

            else// 如果有峰，找到最大的峰，之前之后的峰都归0
            {
                //有峰但是峰的5个值平均值比最后5个数还大，则都置为0
                int finaleIndex = peakIndex.get(peakIndex.size() - 1);
                int minIndex = finaleIndex - 5;
                Double minvalue = out.get(finaleIndex - 5);
                for (int i = finaleIndex - 4; i <= finaleIndex; i++) {
                    if (minvalue > out.get(i)) {
                        minvalue = out.get(i);
                        minIndex = i;
                    }
                }

                for (int i = 0; i <= finaleIndex; i++) {
                    if (minIndex > i) {
                        out.set(i, minvalue);
                    }
                }

                for (int i = 0; i < out.size(); i++) {
                    out.set(i, out.get(i) - minvalue);
                    if (out.get(i) < 0) {
                        out.set(i, 0.0);
                    }
                }
                //最后的峰以后，数都不能小于当前数
                for(int i=finaleIndex;i<out.size();i++)
                {
                    if(out.get(i)<out.get(i-1)-5)
                    {
                        out.set(i, out.get(i-1));
                    }
                }
            }

            return out;
        } catch (Exception e) {
            e.printStackTrace();
            return in;
        }

    }

    public static int GetminValindex(List<Double> in, int endindex) {
        // double
        try {
            double dmin = 0;
            int minIndex = 0;
            if (in.size() > 25) {
                dmin = in.get(10);
                minIndex = 10;
                for (int i = 10; i < endindex - 5; i++) {// 找出最小值
                    if (dmin > in.get(i)) {
                        dmin = in.get(i);
                        minIndex = i;
                    }
                }
            }
            return minIndex;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }

    // 获得最小值，整个基线往下跳了一级的情况
    public static int GetminValindexDown(List<Double> in, int endindex) {
        // double
        try {
            double dmin = 0;
            int minIndex = 0;
            if (in.size() > 25) {
                dmin = in.get(5);
                minIndex = 5;
                for (int i = endindex; i < in.size() - 5; i++) {// 找出最小值
                    if (dmin > in.get(i)) {
                        dmin = in.get(i);
                        minIndex = i;
                    }
                }
            }
            return minIndex;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }

    // 判读是否有峰

    /// 腹泻项目各阶段Ct荧光变化量限制条件---
    // 1.如果找到起峰处了，先看看是不是腹泻类型if
    /// (CurrentData.getInstance().ProjectType.getName().equals(QPCRProjectType.呼吸道.toString()))
    // 2.找出起峰后的最大值，
    // 3.最大减最小值的差
    // 4.根据值，通道，A/B等相关与3相比较，小于3则认为有峰
    public static List<Integer> HavePeak(List<Double> in, String LineTypewhole, Double maxvalue,String ProjectType) {
        List<Integer> peakIndex = new ArrayList<Integer>();
        for (int i = 5; i < in.size() - 5; i++) {
            if (i + 5 > in.size()) {
                break;
            }


            String LineType = LineTypewhole.split("-")[2];

            //如果符合指数级增长
            if (CompareValue(in.get(i), in.get(i + 1), in.get(i + 2), in.get(i + 3), in.get(i + 4), in.get(i + 5),LineType, maxvalue))
            {
                if ((peakIndex.size() > 0 && CompareValue(in.get(i - 5), in.get(i - 4), in.get(i - 3), in.get(i - 2),
                        in.get(i - 1), in.get(i), LineType, maxvalue))) //如果它的前5个数也符合指数级增长，则不算这个峰
                {
                    continue;
                }

                String[] linetypelist = LineTypewhole.split("-");
                // 1.如果找到起峰处了，先看看是不是腹泻类型
                if (ProjectType.equals(QPCRProjectType.腹泻.toString()))
//						|| (linetypelist[1].equals("Tube2") && linetypelist[2].equals("FAM"))
//						|| (linetypelist[1].equals("Tube2") && linetypelist[2].equals("ROX")))
                {
                    //往下是最终符合指数级增长的情况---------------------------------
                    //0.index前的值都清0

                    //1.算CT值
//					Double CtValue = CalCt.GetCTValue(currentDatasmooth0, this.baseLineStart, this.baseLineEnd,
//							this.name.split("-")[2]);//

                    //2.根据CT值求出对应的索引值

                    //3.带入havePeakbyRange，判断算不算CT值


                    // 2.找出起峰后的最大值，
                    Double dbMaxValue = GetMaxVal(in);
                    // 3.最大减最小值的差
                    Double DValue = dbMaxValue - in.get(i);//DValue这也要变？//i也要变？

                    // 4.根据值，通道，A/B等相关与3相比较，小于3则认为有峰//这也要变
                    if (havePeakbyRange(DValue, LineTypewhole, i, ProjectType))
                        {
                        peakIndex.add(i);
                        i += 5;
                        continue;
                    }
                }

                else {
                    if (LineType.equals("ROX") && i <= 20)
                    {
                        if (!(in.get(i) + 100 < maxvalue))
                        {
                            peakIndex.add(i);
                            i += 5;
                            continue;
                        }
                    }
                    else
                    {
                        peakIndex.add(i);
                        // 判断下个峰前，要招后面的五个数，差值不能都大于20？
                        // 如果没都大于20？再找i+10以后的峰
                        // 如果有则继续
                        i += 5;
                        continue;
                    }
                }
            }
        }
        return peakIndex;
    }

    // 4.根据值，通道，A/B等相关与3相比较，小于3则认为有峰
    // 4.根据值，通道，A/B等相关与3相比较，小于3则认为有峰
    private static boolean havePeakbyRange(Double DValue, String LineTypewhole, int indexDValue,String ProjectType) {
        String[] chanleInfolist = LineTypewhole.split("-");
        boolean result = false;
        indexDValue++;
        // 呼吸道的判读CT范围限定
        if (!ProjectType.equals(QPCRProjectType.腹泻.toString())) {
            if (chanleInfolist[2].equals("FAM")) {
                if (indexDValue >= 1 && indexDValue <= 15)
                    result = DValue >= 500 ? true : false;
                else if (indexDValue >= 16 && indexDValue <= 25)
                    result = DValue >= 300 ? true : false;
                else if (indexDValue >= 26 && indexDValue <= 45)
                    result = DValue >= 100 ? true : false;
            } else {
                if (indexDValue >= 1 && indexDValue <= 15)
                    result = DValue >= 200 ? true : false;
                else if (indexDValue >= 16 && indexDValue <= 25)
                    result = DValue >= 100 ? true : false;
                else if (indexDValue >= 26 && indexDValue <= 45)
                    result = DValue >= 50 ? true : false;
            }
        } else {// 腹泻的判读CT范围限定
            switch (chanleInfolist[2]) {
                case "FAM":
                    if (indexDValue >= 1 && indexDValue <= 20)
                        result = DValue >= 600 ? true : false;
                    else if (indexDValue >= 21 && indexDValue <= 29)
                        result = DValue >= 400 ? true : false;
                    else if (indexDValue >= 30 && indexDValue <= 32)
                        result = DValue >= 300 ? true : false;
                    else if (indexDValue >= 33 && indexDValue <= 35)
                        result = DValue >= 250 ? true : false;
                    else if (indexDValue >= 36 && indexDValue <= 45)
                        result = DValue >= 200 ? true : false;
                    break;
                case "VIC":
                    if (indexDValue >= 1 && indexDValue <= 20)
                        result = DValue >= 500 ? true : false;
                    else if (indexDValue >= 21 && indexDValue <= 29)
                        result = DValue >= 200 ? true : false;
                    else if (indexDValue >= 30 && indexDValue <= 32)
                        result = DValue >= 150 ? true : false;
                    else if (indexDValue >= 33 && indexDValue <= 35)
                        result = DValue >= 150 ? true : false;
                    else if (indexDValue >= 36 && indexDValue <= 45)
                        result = DValue >= 150 ? true : false;
                    break;
                case "ROX":
                    if (indexDValue >= 1 && indexDValue <= 20)
                        result = DValue >= 500 ? true : false;
                    else if (indexDValue >= 21 && indexDValue <= 29)
                        result = DValue >= 300 ? true : false;
                    else if (indexDValue >= 30 && indexDValue <= 32)
                        result = DValue >= 200 ? true : false;
                    else if (indexDValue >= 33 && indexDValue <= 35)
                        result = DValue >= 160 ? true : false;
                    else if (indexDValue >= 36 && indexDValue <= 45)
                        result = DValue >= 120 ? true : false;
                    break;
                case "CY5":
                    if (indexDValue >= 1 && indexDValue <= 20)
                        result = DValue >= 600 ? true : false;
                    else if (indexDValue >= 21 && indexDValue <= 29)
                        result = DValue >= 300 ? true : false;
                    else if (indexDValue >= 30 && indexDValue <= 32)
                        result = DValue >= 200 ? true : false;
                    else if (indexDValue >= 33 && indexDValue <= 35)
                        result = DValue >= 160 ? true : false;
                    else if (indexDValue >= 36 && indexDValue <= 45)
                        result = DValue >= 120 ? true : false;
                    break;

            }
        }
        return result;
    }

//	private static boolean havePeakbyRange(Double DValue, String LineTypewhole,int indexDValue) {
//		String[] chanleInfolist = LineTypewhole.split("-");
//		boolean result = false;
//		indexDValue++;
//		// A
//		if (chanleInfolist[1].equals("Tube1")) {
//			switch (chanleInfolist[2]) {
//			case "FAM":
//				if (indexDValue >= 1 && indexDValue <= 20)
//					result = DValue >= 800 ? true : false;
//				else if (indexDValue >= 21 && indexDValue <= 29)
//					result = DValue >= 500 ? true : false;
//				else if (indexDValue >= 30 && indexDValue <= 32)
//					result = DValue >= 400 ? true : false;
//				else if (indexDValue >= 33 && indexDValue <= 35)
//					result = DValue >= 300 ? true : false;
//				else if (indexDValue >= 36 && indexDValue <= 45)
//					result = DValue >= 200 ? true : false;
//				break;
//			case "VIC":
//				if (indexDValue >= 1 && indexDValue <= 20)
//					result = DValue >= 150 ? true : false;
//				else if (indexDValue >= 21 && indexDValue <= 29)
//					result = DValue >= 130 ? true : false;
//				else if (indexDValue >= 30 && indexDValue <= 32)
//					result = DValue >= 130 ? true : false;
//				else if (indexDValue >= 33 && indexDValue <= 35)
//					result = DValue >= 130 ? true : false;
//				else if (indexDValue >= 36 && indexDValue <= 45)
//					result = DValue >= 120 ? true : false;
//				break;
//			case "ROX":
//				if (indexDValue >= 1 && indexDValue <= 20)
//					result = DValue >= 500 ? true : false;
//				else if (indexDValue >= 21 && indexDValue <= 29)
//					result = DValue >= 300 ? true : false;
//				else if (indexDValue >= 30 && indexDValue <= 32)
//					result = DValue >= 200 ? true : false;
//				else if (indexDValue >= 33 && indexDValue <= 35)
//					result = DValue >= 100 ? true : false;
//				else if (indexDValue >= 36 && indexDValue <= 45)
//					result = DValue >= 50 ? true : false;
//				break;
//			case "CY5":
//				if (indexDValue >= 1 && indexDValue <= 20)
//					result = DValue >= 800 ? true : false;
//				else if (indexDValue >= 21 && indexDValue <= 29)
//					result = DValue >= 500 ? true : false;
//				else if (indexDValue >= 30 && indexDValue <= 32)
//					result = DValue >= 400 ? true : false;
//				else if (indexDValue >= 33 && indexDValue <= 35)
//					result = DValue >= 300 ? true : false;
//				else if (indexDValue >= 36 && indexDValue <= 45)
//					result = DValue >= 150 ? true : false;
//				break;
//
//			}
//
//		}
//
//		// --------------------------------
//		// B
//		else {
//			switch (chanleInfolist[2]) {
//			case "FAM":
//				if (indexDValue >= 1 && indexDValue <= 20)
//					result = DValue >= 600 ? true : false;
//				else if (indexDValue >= 21 && indexDValue <= 29)
//					result = DValue >= 400 ? true : false;
//				else if (indexDValue >= 30 && indexDValue <= 32)
//					result = DValue >= 300 ? true : false;
//				else if (indexDValue >= 33 && indexDValue <= 35)
//					result = DValue >= 250 ? true : false;
//				else if (indexDValue >= 36 && indexDValue <= 45)
//					result = DValue >= 150 ? true : false;
//				break;
//			case "VIC":
//				if (indexDValue >= 1 && indexDValue <= 20)
//					result = DValue >= 150 ? true : false;
//				else if (indexDValue >= 21 && indexDValue <= 29)
//					result = DValue >= 130 ? true : false;
//				else if (indexDValue >= 30 && indexDValue <= 32)
//					result = DValue >= 130 ? true : false;
//				else if (indexDValue >= 33 && indexDValue <= 35)
//					result = DValue >= 130 ? true : false;
//				else if (indexDValue >= 36 && indexDValue <= 45)
//					result = DValue >= 120 ? true : false;
//				break;
//			case "ROX":
//				if (indexDValue >= 1 && indexDValue <= 20)
//					result = DValue >= 500 ? true : false;
//				else if (indexDValue >= 21 && indexDValue <= 29)
//					result = DValue >= 300 ? true : false;
//				else if (indexDValue >= 30 && indexDValue <= 32)
//					result = DValue >= 200 ? true : false;
//				else if (indexDValue >= 33 && indexDValue <= 35)
//					result = DValue >= 100 ? true : false;
//				else if (indexDValue >= 36 && indexDValue <= 45)
//					result = DValue >= 50 ? true : false;
//				break;
//			case "CY5":
//				if (indexDValue >= 1 && indexDValue <= 20)
//					result = DValue >= 600 ? true : false;
//				else if (indexDValue >= 21 && indexDValue <= 29)
//					result = DValue >= 300 ? true : false;
//				else if (indexDValue >= 30 && indexDValue <= 32)
//					result = DValue >= 300 ? true : false;
//				else if (indexDValue >= 33 && indexDValue <= 35)
//					result = DValue >= 150 ? true : false;
//				else if (indexDValue >= 36 && indexDValue <= 45)
//					result = DValue >= 100 ? true : false;
//				break;
//
//			}
//		}
//		return result;
//	}

    // 判断峰的值比较
    public static boolean CompareValue(Double firstNum, Double secondNum, Double thirdNum, Double fourthNum,
                                       Double fifthNum, Double sixthNum, String LineType, Double maxvalue) {
        // return true;
        Double Compare1st = secondNum - firstNum;
        Double Compare2st = thirdNum - secondNum;
        Double Compare3st = fourthNum - thirdNum;
        if (fifthNum < fourthNum) {
            fifthNum = (fourthNum + sixthNum) / 2;
        }
        Double Compare4st = fifthNum - fourthNum;
        Double Compare5st = sixthNum - fifthNum;
        if (Compare1st < 0 || Compare2st < 0 || Compare3st < 0 || Compare4st < 0) {
            return false;
        }

        int numacc = 0;
        if (Compare1st > 32) {
            numacc++;
        }
        if (Compare2st > 32) {
            numacc++;
        }
        if (Compare3st > 32) {
            numacc++;
        }
        if (Compare4st > 32) {
            numacc++;
        }
        if (Compare5st > 32) {
            numacc++;
        }
        if (numacc >= 4) {
            return true;
        }
        if (LineType.equals("ROX")) {
            if ((Compare3st >= 10 && Compare4st >= 15
                    || (Compare1st >= 2 && Compare2st >= 4 && Compare3st >= 8 && Compare4st > 10))) {
                return true;
            } else {
                return false;
            }
        }
        if (((Compare2st + 1) > Compare1st * 1.35 || (Compare2st - 1) > Compare1st * 1.35)
                && ((Compare3st + 1) > Compare2st * 1.35 || (Compare3st - 1) > Compare2st * 1.35
                || Compare3st > Compare1st * 1.8)
                && ((Compare4st + 1) > Compare3st * 1.35 || (Compare4st - 1) > Compare3st * 1.35
                || Compare4st > Compare2st * 1.8)) {
            if (Compare1st >= 2 && Compare3st >= 10 && Compare4st >= 15)

            {
                return true;
            }
            int numacc2 = 0;
            if (Compare1st >= 2) {
                numacc2++;
            }
            if (Compare2st >= 4) {
                numacc2++;
            }
            if (Compare3st >= 8) {
                numacc2++;
            }
            if (Compare4st > 15) {
                numacc2++;
            }
            if (Compare5st > 15) {
                numacc2++;
            }
            if (numacc2 >= 4) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

}