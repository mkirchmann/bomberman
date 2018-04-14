package de.neuenberger.games.bomberman.ki;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Scheduler to ensure runnables are run, but only one in parallel.
 * 
 * @author Michael
 *
 */
public class RunnableScheduler {
	private static final RunnableScheduler INSTANCE = new RunnableScheduler();
	private Thread thread;
	private Logger log = Logger.getLogger(getClass().getSimpleName());

	private RunnableScheduler() {
		
	}

	Queue<Runnable> runnables = new LinkedList<>();

	public synchronized void schedule(Runnable runnable) {
		runnables.add(runnable);
		if (thread == null) {
			thread = new Thread(new Runner());
			thread.start();
		}
	}

	public static RunnableScheduler getInstance() {
		return INSTANCE;
	}

	private class Runner implements Runnable {
		@Override
		public void run() {
			while (true) {
				Runnable runnable = runnables.poll();
				if (runnable != null) {
					try {
						runnable.run();
					} catch (RuntimeException e) {
						log.log(Level.SEVERE, "error while running", e);
					}
				}
				synchronized (RunnableScheduler.this) {
					if (runnables.isEmpty()) {
						thread = null;
						break;
					}
				}
			}
		}
	}

}
