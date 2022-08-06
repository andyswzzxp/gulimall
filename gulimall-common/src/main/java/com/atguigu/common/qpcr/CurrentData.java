package com.bohui.common.qpcr;

import java.util.HashMap;
import java.util.List;

//判读等一些常用状态信息，采用懒汉式单例模式
public class CurrentData {

    private static volatile CurrentData instance;
    private CurrentData() {}

    //提供一个静态的公有方法，加入双重检查代码，解决线程安全问题, 同时解决懒加载问题
    //同时保证了效率, 推荐使用

    public static synchronized CurrentData getInstance() {
        if(instance == null) {
            synchronized (CurrentData.class) {
                if(instance == null) {
                    instance = new CurrentData();
                }
            }

        }
        return instance;
    }

    public HashMap<String, SampleModel> CurrentSampleList;

    public  HashMap<String, Double> CurrentcalibrationList;

    public  boolean IsLoadSample;
    public  boolean IsFluoConnect;
    //是否是新的加样枪
    public  boolean IsdispenserNew;
    public  String filUrl;
    public  String samplefilUrl;;
    public  boolean IsTruerunning;

    public  boolean Is200Tip;
    public  double ReturnPosX;
    public  double ReturnPosY;

    public  QPCRProjectType	ProjectType;
    public  Boolean	ProjectTypeInit;

    public Double	ConcentrationValueFAM;
    public Double	ConcentrationValueVIC;
    public Double	ConcentrationValueROX;
    public Double	ConcentrationValueCY5;
    public  HashMap<Integer, PointXY> FAMPeakList;
    public  HashMap<Integer, PointXY> VICPeakList;
    public  HashMap<Integer, PointXY> ROXPeakList;
    public  HashMap<Integer, PointXY> CY5PeakList;

    public List<Double> FAMListdt;
    public  List<Double> VICListdt;
    public  List<Double> ROXListdt;
    public  List<Double> CY5Listdt;

    public  boolean IsCollectBottom;
    public  boolean  SampleConcentration;
    public int PeakListStatus;



}