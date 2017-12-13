package me.igorek536.tasksim.proc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("FieldCanBeLocal")
public class Scheduler {

    private final int minprocs = 10, maxprocs = 30;
    private final SecureRandom rand = new SecureRandom();
    private final ProcGen gen = new ProcGen();
    private final QueueCPU queueCPU = new QueueCPU();
    private final QueueR queueR = new QueueR();
    private Timer procTimer;
    private Logger logger = LoggerFactory.getLogger("Scheduler");
    private int xIndexCPU = 0;
    private MemoryManager manager = new MemoryManager();

    private static float waitingTime = 0;

    public Scheduler() {
        procTimer();
    }

    public void schedule() {
        for (int i = 0; i <= minprocs + rand.nextInt(maxprocs - minprocs); i++) {
            queueCPU.addProc(gen.genProc(), ProcStat.W);
        }
    }



    private void procTimer() {
        procTimer = new Timer();
        procTimer.schedule(new TimerTask() {
            @SuppressWarnings("UnnecessaryLocalVariable")
            @Override
            public void run() {
                queueCPU.memorySort(ProcStat.W);

                // Memory allocation
                for (int i = 0; i < queueCPU.getProcs(ProcStat.W).size(); i++) {
                    Process pr = queueCPU.getProcs(ProcStat.W).get(i);
                    if (pr.getResource() != ProcRes.CPU) continue;
                    if (pr.getStatus() != ProcStat.A && manager.addToMemory(pr)) {
                        queueCPU.changeStatus(pr, ProcStat.W, ProcStat.X);
                    } else {
                        queueCPU.changeStatus(pr, ProcStat.W, ProcStat.A);
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) {
                    }
                }

                if (xIndexCPU < queueCPU.getProcs(ProcStat.X).size()) {
                    xIndexCPU = xIndexCPU < queueCPU.getProcs(ProcStat.X).size() ? xIndexCPU++ : 0;
                    Process pr = Collections.min(queueCPU.getProcs(ProcStat.X));
                    if (pr.getCurrentTime() != pr.getRealTime()) {
                        pr.setCurrentTime(pr.getCurrentTime() + 1);
                    } else {
                        pr.setCurrentTime(0);
                        queueR.addResProc(pr);
                        queueCPU.removeProc(pr, ProcStat.X);
                    }
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException ignored) {
                    }
                }

                for (Process pr : queueR.getProcRes()) {
                    int resTime = pr.getEstimatedTime() > pr.getRealTime() ?
                            pr.getEstimatedTime() : pr.getEstimatedTime() + pr.getRealTime();
                    resTime = resTime > 0 ? -1 + resTime + rand.nextInt(4 - (-1)) : 1;
                    if (pr.getCurrentTime() <= resTime) {
                        pr.setCurrentTime(pr.getCurrentTime() + 1);
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException ignored) {
                        }
                    } else {
                        queueR.setRelResProc(pr);
                        manager.removeProc(pr);
                    }
                }

                int prCount = queueCPU.getProcs(ProcStat.X).size();
                for (Process pr : queueCPU.getProcs(ProcStat.X)) {
                    waitingTime = waitingTime + pr.getRealTime();
                }
                waitingTime = waitingTime / prCount == 0 ? 1 : prCount;

            }
        }, 0, 100);
    }

    public float getWaitingTime() {
        return waitingTime;
    }

    private void killProcTimer() {
        procTimer.cancel();
        procTimer = null;
    }
}
