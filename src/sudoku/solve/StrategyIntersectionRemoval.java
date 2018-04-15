package sudoku.solve;

import java.util.HashSet;
import java.util.Set;

import sudoku.model.Board;
import sudoku.model.Cell;
import sudoku.model.CellGroup;

public class StrategyIntersectionRemoval implements Strategy {
	
	private StrategyMode mode;
	private int min = 2;
	private int max = 3;
	
	public StrategyIntersectionRemoval(StrategyMode mode) {
		this.mode = mode;
	}

	@Override
	public Set<Board> apply(Board b) {
		Set<Board> branches = new HashSet<Board>();
		
		// These data structures will hold the found naked set
		Set<Cell> pointerCells = null;
		Character pointerValue = null;	
		Set<CellGroup> intersectingGroups = null;
		
		// Consider each cell group
		FIND_AN_INTERSECTION:
		for (CellGroup cg : b.getCellGroups().values())
		{
			// Consider each missing value from the cell group
			for (Character v : cg.missingValues())
			{
				// Generate the list of possible cell locations
				Set<Cell> possibleLocations = cg.possibleCells(v);
				
				// Does it have 2-3 possible cell positions?
				if (possibleLocations.size() >= min && possibleLocations.size() <= max)
				{
					// If yes, do those cells shares 2(+) cell groups?
					Set<CellGroup> sharedGroups = Cell.sharedCellGroups(possibleLocations);
					if (sharedGroups.size() > 1)
					{
						pointerCells = possibleLocations;
						pointerValue = v;
						intersectingGroups = sharedGroups;
						break FIND_AN_INTERSECTION;
					}
				}
			}
		}
		
		// Apply excluding or branching behaviour
		if (pointerCells != null && pointerValue != null)
		{
			switch (mode)
			{
			case BRANCHING:
				// TODO: Test
				for (Cell c : pointerCells) {
					c.changeValue(pointerValue);
					branches.add(b.copyBoard());
				}
				break;				
			case EXCLUDING:
				// TODO: Test
				for (CellGroup cg : intersectingGroups) {
					for (Cell c : cg.getCells()) {
						if (pointerCells.contains(c)) continue;
						c.addPossibleValueExclusion(pointerValue);
					}
				}
				break;
			default:
				break;
			}
		}

		return branches;
	}

}
