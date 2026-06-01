package dev.m4nd3l.cubegame.toolbox.multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MultiThreadSystem<T> {
    private List<WorkerThread<T>> threadList;
    private Queue<T> ready;
    private int threads, worker;

    public MultiThreadSystem(int threads) { this(threads, threads + "ThreadSystem - " + Math.random()); }
    public MultiThreadSystem(int threads, String purpose) {
        if (threads >= 13) threads = 12;
        this.threads = threads;
        this.threadList = new ArrayList<>(threads);
        this.ready = new ConcurrentLinkedQueue<>();
        this.worker = 0;
        for (int i = 0; i < this.threads; i++) threadList.add(i, new WorkerThread<>(purpose, i, false));
    }

    public void addThread() { addThread(new WorkerThread<>(threads - 1, true)); }
    public void addThread(WorkerThread<T> thread) {
        this.threads++;
        threadList.add(threads - 1, thread);
    }


    public void addToQueue(T value, Runnable runnable) {
        getCurrent().addToQueue(value, runnable);
        var polled = getCurrent().pollReady();
        if (polled != null) ready.add(polled);
        increment();
    }

    public void update() {
        threadList.forEach((thread) -> ready.addAll(thread.pollAll()));
    }

    public T pollReady() { var polled = getCurrent().pollReady(); if (polled != null) ready.add(polled); return ready.poll(); }
    public boolean isReadyQueueEmpty() { return ready.isEmpty(); }

    public void delete() { threadList.forEach(WorkerThread::delete); }

    private WorkerThread<T> getCurrent() { return threadList.get(worker); }

    private void increment() {
        worker++;
        if (worker >= threads) worker = 0;
    }
}
