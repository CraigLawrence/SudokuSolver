package sudoku.test;

import static org.junit.Assert.*;

import java.io.IOException;
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

	@Test
	public void OnePossibleValueTest1() throws BoardCreationException, SudokuInputReadException {
		Board b = new Board(new BasicTextInput("test5OnePossValueTest.txt"), 9);
		Strategy s = new StrategyOnePossibleValue();
		Set<Board> result = s.apply(b);
		assertTrue(result.size() == 1);
		
		boolean CompareResult = false;
		try {
			CompareResult = TestUtils.boardCompare((Board) result.toArray()[0], "test5OnePossValueTestGold.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		assertTrue(CompareResult);
	}

}
