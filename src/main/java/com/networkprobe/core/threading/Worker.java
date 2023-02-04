package com.networkprobe.core.threading;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Worker implements Runnable {

    private AtomicBoolean runningState = new AtomicBoolean(false);

    private volatile Thread worker;
    private boolean updatable;
    private boolean daemon;
    private String name;

    public Worker(String name, boolean updatable, boolean daemon) {
        this.name = name;
        this.daemon = daemon;
        this.updatable = updatable;
    }

    @Override
    public void run() {
        runningState.set(true);
        onBegin();

        do {
            onUpdate();
        } while (runningState.get() && updatable);

        if (!runningState.get() && updatable)
            runningState.set(false);

        onStop();
    }

    public synchronized void start() {
        worker = new Thread(this, name);
        worker.setDaemon(daemon);
        worker.start();
    }

    public synchronized void stop() {
        runningState.set(false);
        worker.interrupt();
    }

    public boolean isStopped() {
        return !runningState.get();
    }

    public abstract void onBegin();

    public abstract void onUpdate();

    public abstract void onStop();

}
