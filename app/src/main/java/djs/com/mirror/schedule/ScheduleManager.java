package djs.com.mirror.schedule;

import java.io.Serializable;

import djs.com.mirror.events.Notifiable;
import djs.com.mirror.ui.MainActivity;
import it.sauronsoftware.cron4j.Scheduler;

/**
 * Created by Daniel on 24/02/2018.
 */

public class ScheduleManager {

    private static final String RETRY_CRON = "*/5 * * * *";

    private final Scheduler scheduler;

    private final Notifiable notifiable;

    public ScheduleManager(Notifiable notifiable) {
        this.notifiable = notifiable;
        scheduler = new Scheduler();
        scheduler.start();
    }

    public Serializable schedule(final ScheduleTask scheduleTask) {
        try{
            if(scheduleTask.shouldCallImmediately()){
                runTask(scheduleTask);
            }

            return scheduler.schedule(scheduleTask.getCronPattern(), new Runnable() {
                public void run() {
                    boolean result = runTask(scheduleTask);

                    if(result){

                    }
                }
            });
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    private boolean runTask(ScheduleTask scheduleTask) {
        boolean state = scheduleTask.getRetriever().retrieve(notifiable);
        MainActivity.print(String.format("running of '%s' was %s", scheduleTask.getName(), state ? "successful" : "unsuccessful"));
        return state;
    }

}
