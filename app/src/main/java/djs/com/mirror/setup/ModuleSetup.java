package djs.com.mirror.setup;

import djs.com.mirror.events.Notifiable;
import djs.com.mirror.schedule.ScheduleManager;
import djs.com.mirror.schedule.ScheduleTask;

/**
 * Created by Daniel on 24/02/2018.
 */

public class ModuleSetup {

    private final Notifiable notifiable;

    public ModuleSetup(Notifiable notifiable) {
        this.notifiable = notifiable;
    }

    public void schedule(ScheduleTask... tasks){
        ScheduleManager scheduleManager = new ScheduleManager(notifiable);
        for(ScheduleTask task : tasks){
            scheduleManager.schedule(task);
        }
    }

}
