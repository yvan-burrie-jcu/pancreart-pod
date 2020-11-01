package jcu.cp3407.pancreart;

import java.util.*;

public abstract class Task extends TimerTask {

    final ArrayList<Task> tasks = new ArrayList<>();

    final Timer timer;

    /** Specifies running period in number of seconds. */
    final long interval;

    double speed;

    Handler handler;

    Task(Timer timer, long interval, double speed) {
        this.timer = timer;
        this.interval = interval;
        this.speed = speed;
    }

    void initiate() {
        long oneSecond = (long) (speed * 1000);
        timer.schedule(this, 0, interval * oneSecond);
    }

    void registerTask(Task task) {
        tasks.add(task);
    }

    void assignHandler(Handler handler) {
        this.handler = handler;
        for (Task task : tasks) {
            task.assignHandler(handler);
        }
    }
}
