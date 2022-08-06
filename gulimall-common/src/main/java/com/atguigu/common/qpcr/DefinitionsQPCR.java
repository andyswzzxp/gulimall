package com.bohui.common.qpcr;

public class DefinitionsQPCR {
    // --------------- QPCR module  ----------------------------------------------------------- //
    public static final int   FLUOS_PER_VIAL       = 4;
    public static final int   VIALS_PER_LANE       = 2;
    public static final int   LANES_PER_CHIP 	   = 4;
    public static final int   CHIPS_PER_MODULE     = 3;
    public static final int   MODULES              = 1;

    public static final int   LINES_PER_LANE       = FLUOS_PER_VIAL * VIALS_PER_LANE;
    public static final int   LANES_PER_MODULE     = CHIPS_PER_MODULE * LANES_PER_CHIP;
    public static final int   VIALS_PER_MODULE 	   = LANES_PER_MODULE * VIALS_PER_LANE;

    public static final int   ALL_LANES            = MODULES * CHIPS_PER_MODULE * LANES_PER_CHIP;
    public static final int   ALL_LINES            = ALL_LANES * VIALS_PER_LANE * FLUOS_PER_VIAL;

    // --------------- QPCR settings keys----------------------------------------------------------- //
    public static final String CH1_SAMPLING_START_KEY =       		   "ch1SamplingStart";
    public static final String CH2_SAMPLING_START_KEY =      		   "ch2SamplingStart";
    public static final String CH3_SAMPLING_START_KEY =      		   "ch3SamplingStart";
    public static final String CH4_SAMPLING_START_KEY =      		   "ch4SamplingStart";

    public static final String CH1_SAMPLING_END_KEY =        		   "ch1SamplingEnd";
    public static final String CH2_SAMPLING_END_KEY =        		   "ch2SamplingEnd";
    public static final String CH3_SAMPLING_END_KEY =        		   "ch3SamplingEnd";
    public static final String CH4_SAMPLING_END_KEY =        		   "ch4SamplingEnd";

    // --------------- QPCR sampling settings ----------------------------------------------------------- //
    public static final int    S_AXIS_SCAN_VOLICITY = 100;
    public static final int    S_AXIS_MOVE_VOLICITY = 350;
    public static final double S_AXIS_QPCR_START_POSITION = 330;
    public static final double S_AXIS_QPCR_END_POSITION = 10;

    public static int CH1_SAMPLING_START_OFFSET = 283;
    public static int CH2_SAMPLING_START_OFFSET = 920;
    public static int CH3_SAMPLING_START_OFFSET = 1566;
    public static int CH4_SAMPLING_START_OFFSET = 2210;
    public static int CH1_SAMPLING_END_OFFSET = 2283;
    public static int CH2_SAMPLING_END_OFFSET = 2920;
    public static int CH3_SAMPLING_END_OFFSET = 3566;
    public static int CH4_SAMPLING_END_OFFSET = 4210;

    // -------------------Melt curve------------------------------------------------------------ //
    public static boolean IS_MELT_RUNNING = false;

    public static boolean MELT_AUTO_START = false;
    public static double MELT_TEMP_START = 60.0;
    public static double MELT_TEMP_END = 95.0;
    public static double MELT_TEMP_STEP = 1.0;
    public static double MELT_STEP_TIME = 15.0;

    public static final String [] requiredTags = {
            CH1_SAMPLING_START_KEY,
            CH2_SAMPLING_START_KEY,
            CH3_SAMPLING_START_KEY,
            CH4_SAMPLING_START_KEY,
            CH1_SAMPLING_END_KEY,
            CH2_SAMPLING_END_KEY,
            CH3_SAMPLING_END_KEY,
            CH4_SAMPLING_END_KEY
    };

    public static void updateQPCROffsets(Integer ch1Start, Integer ch2Start,Integer ch3Start, Integer ch4Start,
                                         Integer ch1End,	Integer ch2End, Integer ch3End, Integer ch4End) {
        CH1_SAMPLING_START_OFFSET = ch1Start;
        CH2_SAMPLING_START_OFFSET = ch2Start;
        CH3_SAMPLING_START_OFFSET = ch3Start;
        CH4_SAMPLING_START_OFFSET = ch4Start;
        CH1_SAMPLING_END_OFFSET = ch1End;
        CH2_SAMPLING_END_OFFSET = ch2End;
        CH3_SAMPLING_END_OFFSET = ch3End;
        CH4_SAMPLING_END_OFFSET = ch4End;
    }

    public static void updateMeltCurvePara(boolean startAfterPCR,Double start,double end, double step, double stepTime) {
        MELT_AUTO_START = startAfterPCR;
        MELT_TEMP_START = start;
        MELT_TEMP_END = end;
        MELT_TEMP_STEP = step;
        MELT_STEP_TIME = stepTime;
    }

    public static void setMeltRunningStatus(boolean isRunning) {
        IS_MELT_RUNNING =  isRunning;
    }
}
