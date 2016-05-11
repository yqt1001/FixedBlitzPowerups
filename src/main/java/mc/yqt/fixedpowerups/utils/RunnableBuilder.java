package mc.yqt.fixedpowerups.utils;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import mc.yqt.fixedpowerups.FixedPowerups;

/**
 * Wrapper class for BukkitRunnables that is incredibly convenient, now that my server is on Java 8.
 * Something similar is used at the server I work for.
 * 
 */
public class RunnableBuilder {

	private int delay = 1;
	private int period = 0;
	private FixedPowerups main;
	private Runnable run;
	
	private int cycles = 1;
	private int cycled = 0;
	
	public RunnableBuilder(FixedPowerups main, int delay, int period, int cycles) {
		this.main = main;
		this.delay = delay;
		this.period = period;
		this.cycled = cycles;
	}
	
	/**
	 * Default constructor.
	 * @param main plugin class
	 */
	public RunnableBuilder(FixedPowerups main) {
		this.main = main;
	}
	
	public static RunnableBuilder make(FixedPowerups main) {
		return new RunnableBuilder(main);
	}
	
	/**
	 * Specifies first-run delay in ticks
	 * @param ticks
	 * @return Builder
	 */
	public RunnableBuilder delay(int ticks) {
		this.delay = ticks;
		return this;
	}
	
	/**
	 * Specifies interval delay in ticks
	 * @param ticks
	 * @return Builder
	 */
	public RunnableBuilder interval(int ticks) {
		this.period = ticks;
		return this;
	}
	
	/**
	 * Specifies the amount of times the runnable should run
	 * @param cycles
	 * @return Builder
	 */
	public RunnableBuilder limit(int cycles) {
		this.cycles = cycles;
		return this;
	}
	
	/**
	 * Specifies that the runnable will run until canceled or server shut down
	 * @return Builder
	 */
	public RunnableBuilder unlim() {
		this.cycles = 0;
		return this;
	}
	
	/**
	 * Starts the built runnable with the given runnable fucntion
	 * @param run
	 * @return The generated task
	 */
	public BukkitTask run(Runnable run) {
		this.run = run;
		
		// make unlimited timer
		if(cycles < 1)
			return buildUnlimTimer();
		
		// make cycle limited timer
		else if(cycles > 1)
			return buildLimitedTimer();
		
		// make delayed task
		else
			return buildDelayedTask();
	}
	
	private BukkitTask buildLimitedTimer() {
		return new BukkitRunnable() {
			@Override
			public void run() {
				if(cycles <= cycled) {
					cancel();
					return;
				}
				
				cycled++;
				
				run.run();
			}
		}.runTaskTimer(main, delay, period);
	}
	
	private BukkitTask buildUnlimTimer() {
		return new BukkitRunnable() {
			@Override
			public void run() {
				run.run();
			}
		}.runTaskTimer(main, delay, period);
	}
	
	private BukkitTask buildDelayedTask() {
		return new BukkitRunnable() {
			@Override
			public void run() {
				run.run();
			}
		}.runTaskLater(main, delay);
	}
}