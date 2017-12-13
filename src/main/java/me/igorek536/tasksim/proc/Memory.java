package me.igorek536.tasksim.proc;

public class Memory {
    private int baseAdderss;
    private int size;
    private int PPID;

    // Code

    public Memory() {

    }

    public Memory(int baseAdderss, int size, int ppid) {
        this.baseAdderss = baseAdderss;
        this.size = size;
        this.PPID = ppid;
    }

    // Getters/Setters

    public int getBaseAdderss() {
        return baseAdderss;
    }

    public void setBaseAdderss(int baseAdderss) {
        this.baseAdderss = baseAdderss;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPPID() {
        return PPID;
    }

    public void setPPID(int PPID) {
        this.PPID = PPID;
    }
}
