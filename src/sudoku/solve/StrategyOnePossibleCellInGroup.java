package sudoku.solve;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import sudoku.model.Board;
import sudoku.model.Cell;
import sudoku.model.CellGroup;

/*
 * This strategy considers each value, checks within a given CellGroup
 * which cells could hold that value, and assigns if there is only one. 
 */


public class StrategyOnePossibleCellInGroup implements Strategy {

	@Override
	public Set<Board> apply(Board b) {
		Set<Board> branches = new HashSet<Board>();
		Board board = b.copyBoard();
		boolean changed = false;		
		
		// Consider each cell group
		for (CellGroup cg : board.getCellGroups().values()) {
			// Create a collection for each value
			HashMap<Character, Set<Cell>> possiblePlaces = new HashMap<>();
			for (char v : board.getNumberSet()) {
				possiblePlaces.put(v, new HashSet<Cell>());
			}
			// Consider each cell
			for (Cell c : cg.getCells()) {
				if (c.getValue() != board.getEmptyValue())
					continue;
				// Get possible values
				Set<Character> pvs = c.possibleValues();
				// Add this cell to corresponding collections
				for (char pv : pvs) {
					possiblePlaces.get(pv).add(c);
				}
			}
			// Consider each value
			for (char v : board.getNumberSet()) {
				// If it's collection has only one cell, assign the cell that value
				if (possiblePlaces.get(v).size() == 1) {
					Cell c = (Cell) possiblePlaces.get(v).toArray()[0];
					c.changeValue(v);
					changed = true;
				}
			}
		}
		
		if (changed)
			branches.add(board);		
		return branches;
	}

}
