package Entity;

public class AlarmRule {
    public AlarmRule(){}
    public AlarmRule(int jobid,String name,int timewindow,long counts){
        this.counts=counts;
        this.jobid=jobid;
        this.name=name;
        this.timewindow=timewindow;
    }
    private int jobid;
    private String name;
    private int timewindow;
    private long counts;

    public long getCounts() {
        return counts;
    }

    public int getJobid() {
        return jobid;
    }

    public int getTimewindow() {
        return timewindow;
    }

    public String getName() {
        return name;
    }

    public void setCounts(long counts) {
        this.counts = counts;
    }

    public void setJobid(int jobid) {
        this.jobid = jobid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimewindow(int timewindow) {
        this.timewindow = timewindow;
    }
}
