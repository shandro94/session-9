package ru.sbt.jschool.session9;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ContextImpl implements Context {

    private List<Future> list;
    private AtomicInteger completedTaskCount;
    private AtomicInteger failedTaskCount;
    private AtomicInteger interruptedTaskCount;
    private ExecutorService service;
    private AtomicBoolean isFinished = new AtomicBoolean(false);

    public ContextImpl() {
        this.list = new ArrayList<>();
    }

    public ContextImpl(List<Future> list, ExecutorService service) {
        this.list = list;
        this.completedTaskCount = new AtomicInteger(0);
        this.failedTaskCount = new AtomicInteger(0);
        this.interruptedTaskCount = new AtomicInteger(0);
        this.service = service;
    }

    @Override
    public int getCompletedTaskCount() {
        return completedTaskCount.get();
    }

    @Override
    public int getFailedTaskCount() {
        return failedTaskCount.get();
    }

    @Override
    public int getInterruptedTaskCount() {
        return interruptedTaskCount.get();
    }

    @Override
    public void interrupt() {
        service.shutdownNow();
        isFinished.set(true);
    }

    /** isFinished
     * Проверяет, завершились ли все потоки.
     * Если все потоки не отменены и не завершены, значит еще не завершились.
     * Условие: isCancelled = false; isDone = false. Return false.
     * Если поток отменен, значит он не завершен.
     * Условие: isCancelled = true; isDone = false. Проверяем следующий. Если так для всех, то return true.
     * Если все завершены, аналогично.
     * @return
     */
    @Override
    public boolean isFinished() {
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            Future future = (Future)iterator.next();


            //System.out.println(isFinished.get());
            if (!isFinished.get() && !future.isDone() && !future.isCancelled()) {
                return false;
            }

            //System.out.println("is cancelled: " + future.isCancelled());
            if (future.isCancelled()) {
                list.remove(future);
                interruptedTaskCount.incrementAndGet();
                //System.out.println("Interrupted: " + interruptedTaskCount.get());
            }

            if (future.isDone()) {
                iterator.remove();
                //System.out.println("Done: " + completedTaskCount.get());
                tryIncrementCounts(future);
            }

            if (isFinished.get()) {
                iterator.remove();
                if (future.isDone()) {
                    tryIncrementCounts(future);
                } else {
                    interruptedTaskCount.incrementAndGet();
                    System.out.println("Interrupted: " + interruptedTaskCount.get());
                }
            }
        }

        return isFinished.get();
    }

    private void tryIncrementCounts(Future future) {
        try {
            future.get();
            completedTaskCount.incrementAndGet();
        } catch (InterruptedException e) {
            System.out.println("не могу тут");
            interruptedTaskCount.incrementAndGet();
            System.out.println("Interrupted: " + interruptedTaskCount.get());
        } catch (ExecutionException e) {
            failedTaskCount.incrementAndGet();
            //System.out.println("Failed: " + failedTaskCount.get());
        }
    }
}