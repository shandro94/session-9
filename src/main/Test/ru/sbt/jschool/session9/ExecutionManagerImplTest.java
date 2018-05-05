package ru.sbt.jschool.session9;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ExecutionManagerImplTest {

    @Test
    public void testCompletedTask() {
        ExecutionManager em = new ExecutionManagerImpl();
        List<Runnable> runnables = new ArrayList<>(2);
        Context context;

        runnables.add(() -> {
            System.out.print("");
        });

        Runnable callback = () -> {
            System.out.print("");
        };

        context = em.execute(callback, runnables.toArray(new Runnable[runnables.size()]));

        assertTrue(context.getCompletedTaskCount() > 0);
    }

    @Test
    public void testFailedTask() {
        ExecutionManager em = new ExecutionManagerImpl();
        List<Runnable> runnables = new ArrayList<>(2);
        Context context;

        //таск, который точно вылетает с ошибкой
        runnables.add(() -> {
            List list = new ArrayList();
            list.get(3);
        });

        Runnable callback = () -> {
            System.out.print("");
        };

        context = em.execute(callback, runnables.toArray(new Runnable[runnables.size()]));

        assertTrue(context.getFailedTaskCount() > 0);
    }

    @Test
    public void testInterruptedTask() {
        ExecutionManager em = new ExecutionManagerImpl();
        List<Runnable> runnables = new ArrayList<>(2);
        Context context;

        runnables.add(() -> {
            Thread.currentThread().interrupt();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
        });

        Runnable callback = () -> {
            System.out.print("");
        };

        context = em.execute(callback, runnables.toArray(new Runnable[runnables.size()]));

        context.interrupt();
        assertTrue(context.getInterruptedTaskCount() > 0);
    }

    @Test
    public void testIsFinished() {
        ExecutionManager em = new ExecutionManagerImpl();
        List<Runnable> runnables = new ArrayList<>(2);
        Context context;

        //таск, который точно вылетает с ошибкой
        runnables.add(() -> {
        });

        Runnable callback = () -> {
            System.out.print("");
        };

        context = em.execute(callback, runnables.toArray(new Runnable[runnables.size()]));

        try {
            Thread.sleep(100);
            assertTrue(context.isFinished());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void addKuchaTaskov(List<Runnable> runnables) {
        for (int i = 0; i < 10000; i++) {
            int finalI = i;
            runnables.add(() -> {
                for (int j = 0; j < 10000; j++) {
                    //System.out.println(finalI + "," + j + " started from " + Thread.currentThread().getName());
                    System.out.print("");
                }
            });
        }
    }
}