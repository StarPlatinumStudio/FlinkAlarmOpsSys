package com.ktvmi.flinkconfig.EntityClass;
//接收新旧RuleID，NacosController/updateAlarmRule()专用实体类
public class AlarmRuleMap {
    private int jobid;
    private String oldrule;
    private int newruleid;
    private String rulecontent;
    public AlarmRuleMap(){}
    public AlarmRuleMap(int jobid,String oldrule,int newruleid,String rulecontent){
        this.jobid=jobid;
        this.newruleid=newruleid;
        this.oldrule=oldrule;
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


    public String getRulecontent() {
        return rulecontent;
    }

    public void setNewruleid(int newruleid) {
        this.newruleid = newruleid;
    }


    public void setRulecontent(String rulecontent) {
        this.rulecontent = rulecontent;
    }

    public String getOldrule() {
        return oldrule;
    }

    public void setOldrule(String oldrule) {
        this.oldrule = oldrule;
    }
}
