package com.ktvmi.flinkops.apicenter.EntityClass;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;


@Entity
@Table(name="jobs")
@EntityListeners(AuditingEntityListener.class)
public class Job {
    @Id
    @Column(name="jobid",nullable = false)
    private long jobid;

    @Column(name="jobname",nullable = true)
    private String jobname;

    @Column(name = "rules",nullable = true)
    private String rules;
    @Column(name = "isZip",nullable = true)
    private String isZip;
    @Column(name = "hostname",nullable = true)
    private String hostname;
    @Column(name = "port",nullable = true)
    private long port;
    public Job(){}

    public Job(Long jobId,String jobName,String rules,String isZip,String hostname,long port){
        this.jobid=jobId;
        this.jobname=jobName;
        this.rules=rules;
        this.isZip=isZip;
        this.hostname=hostname;
        this.port=port;
    }

    public long getJobId() {
        return jobid;
    }

    public void setJobId(long jobId) {
        this.jobid = jobId;
    }

    public String getJobName() {
        return jobname;
    }

    public void setJobName(String jobName) {
        this.jobname = jobName;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getHostname() {
        return hostname;
    }

    public String getIsZip() {
        return isZip;
    }

    public void setIsZip(String isZip) {
        this.isZip = isZip;
    }

    public long getPort() {
        return port;
    }

    public void setPort(long port) {
        this.port = port;
    }
}
