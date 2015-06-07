package sudoku.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CellGroup {
	
	/*
	 * Attributes
	 */
	
	private List<Cell> cells;
	private Set<Character> numberSet;
	
	/*
	 * Constructor
	 */
	
	public CellGroup(Set<Character> numberSet) throws CellGroupException {
		if (numberSet ==  null)
			throw new CellGroupException("Number set provided was null");
		cells = new ArrayList<Cell>();
		this.numberSet = numberSet;
	}
	
	/*
	 * Methods
	 */
	
	public Validity isValid(){
		// Iterate over cells and make sure there are no repeated values
		// Except for '0' with represents blank
		
		// Use a ACSII boolean array to keep track
		boolean[] ascii = new boolean[128];
		
		for (Cell c : cells) {
			int value = c.getValue();
			
			// Check if already present in group
			if (ascii[value] && value != '0')
				return Validity.INVALID;
			
			// Set as present
			ascii[value] = true;			
		}
		
		// Check if complete or not (i.e. are there any blanks)
		if (ascii['0'])
			return Validity.VALID_INCOMPLETE;
		else
			return Validity.VALID_COMPLETE;
		
	}
	
	public Set<Character> missingValues(){
		// Return values in numberSet, but not in any cell
		Set<Character> missing = new HashSet<Character>(numberSet);
		for (Cell c : cells) {
			missing.remove(c.getValue());
		}
		return missing;
	}
	
	public List<Cell> getCells() {
		return cells;
	}
	
	public void addCell(Cell newCell) throws CellGroupException{
		if (!numberSet.contains(newCell.getValue()) && newCell.getValue() != '0') {
			throw new CellGroupException("Illegal cell value: " + newCell.getValue());
		}
		cells.add(newCell);
	}
	
	public class CellGroupException extends Exception {
		public CellGroupException(String message) {
			super(message);
		}
	}
	
}
