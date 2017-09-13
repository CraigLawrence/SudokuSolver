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
	 * Checks the number of cell groups another cell has in common with this one
	 * @param otherCell
	 * @return
	 */
	public Set<CellGroup> sharedCellGroups(Cell otherCell){
		Set<CellGroup> temp = new HashSet<CellGroup>();
		temp.addAll(groups);
		temp.retainAll(otherCell.getCellGroups());
		return temp;
	}
	
}
