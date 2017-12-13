package me.igorek536.tasksim.proc;

public class Process implements Comparable<Process> {

    private int id;
    private String name;
    private ProcStat status;
    private ProcRes resource;
    private int memory;
    private int estimatedTime;
    private int realTime;
    private int currentTime = 0;

    // Code



    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProcStat getStatus() {
        return status;
    }

    public void setStatus(ProcStat status) {
        this.status = status;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public int getRealTime() {
        return realTime;
    }

    public void setRealTime(int realTime) {
        this.realTime = realTime;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ProcRes getResource() {
        return resource;
    }

    public void setResource(ProcRes resource) {
        this.resource = resource;
    }

    @Override
    public int compareTo(Process process) {
        Integer ETM1 = process.getEstimatedTime();
        Integer ETM2 = this.getEstimatedTime();
        return ETM2.compareTo(ETM1);
    }
}
