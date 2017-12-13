package me.igorek536.tasksim.gui;

import me.igorek536.tasksim.proc.*;
import me.igorek536.tasksim.proc.Process;
import me.igorek536.tasksim.utils.GuiUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("FieldCanBeLocal")
public class MainFrame extends JFrame implements GuiFrame {

    // Changable vars

    private final int width = 1205, height = 675;
    private final int tableHeight = 300, tableWidth = 400;
    private final String laf = "Nimbus";
    private boolean autogen;
    private final Logger logger = LoggerFactory.getLogger("MainFrame");

    // Components vars

    private final JTable queueTable;
    private final JTable workTable;
    private final JTable readyTable;
    private final JTable errorTable;
    private final JButton autoGenBtn;
    private final JButton genBtn;
    private final JScrollPane queueTableS;
    private final JScrollPane workTableS;
    private final JScrollPane readyTableS;
    private final JScrollPane errorTableS;
    private final JProgressBar memoryProgress;
    private final GridBagConstraints
            queueTableC = new GridBagConstraints(),
            workTableC = new GridBagConstraints(),
            readyTableC = new GridBagConstraints(),
            errorTableC = new GridBagConstraints(),
            autoGenBtnC = new GridBagConstraints(),
            genBtnC = new GridBagConstraints(),
            memoryProgressC = new GridBagConstraints();
    // Other vars

    private SimpleTableModel
            queueData = new SimpleTableModel(),
            workData = new SimpleTableModel(),
            readyData = new SimpleTableModel(),
            errorData = new SimpleTableModel();
    private Scheduler scheduler = new Scheduler();
    private Timer updateTimer, autoGenTimer;
    private QueueCPU queueCPU = new QueueCPU();
    private QueueR queueR = new QueueR();
    private MemoryManager manager = new MemoryManager();
    private int wtime = 0, relCount = 0;

    // Constraits initialization

    {
        // QueueTable                 // (rus)
        queueTableC.gridx      = 0;   // Положение по X
        queueTableC.gridy      = 0;   // Положение по Y
        queueTableC.gridwidth  = 2;   // Сколько занимает ячеек по X
        queueTableC.gridheight = 5;   // Сколько занимает ячеек по Y
        queueTableC.weightx    = 0;   // На сколько % растягивается по X
        queueTableC.weighty    = 0;   // На сколько % растягивается по Y
        queueTableC.ipadx      = 0;   // Сколько пикселей добавлять по X
        queueTableC.ipady      = 265; // Сколько пикселей добавлять по Y
        queueTableC.anchor     = GridBagConstraints.NORTH;    // Якорь
        queueTableC.fill       = GridBagConstraints.VERTICAL; // Как компонент будет заполнять собою пространство
        queueTableC.insets = new Insets(0, 0, 0, 0); // top, left, bottom, right - отступы

        // WorkTable
        workTableC.gridx      = 2;
        workTableC.gridy      = 0;
        workTableC.gridwidth  = 2;
        workTableC.gridheight = 8;
        workTableC.weightx    = 0;
        workTableC.weighty    = 0.1;
        workTableC.anchor     = GridBagConstraints.NORTH;
        workTableC.fill       = GridBagConstraints.VERTICAL;
        workTableC.insets = new Insets(0, 0, 0, 0);

        // ReadyTable
        readyTableC.gridx      = 4;
        readyTableC.gridy      = 0;
        readyTableC.gridwidth  = 2;
        readyTableC.gridheight = 4;
        readyTableC.weightx    = 0;
        readyTableC.weighty    = 0;
        readyTableC.anchor     = GridBagConstraints.NORTH;
        readyTableC.fill       = GridBagConstraints.BOTH;
        readyTableC.insets = new Insets(0, 0, 0, 0);

        // ErrorTable
        errorTableC.gridx      = 4;
        errorTableC.gridy      = 4;
        errorTableC.gridwidth  = 2;
        errorTableC.gridheight = 4;
        errorTableC.weightx    = 0;
        errorTableC.weighty    = 0;
        errorTableC.anchor     = GridBagConstraints.NORTH;
        errorTableC.fill       = GridBagConstraints.VERTICAL;
        errorTableC.insets = new Insets(0, 0, 0, 0);

        // AutoGenButton
        autoGenBtnC.gridx      = 0;
        autoGenBtnC.gridy      = 6;
        autoGenBtnC.gridwidth  = 2;
        autoGenBtnC.gridheight = 1;
        autoGenBtnC.weightx    = 0;
        autoGenBtnC.weighty    = 0;
        autoGenBtnC.anchor     = GridBagConstraints.SOUTH;
        autoGenBtnC.fill       = GridBagConstraints.HORIZONTAL;
        autoGenBtnC.insets = new Insets(0, 0, 0, 0);

        // GenButton
        genBtnC.gridx      = 0;
        genBtnC.gridy      = 7;
        genBtnC.gridwidth  = 2;
        genBtnC.gridheight = 1;
        genBtnC.weightx    = 0;
        genBtnC.weighty    = 0;
        genBtnC.anchor     = GridBagConstraints.NORTHWEST;
        genBtnC.fill       = GridBagConstraints.HORIZONTAL;
        genBtnC.insets = new Insets(0, 0, 0, 0);

        // MemoryProgress
        memoryProgressC.gridx      = 0;
        memoryProgressC.gridy      = 8;
        memoryProgressC.gridwidth  = 6;
        memoryProgressC.gridheight = 1;
        memoryProgressC.weightx    = 0.1;
        memoryProgressC.weighty    = 0;
        memoryProgressC.ipadx      = 0;
        memoryProgressC.ipady      = 0;
        memoryProgressC.anchor     = GridBagConstraints.SOUTH;
        memoryProgressC.fill       = GridBagConstraints.HORIZONTAL;
        memoryProgressC.insets = new Insets(0, 0, 0, 0);
    }

