package me.igorek536.tasksim.proc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MemoryManager {
    private static List<Memory> memories = new ArrayList<>();

    private static int totalMem = 4096;

    private final Logger logger = LoggerFactory.getLogger("MemoryManager");

    private void addressSort() {
        memories.sort(Comparator.comparingInt(Memory::getBaseAdderss));
    }

    public int getBusyMem() {
        int result = 0;
        for (Memory m : memories) {
            result = result + m.getSize();
        }
        return result;
    }

    public boolean addToMemory(Process process) {
        int taskSize = process.getMemory();
        int processId = process.getId();

        if (memories.size() == 0) {
            if (taskSize <= totalMem) {
                memories.add(new Memory(0, taskSize, processId));
                return true; // Выделили память в нуле
            } else {
                return false;
            }
        }

        // Ищем свободные участки:
        for (int i = 0; i < memories.size() - 1; i++) {
            Memory current = memories.get(i);
            Memory next = memories.get(i + 1);

            if ((next.getBaseAdderss() - (current.getBaseAdderss() + current.getSize())) >= taskSize) {
                memories.add(new Memory(current.getBaseAdderss() + current.getSize(), taskSize, processId));
                addressSort(); // Сортируем, дабы дескрипторы шли по порядку возрастания базовых адресов
                return true;
            }
        }

        // Свободных участков нет, пробуем конец:
        Memory last = memories.get(memories.size() - 1);
        if ((totalMem - (last.getBaseAdderss() + last.getSize())) >= taskSize) {
            memories.add(new Memory(last.getBaseAdderss() + last.getSize(), taskSize, processId));
            addressSort();
            return true; // Добавили в конец
        }
        return false; // Места не нашлось
    }

    public void removeProc(Process process) {
        for (int i = 0; i < memories.size(); i++) {
            if (memories.get(i).getPPID() == process.getId()) {
                Memory m = memories.get(i);
                memories.remove(m);
            }
        }
    }

    public int getTotalMem() {
        return totalMem;
    }
}