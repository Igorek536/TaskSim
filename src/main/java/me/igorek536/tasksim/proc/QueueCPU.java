package me.igorek536.tasksim.proc;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class QueueCPU {

    private static List<Process> queueProcs = new ArrayList<>();
    private static List<Process> workProcs = new ArrayList<>();
    private static List<Process> readyProcs = new ArrayList<>();
    private static List<Process> errorProcs = new ArrayList<>();

    public List<Process> getProcs(ProcStat status) {
        switch (status) {
            case W: return queueProcs;
            case X: return workProcs;
            case R: return readyProcs;
            case A: return errorProcs;
            default: return null;
        }
    }

    public void changeStatus(Process process, ProcStat oldstat, ProcStat newstat) {
        removeProc(process, oldstat);
        addProc(process, newstat);
    }

    public void addProc(Process process, ProcStat status) {
        process.setStatus(status);
        switch (status) {
            case W: queueProcs.add(process); break;
            case X: workProcs.add(process); break;
            case R: readyProcs.add(process); break;
            case A: errorProcs.add(process); break;
        }
    }

    @SuppressWarnings("ConstantConditions")
    public int getSize(ProcStat status) {
        return getProcs(status).size();
    }

    public void removeProc(Process process, ProcStat status) {
        switch (status) {
            case W: queueProcs.remove(process); break;
            case X: workProcs.remove(process); break;
            case R: readyProcs.remove(process); break;
            case A: errorProcs.remove(process); break;
        }
    }

    private int getProcListId(int pid, ProcStat status) {
        switch (status) {
            case W:
                for (Process pr : queueProcs) {
                    if (pr.getId() == pid) return queueProcs.indexOf(pr);
                }
            case X:
                for (Process pr : workProcs) {
                    if (pr.getId() == pid) return queueProcs.indexOf(pr);
                }
            case R:
                for (Process pr : readyProcs) {
                    if (pr.getId() == pid) return queueProcs.indexOf(pr);
                }
            case A:
                for (Process pr : errorProcs) {
                    if (pr.getId() == pid) return queueProcs.indexOf(pr);
                }
            default: return -1;
        }
    }

    public Process getProc(int pid, ProcStat status) {
        switch (status) {
            case W: return queueProcs.get(getProcListId(pid, status));
            case X: return workProcs.get(getProcListId(pid, status));
            case R: return readyProcs.get(getProcListId(pid, status));
            case A: return errorProcs.get(getProcListId(pid, status));
            default: return null;
        }
    }

    public void setProc(int pid, Process proc, ProcStat status) {

    }

    @SuppressWarnings("ConstantConditions")
    public void memorySort(ProcStat status) {
        getProcs(status).sort(new Comparator<Process>() {
            @Override
            public int compare(Process process, Process t1) {
                Integer MEM1 = process.getMemory(), MEM2 = t1.getMemory();
                return MEM2.compareTo(MEM1);
            }
        });
    }

    @SuppressWarnings("ConstantConditions")
    public void etmSort(ProcStat status) {
        getProcs(status).sort(new Comparator<Process>() {
            @Override
            public int compare(Process process, Process t1) {
                Integer ETM1 = process.getEstimatedTime(), ETM2 = t1.getEstimatedTime();
                return ETM1.compareTo(ETM2);
            }
        });
    }
}
