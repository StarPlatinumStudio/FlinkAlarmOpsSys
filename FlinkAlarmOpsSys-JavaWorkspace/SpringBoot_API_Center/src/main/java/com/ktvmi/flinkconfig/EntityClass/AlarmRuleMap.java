package com.ktvmi.flinkconfig.EntityClass;
//接收新旧RuleID，NacosController/updateAlarmRule()专用实体类
public class AlarmRuleMap {
    private int jobid;
    private int oldruleid;
    private int newruleid;
    private String rulecontent;
    public AlarmRuleMap(){}
    public AlarmRuleMap(int jobid,int oldruleid,int newruleid,String rulecontent){
        this.jobid=jobid;
        this.newruleid=newruleid;
        this.oldruleid=oldruleid;
        this.rulecontent=rulecontent;
    }

    public void setJobid(int jobid) {
        this.jobid = jobid;
    }

    public int getJobid() {
        return jobid;
    }

    public int getNewruleid() {
        return newruleid;
    }

    public int getOldruleid() {
        return oldruleid;
    }

    public String getRulecontent() {
        return rulecontent;
    }

    public void setNewruleid(int newruleid) {
        this.newruleid = newruleid;
    }

    public void setOldruleid(int oldruleid) {
        this.oldruleid = oldruleid;
    }

    public void setRulecontent(String rulecontent) {
        this.rulecontent = rulecontent;
    }
}
