package me.igorek536.tasksim.proc;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class QueueR {
    private static List<Process> procRes = new ArrayList<>();

    private final SecureRandom rand = new SecureRandom();

    private ProcRes getRandomRes() {
        int r = rand.nextInt(3);
        switch (r) {
            case 0: return ProcRes.RS1;
            case 1: return ProcRes.RS2;
            case 2: return ProcRes.RS3;
            default: return ProcRes.RS1;
        }
    }

    public void addResProc(Process process) {
        process.setResource(getRandomRes());
        procRes.add(process);
    }

    public List<Process> getProcRes() {
        return procRes;
    }

    public void setRelResProc(Process process) {
        int id = procRes.indexOf(process);
        procRes.get(id).setStatus(ProcStat.R);
    }
}