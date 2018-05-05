package ru.sbt.jschool.session9;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 */
public interface Context {

    public List<Future> list = new ArrayList<>();

    int getCompletedTaskCount();

    int getFailedTaskCount();

    int getInterruptedTaskCount();

    void interrupt();

    boolean isFinished();
}
