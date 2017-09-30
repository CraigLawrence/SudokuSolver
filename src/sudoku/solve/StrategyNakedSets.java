package sudoku.solve;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import sudoku.model.Board;
import sudoku.model.Cell;
import sudoku.model.CellGroup;

public class StrategyNakedSets implements Strategy {
	
	private StrategyMode mode;
	private int N;
	
	public StrategyNakedSets(int N, StrategyMode mode) {
		this.mode = mode;
		this.N = N;
	}

	@Override
	public Set<Board> apply(Board b) {
		Set<Board> branches = new HashSet<Board>();
		
		// These data structures will hold the found naked set
		Set<Cell> nakedSetCells = null;
		Set<Character> nakedSetValues = null;		
		
		// Consider each cell group
		FIND_A_NAKED_SET:
		for (CellGroup cg : b.getCellGroups().values())
		{
			// This data structures will hold possible candidate naked sets, we have one when N cells -> N characters
			HashMap<Set<Cell>, Set<Character>> candidates = new HashMap<Set<Cell>, Set<Character>>();

			// Consider each cell with the group
			for (Cell c : cg.getCells())
			{		
				// Generate the list of possible values
				Set<Character> possibleValues = c.possibleValues();
				
				// If it has <= N
				if (possibleValues.size() <= N && c.getValue() == b.getEmptyValue())
				{			
					// Test if it can be added to each existing candidate set, if so do we have a set of N cells?			
					// Also, create a new candidate set and test if we have a set of N cells (relevant for N = 1)
					Set<Cell> cells = new HashSet<Cell>();
					Set<Character> values = new HashSet<Character>();
					candidates.put(cells, values);
					
					for (Entry<Set<Cell>, Set<Character>> e : candidates.entrySet())
					{
						cells = e.getKey(); values = e.getValue();
						// Test addition of c's possible Values
						Set<Character> tempCopy = new HashSet<Character>(values);
						tempCopy.addAll(possibleValues);
						
						// If possible add it
						if (tempCopy.size() <= N){
							cells.add(c);
							e.setValue(tempCopy);
							values = tempCopy;
						}
						
						// Test if we've found a naked set
						if (cells.size() == N && values.size() == N)
						{
							nakedSetCells = cells;
							nakedSetValues = values;
							break FIND_A_NAKED_SET;
						}
					}					
				}
			}
		}
		
		// Apply excluding or branching behaviour
		if (nakedSetCells !=  null && nakedSetValues != null)
		{
			switch (mode)
			{
			case BRANCHING:
				
				// Use recursion to add a Board with every possible combination of naked set values to the pool
				BranchHelper (0, nakedSetCells.toArray(), branches, b);
				
				break;				
			case EXCLUDING:
				
				// Exclude the possible values of the naked set from other cells that share groups.
				// Only continue this branch if an exclusion list is grown.
				boolean changes = false;
				
				for (CellGroup cg: Cell.sharedCellGroups(nakedSetCells)){
					for (Cell cgCell : cg.getCells()){
						if (nakedSetCells.contains(cgCell))
							continue;
						
						for (char v : nakedSetValues){
							if (cgCell.addPossibleValueExclusion(v))
								changes = true;
						}
					}
				}
				
				if (changes)
					branches.add(b);
				
				break;
			default:
				break;
			}
		}
	
		return branches;
	}
	
	private void BranchHelper (int i, Object[] cells, Set<Board> branches, Board b)
	{
		Cell c = (Cell) cells[i];
		for (char p : c.possibleValues()){
			
			c.changeValue(p);
			
			if (i == cells.length - 1) {
				// Base case: last cell
				branches.add(b.copyBoard());
			}
			else {
				BranchHelper (i+1, cells, branches, b);
			}
			
		}	
	}

}
