package ru.sbt.jschool.session9;

import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class ContextImpl implements Context {

    private List<Future> list;

    public ContextImpl(List<Future> list) {
        this.list = list;
    }

    @Override
    public int getCompletedTaskCount() {
        return calcTaskCount("Completed");
    }

    @Override
    public int getFailedTaskCount() {
        return calcTaskCount("Failed");
    }

    @Override
    public int getInterruptedTaskCount() {
        return calcTaskCount("Interrupted");
    }

    @Override
    public void interrupt() {
        for (Future future : list) {
            future.cancel(false);
        }
    }

    @Override
    public boolean isFinished() {
        return isAllFinished();
    }

    private boolean isAllFinished() {
        for (Future future : list) {
            if (!future.isDone())
                return false;
        }
        return true;
    }

    private int calcTaskCount(String type) {
        switch (type.toLowerCase()) {
            case "completed":
                return calcCompleted();
            case "failed":
                return calcFailed();
            case "interrupted":
                return calcInterrupted();
            case "finished":
                return calcFinished();
            default:
                return -1;
        }
    }

    private int calcCompleted() {
        AtomicInteger count = new AtomicInteger(0);

        for (Future future : list) {
            try {
                future.get();
                count.incrementAndGet();
            } catch (InterruptedException | ExecutionException | CancellationException e) {
                /*nothing to do*/
            }
        }

        return count.get();
    }

    private int calcFailed() {
        AtomicInteger count = new AtomicInteger(0);

        for (Future future : list) {
            try {
                future.get();
            } catch (InterruptedException | CancellationException e) {
                /*nothing to do*/
            } catch (ExecutionException e) {
                count.incrementAndGet();
            }
        }

        return count.get();
    }

    private int calcInterrupted() {
        AtomicInteger count = new AtomicInteger(0);

        for (Future future : list) {
            if (future.isCancelled())
                count.incrementAndGet();
        }

        return count.get();
    }

    private int calcFinished() {
        AtomicInteger count = new AtomicInteger(0);

        for (Future future : list) {
            if (future.isDone() || future.isCancelled())
                count.incrementAndGet();
        }

        return count.get();
    }
}