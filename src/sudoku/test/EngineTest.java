package sudoku.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import sudoku.IO.BasicTextInput;
import sudoku.IO.SudokuInput.SudokuInputReadException;
import sudoku.IO.SudokuOutput;
import sudoku.IO.TerminalOutput;
import sudoku.model.Board;
import sudoku.model.Board.BoardCreationException;
import sudoku.solve.*;

public class EngineTest {

	@Test
	public void SanityTest() throws BoardCreationException, SudokuInputReadException {
		Engine e = new EngineV1();
		assertNotNull(e);
	}
	
	@Test
	public void FileTest5() throws BoardCreationException, SudokuInputReadException {
		Board b = new Board(new BasicTextInput("test5OnePossValueTest.txt"), 9);
		Engine e = new EngineV1();
		Board solution = e.solve(b);
		
		boolean CompareResult = false;
		try {
			CompareResult = TestUtils.boardCompare(solution, "test5OnePossValueTestGold.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		assertTrue(CompareResult);
	}

}
