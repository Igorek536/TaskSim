package me.igorek536.tasksim;

/**
 * TaskSim - this is simple implementation of the SJFwD(SJF with displacement) scheduler algorithm.
 * .------..------..------.
 * |S.--. ||J.--. ||F.--. | Simple
 * | :/\: || :(): || :(): | Job
 * | :\/: || ()() || ()() | First
 * | '--'S|| '--'J|| '--'F|        ...with displacement
 * `------'`------'`------'
 *
 * @author igorek536
 * @version 0.1             // THIS IS EARLY BETA! USE AT YOUR OWN RISK
 */

public class TaskSim {

    public static void main(String... args) {
        new Launcher().launch();
    }
}
