package com.ktvmi.flinkops.apicenter.EntityClass;

public class AlarmConfig {
    private String dataId;
    private String group;
    private String content;
public AlarmConfig(){}
    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getDataId() {
        return dataId;
    }

    public String getGroup() {
        return group;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
