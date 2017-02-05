package edu.gordon.atm.event;

public class PromptReadPinEvent implements ATMEvent {
	private String prompt;
	
	public PromptReadPinEvent(String prompt) {
		this.prompt = prompt;
	}
	
	public String getPrompt(){
		return prompt;
	}
}
