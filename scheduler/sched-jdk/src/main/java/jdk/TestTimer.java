package jdk;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TestTimer {
    public static void main(String[] args) {
        Timer timer = new Timer();
        TimerTask task = new TestTimerTask();
        timer.schedule(task, 5000L, 1000L);
    }

    private static class TestTimerTask extends TimerTask {
        /**
         * 此计时器任务要执行的操作。
         */
        public void run() {
            Date executeTime = new Date(this.scheduledExecutionTime());
            String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            System.out.println("任务执行了：" + dateStr);
        }
    }
}
