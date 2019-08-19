package com.ktvmi.finkops.filter.entity;

import java.util.List;

public class Config {
    //{"id":1001,"name":"config","isZip":"-1","keys":["key1"],"hostname":"127.0.0.1","port":9000}
    //{"id":1001,"name":"缓存()","isZip":"-1","keys":["nood"],"hostname":"127.0.0.1","port":9000}
    public Config(){}
    private long id;
    private String name;
    private String isZip;
    private List<String> keys;
    private String hostname;
    private long port;
    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setIsZip(String isZip) {
        this.isZip = isZip;
    }
    public String getIsZip() {
        return isZip;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }
    public List<String> getKeys() {
        return keys;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
    public String getHostname() {
        return hostname;
    }

    public void setPort(long port) {
        this.port = port;
    }
    public long getPort() {
        return port;
    }

}