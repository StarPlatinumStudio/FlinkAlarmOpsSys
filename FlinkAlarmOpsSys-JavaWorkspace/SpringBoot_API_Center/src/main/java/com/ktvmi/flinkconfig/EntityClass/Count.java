package com.ktvmi.flinkconfig.EntityClass;

public class Count {
    public Count(){}
    private int jobId;
    private String content;
    private int counts;
    private int flinkTime;

    public int getFlinkTime() {
        return flinkTime;
    }

    public void setFlinkTime(int flinkTime) {
        this.flinkTime = flinkTime;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public int getCounts() {
        return counts;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
