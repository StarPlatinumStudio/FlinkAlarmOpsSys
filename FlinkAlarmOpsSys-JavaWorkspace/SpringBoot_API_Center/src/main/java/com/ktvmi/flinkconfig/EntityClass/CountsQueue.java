package com.ktvmi.flinkconfig.EntityClass;

import java.util.LinkedList;
import java.util.Queue;

public class CountsQueue {
    private Queue<Integer> queue=new LinkedList<>();
    private int queueTime=0;
    public CountsQueue(){
        int i=0;
        while (i<30)
        {
            queue.offer(0);
            i++;
        }
    }

    public void pushCount(int counts){
        queue.poll();
        queue.offer(counts);
    }
    public Boolean isAlarm(){
        return false;
    }

    public int getQueueTime() {
        return queueTime;
    }
    public void addTime(){
        this.queueTime++;
    }
}
