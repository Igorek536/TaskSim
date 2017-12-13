package me.igorek536.tasksim.proc;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ProcGen {

    private static List<Integer> ids = Collections.synchronizedList(new CopyOnWriteArrayList<>());
    private final SecureRandom rand = new SecureRandom();

    private int genId() {
        int id = 1000 + rand.nextInt(9999 - 1000);
        ids.add(id);
        return id;
    }

    public Process genProc() {

        int id = genId();
        for (Integer i : ids) {
            while (i == id) {
                id = genId();
            }
        }

        int ETM = 1 + rand.nextInt(10 - 1), RTM = ETM +  (-1 + rand.nextInt(1 - (-1)));

        Process process = new Process();
        process.setStatus(ProcStat.W);
        process.setId(id);
        process.setName("proc" + String.valueOf(1 + rand.nextInt(99999 - 1)));
        process.setMemory(50 + rand.nextInt(999 - 50));
        process.setEstimatedTime(ETM);
        process.setRealTime(RTM);
        process.setResource(ProcRes.CPU);
        return process;
    }
}
