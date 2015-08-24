package sudoku.test;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import sudoku.IO.BasicTextInput;
import sudoku.IO.SudokuInput.SudokuInputReadException;
import sudoku.IO.SudokuOutput;
import sudoku.IO.TerminalOutput;
import sudoku.model.Board;
import sudoku.model.Board.BoardCreationException;
import sudoku.solve.Strategy;
import sudoku.solve.StrategyOnePossibleValue;

public class StrategyTest {
	
	// TODO: create output class to test against gold file

	@Test
	public void OnePossibleValueTest1() throws BoardCreationException, SudokuInputReadException {
		Board b = new Board(new BasicTextInput("test5OnePossValueTest.txt"), 9);
		Strategy s = new StrategyOnePossibleValue();
		Set<Board> result = s.apply(b);
		assertTrue(result.size() == 1);
		
		SudokuOutput so = new TerminalOutput();
		so.outputBoard((Board) result.toArray()[0]);
	}

}
