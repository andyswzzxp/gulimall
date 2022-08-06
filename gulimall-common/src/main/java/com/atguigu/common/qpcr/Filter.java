package com.bohui.common.qpcr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Filter {

	 public static List<Double> FiltInFreqDomain(int nType, List<Double> pData,int nLen,int nDotNum ,int nDotNumEnd,int minIndexfinal2)
     {
         if (nLen < 1)
         {
             return pData;
         }
         if (nType < 1)
         {
             return pData;
         }
         int m_tempAddDataLength = nDotNum;   //��ʱ����������ͷ�ĵ���������Ĭ��ȡƽ������
         if (pData.size() < m_tempAddDataLength)
         {
             m_tempAddDataLength = pData.size() ;
         }
        pData.addAll(0,  pData.subList(0, m_tempAddDataLength));//��ͷ

        pData.addAll(pData.size(), pData.subList(pData.size()-m_tempAddDataLength, pData.size()));//��β
       //���￴
         nLen = pData.size();
         int nFFTLen = Get2Power(nLen);
         List<Double>lDataIn= new ArrayList<Double>();;
         Double []pDataFFT = new Double[nFFTLen * 2+1];	//�����ź����� 
         Double []pDataIntemp = new Double[nFFTLen * 2];	//�����ź����� 
       
         int i = 0;
         for (int nIndex = 0; nIndex < nFFTLen * 2; nIndex++)
         {
             lDataIn.add(0.0);
         }
         for (i = 0; i < nLen; i++)
         {
        	 lDataIn.set(2 * i, pData.get(i));
            // lDataIn.get(2 * i) = pData.[i];
         }
         double cvalue = pData.get(nLen - 1);
         for (i = nLen; i < nFFTLen; i++)
         {
             lDataIn.set(2 * i, cvalue);
         }
         lDataIn.add(0, 0.0);
         Double[] doubles=new Double[lDataIn.size()];
         for(int j=0;j<lDataIn.size();j++)
         {
             doubles[j]=lDataIn.get(j).doubleValue();
         }
         pDataFFT = four1(doubles, nFFTLen, 1);	//�����źŵ�FFT���任
         System.arraycopy(pDataFFT, 1, pDataIntemp, 0, nFFTLen * 2);//�����ݴ�pDataIn,������pDataIntemp
        pDataFFT = GetFilter(nType, pDataIntemp, nFFTLen, nDotNum);
         List<Double> lDataInFilter = new ArrayList<Double>();
         for(int k=0;k<pDataFFT.length;k++)
         {
        	 lDataInFilter.add(pDataFFT[k]);
         }
         lDataInFilter.add(0, 0.0);
         Double[] doubles2=new Double[lDataInFilter.size()];
         for(int j=0;j<lDataInFilter.size();j++)
         {
             doubles2[j]=lDataInFilter.get(j).doubleValue();
         }
         pDataFFT = four1(doubles2, nFFTLen, -1);
         System.arraycopy(pDataFFT, 1, pDataIntemp, 0, nFFTLen * 2);//�����ݴ�pDataIn,������pDataIntemp

         for (i = 0; i < nLen; i++)
         {
            
             pData.set(i,  pDataIntemp[2 * i]);
         }
//        // �˲���ϣ���ԭ������������ͷ����ʱ������ȥ��
         nLen = nLen - m_tempAddDataLength-m_tempAddDataLength;
         
       
         List<Double> m_LastDataList =  pData.subList(m_tempAddDataLength, m_tempAddDataLength+nLen);
         
        
         return m_LastDataList;
        

        // return pData;
     }
	 
	 
	 //�㷨��Դ�������顶�����źŴ���Page305
	    //������(Hanning)��������(Hamming)������������(Blackman)�˲�
	    //win_Hanning(n) = 0.5 - 0.5cos(2*PI*n/N), n = 0,1,2,...,N-1;
	    //win_Hamming(n) = 0.54 - 0.46cos(2*PI**n/N), n = 0,1,...N-1;
	    //win_Blackman(n) = 0.42 - 0.5cos(2*PI*n/N) + 0.08cos(4*PI*n/N), n = 0,1...N-1;
	    /*************************************************************************************/
	    //	U(w) = exp(jw/2)*sin(wN/2)/sin(w/2) = ctg(w/2)*sin(w*N/2) + j*sin(w*N/2);
	    //WIN_Hanning(exp(jw)) = 0.5*U(w) + 0.25*[U(w - 2*PI/N) + U(w + 2*PI/N)];
	    //WIN_Hamming(exp(jw)) = 0.54*U(w) + 0.23*U(w - 2*PI/N) + 0.23*U(w + 2*PI/N);
	    //WIN_Blackman(exp(jw)) = 0.42*U(w) + 0.25*[U(w - 2*PI/N) + U(w + 2*PI/N)] 
	    //									+ 0.04*[U(w - 4*PI/N) + U(w + 4*PI/N)];
	    //    (int filterType,				// �˲����ͣ�0���� 1���� 2���� 3���� 4��������
	    //	double []signalIn_out,		//�����ź�����,����ִ�к�Ϊ����ź�����,����Ϊ2*nSigLen(����)
	    //	int nSigLen,				//�ź����еĸ�����2��������
	    //	short nSmtDotNum			//�ͻ������ƽ������
	    /**************************************************************************************/
	    public static Double[] GetFilter(int filterType, Double[] signalIn_out, int nSigLen, int nSmtDotNum)
	    {
	    	int nFilterLen = 2 * nSmtDotNum;	//�˲������и���
	        if (nFilterLen > nSigLen)
	        {
	            return signalIn_out;
	        }
	        int i = 0;
	        int j = 0;
	        double []pDataProc= new double[nSigLen];
	        for (i = 0; i < nSigLen; i++)
	        {
	            pDataProc[i]=0;
	        }
	        i = 0;
	        double fTemp = 0.0;
	        switch (filterType)
	        {
	            case 1:		//����
	                {
	                    
	                    for (i = nSmtDotNum; i < (nSigLen - nSmtDotNum); i++)
	                    {
	                        signalIn_out[2 * i] = signalIn_out[2 * i + 1] = 0.0;
	                    }
	                    break;
	                }
	            case 2:		//triangel
	                {
	                    for (i = 0; i < nSmtDotNum; i++)
	                    {
	                        fTemp = 1.0f - 2.0f * i / nFilterLen;
	                        signalIn_out[2 * i] *= fTemp;
	                        signalIn_out[2 * i + 1] *= fTemp;
	                    }
	                    for (i = nSmtDotNum; i < nSigLen - nSmtDotNum; i++)
	                    {
	                        signalIn_out[2 * i] = signalIn_out[2 * i + 1] = 0.0;
	                    }
	                    for (i = nSigLen - nSmtDotNum; i < nSigLen; i++)
	                    {
	                        fTemp = 2.0f * (i - nSigLen + nSmtDotNum + 1) / nFilterLen;
	                        signalIn_out[2 * i] *= fTemp;
	                        signalIn_out[2 * i + 1] *= fTemp;
	                    }
	                    break;
	                }
	            case 3:		//Hannning
	                {

	                    for (i = 0; i < nSmtDotNum; i++)
	                    {
	                        fTemp = (float)(0.5f + 0.5f * Math.cos(2 * Math.PI * i / nFilterLen));
	                        signalIn_out[2 * i] *= fTemp;
	                        signalIn_out[2 * i + 1] *= fTemp;
	                    }                      
	                    for (i = nSmtDotNum; i < nSigLen - nSmtDotNum; i++)
	                    {
	                        signalIn_out[2 * i] = signalIn_out[2 * i + 1] = 0.0;
	                    }
	                    for (i = nSigLen - nSmtDotNum; i < nSigLen; i++)
	                    {// 0.5 - 0.5cos(2*PI*n/N)
	                        fTemp = (float)(0.5f - 0.5f * Math.cos(2 * Math.PI * (i - nSigLen + nSmtDotNum + 1) / nFilterLen));
	                        signalIn_out[2 * i] *= fTemp;
	                        signalIn_out[2 * i + 1] *= fTemp;
	                    }
	                    break;
	                }
	            case 4:		//Hamming
	                {
	                    for (i = 0; i < nSmtDotNum; i++)
	                    {
	                        fTemp = (float)(0.54f + 0.46f * Math.cos(2 * Math.PI * i / nFilterLen));
	                        signalIn_out[2 * i] *= fTemp;
	                        signalIn_out[2 * i + 1] *= fTemp;
	                    }
	                    for (i = nSmtDotNum; i < nSigLen - nSmtDotNum; i++)
	                    {
	                        signalIn_out[2 * i] = signalIn_out[2 * i + 1] = 0.0;
	                    }
	                    for (i = nSigLen - nSmtDotNum; i < nSigLen; i++)
	                    {
	                        fTemp = (float)(0.54f - 0.46f * Math.cos(2 * Math.PI * (i - nSigLen + nSmtDotNum + 1) / nFilterLen));
	                        signalIn_out[2 * i] *= fTemp;
	                        signalIn_out[2 * i + 1] *= fTemp;
	                    }
	                    break;
	                }
	            case 5:	//Blackman
	                {
	                    for (i = 0; i < nSmtDotNum; i++)
	                    {
	                        fTemp = (float)(0.42f + 0.5f * Math.cos(2 * Math.PI * i / nFilterLen) + 0.08f * Math.cos(4 * Math.PI * i / nFilterLen));
	                        signalIn_out[2 * i] *= fTemp;
	                        signalIn_out[2 * i + 1] *= fTemp;
	                    }
	                    for (i = nSmtDotNum; i < nSigLen - nSmtDotNum; i++)
	                    {
	                        signalIn_out[2 * i] = signalIn_out[2 * i + 1] = 0.0;
	                    }
	                    for (i = nSigLen - nSmtDotNum; i < nSigLen; i++)
	                    {
	                        fTemp = (float)(0.42f - 0.5f * Math.cos(2 * Math.PI * (i - nSigLen + nSmtDotNum + 1) / nFilterLen)
	                                            + 0.08f * Math.cos(4 * Math.PI * (i - nSigLen + nSmtDotNum + 1) / nFilterLen));
	                        signalIn_out[2 * i] *= fTemp;
	                        signalIn_out[2 * i + 1] *= fTemp;
	                    }
	                    break;
	                }
	            case 6:	//���η�
	                for (i = nSmtDotNum; i < nFilterLen; i++)
	                {

	                    fTemp = 1.0f - 2.0f * (i - nSmtDotNum) / nFilterLen;
	                    signalIn_out[2 * i] *= fTemp;
	                    signalIn_out[2 * i + 1] *= fTemp;
	                }
	                for (i = nFilterLen; i < nSigLen - nFilterLen; i++)
	                {
	                    signalIn_out[2 * i] = signalIn_out[2 * i + 1] = 0.0;
	                }
	                for (i = nSigLen - nFilterLen; i < nSigLen - nSmtDotNum; i++)
	                {
	                    fTemp = 2.0f * (i - nSigLen + nFilterLen + 1) / nFilterLen;
	                    signalIn_out[2 * i] *= fTemp;
	                    signalIn_out[2 * i + 1] *= fTemp;
	                }
	                break;
	            case 7:	//����ʽ���ƽ���˲�-ʱ��
	                {
	                    double[] Coff_Plnml = { -2.0, 3.0, 6.0, 7.0, 6.0, 3.0, -2.0 };
	                    int nWinLen = 7;	//����ʽ���ƽ�����ڿ��
	                    int nHalfWinLen = nWinLen / 2;
	                    for (i = nHalfWinLen; i < nSigLen - nHalfWinLen; i++)
	                    {
	                        for (j = -nHalfWinLen; j <= nHalfWinLen; j++)
	                        {
	                            pDataProc[i] += signalIn_out[i + j] * Coff_Plnml[j + nHalfWinLen];
	                        }
	                        pDataProc[i] /= 21.0;
	                    }
	                    for (i = 0; i < nHalfWinLen; i++)
	                    {
	                        signalIn_out[i] = pDataProc[nHalfWinLen];
	                    }
	                    for (i = nHalfWinLen; i < nSigLen - nHalfWinLen; i++)
	                    {
	                        signalIn_out[i] = pDataProc[i];
	                    }
	                    for (i = nSigLen - nHalfWinLen; i < nSigLen; i++)
	                    {
	                        signalIn_out[i] = pDataProc[nSigLen - nHalfWinLen - 1];
	                    }
	                }
	                break;
	            case 8:	//��Ȩƽ���˲�-ʱ��
	                {
	                    int nJiaQuanSpan = nSmtDotNum / 2 * 2 + 1;
	                    double sum = 0.0;
	                    for (j = 1; j <= nJiaQuanSpan / 2; j++)
	                    {
	                        sum += j;
	                    }
	                    sum = sum * 2 + nJiaQuanSpan / 2 + 1;	//Ȩֵ���ܺ�
	                    for (i = nJiaQuanSpan / 2; i < nSigLen - nJiaQuanSpan / 2; i++)
	                    {
	                        for (j = -nJiaQuanSpan / 2; j <= 0; j++)
	                        {
	                            pDataProc[i] += signalIn_out[i + j] * (j + nJiaQuanSpan / 2 + 1);
	                        }
	                        for (j = 1; j <= nJiaQuanSpan / 2; j++)
	                        {
	                            pDataProc[i] += signalIn_out[i + j] * (nJiaQuanSpan / 2 - j + 1);
	                        }
	                        pDataProc[i] /= sum;
	                    }
	                    for (i = 0; i < nJiaQuanSpan / 2; i++)
	                    {
	                        signalIn_out[i] = pDataProc[nJiaQuanSpan / 2];
	                    }
	                    for (i = nJiaQuanSpan / 2; i < nSigLen - nJiaQuanSpan / 2; i++)
	                    {
	                        signalIn_out[i] = pDataProc[i];
	                    }
	                    for (i = nSigLen - nJiaQuanSpan / 2; i < nSigLen; i++)
	                    {
	                        signalIn_out[i] = pDataProc[nSigLen - nJiaQuanSpan / 2 - 1];
	                    }
	                }
	                break;
	            case 9:		//��ֵ�˲�
	                {
	                    int nJunzhiSpan = nSmtDotNum / 2 + 1;
	                    float sum = 1.0f * nJunzhiSpan;		//Ȩֵ���ܺ�
	                    double temp = 0;
	                    double tempTotal = 0;
	                    double tempAvg = 0;
	                    //2017.9.12 zk

	                    tempTotal = 0;
	                    for (j = 0; j < nJunzhiSpan; j++)
	                    {
	                        tempTotal += signalIn_out[j];
	                    }
	                    tempAvg = tempTotal / nJunzhiSpan;
	                    for (i = 0; i < nJunzhiSpan; i++)
	                    {
	                        pDataProc[i] = tempAvg;
	                    }

	                    for (i = nJunzhiSpan; i < nSigLen - nJunzhiSpan; i++)                        
	                    {
	                        for (j = 0; j < nJunzhiSpan; j++)
	                        {
	                            pDataProc[i] += signalIn_out[i + j];
	                        }
	                        pDataProc[i] /= nJunzhiSpan;
	                    }

	                    tempTotal = 0;
	                    for (j = nSigLen - nJunzhiSpan; j < nSigLen; j++)
	                    {
	                        tempTotal += signalIn_out[j];
	                    }
	                    tempAvg = tempTotal / nJunzhiSpan;
	                    for (i = nSigLen - nJunzhiSpan; i < nSigLen; i++)
	                    {
	                        pDataProc[i] = tempAvg;
	                    }

	                    for (i = 0; i < nSigLen; i++)
	                    {
	                        signalIn_out[i] = pDataProc[i];
	                    } 
	                }
	                break;
	            case 10:		//��ֵ�˲�
	                Filter_MId(signalIn_out, nSigLen, nSmtDotNum);
	                break;
	            default:
	                break;
	        }

	        return signalIn_out;
	    }
		public static  List<Double> FiltInTimeDomain(int nType, List<Double> pData, int nLen, int nDotNum)
	    {
			List<Double> list=new ArrayList<Double>();
			list.add(1.0);
			list.add(2.0);
			list.add(3.0);
			Double[] pDataFilter = new Double[nLen];
	        pDataFilter = GetFilter(nType,(Double[]) pData.toArray(), nDotNum, nDotNum);
	        List<Double> lData = new ArrayList<Double>();
	        return lData;
	    }
		
		
		  
	    
	    private static void Filter_MId(Double[] data, int n, int nwidth)//data���˲������źţ�n��data�ĳ��ȣ�nwidth�˲����ڿ��
	    {
	        if (nwidth > n / 2)	//���ڿ�Ȳ���̫��
	            return;
	        if (0 == nwidth % 2)	//���ڿ�ȱ���Ϊ����
	        {
	            nwidth++;
	        }
	        int nStart = nwidth / 2;	//���
	        int nEnd = n - nStart;		//�յ�
	        double[] pNewData;
	        pNewData = new double[n];
	        for (int m = 0; m < n; m++)
	        {
	            pNewData[m] = data[m];
	        }
	        double[] pOrder;
	        pOrder = new double[nwidth];
	        for (int i = nStart; i < nEnd; i++)
	        {
	            for (int j = 0; j < nwidth; j++)
	            {
	                pOrder[j] = data[i - nStart + j];
	            }
	            pNewData[i] = FindMidData(pOrder, nwidth);
	        }
	        for (int m = 0; m < n; m++)
	        {
	            data[m] = pNewData[m];
	        }

	    }
	    
	    
	    private static double FindMidData(double[] data, int n)
	    {
	        double nMid = 0;
	        double tempr;
	        for (int i = 0; i < n - 1; i++)
	            for (int j = 0; j < n - 1 - i; j++)
	            {
	                if (data[j] > data[j + 1])
	                {
	                    tempr = data[j];
	                    data[j] = data[j + 1];
	                    data[j + 1] = tempr;
	                }
	            }
	        nMid = data[n / 2];
	        return nMid;
	    }
		
	 
	 
	 public static int Get2Power(int n)
     {
         boolean m_IsHaveMod = false;	//�Ƿ�������
         int m_nDividecnt = 0;	//��2�����Ĵ���
         int power = 1;
         int m = n;
         while (m > 1)
         {
             if (m % 2 != 0)
             {
                 m_IsHaveMod = true;
                 break;
             }
             m = m / 2;
         }

         m = n;
         while (m != 0)
         {
             m = m / 2;
             m_nDividecnt++;
         }
         for (int i = 0; i < m_nDividecnt; i++)
         {
             power *= 2;
         }

         if (!m_IsHaveMod)
             power /= 2;
         return power;
     }
	 
	 
	 
	 private static Double[] four1(Double[] data, int nn, int isign)	//nn������2��������
     {
         int n, mmax, m, j, istep, i;
         double wtemp, wr, wpr, wpi, wi, theta;		//���ǵݹ�ľ���
         float tempr, tempi;

         n = nn << 1;
         j = 1;
         for (i = 1; i < n; i += 2)	//�任˳��
         {
             if (j > i)
             {
                // SWAP(ref data[j], ref data[i]);			//ʵ��
                 Double tempr1 = 0.0;
                 tempr1 = data[j];
                 data[j] = (data[i]);
                 (data[i]) = (tempr1);
              //   SWAP(ref data[j + 1], ref data[i + 1]);		//�鲿
                 Double tempr2 = 0.0;
                 tempr2 = data[j + 1];
                 data[j + 1] = (data[i + 1]);
                 (data[i + 1]) = (tempr2);
             }
             m = nn;
             while (m >= 2 && j > m)
             {
                 j -= m;
                 m >>= 1;
             }
             j += m;
         }

         //�����ǿ�ʼ�ǳ����Danielson-Lanczos����
         mmax = 2;
         while (n > mmax)				//ִ��log2(nn)��ѭ��
         {
             istep = mmax << 1;
             theta = isign * (2 * Math.PI / mmax);	//���ǵݹ�ĳ�ʼ��ֵ
             wtemp = Math.sin(0.5 * theta);
             wpr = -2.0 * wtemp * wtemp;
             wpi = Math.sin(theta);
             wr = 1.0;
             wi = 0.0;
             for (m = 1; m < mmax; m += 2)	//����Ƕ��ѭ��
             {
                 for (i = m; i <= n; i += istep)
                 {
                     j = i + mmax;
                     //Danielson-Lanczos��ʽ
                     tempr = (float)(wr * data[j] - wi * data[j + 1]);
                     tempi = (float)(wr * data[j + 1] + wi * data[j]);
                     data[j] = data[i] - tempr;
                     data[j + 1] = data[i + 1] - tempi;
                     data[i] += tempr;
                     data[i + 1] += tempi;
                 }
                 wr = (wtemp = wr) * wpr - wi * wpi + wr;	//���ǵݹ�
                 wi = wi * wpr + wtemp * wpi + wi;
             }
             mmax = istep;
         }
         if (-1 == isign)
         {
        	//���￴
             int s;
             for (s = 1; s < n; s += 2)
             {
                 data[s] /= nn;
                 data[s + 1] /= nn;
             }
         }
         return data;
     }
	 
	 
	

}
