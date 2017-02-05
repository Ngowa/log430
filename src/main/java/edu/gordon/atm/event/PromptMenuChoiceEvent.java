package edu.gordon.atm.event;

public class PromptMenuChoiceEvent implements ATMEvent {
	private String prompt;
	private String[] menu;
	
	public PromptMenuChoiceEvent(String prompt, String[] menu) {
		this.prompt = prompt;
		this.menu = menu;
	}
	
	public String[] getMenu() {
		return menu;
	}
	
	public String getPrompt() {
		return prompt;
	}
}
