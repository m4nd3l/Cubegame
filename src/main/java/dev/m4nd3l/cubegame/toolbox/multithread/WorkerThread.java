package dev.m4nd3l.cubegame.toolbox.multithread;

import dev.m4nd3l.cubegame.toolbox.util.Mix;

import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class WorkerThread<T> {
    private BlockingQueue<Mix<T, Runnable>> queue;
    private Set<T> activeTasks;
    private Queue<T> ready;
    private Thread workerThread;
    private volatile boolean running = true;

    public WorkerThread() { this("Worker" + Math.random() * Math.random()); }
    public WorkerThread(int i) { this(i, true); }
    public WorkerThread(int i, boolean randomize) { this("Worker", i, randomize); }
    public WorkerThread(String name, int i) { this(name, i, true); }
    public WorkerThread(String name, int i, boolean randomize) { this(name + " N " + i * (randomize ? Math.random() : 1)); }
    public WorkerThread(String name) {
        queue = new LinkedBlockingQueue<>();
        activeTasks = ConcurrentHashMap.newKeySet();
        ready = new ConcurrentLinkedQueue<>();
        workerThread = new Thread(() -> {
            while (running) {
                try {
                    var mix = queue.take();
                    var task = mix.get2();
                    if (task == null) continue;
                    task.run();
                    ready.add(mix.get1());
                } catch (InterruptedException e) { break; }
            }
        }, name);
        workerThread.start();
    }

    public T addToQueue(T value, Runnable action) {
        if (queue.contains(value)) return value;
        queue.add(new Mix<>(value, action));
        return value;
    }

    public T pollReady() { return ready.poll(); }

    public List<T> pollAll() {
        if (ready.isEmpty()) return List.of();
        var all = ready.stream().toList();
        ready.clear();
        return all;
    }

    public void delete() {
        running = false;
        workerThread.interrupt();
        queue.clear();
        activeTasks.clear();
    }

}
