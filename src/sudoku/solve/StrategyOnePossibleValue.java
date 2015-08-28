package sudoku.solve;

import java.util.HashSet;
import java.util.Set;

import sudoku.model.Board;
import sudoku.model.Cell;
import sudoku.model.CellGroup;

/*
 * This strategy checks the possible values for each unassigned cell,
 * and if there is one possible value, assigns it.
 */

public class StrategyOnePossibleValue implements Strategy {

	@Override
	public Set<Board> apply(Board b) {
		Set<Board> branches = new HashSet<Board>();
		Board board = b.copyBoard();
		boolean changed = false;
		
		Set<Character> possibleValues;
		// Consider each cell
		for (CellGroup cellgroup : board.getCellGroups().values()) {
			for (Cell cell : cellgroup.getCells()) {
				// Assign a new value if the cell is empty
				if (cell.getValue() == board.getEmptyValue()){
					possibleValues = cell.possibleValues();
					// Assign a value if there's only one possibility
					if (possibleValues.size() == 1){
						changed = true;
						cell.changeValue((char) possibleValues.toArray()[0]);
					}
				}
			}
		}
		
		// Add new board to pool
		if (changed)
			branches.add(board);		
		return branches;
	}

}
