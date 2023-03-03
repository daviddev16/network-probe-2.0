package com.networkprobe.core.threading;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Worker implements Runnable {

    private Thread worker;

    private final AtomicBoolean state = new AtomicBoolean(false);

    private final boolean updatable;
    private final boolean daemon;
    private final String name;

    public Worker(String name, boolean updatable, boolean daemon) {
        this.name = name;
        this.daemon = daemon;
        this.updatable = updatable;
    }

    @Override
    public void run() {
        state.set(true);
        onBegin();

        do {
            onUpdate();
        } while (state.get() && updatable);

        if (!state.get() && updatable)
            state.set(false);

        onStop();
    }

    public synchronized void start() {

        if (getActiveState())
            return;

        worker = new Thread(this, name);
        worker.setDaemon(daemon);
        worker.start();
    }

    public synchronized void stop() {

        if (!getActiveState())
            return;

        state.set(false);

        if (!state.get())
            worker.interrupt();

        worker = null;
    }

    public boolean getActiveState() {
        return state.get();
    }

    protected abstract void onBegin();

    protected abstract void onUpdate();

    protected abstract void onStop();

}
