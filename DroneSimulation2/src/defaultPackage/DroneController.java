package defaultPackage;

import java.util.ArrayList;

public class DroneController {
	ArrayList<Drone> dronesInSystem;
	ArrayList<Drone> dronesInRange;
	int maxDrones;
	int posX, posY;
	int signalRange;
	
	public DroneController(int max, int x, int y, int range) {
		maxDrones = max;
		posX = x;
		posY = y;
		signalRange = range;
		
		dronesInSystem = new ArrayList<Drone>();
		dronesInRange = new ArrayList<Drone>();
	}
	
	boolean signalDrone(String m, int droneID) {
		if (dronesInRange == null) {
			return false;
		}
		else {
			for (Drone d : dronesInRange) {
				// controller cannot command reserved drones
				if (d.getID() == droneID && d.getStatus() != Drone.JobStatus.RESERVED) {
					d.setMessage(m);
					return true;
				}
			}
		}
		return false;
	}
	
	boolean signalDeploy(DroneDepot depot) {
		if (maxDrones != dronesInSystem.size()) {
			dronesInSystem.add(depot.deployDrone(0, 0, 0, null));
			return true;
		}
		else {
			return false;
		}
	}
	
	void scanRange() {
		double dist = 0;
		
		dronesInRange.clear();
		for (int i = 0; i < dronesInSystem.size(); i++) {
			dist = Math.sqrt(Math.pow(dronesInSystem.get(i).getPosX() - posX, 2)
					+ Math.pow(dronesInSystem.get(i).getPosY() - posY, 2));
			if (dist < signalRange) {
				dronesInRange.add(dronesInSystem.get(i));
			}
		}
	}
	
	// beginning of each time step:
	// every drone not reserved becomes active
	void startTimeStep(int currentStep) {
		for (Drone d : dronesInSystem) {
			d.updateReservation(currentStep);
			
			if (d.getStatus() != Drone.JobStatus.RESERVED) {
				d.setActive();
			}
		}
	}
	
	// choose some active drones to become idle
	void setDroneIdle(int index) {
		if (index >= 0 && index < dronesInSystem.size()) {
			Drone d = dronesInSystem.get(index);
			if (d.getStatus() == Drone.JobStatus.ACTIVE) {
				d.setIdle();
			}
		}
	}
	
	// choose some idle drones to become reserved
	void reserveDrone(int index, int currentStep, int stepsReserved) {
		if (index >= 0 && index < dronesInSystem.size()) {
			Drone d = dronesInSystem.get(index);
			if (d.getStatus() == Drone.JobStatus.IDLE) {
				d.reserveDrone(currentStep, stepsReserved);
			}
		}
	}
}