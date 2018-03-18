package sudoku.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@SuppressWarnings("serial")
public class Cell implements Serializable{
	
	/*
	 * Attributes
	 */

	private char value;
	private boolean definedAtStart;
	private Set<CellGroup> groups;
	private Set<Character> possibleValueExclusions;
	
	@Override
    public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof Cell)) return false;
		Cell other = (Cell) o;
		return this.toString().equals(other.toString());
	}

	/*
	 * Constructor
	 */
	
	public Cell(char val, boolean defAtStart, CellGroup ... cgs){
		// TODO: enforce at least one group
		value = val;
		definedAtStart = defAtStart;
		groups =  new HashSet<CellGroup>();
		possibleValueExclusions = new HashSet<Character>();
		groups.addAll(Arrays.asList(cgs));
	}
	
	/*
	 * Methods
	 */
	
	public char getValue() {
		return value;
	}
	
	public Set<CellGroup> getCellGroups() {
		return groups;
	}
	
	public void changeValue(char newVal) {
		value = newVal;
	}
	
	public boolean wasDefinedAtStart() {
		return definedAtStart;
	}
	
	public Set<Character> possibleValues(){
		Set<Character> pvs = null;
		
		// Get the values common to the missing values of all cell groups with cell belongs to
		Iterator<CellGroup> i = groups.iterator();
		pvs = i.next().missingValues();
		while (i.hasNext()){
			pvs.retainAll(i.next().missingValues());
		}
		
		// Remove the specified exclusions
		pvs.removeAll(possibleValueExclusions);
		
		return pvs;
	}
	
	public boolean addPossibleValueExclusion(char c) {
		return possibleValueExclusions.add(c);
	}
	
	public boolean removePossibleValueExclusion(char c) {
		return possibleValueExclusions.remove(c);
	}
	
	public String toString() {
		return String.valueOf(value);
	}
	
	/**
	 * Checks the number of cell groups a set of cells have in common
	 * @param cells
	 * @return
	 */
	public static Set<CellGroup> sharedCellGroups(Set<Cell> cells){
		Set<CellGroup> temp = new HashSet<CellGroup>();
		Iterator<Cell> i = cells.iterator();
		
		if (i.hasNext()) temp.addAll(i.next().getCellGroups());
		while (i.hasNext())
			temp.retainAll(i.next().getCellGroups());
		return temp;
	}
	
}
