package edu.gordon.simulation;

import java.awt.Button;
import java.awt.Component;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class GUIQuery {
	private static HashMap<String, Component> components = new HashMap<String, Component>();
	
	public static void add(String key, Component value){
		components.put(key, value);
	}
	
	public static Component get(String key){
		Component component = components.get(key);
		if(component == null)
			System.err.println("\"" + key + "\" is not a valid component.");
		return component;
	}
	
	public static TextField getTextField(String key){
		return (TextField) get(key);
	}
	
	public static Button getButton(String key){
		return (Button) get(key);
	}
	
	public static void setTextField(String key, String value){
		TextField tf = getTextField(key);
		tf.setText(value);
		triggerListeners(tf.getActionListeners(), null);
		waitForOtherThreads();
	}
	
	public static void clickButton(String key){
		Button btn = getButton(key);
		triggerListeners(btn.getActionListeners(), new ActionEvent(btn, 1, key));
		waitForOtherThreads();
	}
	
	private static void waitForOtherThreads() {
		try {
			Thread.sleep(125);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static void triggerListeners(ActionListener[] listeners, ActionEvent event){
		for(ActionListener l : listeners){
			l.actionPerformed(event);
		}
	}
}
