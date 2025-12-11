package utils;

public class ThreadManager implements Runnable {
    private final String taskName;
    public ThreadManager(String taskName) { this.taskName = taskName; }
    @Override
    public void run() {
        System.out.println("Démarrage tâche: " + taskName);
        try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
        System.out.println("Fin tâche: " + taskName);
    }
}
