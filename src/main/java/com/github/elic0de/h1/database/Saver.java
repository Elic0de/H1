package com.github.elic0de.h1.database;

import com.github.elic0de.h1.H1Plugin;
import com.github.elic0de.h1.utils.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Saver extends Thread implements Listener {
    private final Connector con;

    private final ConcurrentLinkedQueue<Record> queue;

    private boolean running;

    public Saver() {
        this.con = new Connector();
        this.queue = new ConcurrentLinkedQueue<>();
        this.running = true;
        Bukkit.getPluginManager().registerEvents(this, H1Plugin.INSTANCE.getPlugin());
    }

    public void run() {
        boolean active = false;
        while (true) {
            while (this.queue.isEmpty()) {
                if (!this.running)
                    return;
                synchronized (this) {
                    try {
                        active = false;
                        wait();
                    } catch (InterruptedException e) {
                        LogUtil.error("There was a exception with SQL");
                    }
                }
            }
            if (!active) {
                this.con.refresh();
                active = true;
            }
            Record rec = this.queue.poll();
            this.con.updateSQL(rec.getType(), rec.getArgs());
        }
    }

    public void add(Record rec) {
        synchronized (this) {
            this.queue.add(rec);
            notifyAll();
        }
    }

    public void end() {
        synchronized (this) {
            this.running = false;
            notifyAll();
        }
    }

    public static class Record {
        private final Connector.UpdateType type;

        private final String[] args;

        public Record(Connector.UpdateType type, String... args) {
            this.type = type;
            this.args = (args == null) ? null : Arrays.copyOf(args, args.length);
        }

        private Connector.UpdateType getType() {
            return this.type;
        }

        private String[] getArgs() {
            return this.args;
        }
    }
}