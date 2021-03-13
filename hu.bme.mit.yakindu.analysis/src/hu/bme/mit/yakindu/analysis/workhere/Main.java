package hu.bme.mit.yakindu.analysis.workhere;

import org.eclipse.emf.common.util.TreeIterator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Transition;
import org.yakindu.sct.model.stext.stext.EventDefinition;
import org.yakindu.sct.model.stext.stext.VariableDefinition;
import org.yakindu.sct.model.sgraph.Statechart;



import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	@Test
	public void test() {
		main(new String[0]);
	}
	
	public static void main(String[] args) {
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		// Reading model
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		//int count=1;
		
		List<String> eventdefList=new ArrayList<>();
		List<String> variabledefList=new ArrayList<>();
		
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			
			if(content instanceof EventDefinition) {
				EventDefinition eventdef=(EventDefinition) content;
				eventdefList.add(eventdef.getName());
			}
			else if(content instanceof VariableDefinition) {
				VariableDefinition variabledef=(VariableDefinition) content;
				variabledefList.add(variabledef.getName());
			}
			
			/*if(content instanceof State) {
				State state = (State) content;
				if(state.getOutgoingTransitions().size()==0) {
					if(state.getName().equals("noname")) {
						String newName="STATE"+count;
						state.setName(newName);
						System.out.println("Trap: " + state.getName());
						count+=1;
					}
					else {
						System.out.println("Trap: " + state.getName());
					}
				}
				
				else if(state.getName().equals("noname")) {
					String newName="STATE"+count;
					state.setName(newName);
					System.out.println(state.getName());
					count+=1;
				}
				
				else {
					System.out.println(state.getName());
				}
			}
			else if(content instanceof Transition) {
				Transition t=(Transition) content;
				if(t.getSource().getName().equals("")) {
					System.out.println(t.getTarget().getName());
				}
				else {
					System.out.println(t.getSource().getName()  +" -> "+ t.getTarget().getName());
				}
			}*/
		}
		
		
		System.out.println("	public static void main(String[] args) throws IOException {	\r\n" + 
				"		ExampleStatemachine s = new ExampleStatemachine();\r\n" + 
				"		s.setTimer(new TimerService());\r\n" + 
				"		RuntimeService.getInstance().registerStatemachine(s, 200);\r\n" + 
				"		s.init();\r\n" + 
				"		s.enter();\r\n" + 
				"		\r\n" + 
				"		Scanner sc=new Scanner(System.in);\r\n" + 
				"		while(true) {\r\n" + 
				"			String inputStream=sc.next();\r\n" + 
				"			\r\n" +
				"			switch(inputStream) {\r\n");
		
		
		for(String ev: eventdefList) {
			String capNAME = ev.substring(0,1).toUpperCase() + ev.substring(1);
			System.out.println("				case  \""+ev+ "\":\r\n" +
					"					s.raise"+capNAME+"();\r\n"+
					"					s.runCycle();\r\n" + 
					"					break;\r\n");
		}
		System.out.println(
				"				case \"exit\":\r\n" + 
				"					sc.close();\r\n" + 
				"					System.exit(0);\r\n" + 
				"				\r\n" + 
				"				default:\r\n" + 
				"					break;\r\n" + 
				"			}\r\n" + 
				"			print(s);\r\n" + 
				"		}\r\n" + 
				"	}");
		
		System.out.println("public static void print(IExampleStatemachine s) {");
		for(String var: variabledefList) {
			String NAME = var.toUpperCase();
			System.out.println("\tSystem.out.println(\"" + NAME.charAt(0) + " = \" + s.getSCInterface().get" + var + "());");
		}
		System.out.println("}");
		

		/*4.4.
		 * 
		 * System.out.println("public static void print(IExampleStatemachine s) {");
		for(String ev: eventdefList) {
			String NAME = ev.toUpperCase();
			System.out.println("\tSystem.out.println(\"" + NAME.charAt(0) + " = \" + s.getSCInterface().get" + ev + "());");
		}
		for(String var: variabledefList) {
			String NAME = var.toUpperCase();
			System.out.println("\tSystem.out.println(\"" + NAME.charAt(0) + " = \" + s.getSCInterface().get" + var + "());");
		}
		System.out.println("}");*/
		
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}
