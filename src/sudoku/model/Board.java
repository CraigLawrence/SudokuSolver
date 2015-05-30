package sudoku.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import sudoku.IO.SudokuInput;

public class Board {
	
	/*
	 * Attributes
	 */

	private Map<String, CellGroup> cellGroups;
	private Set<Integer> numberSet;
	
	/*
	 * Constructors
	 */
	
	public Board(SudokuInput si) throws BoardCreationException{
		this(si, 9);
	}
	
	public Board(SudokuInput si, int size) throws BoardCreationException {
		cellGroups = new HashMap<String, CellGroup>();
		numberSet = new HashSet<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9));
		
		switch (size){
		
		case 9:		// Create 9 rows, columns, quadrants
					String[] types = {"row", "col", "quad"};
					for (String t : types) {
						for (Integer i=0; i<9; i++) {
							CellGroup cg = new CellGroup();
							cellGroups.put(t + i.toString() , cg);
						}
					}
					// Fill board
					break;
		
		default:	throw new BoardCreationException("Invalid board size specified");
		}
	}
	
	/*
	 * Exceptions
	 */
	
	public class BoardCreationException extends Exception {
		public BoardCreationException(String message) {
			super(message);
		}
	}
}