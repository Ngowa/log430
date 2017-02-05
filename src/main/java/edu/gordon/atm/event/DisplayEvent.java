package edu.gordon.atm.event;

public class DisplayEvent implements ATMEvent {
	private String message;
	
	public DisplayEvent(String message){
		this.message = message;
	}
	
	public String getMessage(){
		return this.message;
	}
}
