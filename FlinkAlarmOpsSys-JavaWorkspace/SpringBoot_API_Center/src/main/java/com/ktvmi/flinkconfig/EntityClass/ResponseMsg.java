package com.ktvmi.flinkconfig.EntityClass;

public class ResponseMsg {

    private String msg;
    private int ret;
    public ResponseMsg(){
    }
    public ResponseMsg(String msg,int ret){
        this.msg=msg;
        this.ret=ret;
    }

    public int getRet() {
        return ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }
}
