package ru.sbt.jschool.session9;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutionManagerImpl implements ExecutionManager {

    @Override
    public Context execute(Runnable callback, Runnable... tasks) {
        Context context;
        List<Future> list = new ArrayList<>();
        ExecutorService service = Executors.newFixedThreadPool(1);
        //ExecutorService service = Executors.newCachedThreadPool();

        for (Runnable task : tasks) {
            list.add(service.submit(task));
        }

        context = new ContextImpl(list);

        new Thread(() -> runCallback(callback, context)).start();
        new Thread(() -> shutdownIfFinished(service, context)).start();

        return context;
    }

    private void runCallback(Runnable callback, Context context) {
        while (true) {
            if (context.isFinished()) {
                callback.run();
                break;
            }
        }
    }

    private void shutdownIfFinished(ExecutorService service, Context context) {
        while (true) {
            if (context.isFinished()) {
                service.shutdown();
                break;
            }
        }
    }
}
