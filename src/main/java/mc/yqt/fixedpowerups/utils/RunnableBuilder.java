package mc.yqt.fixedpowerups.utils;

import java.util.function.BooleanSupplier;

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
	private int period = 1;
	private FixedPowerups main;
	private Runnable run;
	private BooleanSupplier cancelable;
	
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
		if(ticks < 1)
			ticks = 1;
		this.delay = ticks;
		return this;
	}
	
	/**
	 * Specifies interval delay in ticks
	 * @param ticks
	 * @return Builder
	 */
	public RunnableBuilder interval(int ticks) {
		if(ticks < 1)
			ticks = 1;
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
	
	/**
	 * Starts the given boolean supplier in the runnable.
	 * If the given supplier returns false, the task will stop repeating no matter how many cycles it has left.
	 * @param run
	 * @return The generated task
	 */
	public BukkitTask cancelable(BooleanSupplier run) {
		this.cancelable = run;
		
		// make unlimited timer
		if(cycles < 1)
			return buildUnlimCancelableTimer();
		
		// make cycle limited timer
		else if(cycles > 1)
			return buildLimCancelableTimer();
		
		// make delayed task
		else
			return buildDelayedCancelableTask();
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
	
	private BukkitTask buildLimCancelableTimer() {
		return new BukkitRunnable() {
			@Override
			public void run() {
				if(cycles <= cycled) {
					cancel();
					return;
				}
				
				cycled++;
				
				if(!cancelable.getAsBoolean())
					cancel();
			}
		}.runTaskTimer(main, delay, period);
	}
	
	private BukkitTask buildUnlimCancelableTimer() {
		return new BukkitRunnable() {
			@Override
			public void run() {
				if(!cancelable.getAsBoolean())
					cancel();
			}
		}.runTaskTimer(main, delay, period);
	}
	
	private BukkitTask buildDelayedCancelableTask() {
		// this doesn't really make sense since it will never repeat but I like consistency
		return new BukkitRunnable() {
			@Override
			public void run() {
				if(!cancelable.getAsBoolean())
					cancel();
			}
		}.runTaskLater(main, delay);
	}
}