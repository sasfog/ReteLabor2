package hu.bme.mit.yakindu.analysis.workhere;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Vertex;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	@Test
	public void test() {
		main(new String[0]);
	}
	
	private static int counter = 1;
	public static void generateName(List<String> names, State state) {
		if(state.getName().isEmpty()) {
			String name = String.format("customState %s",counter++); 
			while(names.contains(name))
				name = String.format("customState %s",counter++);
			names.add(name);
			state.setName(name);
		}
	}
	
	public static List<String> fillNames(TreeIterator<EObject> iterator) {
		List<String> nameList = new ArrayList<String>();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if (content instanceof Vertex) {
				Vertex v = (Vertex) content;
				if(!v.getName().isEmpty()) {
					nameList.add(v.getName());
				}
			}
		}
		return nameList;
	}
	public static void main(String[] args) {
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		// Reading model
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		TreeIterator<EObject> nameIterator = s.eAllContents();
		List<String> names = fillNames(nameIterator);
		State previousState = null;
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof State) {
				State state = (State) content;
				
				// Task 2.5
				generateName(names, state);
				
				// Task 2.3
				if(previousState != null) {
					System.out.println(String.format("%s - %s",previousState.getName(), state.getName()));
				}
				previousState = state;
				System.out.println(state.getName());
				
				// Task 2.4
				Vertex v = (Vertex) state;
				if(v.getOutgoingTransitions().size() == 0)
					System.out.println(state.getName());
				
			}
				
		}
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}
