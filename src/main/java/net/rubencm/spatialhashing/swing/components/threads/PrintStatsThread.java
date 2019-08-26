package net.rubencm.spatialhashing.swing.components.threads;

public class PrintStatsThread extends Thread {

    /**
     * Display stats by console each 5 seconds
     */
    final int displayTime = 5000;

    @Override
    public void run() {
        long now;
        long last = 0;

        while(!Thread.currentThread().isInterrupted()) {
            now = System.currentTimeMillis();

            if(now - last > displayTime) {
//                double avgExecTime = execTime.stream().mapToLong(val -> val).average().orElse(0.0);
//                double avgComparisions = comparisions.stream().mapToInt(val -> val).average().orElse(0.0);
//
//                System.out.println("Avg execution time(ms): " + Math.round(avgExecTime*100)/100f + "\tAvg comparisions: " + Math.round(avgComparisions));
//
//                execTime.clear();
//                comparisions.clear();

                last = now;
            }

            // Sleep while there is nothing to do
            long sleepTime = displayTime - (System.currentTimeMillis() - last);
            if (sleepTime > 0) {
                try {
                    Thread.sleep(displayTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
