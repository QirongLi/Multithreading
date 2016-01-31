package p2;

import java.util.LinkedList;
import java.util.Queue;

/**
 * A Machine is used to make a particular Food. Each Machine makes just one kind
 * of Food. Each machine has a capacity: it can make that many food items in
 * parallel; if the machine is asked to produce a food item beyond its capacity,
 * the requester blocks. Each food item takes at least item.cookTimeMS
 * milliseconds to produce.
 */
public class Machine {
	public final String machineName;
	public final Food machineFoodType;
	private int capacity;
	private Queue<Food> foodInMachine = new LinkedList<Food>();

	// YOUR CODE GOES HERE...

	public int getCapacity() {
		return capacity;
	}

	public synchronized Queue<Food> getFoodInMachine() {
		return foodInMachine;
	}

	/**
	 * The constructor takes at least the name of the machine, the Food item it
	 * makes, and its capacity. You may extend it with other arguments, if you
	 * wish. Notice that the constructor currently does nothing with the
	 * capacity; you must add code to make use of this field (and do whatever
	 * initialization etc. you need).
	 */
	public Machine(String nameIn, Food foodIn, int capacityIn) {
		this.machineName = nameIn;
		this.machineFoodType = foodIn;
		this.capacity = capacityIn;
	}

	/**
	 * This method is called by a Cook in order to make the Machine's food item.
	 * You can extend this method however you like, e.g., you can have it take
	 * extra parameters or return something other than Object. It should block
	 * if the machine is currently at full capacity. If not, the method should
	 * return, so the Cook making the call can proceed. You will need to
	 * implement some means to notify the calling Cook when the food item is
	 * finished.
	 */
	public void makeFood(Cook cook) throws InterruptedException {
		// YOUR CODE GOES HERE...
		foodInMachine.add(this.machineFoodType);
		Thread thread = new Thread(new CookAnItem(cook));
		thread.start();
	}

	// THIS MIGHT BE A USEFUL METHOD TO HAVE AND USE BUT IS JUST ONE IDEA
	private class CookAnItem implements Runnable {

		private Cook cook;

		public CookAnItem(Cook cookIn) {
			this.cook = cookIn;
		}

		public void run() {
			try {
				Simulation.logEvent(SimulationEvent.machineCookingFood(
						Machine.this, machineFoodType));
				Thread.sleep(machineFoodType.cookTimeMS);
				Simulation.logEvent(SimulationEvent.machineDoneFood(
						Machine.this, machineFoodType));
				Simulation.logEvent(SimulationEvent.cookFinishedFood(cook,
						machineFoodType, 1));
				
				synchronized (foodInMachine) {
					foodInMachine.remove();
					foodInMachine.notifyAll();
				}

				synchronized (cook.getReturnOrderFoods()) {
					cook.getReturnOrderFoods().add(machineFoodType);
					cook.getReturnOrderFoods().notifyAll();
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public String toString() {
		return machineName;
	}
}