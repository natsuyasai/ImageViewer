package com.nyasai.clock;

public class StructAlarmData {
    private final short WEEK_MAX = 7;
    public  final short SUN = 0;
    public  final short MON = 1;
    public  final short TUES = 2;
    public  final short WED = 3;
    public  final short THURS = 4;
    public  final short FRI = 5;
    public  final short SAT = 6;

    public short stHH;          // 時
    public short stMM;          // 分
    public short[] stWeek;      // 曜日（0：日～6：土）
    public boolean blDo;        // 有効 or 無効

    StructAlarmData(){
        stHH = 00;
        stMM = 00;
        stWeek = new short[WEEK_MAX];
        blDo = false;
        this.InitArray();
    }

    private void InitArray(){
        for(int i=0; i<WEEK_MAX; i++){
            stWeek[i] = 0;
        }
    }

}
