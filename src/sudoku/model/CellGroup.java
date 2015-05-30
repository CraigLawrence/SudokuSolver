package sudoku.model;

import java.util.HashSet;
import java.util.Set;

public class CellGroup {
	
	/*
	 * Attributes
	 */
	
	private Set<Cell> cells;
	
	/*
	 * Constructor
	 */
	
	public CellGroup() {
		cells = new HashSet<Cell>();
	}
	
	/*
	 * Methods
	 */
	
	public Validity isValid(){
		return Validity.INVALID;
	}
	
	public Set<Integer> missingValues(){
		return null;
	}
	
}
