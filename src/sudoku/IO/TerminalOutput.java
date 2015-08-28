package sudoku.IO;

import java.util.List;
import java.util.Map;

import sudoku.model.Board;
import sudoku.model.Cell;
import sudoku.model.CellGroup;

public class TerminalOutput implements SudokuOutput {

	@Override
	public void outputBoard(Board b) {
		System.out.println(b.toString());
	}

}
