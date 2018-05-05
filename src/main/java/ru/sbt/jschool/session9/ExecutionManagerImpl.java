package ru.sbt.jschool.session9;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutionManagerImpl implements ExecutionManager {

    public static void main(String[] args) {
        ExecutionManager em = new ExecutionManagerImpl();

        List<Runnable> runnables = new ArrayList<>(1024);

        //первый таск, который не фейлит на интеррупте
        /*runnables.add( () -> {
            Thread.currentThread().interrupt();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("4rd started from " + Thread.currentThread().getName());

        });*/
        //таск, который точно вылетает с ошибкой
        runnables.add(() -> {
            List list = new ArrayList();
            list.get(3);
            System.out.println("5rd started from " + Thread.currentThread().getName());
        });
        //куча тасков
        for (int i = 0; i < 2000; i++) {
            int finalI = i;
            runnables.add(() -> {
                for (int j = 0; j < 2000; j++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(finalI + "," + j + " started from " + Thread.currentThread().getName());
                }
            });
        }

        Runnable callback = () -> System.out.println("Callback started from " + Thread.currentThread().getName()
        );

        Context context;
        context = em.execute(callback, runnables.toArray(new Runnable[runnables.size()]));

        System.out.println("heh");
        context.interrupt();
        System.out.println("heh");
    }

    /**
     * Метод execute принимает массив тасков,
     * это задания которые ExecutionManager должен выполнять параллельно
     * (в вашей реализации пусть будет в своем пуле потоков).
     * После завершения всех тасков должен выполниться callback (ровно 1 раз).
     * Метод execute – это неблокирующий метод,
     * который сразу возвращает объект Context.
     * Context это интерфейс следующего вида:
     *
     * @param callback выполняется после завершения всех тасков
     * @param tasks    таски
     * @return context
     */

    @Override
    public Context execute(Runnable callback, Runnable... tasks) {
        Context context;
        List<Future> list = new ArrayList<>();
        ExecutorService service = Executors.newFixedThreadPool(2);
        //ExecutorService service = Executors.newCachedThreadPool();

        for (Runnable task : tasks) {
            list.add(service.submit(task));
        }

        context = new ContextImpl(list, service);

        //runCallback(callback, context);

        return context;
    }



    /*private void runCallback(Runnable callback, Context context) {
        while (true) {

            if (context.isFinished()) {
                System.out.println("Is finished!");
                System.out.println("completed: " + context.getCompletedTaskCount());
                System.out.println("failed: " + context.getFailedTaskCount());
                System.out.println("interrupted: " + context.getInterruptedTaskCount());

                callback.run();
                break;
            }

            context.interrupt();
            System.out.println("Not finished yet");
        }
    }*/
}
