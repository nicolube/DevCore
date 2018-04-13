/*
 * Copyright (C) 2018 Nico Lube
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package de.nicolube.devcore.utils.Scheduler;

/**
 *
 * @author Nico Lube
 */
public class SchedulerTask {

    private boolean fistRun = true;
    private boolean isCanceled = false;
    private final Runnable r;
    private final SchedulerType type;
    private int counter = 0;

    private final int delay;
    private int period;

    protected SchedulerTask(Runnable r, SchedulerType type, int delay) {
        this.r = r;
        this.type = type;
        this.delay = delay;
    }

    protected SchedulerTask(Runnable r, SchedulerType type, int delay, int period) {
        this.r = r;
        this.type = type;
        this.delay = delay;
        this.period = period;
    }

    protected boolean run() {
        if (isCanceled) {
            return false;
        }
        if (fistRun) {
            if (counter < delay) {
                counter++;
                return true;
            }
            counter = 0;
            fistRun = false;
            r.run();
            return type != SchedulerType.Later;
        }

        if (counter < period) {
            counter++;
            return true;
        }
        counter = 0;
        r.run();
        return true;
    }

    public void cancel() {
        isCanceled = true;
    }

}
