package sudoku.test;

import static org.junit.Assert.*;

import org.junit.Test;

import sudoku.IO.BasicTextInput;
import sudoku.IO.SudokuInput.SudokuInputReadException;
import sudoku.model.Board;
import sudoku.model.Board.BoardCreationException;
import sudoku.solve.*;

public class EngineTest {

	@Test
	public void test() throws BoardCreationException, SudokuInputReadException {
		Board b = new Board(new BasicTextInput("test1.txt"), 9);
		Engine e = new EngineV1();
		e.solve(b);
	}

}
