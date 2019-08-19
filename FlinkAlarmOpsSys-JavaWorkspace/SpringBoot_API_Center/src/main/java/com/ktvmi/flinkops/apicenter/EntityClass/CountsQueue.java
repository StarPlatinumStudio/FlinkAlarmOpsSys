package com.ktvmi.flinkops.apicenter.EntityClass;

import java.util.LinkedList;
import java.util.Queue;

public class CountsQueue {
    private int[] data=new int[30];
    private int size=30;
    private int queueTime=0;
    public CountsQueue(){
for(int i=0;i<size;i++)
    data[i]=0;
    }
    public void add(int e){
        for(int i = size - 2; i >=0; i --)
            data[i + 1] = data[i];
        data[0] = e;
    }

    public Boolean isAlarm(int timeWindow,int ruleCounts){

        int counts=0;
        for(int i=0;i<timeWindow;i++){
            counts+=data[i];
        }
     if(counts>=ruleCounts){
       return true;
     }
     return false;
    }

    public int getQueueTime() {
        return queueTime;
    }
    public void addTime(){
        this.queueTime++;
    }
}
