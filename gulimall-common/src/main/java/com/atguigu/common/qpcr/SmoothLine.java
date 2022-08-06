package com.bohui.common.qpcr;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SmoothLine {
    // --------------------------------smooth----------------------------------//

    public static List<Double> linearSmooth3(List<Double> in) {
        List<Double> out = new ArrayList<Double>(in);
        int N = in.size();
        int i;
        if (N < 3) {
            for (i = 0; i <= N - 1; i++) {
                out.set(i, in.get(i));
            }
        } else {
            out.set(0, (5.0 * in.get(0) + 2.0 * in.get(1) - in.get(2)) / 6.0);
            for (i = 1; i <= N - 2; i++) {
                out.set(i, (in.get(i - 1) + in.get(i) + in.get(i + 1)) / 3.0);
            }
            out.set(N - 1, (5.0 * in.get(N - 1) + 2.0 * in.get(N - 2) - in.get(N - 3)) / 6.0);
        }
        return out;
    }

    public static List<Double> linearSmooth5(List<Double> in) {
        List<Double> out = new ArrayList<Double>(in);
        int N = in.size();
        int i;
        if (N < 5) {
            for (i = 0; i <= N - 1; i++) {
                out.set(i, in.get(i));
            }
        } else {
            out.set(0, (3.0 * in.get(0) + 2.0 * in.get(1) + in.get(2) - in.get(4)) / 5.0);
            out.set(1, (4.0 * in.get(0) + 3.0 * in.get(1) + 2 * in.get(2) + in.get(3)) / 10.0);
            for (i = 2; i <= N - 3; i++) {
                out.set(i, (in.get(i - 2) + in.get(i - 1) + in.get(i) + in.get(i + 1) + in.get(i + 2)) / 5.0);
            }
            out.set(N - 2, (4.0 * in.get(N - 1) + 3.0 * in.get(N - 2) + 2 * in.get(N - 3) + in.get(N - 4)) / 10.0);
            out.set(N - 1, (3.0 * in.get(N - 1) + 2.0 * in.get(N - 2) + in.get(N - 3) - in.get(N - 5)) / 5.0);
        }
        return out;
    }


    public static List<Double> linearSmooth70(List<Double> in) {
        List<Double> out = new ArrayList<Double>(in);
        int N = in.size();
        int i;
        if (N < 7) {
            for (i = 0; i <= N - 1; i++) {
                out.set(i, in.get(i));
            }
        } else {


            int noZeroIndex=0;
            for(i = 0; i <= N - 4; i++)
            {
                if(in.get(i)==0)
                {
                    out.set(i,0.0);
                    noZeroIndex++;

                }
                else
                {
                    break;
                }
            }


//			if(noZeroIndex<20)
//			{
            double tempTotal = 0;
            double tempAvg =0;
            int nJunzhiSpan=0;
            int inivalindex=noZeroIndex;
            for(int j=inivalindex;j <= N - 4;j++)
            {
//					out.set(j,in.get(j));
                if(in.get(j)-in.get(j-1)>15||j==N - 4)
                {
                    noZeroIndex=j;
                    break;
                }
                else
                {
                    tempTotal+=in.get(j);
                }
                nJunzhiSpan++;
            }
            tempAvg = tempTotal / nJunzhiSpan;
            for(int j=inivalindex;j <noZeroIndex;j++)
            {
                out.set(j, (in.get(j)+tempAvg)/2);
            }
//				noZeroIndex=20;
//			}

            if((noZeroIndex-inivalindex>3))
            {


                for(i = 0; i <noZeroIndex; i++)
                {

                    out.set(i,0.0);

                }
            }
            else{

                if(N>noZeroIndex+6)
                {

                    out.set(noZeroIndex, (13.0 * in.get(noZeroIndex) + 10.0 * in.get(noZeroIndex+1) + 7.0 * in.get(noZeroIndex+2) + 4.0 * in.get(noZeroIndex+3) + in.get(noZeroIndex+4)
                            - 2.0 * in.get(noZeroIndex+5) - 5.0 * in.get(noZeroIndex+6)) / 28.0);
                    out.set(noZeroIndex+1,
                            (5.0 * in.get(noZeroIndex) + 4.0 * in.get(noZeroIndex+1) + 3 * in.get(noZeroIndex+2) + 2 * in.get(noZeroIndex+3) + in.get(noZeroIndex+4) - in.get(noZeroIndex+6)) / 14.0);
                    out.set(noZeroIndex+2, (7.0 * in.get(noZeroIndex) + 6.0 * in.get(noZeroIndex+1) + 5.0 * in.get(noZeroIndex+2) + 4.0 * in.get(noZeroIndex+3) + 3.0 * in.get(4)
                            + 2.0 * in.get(noZeroIndex+5) + in.get(noZeroIndex+6)) / 28.0);
                }
                else
                {
                    out.set(noZeroIndex,0.0);
                    out.set(noZeroIndex+1,0.0);
                    out.set(noZeroIndex+2,0.0);
                }
                noZeroIndex=noZeroIndex+3;
            }

            for (i = noZeroIndex; i <= N - 4; i++) {
                if(in.get(i)!=0)
                {
                    out.set(i, (in.get(i - 3) + in.get(i - 2) + in.get(i - 1) + in.get(i) + in.get(i + 1) + in.get(i + 2)
                            + in.get(i + 3)) / 7.0);
                }
                else
                {
                    out.set(i, 0.0);
                }
            }

            out.set(N - 3, (7.0 * in.get(N - 1) + 6.0 * in.get(N - 2) + 5.0 * in.get(N - 3) + 4.0 * in.get(N - 4)
                    + 3.0 * in.get(N - 5) + 2.0 * in.get(N - 6) + in.get(N - 7)) / 28.0);
            out.set(N - 2, (5.0 * in.get(N - 1) + 4.0 * in.get(N - 2) + 3.0 * in.get(N - 3) + 2.0 * in.get(N - 4)
                    + in.get(N - 5) - in.get(N - 7)) / 14.0);
            out.set(N - 1, (13.0 * in.get(N - 1) + 10.0 * in.get(N - 2) + 7.0 * in.get(N - 3) + 4 * in.get(N - 4)
                    + in.get(N - 5) - 2 * in.get(N - 6) - 5 * in.get(N - 7)) / 28.0);
        }
        for (int j = 0; j < out.size(); j++) {
            if (out.get(j) < 0) {
                out.set(j, 0.0);
            }
        }

        out = GetMinVal(out);
        return out;
    }




    public static List<Double> linearSmooth7( List<Double> in) {
        List<Double> out = new ArrayList<Double>(in);
        int N = in.size();
        int i;
        if (N < 7) {
            for (i = 0; i <= N - 1; i++) {
                out.set(i, in.get(i));
            }
        }
        else{
            out.set(0, (13.0 * in.get(0) + 10.0 * in.get(1) + 7.0 * in.get(2) + 4.0 * in.get(3) +
                    in.get(4) - 2.0 * in.get(5) - 5.0 * in.get(6)) / 28.0);
            out.set(1, (5.0 * in.get(0) + 4.0 * in.get(1) + 3 * in.get(2) + 2 * in.get(3) +
                    in.get(4) - in.get(6)) / 14.0);
            out.set(2, (7.0 * in.get(0) + 6.0 * in.get(1) + 5.0 * in.get(2) + 4.0 * in.get(3) +
                    3.0 * in.get(4) + 2.0 * in.get(5) + in.get(6)) / 28.0);
            for (i = 3; i <= N - 4; i++)
            {
                out.set(i, (in.get(i - 3) + in.get(i - 2) + in.get(i - 1) + in.get(i) + in.get(i + 1) + in.get(i + 2) + in.get(i + 3)) / 7.0);
            }

            out.set(N - 3, (7.0 * in.get(N - 1) + 6.0 * in.get(N - 2) + 5.0 * in.get(N - 3) +
                    4.0 * in.get(N - 4) + 3.0 * in.get(N - 5) + 2.0 * in.get(N - 6) + in.get(N - 7)) / 28.0);
            out.set(N - 2, (5.0 * in.get(N - 1) + 4.0 * in.get(N - 2) + 3.0 * in.get(N - 3) +
                    2.0 * in.get(N - 4) + in.get(N - 5) - in.get(N - 7)) / 14.0);
            out.set(N - 1, (13.0 * in.get(N - 1) + 10.0 * in.get(N - 2) + 7.0 * in.get(N - 3) +
                    4 * in.get(N - 4) + in.get(N - 5) - 2 * in.get(N - 6) - 5 * in.get(N - 7)) / 28.0);
        }
        return out;
    }

    public static List<Double> GetMinVal(List<Double> in) {
        double dmin = 99999;
        int minIndex = 0;
        for (int i = 3; i < in.size(); i++) {// 找出最小值
            if (dmin > in.get(i)) {
                dmin = in.get(i);
                minIndex = i;
            }
        }

        for (int j = 0; j < minIndex; j++) {
            in.set(j, dmin);

        }

        return in;
    }



    // --------------------------------log----------------------------------//

    public static List<Double> log2(List<Double> in) {
        List<Double> out = new ArrayList<Double>();
        BigDecimal base = new BigDecimal(Math.log(10));
        BigDecimal muti = new BigDecimal(1000);
        for (int i = 0; i < in.size(); i++) {
            Double y = null;
            Double y1 = null;
            try {
                BigDecimal value1 = new BigDecimal(Math.log(in.get(i)));
                y = value1.multiply(muti).doubleValue();// .divide(base, 6, BigDecimal.ROUND_HALF_UP).doubleValue();
            } catch (Exception e) {
            }

            try {
                BigDecimal value1 = new BigDecimal(Math.log(in.get(i + 1)));
                y1 = value1.multiply(muti).doubleValue();// .divide(base, 6, BigDecimal.ROUND_HALF_UP).doubleValue();
            } catch (Exception e) {
            }

            if (y1 != null && y == null) {
                y = 0.0001;
            }

            out.add(y);
        }
        return out;
    }

    // (int filterType, // 滤波类型：0矩形 1三角 2汉宁 3汉明 4布莱克曼
    // double []signalIn_out, //输入信号序列,函数执行后为输出信号序列,长度为2*nSigLen(复数)
    // int nSigLen, //信号序列的个数，2的整数幂
    // short nSmtDotNum //客户输入的平滑点数
    public static List<Double> GetFilter(int filterType, List<Double> signalIn_out, int nSigLen, int nSmtDotNum,
                                         int minIndexfinal) {
        int nFilterLen = 2 * nSmtDotNum; // 滤波器序列个数
        if (nFilterLen > nSigLen||minIndexfinal+4>nSigLen) {
            return signalIn_out;
        }
        int i = 0;
        int j = 0;
        List<Double> pDataProc = new ArrayList<Double>();
        for (i = 0; i < nSigLen; i++) {
            pDataProc.add(0.0);
        }
        i = 0;
        switch (filterType) {
            case 9: // 均值滤波

            {
                int nJunzhiSpan = nSmtDotNum / 2 + 1;// nSmtDotNum:客户输入的平滑点数
                float sum = 1.0f * nJunzhiSpan; // 权值的总和

                // tempTotal = 0;
                // nJunzhiSpan= minIndexfinal;
                for (int k = minIndexfinal + 1; k < minIndexfinal + 1 + nJunzhiSpan; k++) {
                    for (int l = minIndexfinal + 1; l <= k; l++) {
                        pDataProc.set(k, pDataProc.get(k) + signalIn_out.get(l));
                    }
                    pDataProc.set(k, pDataProc.get(k) / (k - minIndexfinal));
                }

                for (i = minIndexfinal + nJunzhiSpan+1; i < nSigLen - nJunzhiSpan; i++) {
                    for (j = i -nJunzhiSpan / 2; j <= i+nJunzhiSpan / 2; j++) {
                        pDataProc.set(i, pDataProc.get(i) + signalIn_out.get(j));
                    }
                    pDataProc.set(i, pDataProc.get(i) / (sum+1));
                }

                for (i = nSigLen - nJunzhiSpan; i < nSigLen; i++) {
                    for (j = i; j < nSigLen; j++) {
                        pDataProc.set(i, pDataProc.get(i) + signalIn_out.get(j));
                    }
                    pDataProc.set(i, pDataProc.get(i) / (nSigLen - i));
                }


                for (i = 0; i < nSigLen; i++) {
                    signalIn_out.set(i, pDataProc.get(i));
                }
            }
        }

        return signalIn_out;
    }

    public static List<Double> linearSmooth7New( List<Double> in) {
        List<Double> out = new ArrayList<Double>(in);
        int N = in.size();
        int i;
        if (N < 7) {
            for (i = 0; i <= N - 1; i++) {
                out.set(i, in.get(i));
            }
        }
        else{
            out.set(0, in.get(0));
            out.set(1, in.get(1));
            out.set(2, in.get(2));
            for (i = 3; i <= N-4 ; i++)
            {
                out.set(i, (in.get(i - 3) + in.get(i - 2) + in.get(i - 1) + in.get(i) )/ 4.0);
            }
            out.set(N - 3, (7.0 * in.get(N - 1) + 6.0 * in.get(N - 2) + 5.0 * in.get(N - 3) + 4.0 * in.get(N - 4)
                    + 3.0 * in.get(N - 5) + 2.0 * in.get(N - 6) + in.get(N - 7)) / 28.0);
            out.set(N - 2, (5.0 * in.get(N - 1) + 4.0 * in.get(N - 2) + 3.0 * in.get(N - 3) + 2.0 * in.get(N - 4)
                    + in.get(N - 5) - in.get(N - 7)) / 14.0);
            out.set(N - 1, (13.0 * in.get(N - 1) + 10.0 * in.get(N - 2) + 7.0 * in.get(N - 3) + 4 * in.get(N - 4)
                    + in.get(N - 5) - 2 * in.get(N - 6) - 5 * in.get(N - 7)) / 28.0);
        }
        return out;
    }




    /// <summary>
    /// 获取平滑和平均后的数据
    /// </summary>
    /// <param name="flag">0为先平滑，1为先平均</param>
    /// <returns></returns>
    public static List<PointXY>  GetPointListAfterSmooth(int flag, List<Double> m_XDataTime, List<Double> data, int nFiterType, int nSmoothCount,int nEndSmoothCount, int m_frequency,int minIndexfinal)
    {
        return GetGraphDataList(data, nSmoothCount,nEndSmoothCount, m_frequency, minIndexfinal) ;
    }



    /// <summary>
    /// 获取平滑和平均后的数据
    /// </summary>
    /// <param name="flag">0为先平滑，1为先平均</param>
    /// <returns></returns>
    public static List<PointXY> GetGraphDataList(List<Double> data, int nSmoothCount,int nEndSmoothCount, int m_frequency,int minIndexfinal2)
    {
        List<PointXY> retList=new ArrayList<PointXY>();
        List<PointXY> tempChannelAList=new ArrayList<PointXY>();
        int pCount = 5;    //间隔5个数取一个值，中间4个数用差量平均值填充
        int m_nDataLenth = data.size();
        if (m_nDataLenth > 0)
        {
            int m_AvgCount = 1;  //取平均数的间隔点数，默认为5个数
            List<Double> lYData = new ArrayList<Double>();
            List<Double> lYDataFilter =new ArrayList<Double>();
            for (int i = 0; i < m_nDataLenth; i++)
            {
                lYData.add(data.get(i));
            }
//            //求一次滤波
            lYDataFilter = Filter.FiltInFreqDomain(5, lYData, m_nDataLenth, nSmoothCount,nEndSmoothCount,minIndexfinal2);
            for (int i = 0; i < m_nDataLenth; i++)
            {
                PointXY pxymodel=new PointXY(i * (1.0 / 1), lYDataFilter.get(i));
                tempChannelAList.add(pxymodel);  //lYDataFilter[i]
            }
//            //间隔50个数取一个平均值点
            tempChannelAList = GetAvgSmoothDataList(tempChannelAList, m_AvgCount);
//            //在平均值中插入平滑点数
            if (tempChannelAList.size() > 0)
            {
                PointXY pxymodel=new PointXY(tempChannelAList.get(0).getX(), tempChannelAList.get(0).getY());
                retList.add(pxymodel);
                for (int i = 1; i < tempChannelAList.size(); i++)
                {
                    for (int j = 1; j < pCount; j++)
                    {
                        double x = tempChannelAList.get(i-1).getX() + (tempChannelAList.get(i).getX() -tempChannelAList.get(i-1).getX()) * j / pCount;
                        double y = tempChannelAList.get(i-1).getY() + (tempChannelAList.get(i).getY()- tempChannelAList.get(i-1).getY()) * j / pCount;
                        PointXY ptxymodel=new PointXY(x,y);
                        retList.add(ptxymodel);
                    }

                    retList.add(new PointXY(tempChannelAList.get(i).getX(), tempChannelAList.get(i).getY()));
                }
            }
        }
        return retList;
    }


    //获取最小值
    public static List<PointXY> GetminVal(List<PointXY> retList,int minIndexfinal,int m_nDataLenth ) {
        // double

        try {

            int minIndex = (minIndexfinal+1)*retList.size()/m_nDataLenth-1;
            double dmin=0;
            if(minIndex<retList.size()-3)
            {
                dmin= retList.get(minIndex).getY();

            }
            List<PointXY> retListresult=new ArrayList<PointXY>();
            {
                for (int i = 0; i < retList.size(); i++) {// 找出最小值
                    double x = retList.get(i).getX() ;
                    double y=retList.get(i).getY();
                    if(i<=minIndex)
                    {
                        y=0;
                    }
                    else
                    {
                        y = retList.get(i).getY()-dmin;
                    }

                    if(y<0)
                    {
                        y=0;
                    }
                    PointXY ptxymodel=new PointXY(x,y);
                    retListresult.add(ptxymodel);
                }
            }



            return retListresult;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return retList;

        }
    }



    public static List<PointXY> GetAvgSmoothDataList(List<PointXY> m_list, int m_SmoothPointCount)
    {
        //int m_SmoothPointCount = 10;
        int m_count = m_list.size();
        List<PointXY> tempList = new ArrayList<PointXY>();

        if (m_list.size() > 0)
        {
            PointXY pxymodel=new PointXY(m_list.get(0).getX(), m_list.get(0).getY());
            tempList.add(pxymodel);
            int i = 0;
            int k = 0;
            double tempYSum = 0;
            while (i < m_count)
            {
                tempYSum +=m_list.get(i).getY();
                i++;
                k++;
                if (i < m_count)
                {
                    if (k >= m_SmoothPointCount)
                    {
                        PointXY ptxymodel=new PointXY(m_list.get(i-1).getX(), tempYSum / k);
                        tempList.add(ptxymodel);
                        k = 0;
                        tempYSum = 0;
                    }
                }
                else
                {

                    PointXY ptxymodel=new PointXY(m_list.get(i-1).getX(), tempYSum / k);
                    tempList.add(ptxymodel);
                    k = 0;
                    tempYSum = 0;
                }
            }
        }

        return tempList;
    }



}
