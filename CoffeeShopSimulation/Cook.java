package p2;

import java.util.ArrayList;
import java.util.List;

/**
 * Cooks are simulation actors that have at least one field, a name. When
 * running, a cook attempts to retrieve outstanding orders placed by Eaters and
 * process them.
 */
public class Cook implements Runnable {
	private final String name;
	private Customer customer;
	private List<Food> returnOrderFoods = new ArrayList<Food>();

	public synchronized List<Food> getReturnOrderFoods() {
		return returnOrderFoods;
	}

	/**
	 * You can feel free modify this constructor. It must take at least the
	 * name, but may take other parameters if you would find adding them useful.
	 *
	 * @param: the name of the cook
	 */
	public Cook(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	/**
	 * This method executes as follows. The cook tries to retrieve orders placed
	 * by Customers. For each order, a List<Food>, the cook submits each Food
	 * item in the List to an appropriate Machine, by calling makeFood(). Once
	 * all machines have produced the desired Food, the order is complete, and
	 * the Customer is notified. The cook can then go to process the next order.
	 * If during its execution the cook is interrupted (i.e., some other thread
	 * calls the interrupt() method on it, which could raise
	 * InterruptedException if the cook is blocking), then it terminates.
	 */
	public void run() {

		Simulation.logEvent(SimulationEvent.cookStarting(this));

		try {
			while (true) {
				// YOUR CODE GOES HERE...
				synchronized (Simulation.orders) {
					while (Simulation.orders.size() == 0) {
						Simulation.orders.wait();
					}

					customer = Simulation.orders.remove();

					Simulation.logEvent(SimulationEvent.cookReceivedOrder(this,
							customer.getOrder(), customer.getOrderNum()));
					Simulation.orders.notifyAll();

				}

				for (int i = 0; i < customer.getOrder().size(); i++) {
					if (customer.getOrder().get(i).equals(FoodType.burger)) {
						synchronized (Simulation.grill.getFoodInMachine()) {
							while (Simulation.grill.getFoodInMachine().size() >= Simulation.grill
									.getCapacity()) {
								Simulation.grill.getFoodInMachine().wait();
							}
							Simulation.logEvent(SimulationEvent
									.cookStartedFood(this, FoodType.burger,
											customer.getOrderNum()));
							Simulation.grill.makeFood(this);
							Simulation.grill.getFoodInMachine().notifyAll();
						}
					}

					if (customer.getOrder().get(i).equals(FoodType.fries)) {
						synchronized (Simulation.fryer.getFoodInMachine()) {

							while (Simulation.fryer.getFoodInMachine().size() >= Simulation.fryer
									.getCapacity()) {
								Simulation.fryer.getFoodInMachine().wait();
							}
							Simulation.logEvent(SimulationEvent
									.cookStartedFood(this, FoodType.fries,
											customer.getOrderNum()));
							Simulation.fryer.makeFood(this);
							Simulation.fryer.getFoodInMachine().notifyAll();
						}
					}

					if (customer.getOrder().get(i).equals(FoodType.coffee)) {
						synchronized (Simulation.coffeeMaker2000
								.getFoodInMachine()) {

							while (Simulation.coffeeMaker2000
									.getFoodInMachine().size() >= Simulation.coffeeMaker2000
									.getCapacity()) {
								Simulation.coffeeMaker2000.getFoodInMachine()
										.wait();
							}

							Simulation.logEvent(SimulationEvent
									.cookStartedFood(this, FoodType.coffee,
											customer.getOrderNum()));
							Simulation.coffeeMaker2000.makeFood(this);
							Simulation.coffeeMaker2000.getFoodInMachine()
									.notifyAll();
						}
					}
				}

				synchronized (returnOrderFoods) {
					while (returnOrderFoods.size() != customer.getOrder()
							.size()) {
						returnOrderFoods.wait();
						returnOrderFoods.notifyAll();
					}
				}

				synchronized (Simulation.compeltedOrder) {
					Simulation.compeltedOrder.add(customer);
					Simulation.compeltedOrder.notifyAll();
				}

				returnOrderFoods.removeAll(returnOrderFoods);
				Simulation.logEvent(SimulationEvent.cookCompletedOrder(this,
						customer.getOrderNum()));
			}

		} catch (InterruptedException e) {
			// This code assumes the provided code in the Simulation class
			// that interrupts each cook thread when all customers are done.
			// You might need to change this if you change how things are
			// done in the Simulation class.
			Simulation.logEvent(SimulationEvent.cookEnding(this));
		}
	}
}