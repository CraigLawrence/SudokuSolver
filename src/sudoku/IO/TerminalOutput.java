package sudoku.IO;

import java.util.List;
import java.util.Map;

import sudoku.model.Board;
import sudoku.model.Cell;
import sudoku.model.CellGroup;

public class TerminalOutput implements SudokuOutput {

	@Override
	public void outputBoard(Board b) {
		Map<String, CellGroup> cgs = b.getCellGroups();
		for (int row=0; row<9; row++) {
			List<Cell> cells = cgs.get("row"+row).getCells();
			for (Cell c : cells) {
				System.out.printf("%c", c.getValue());
			}
			System.out.println();
		}
	}

}
