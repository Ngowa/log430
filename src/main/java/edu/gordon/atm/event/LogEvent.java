package edu.gordon.atm.event;

public class LogEvent implements ATMEvent {
	private String message;
	
	public LogEvent(String message){
		this.message = message;
	}
	
	public String getMessage(){
		return message;
	}
}
