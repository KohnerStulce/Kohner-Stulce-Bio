package defaultPackage;

public class Drone {

	// enum for the drone's current job status
	public enum JobStatus {
		ACTIVE,
		IDLE,
		RESERVED
	}
	
	private int ID;
	private int posX, posY;
	private int topSpeed;
	private int signalRange;
	private int payloadLimit;
	private String message;
	private int currentPayload;
	
	private JobStatus status;
	private int returnTimeStep;
	
	public Drone(int ID, int x, int y, int topSpeed, int signalRange, int payloadLimit, String message) {
		this.setID(ID);
		this.setPosX(x);
		this.setPosY(y);
		this.setTopSpeed(topSpeed);
		this.setSignalRange(signalRange);
		this.payloadLimit = payloadLimit;
		this.setMessage(message);
		this.currentPayload = 0;
		
		status = JobStatus.ACTIVE;
		returnTimeStep = -1;
	}

	public void moveX(int dx) {
		if (status == JobStatus.ACTIVE) {
			setPosX(getPosX() + dx);
		}
	}

	public void moveY(int dy) {
		if (status == JobStatus.ACTIVE) {
			setPosY(getPosY() + dy);
		}
	}

	public boolean loadPayload(int weight) {
		if (currentPayload + weight <= payloadLimit) {
			currentPayload += weight;
			return true;
		}
		else {
			return false;
		}
	}
	
	// set drone to active only if it is not reserved
	public void setActive() {
		if (status != JobStatus.RESERVED) {
			status = JobStatus.ACTIVE;
		}
	}
	
	// set drone to idle only if it is not reserved
	public void setIdle() {
		if (status != JobStatus.RESERVED) {
			status = JobStatus.IDLE;
		}
	}
	
	// a drone can only become reserved if it was idle
	public void reserveDrone(int currentStep, int stepsReserved) {
		if (status == JobStatus.IDLE) {
			status = JobStatus.RESERVED;
			returnTimeStep = currentStep + stepsReserved;
		}
	}
	
	// when reservation is over, give drone back to controller
	public void updateReservation(int currentStep) {
		if (status == JobStatus.RESERVED && currentStep >= returnTimeStep) {
			status = JobStatus.ACTIVE;
			returnTimeStep = -1;
		}
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getTopSpeed() {
		return topSpeed;
	}

	public void setTopSpeed(int topSpeed) {
		this.topSpeed = topSpeed;
	}

	public int getSignalRange() {
		return signalRange;
	}

	public void setSignalRange(int signalRange) {
		this.signalRange = signalRange;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}
	
	public JobStatus getStatus() {
		return status;
	}
	
	public int getReturnTimeStep() {
		return returnTimeStep;
	}
}