package hu.bme.mit.yakindu.analysis.workhere;

import java.io.IOException;


import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;

import java.util.Scanner;



public class RunStatechart {
	
	public static void main(String[] args) throws IOException {	
		ExampleStatemachine s = new ExampleStatemachine();
		s.setTimer(new TimerService());
		RuntimeService.getInstance().registerStatemachine(s, 200);
		s.init();
		s.enter();
		
		Scanner sc=new Scanner(System.in);
		while(true) {
			String inputStream=sc.next();
			
			switch(inputStream) {
				case "start":
					s.raiseStart();
					s.runCycle();
					break;
						
				case "exit":
					sc.close();
					System.exit(0);
				
				case "white":
					s.raiseWhite();
					s.runCycle();
					break;
				
				case "black":
					s.raiseBlack();
					s.runCycle();
					break;
				
				default:
					break;
			}
			print(s);
		}
	}

	public static void print(IExampleStatemachine s) {
		System.out.println("W = " + s.getSCInterface().getWhiteTime());
		System.out.println("B = " + s.getSCInterface().getBlackTime());
	}
}
