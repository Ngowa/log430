package edu.gordon.atm.event;

public class RequestAmountEvent implements ATMEvent {
	private String prompt;
	
	public RequestAmountEvent(String prompt){
		this.prompt = prompt;
	}
	
	public String getPrompt() {
		return prompt;
	}
}
