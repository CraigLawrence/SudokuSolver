package sudoku.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Cell {
	
	/*
	 * Attributes
	 */

	private char value;
	private boolean definedAtStart;
	private Set<CellGroup> groups;
	
	/*
	 * Constructor
	 */
	
	public Cell(char val, boolean defAtStart, CellGroup ... cgs){
		// TODO: enforce at least one group
		value = val;
		definedAtStart = defAtStart;
		groups =  new HashSet<CellGroup>();
		groups.addAll(Arrays.asList(cgs));
	}
	
	/*
	 * Methods
	 */
	
	public char getValue() {
		return value;
	}
	
	public boolean wasDefinedAtStart() {
		return definedAtStart;
	}
	
	public Set<Character> possibleValues(){
		Set<Character> pvs = null;
		
		Iterator<CellGroup> i = groups.iterator();
		pvs = i.next().missingValues();
		while (i.hasNext()){
			pvs.retainAll(i.next().missingValues());
		}
		
		return pvs;
	}
	
}
