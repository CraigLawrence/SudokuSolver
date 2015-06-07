package sudoku.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import sudoku.IO.SudokuInput;
import sudoku.IO.SudokuInput.SudokuInputReadException;
import sudoku.model.CellGroup.CellGroupException;

public class Board {
	
	/*
	 * Attributes
	 */

	private Map<String, CellGroup> cellGroups;
	private Set<Character> numberSet;
	
	/*
	 * Constructors
	 */
	
	public Board(SudokuInput si) throws BoardCreationException{
		this(si, 9);
	}
	
	public Board(SudokuInput si, int size) throws BoardCreationException {
		cellGroups = new HashMap<String, CellGroup>();
		numberSet = new HashSet<Character>(Arrays.asList('1','2','3','4','5','6','7','8','9'));
		
		// TODO: Generalise this code, remove magic numbers
		try {
		
			switch (size){
			
			case 9:		
	
				// Create 9 rows, columns, quadrants
				String[] types = {"row", "col", "quad"};
				for (String t : types) {
					for (Integer i=0; i<9; i++) {
						CellGroup cg = new CellGroup(numberSet);
						cellGroups.put(t + i.toString() , cg);
					}
				}
				// Fill board
				int charsRead = 0;
				
	
				while (si.hasNext()){
					char numberRead = (char) si.getNext();
					int rowNum = charsRead/9;
					int colNum = charsRead%9;
					int quadNum = 3*(rowNum/3) + (colNum/3);
					
					CellGroup row = cellGroups.get("row"+Integer.toString(rowNum));
					CellGroup col = cellGroups.get("col"+Integer.toString(colNum));
					CellGroup quad = cellGroups.get("quad"+Integer.toString(quadNum));
					
					Cell newCell =  new Cell(numberRead, true, row, col, quad);							
					row.addCell(newCell); col.addCell(newCell); quad.addCell(newCell);
					
					charsRead++;
					}
				
				if (charsRead != 81){
					throw new BoardCreationException("Input file has incorrect size board");
				}
				break;
			
			default:	throw new BoardCreationException("Invalid board size specified");
			}
			
		} 	catch (SudokuInputReadException e) {
				throw new BoardCreationException("Input board failed to read");
		}	catch (CellGroupException e) {
				throw new BoardCreationException(e.getMessage());
		}
	}
	
	/*
	 * Methods
	 */
	
	public Map<String, CellGroup> getCellGroups() {
		return cellGroups;
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