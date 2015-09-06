package sudoku.solve;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import sudoku.model.Board;
import sudoku.model.Cell;
import sudoku.model.CellGroup;

/*
 * This strategy picks a random cell group, and a random missing value from it.
 * Then it creates a board with that value in every possible place
 */

public class StrategyScatterShot implements Strategy {

	@Override
	public Set<Board> apply(Board b) {
		Set<Board> branches = new HashSet<Board>();
		
		// Pick a random cell group
		Random r = new Random();
		Collection<CellGroup> groupValues = b.getCellGroups().values();
		CellGroup cg = (CellGroup) groupValues.toArray() [r.nextInt(groupValues.size())];
		
		// Pick a random missing value
		Set<Character> missing = cg.missingValues();
		char chosenValue = (char) missing.toArray() [r.nextInt(missing.size())];
		
		// Consider each cell
		for (Cell c : cg.getCells()) {
			// Is the chosen value a possible value
			if (c.possibleValues().contains(chosenValue)) {
				// If yes, set the value
				c.changeValue(chosenValue);
				// Copy the board, add it to the pool
				branches.add(b.copyBoard());
				// Set it back to empty
				c.changeValue(b.getEmptyValue());
			}
		}
		
		return branches;
	}
	
	

}
