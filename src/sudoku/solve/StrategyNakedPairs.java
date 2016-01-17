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
					Cell otherCell = b.getAllCells().get(j);
					if (otherCell.getValue() != b.getEmptyValue()){
						continue;
					}
					
					if (otherCell.possibleValues().equals(pvs)){
						// May've found a naked pair, now check they share 1+ cell groups
						Set<CellGroup> temp = new HashSet<CellGroup>();
						temp.addAll(c.getCellGroups());
						temp.retainAll(otherCell.getCellGroups());
						
						if (temp.size() > 0) {						
							// Create two branches
							Object[] options = pvs.toArray();
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
							
							// TODO: Maybe don't branch and exclude these from other cells' possible values
						}
						
					}
				}
			}
		}
		
		return branches;
	}

}
