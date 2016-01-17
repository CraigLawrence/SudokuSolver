package sudoku.solve;

import java.util.HashSet;
import java.util.Set;

import sudoku.model.Board;
import sudoku.model.Cell;
import sudoku.model.CellGroup;

public class StrategyNakedPairs implements Strategy {

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
					if (b.getAllCells().get(j).possibleValues().equals(pvs)){
						// Found a naked pair
						
						// Create two branches
						Object[] options = pvs.toArray();
						b.getAllCells().get(i).changeValue((char)options[0]);
						b.getAllCells().get(j).changeValue((char)options[1]);						
						Board board1 = b.copyBoard();
						
						b.getAllCells().get(i).changeValue((char)options[1]);
						b.getAllCells().get(j).changeValue((char)options[0]);	
						Board board2 = b.copyBoard();
						
						b.getAllCells().get(i).changeValue('0');
						b.getAllCells().get(j).changeValue('0');
						
						branches.add(board1);
						branches.add(board2);
						
						// TODO: Maybe don't branch and exclude these from other cells' possible values
						
					}
				}
			}
		}
		
		return branches;
	}

}
