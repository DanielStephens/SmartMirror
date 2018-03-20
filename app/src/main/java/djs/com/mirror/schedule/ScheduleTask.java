package djs.com.mirror.schedule;

import djs.com.mirror.basics.Supplier;
import djs.com.mirror.modules.retrievers.Retriever;

/**
 * Created by Daniel on 24/02/2018.
 */

public class ScheduleTask {

    private final String name;

    private final String cronPattern;

    private final Retriever retriever;

    private final Supplier<Boolean> callImmediately;

    private ScheduleTask(String name, String cronPattern, Retriever retriever, Supplier<Boolean> callImmediately) {
        this.name = name;
        this.cronPattern = cronPattern;
        this.retriever = retriever;
        this.callImmediately = callImmediately;
    }

    public static ScheduleTask of(String name, Retriever retriever, String cronPattern){
        return new ScheduleTask(name, cronPattern, retriever, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return true;
            }
        });
    }

    public static ScheduleTask of(String name, Retriever retriever, String cronPattern, Supplier<Boolean> callImmediately){
        return new ScheduleTask(name, cronPattern, retriever, callImmediately);
    }

    public String getName() {
        return name;
    }

    public String getCronPattern() {
        return cronPattern;
    }

    public Retriever getRetriever() {
        return retriever;
    }

    public boolean shouldCallImmediately() {
        return callImmediately.get();
    }
}
