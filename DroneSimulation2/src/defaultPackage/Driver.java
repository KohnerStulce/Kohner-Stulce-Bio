package defaultPackage;

import java.util.Random;

public class Driver {

	public static void main(String[] args) {
		
		DroneController controller = new DroneController(5, 0, 0, 10);
		DroneDepot depot1 = new DroneDepot(5, 5);
		controller.signalDeploy(depot1);
		
		Random gen = new Random();
		int direction;
		
		for (int i = 0; i < 20; i++) {
			
			// beginning of each time step:
			// every drone not reserved becomes active
			controller.startTimeStep(i);
			
			// choose one active drone to become idle
			if (controller.dronesInSystem.size() > 0) {
				int idleIndex = gen.nextInt(controller.dronesInSystem.size());
				controller.setDroneIdle(idleIndex);
			}
			
			// choose one idle drone to become reserved for 2 steps
			if (controller.dronesInSystem.size() > 0) {
				int reserveIndex = gen.nextInt(controller.dronesInSystem.size());
				controller.reserveDrone(reserveIndex, i, 2);
			}
			
			// only active drones will move
			for (int j = 0; j < controller.dronesInSystem.size(); j++) {
				direction = gen.nextInt(2);
				if (direction == 0) {
					controller.dronesInSystem.get(j).moveX(gen.nextInt(-2, 2));
				}
				else {
					controller.dronesInSystem.get(j).moveY(gen.nextInt(-2, 2));
				}
			}
			
			controller.scanRange();
			
			if (gen.nextInt(2) == 0) {
				controller.signalDeploy(depot1);
			}
			else if (controller.dronesInSystem.size() > 0) {
				controller.signalDrone("MSG", gen.nextInt(controller.dronesInSystem.size()));
			}
			
			System.out.println("Step " + i);
			System.out.println("DroneID\tposX\tposY\tStatus\t\tMessage");
			
			for (Drone drone : controller.dronesInSystem) {
				if (drone == null)
					break;
				
				System.out.print(drone.getID() + "\t" 
						+ drone.getPosX() + "\t" 
						+ drone.getPosY() + "\t" 
						+ drone.getStatus());
				
				// print return for reserved drones
				if (drone.getStatus() == Drone.JobStatus.RESERVED) {
					System.out.print("\t(return at step " + drone.getReturnTimeStep() + ")");
				}
				else {
					System.out.print("\t\t");
				}
				
				System.out.println("\t" + drone.getMessage());
			}
			
			System.out.println();
		}
	}
}