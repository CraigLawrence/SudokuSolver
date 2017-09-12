package sudoku.solve;

import java.util.HashSet;
import java.util.Set;

import sudoku.model.Board;
import sudoku.model.Cell;
import sudoku.model.CellGroup;

public class StrategyNakedPairs implements Strategy {
	
	private StrategyMode mode;
	
	public StrategyNakedPairs(StrategyMode mode) {
		this.mode = mode;
	}

	@Override
	public Set<Board> apply(Board b) {
		Set<Board> branches = new HashSet<Board>();
		
		// Consider each cell
		for (int i=0; i<b.getAllCells().size(); i++) {
			Cell c = b.getAllCells().get(i);
			if (c.getValue() != b.getEmptyValue()){
				continue;
			}
			
			Set<Character> pvs = c.possibleValues();
			if (pvs.size() == 2){
				// Check the other cells in the group for the same 2 values
				for (int j=i+1; j<b.getAllCells().size(); j++){
					Cell otherCell = b.getAllCells().get(j);
					if (otherCell.getValue() != b.getEmptyValue()){
						continue;
					}
					
					if (otherCell.possibleValues().equals(pvs)){
						// May've found a naked pair, now check they share 1+ cell groups						
						if (c.numberOfSharedCellGroups(otherCell) >= 1) {
							
							Object[] options = pvs.toArray();
							switch (mode) {
							case EXCLUDING:
								// Exclude the possible values of the naked pair from other cells that share groups.
								// Only continue this branch if an exclusion list is grown.
								boolean changes = false;
								Cell[] pair = new Cell[] {c, otherCell};
								
								for (Cell cell : pair){
									for (CellGroup cg: cell.getCellGroups()){
										for (Cell cgCell : cg.getCells()){
											if (cgCell == pair[0] || cgCell == pair[1])
												continue;
											if (cgCell.addPossibleValueExclusion((char)options[0]) || cgCell.addPossibleValueExclusion((char)options[1]))
												changes = true;
										}
									}
								}
								
								if (changes)
									branches.add(b);
								break;
							case BRANCHING:
								// Using the naked pair, create 2 branches.
								c.changeValue((char)options[0]);
								otherCell.changeValue((char)options[1]);						
								Board board1 = b.copyBoard();
								
								c.changeValue((char)options[1]);
								otherCell.changeValue((char)options[0]);	
								Board board2 = b.copyBoard();
								
								c.changeValue('0');
								otherCell.changeValue('0');
								
								branches.add(board1);
								branches.add(board2);
								break;
							}
							
						}
						
					}
				}
			}
		}
		
		return branches;
	}

}
