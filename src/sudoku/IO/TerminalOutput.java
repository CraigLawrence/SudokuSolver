package sudoku.IO;

import sudoku.model.Board;

public class TerminalOutput implements SudokuOutput {

	@Override
	public void outputBoard(Board b) {
		System.out.println(b.toString());
	}

}