    // Components initialization

    {
        String[] cols = {
        /*0*/   "PID",  // Process ID
        /*1*/   "NME",  // Process name
        /*2*/   "MEM",  // Required memory
        /*3*/   "ETM",  // Estimated time
        /*4*/   "RTM",  // Real time
        /*5*/   "CTM",  // Current time
        /*6*/   "STT",  // Process status(X - execution, R - ready, W - waiting, A - aborted)
        /*7*/   "RES"   // Resource
        };
        // DataModels

        // QueueData
        queueData.setCols(cols);

        // WorkData
        workData.setCols(cols);

        // ReadyData
        readyData.setCols(cols);

        // ErrorData
        errorData.setCols(cols);

        // Components

        queueTable = new JTable(queueData);
        workTable = new JTable(workData);
        readyTable = new JTable(readyData);
        errorTable = new JTable(errorData);
        GuiUtils.setJtableColWidth(queueTable,
                new int[]{0, 42}, new int[]{1, 100},
                new int[]{2, 55}, new int[]{3, 35},
                new int[]{4, 35}, new int[]{5, 35},
                new int[]{6, 35}, new int[]{7, 35});
        GuiUtils.setJtableColWidth(workTable,
                new int[]{0, 42}, new int[]{1, 100},
                new int[]{2, 55}, new int[]{3, 35},
                new int[]{4, 35}, new int[]{5, 35},
                new int[]{6, 35}, new int[]{7, 35});
        GuiUtils.setJtableColWidth(readyTable,
                new int[]{0, 42}, new int[]{1, 100},
                new int[]{2, 55}, new int[]{3, 35},
                new int[]{4, 35}, new int[]{5, 35},
                new int[]{6, 35}, new int[]{7, 35});
        GuiUtils.setJtableColWidth(errorTable,
                new int[]{0, 42}, new int[]{1, 100},
                new int[]{2, 55}, new int[]{3, 35},
                new int[]{4, 35}, new int[]{5, 35},
                new int[]{6, 35}, new int[]{7, 35});

        autoGenBtn = new JButton("AUTOGEN");
        genBtn = new JButton("GENERATE");
        queueTableS = new JScrollPane(queueTable);
        workTableS = new JScrollPane(workTable);
        readyTableS = new JScrollPane(readyTable);
        errorTableS = new JScrollPane(errorTable);
        memoryProgress = new JProgressBar();

        // QueueTableScroll
        queueTableS.setMinimumSize(new Dimension(tableWidth, tableHeight));

        // WorkTableScroll
        workTableS.setMinimumSize(new Dimension(tableWidth, tableHeight));

        // ReadyTableScroll
        readyTableS.setMinimumSize(new Dimension(tableWidth, tableHeight));

        // ErrorTableScroll
        errorTableS.setMinimumSize(new Dimension(tableWidth, tableHeight));

        // GenButton
        genBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                scheduler.schedule();
            }
        });

        // AutoGenButton
        Color autogenColor = autoGenBtn.getForeground();
        autoGenBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (autogen) {
                    autoGenBtn.setForeground(autogenColor);
                    killAutoGenTimer();
                    autogen = false;
                } else if (!autogen){
                    autoGenBtn.setForeground(new Color(255, 5, 0));
                    autoGenTimer();
                    autogen = true;
                }
            }
        });

        // LAF

        GuiUtils.setlaf(laf);
        GuiUtils.updateComponentsUi(this, queueTable, workTable, readyTable, errorTable,
                autoGenBtn, genBtn, queueTableS, workTableS, readyTableS, errorTableS, memoryProgress);
    }

    public MainFrame() {
        this.setSize(width, height);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new GridBagLayout());
        this.setVisible(true);

        updateTimer();
    }

    @Override
    public void init() {
        this.add(queueTableS, queueTableC);
        this.add(workTableS, workTableC);
        this.add(readyTableS, readyTableC);
        this.add(errorTableS, errorTableC);
        this.add(autoGenBtn, autoGenBtnC);
        this.add(genBtn, genBtnC);
        this.add(memoryProgress, memoryProgressC);
        memoryProgress.setMinimum(0);
        memoryProgress.setMaximum(manager.getTotalMem());
        memoryProgress.setStringPainted(true);
    }

    private String[] getProcRow(Process process) {
        return new String[]{
                String.valueOf(process.getId()),
                process.getName(),
                String.valueOf(process.getMemory()) + "KB",
                String.valueOf(process.getEstimatedTime()),
                String.valueOf(process.getRealTime()),
                String.valueOf(process.getCurrentTime()),
                String.valueOf(process.getStatus()),
                String.valueOf(process.getResource())
        };
    }

    private void autoGenTimer() {
        autoGenTimer = new Timer();
        autoGenTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                scheduler.schedule();
            }
        }, 0, 20000);
    }

    private void killAutoGenTimer() {
        try {
            autoGenTimer.cancel();
            autoGenTimer = null;
        } catch (Exception ignored) {

        }
    }

    private void updateTitle(String title) {
        this.setTitle(title);
    }

    private void updateTimer() {
        logger.debug("Update timer");
        updateTimer = new Timer();
        updateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(new Runnable() {
                    // 0   "PID"   // Process ID
                    // 1   "NME"   // Process name
                    // 2   "MEM"   // Required memory
                    // 3   "ETM"   // Estimated time
                    // 4   "RTM"   // Real time
                    // 5   "CTM"   // Current time
                    // 6   "STT"   // Process status(X - execution, R - ready, W - waiting, A - aborted)
                    // 7   "RES"   // Resource
                    @Override
                    public void run() {
                        for (int i = 0; i < queueCPU.getProcs(ProcStat.W).size(); i++) {
                            Process process = queueCPU.getProcs(ProcStat.W).get(i);
                            String pid = String.valueOf(process.getId());
                            if (!queueData.isInData(pid)) {
                                queueData.addRow(getProcRow(process));
                            }
                        }

                        for (int i = 0; i < queueCPU.getProcs(ProcStat.X).size(); i++) {
                            Process process = queueCPU.getProcs(ProcStat.X).get(i);
                            String pid = String.valueOf(process.getId());
                            if (!workData.isInData(pid)) {
                                workData.addRow(getProcRow(process));
                            } else {
                                workData.changeRow(workData.searchRowId(pid), getProcRow(process));
                            }
                            if (queueData.isInData(pid)) {
                                queueData.removeRow(queueData.searchRowId(pid));
                            }
                        }


                        for (Process process : queueR.getProcRes()) {
                            String pid = String.valueOf(process.getId());
                            try {
                                workData.changeRow(workData.searchRowId(pid), getProcRow(process));
                                if (process.getStatus() == ProcStat.R) {
                                    if (!readyData.isInData(pid)) {
                                        readyData.addRow(getProcRow(process));
                                        relCount++;
                                    } else {
                                        readyData.changeRow(readyData.searchRowId(pid), getProcRow(process));
                                    }
                                    if (workData.isInData(pid)) {
                                        workData.removeRow(workData.searchRowId(pid));
                                    }
                                    wtime = wtime + process.getRealTime();
                                }
                            } catch (Exception ignored) {}
                        }

                        for (int i = 0; i < queueCPU.getProcs(ProcStat.A).size(); i++) {
                            Process process = queueCPU.getProcs(ProcStat.A).get(i);
                            String pid = String.valueOf(process.getId());
                            if (!errorData.isInData(pid)) errorData.addRow(getProcRow(process));
                            if (queueData.isInData(pid)) {
                                queueData.removeRow(queueData.searchRowId(pid));
                            }
                        }
                        // MemoryProgress
                        memoryProgress.setValue(manager.getBusyMem());
                        memoryProgress.setString(String.valueOf(manager.getBusyMem()) +
                                "KB/" + String.valueOf(manager.getTotalMem()) + "KB");
                        updateTitle("Average waiting time: " + String.valueOf(scheduler.getWaitingTime() + " tacts"));
                    }
                });
            }
        }, 0, 100);
    }
}
