/*
 * Copyright (C) 2018 Nico Lube
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.nicolube.devcore.utils.Scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Nico Lube
 */
public class Scheduler {

    private static final Thread schedulerThread;
    private static final List<Scheduler> registeredScheduler;

    private final List<SchedulerTask> tasks;

    static {
        registeredScheduler = new ArrayList<>();
        schedulerThread = new Thread(() -> {

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    registeredScheduler.forEach(schedular -> {
                        schedular.tasks.forEach((scheduler) -> {
                            if (!scheduler.run()) {
                                schedular.tasks.remove(scheduler);
                            }
                        });
                    });
                }
            }, 1, 1);
        });
        schedulerThread.start();
    }

    public Scheduler() {
        this.tasks = new ArrayList<>();
        registeredScheduler.add(this);
    }

    public void onDisable() {
        tasks.clear();
        registeredScheduler.remove(this);
    }

    public SchedulerTask runTaskLater(Runnable r, int delay) {
        SchedulerTask task = new SchedulerTask(r, SchedulerType.Later, delay);
        tasks.add(task);
        return task;
    }

    public SchedulerTask runRepeatingTask(Runnable r, int delay, int period) {
        SchedulerTask task = new SchedulerTask(r, SchedulerType.RepeatingTask, delay, period);
        tasks.add(task);
        return task;
    }
}
