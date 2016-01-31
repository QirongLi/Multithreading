package p2;

import java.util.List;
import java.util.Queue;

/**
 * Customers are simulation actors that have two fields: a name, and a list of
 * Food items that constitute the Customer's order. When running, an customer
 * attempts to enter the coffee shop (only successful if the coffee shop has a
 * free table), place its order, and then leave the coffee shop when the order
 * is complete.
 */
public class Customer implements Runnable {
	// JUST ONE SET OF IDEAS ON HOW TO SET THINGS UP...
	private final String name;
	private final List<Food> order;
	private final int orderNum;
	// private int priority;

	private static int runningCounter = 0;

	/**
	 * You can feel free modify this constructor. It must take at least the name
	 * and order but may take other parameters if you would find adding them
	 * useful.
	 */
	public Customer(String name, List<Food> order) {
		this.name = name;
		this.order = order;
		// this.priority = priority;
		this.orderNum = ++runningCounter;
	}

	// public void setPriority(int priority) {
	// this.priority = priority;
	// }
	//
	// public int getPriority() {
	// return priority;
	// }

	public List<Food> getOrder() {
		return order;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public String toString() {
		return name;
	}

	/**
	 * This method defines what an Customer does: The customer attempts to enter
	 * the coffee shop (only successful when the coffee shop has a free table),
	 * place its order, and then leave the coffee shop when the order is
	 * complete.
	 */
	public void run() {
		// YOUR CODE GOES HERE...
		// Before entering the coffee shop
		Simulation.logEvent(SimulationEvent.customerStarting(this));

		synchronized (Simulation.inShopCustomers) {
			while (Simulation.inShopCustomers.size() >= Simulation.events
					.get(0).simParams[2]) {
				try {
					Simulation.inShopCustomers.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			Simulation.inShopCustomers.add(this);
			Simulation.logEvent(SimulationEvent.customerPlacedOrder(this,
					this.order, this.orderNum));
			Simulation.inShopCustomers.notifyAll();
		}

		synchronized (Simulation.orders) {
			Simulation.orders.add(this);
			Simulation.logEvent(SimulationEvent.customerPlacedOrder(this,
					this.order, this.orderNum));
			Simulation.orders.notifyAll();
		}

		// synchronized (Simulation.compeltedOrder) {
		// Simulation.compeltedOrder.add(this);
		// }
		//
		synchronized (Simulation.compeltedOrder) {
			while (!Simulation.compeltedOrder.contains(this)) {
				try {
					Simulation.compeltedOrder.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			Simulation.logEvent(SimulationEvent.customerReceivedOrder(this,
					this.order, this.orderNum));
			Simulation.compeltedOrder.notifyAll();
		}

		synchronized (Simulation.compeltedOrder) {
			Simulation.inShopCustomers.remove(this);
			Simulation
					.logEvent(SimulationEvent.customerLeavingCoffeeShop(this));
//			Simulation.inShopCustomers.notifyAll();
		}
	}
}