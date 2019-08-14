package com.ktvmi.flinkconfig.EntityClass;

import java.util.LinkedList;
import java.util.Queue;

public class AlarmRule {
    public AlarmRule(){

    }
    public AlarmRule(int jobid, String rulecontent, int timewindow, int counts,int ruleid){
        this.counts=counts;
        this.jobid=jobid;
        this.rulecontent=rulecontent;
        this.timewindow=timewindow;
        this.ruleid=ruleid;
    }
    private int jobid;
    private String rulecontent;
    private int timewindow;
    private int counts;
    private int ruleid;

    public void setRuleid(int ruleid) {
        this.ruleid = ruleid;
    }

    public int getRuleid() {
        return ruleid;
    }

    public int getCounts() {
        return counts;
    }

    public int getJobid() {
        return jobid;
    }

    public int getTimewindow() {
        return timewindow;
    }

    public String getRulecontent() {
        return rulecontent;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public void setJobid(int jobid) {
        this.jobid = jobid;
    }

    public void setRulecontent(String rulecontent) {
        this.rulecontent = rulecontent;
    }

    public void setTimewindow(int timewindow) {
        this.timewindow = timewindow;
    }
}
