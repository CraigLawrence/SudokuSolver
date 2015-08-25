package sudoku.solve;

import java.util.HashSet;
import java.util.Set;

import sudoku.model.Board;
import sudoku.model.Cell;
import sudoku.model.CellGroup;

/*
 * This strategy checks the possible values for unassigned cell,
 * and if there is one possible value, assigns it.
 */

public class StrategyOnePossibleValue implements Strategy {

	@Override
	public Set<Board> apply(Board b) {
		Set<Board> branches = new HashSet<Board>();
		Board board = b.copyBoard();
		boolean changed = false;
		
		Set<Character> possibleValues;
		for (CellGroup cellgroup : board.getCellGroups().values()) {
			for (Cell cell : cellgroup.getCells()) {
				if (cell.getValue() == board.getEmptyValue()){
					possibleValues = cell.possibleValues();
					if (possibleValues.size() == 1){
						changed = true;
						cell.changeValue((char) possibleValues.toArray()[0]);
					}
				}
			}
		}
		
		if (changed)
			branches.add(board);		
		return branches;
	}

}
