package sudoku.solve;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import sudoku.model.Board;
import sudoku.model.Cell;
import sudoku.model.CellGroup;

public class StrategyHiddenSets implements Strategy {
	
	private StrategyMode mode;
	private int N;
	
	public StrategyHiddenSets(int N, StrategyMode mode) {
		this.mode = mode;
		this.N = N;
	}

	@Override
	public Set<Board> apply(Board b) {
		Set<Board> branches = new HashSet<Board>();
		
		// These data structures will hold the found naked set
		Set<Cell> hiddenSetCells = null;
		Set<Character> hiddenSetValues = null;	
		CellGroup hiddenSetCG = null;
		
		// Consider each cell group
		FIND_A_HIDDEN_SET:
		for (CellGroup cg : b.getCellGroups().values())
		{
			// This data structures will hold possible candidate naked sets, we have one when N cells -> N characters
			HashMap<Set<Character>, Set<Cell>> candidates = new HashMap<Set<Character>, Set<Cell>>();

			// Consider each missing value within the group
			for (Character v : cg.missingValues())
			{		
				// Generate the list of possible cell locations
				Set<Cell> possibleLocations = cg.possibleCells(v);
				
				// If it has <= N
				if (possibleLocations.size() <= N)
				{
					// Test if it can be added to each existing candidate set, if so do we have a set of N cells?			
					// Also, create a new candidate set and test if we have a set of N cells (relevant for N = 1)
					Set<Character> values = new HashSet<Character>();
					Set<Cell> cells = new HashSet<Cell>();
					candidates.put(values, cells);
					
					for (Entry<Set<Character>, Set<Cell>> e : candidates.entrySet())
					{
						values = e.getKey(); cells = e.getValue();
						// Test addition of c's possible cells
						Set<Cell> tempCopy = new HashSet<Cell>(cells);
						tempCopy.addAll(possibleLocations);
						
						// If possible add it
						if (tempCopy.size() <= N){
							values.add(v);
							e.setValue(tempCopy);
							cells = tempCopy;
						}
						
						// Test if we've found a naked set
						if (values.size() == N && cells.size() == N)
						{
							hiddenSetValues = values;
							hiddenSetCells = cells;
							hiddenSetCG = cg;
							break FIND_A_HIDDEN_SET;
						}
					}
				}
			}		
		}
		
		// Apply excluding or branching behaviour
				if (hiddenSetValues !=  null && hiddenSetCells != null)
				{
					switch (mode)
					{
					case BRANCHING:
						
						// Use recursion to add a Board with every possible combination of naked set values to the pool
						BranchHelper (0, hiddenSetValues.toArray(), branches, b, hiddenSetCG);
						
						break;				
					case EXCLUDING:
						//TODO:
						
						break;
					default:
						break;
					}
				}
		
		return branches;
	}
	
	private void BranchHelper (int i, Object[] values, Set<Board> branches, Board b, CellGroup cg)
	{
		char v = (char) values[i];
		for (Cell c : cg.possibleCells(v)){
			
			c.changeValue(v);
			
			if (i == values.length - 1) {
				// Base case: last cell
				branches.add(b.copyBoard());
			}
			else {
				BranchHelper (i+1, values, branches, b, cg);
			}
			
			c.changeValue(b.getEmptyValue()); // Reset this board so future calls to possibleCells works		
			
		}	
	}

}
