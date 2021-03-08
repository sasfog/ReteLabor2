package hu.bme.mit.yakindu.analysis.workhere;

import java.io.IOException;
import java.util.Scanner;

import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;



public class RunStatechart {
	
	public static void main(String[] args) throws IOException {
		Scanner input = new Scanner(System.in);
		String command = "";
		ExampleStatemachine s = new ExampleStatemachine();
		s.setTimer(new TimerService());
		RuntimeService.getInstance().registerStatemachine(s, 2000);
		s.init();
		s.enter();
		try {
	        while (!command.matches("exit") && input.hasNextLine()){
	            command = input.nextLine(); 
	        	doEvent(command, s);
	        }
		}
		finally {
			input.close();
		}
		
		s.exit();
		System.out.println("Exiting");
		System.exit(0);
	}

	public static void print(IExampleStatemachine s) {
		System.out.println("W = " + s.getSCInterface().getWhiteTime());
		System.out.println("B = " + s.getSCInterface().getBlackTime());
	}
	
	public static void doEvent(String command, ExampleStatemachine sm) {
		switch(command) {
		case "black":
			sm.raiseBlack();
			break;
		case "white":
			sm.raiseWhite();
			break;
		case "start":
			sm.raiseBlack();
			break;
		default:
			break;
		}
		sm.runCycle();
		print(sm);
	}
}
