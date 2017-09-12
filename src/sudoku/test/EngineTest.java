package sudoku.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import sudoku.IO.BasicTextInput;
import sudoku.IO.SudokuInput.SudokuInputReadException;
import sudoku.model.Board;
import sudoku.model.Board.BoardCreationException;
import sudoku.solve.*;

public class EngineTest {

	@Test
	public void SanityTest() throws BoardCreationException, SudokuInputReadException {
		System.out.println("---------SanityTest-----------");
		Engine e = new EngineV1();
		assertNotNull(e);
	}
	
	private void EngineHelper(String inputFile, String goldFile) throws BoardCreationException, SudokuInputReadException {	
		Board b = new Board(new BasicTextInput(inputFile), 9);
		Engine e = new EngineV1();
		Board solution = e.solve(b);
		
		boolean CompareResult = false;
		try {
			CompareResult = TestUtils.boardCompare(solution, goldFile);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		assertTrue(CompareResult);
	}
	
	@Test
	public void FileTest5() throws BoardCreationException, SudokuInputReadException {
		System.out.println("---------FileTest5-----------");
		EngineHelper("test51OnePossValueTest.txt", "test51OnePossValueTestGold.txt");
	}
	
	@Test
	public void FileTest0General1_Easy() throws BoardCreationException, SudokuInputReadException {
		System.out.println("---------FileTest0General1_Easy-----------");
		EngineHelper("test0General1.txt", "test0General1Gold.txt");
	}
	
	@Test
	public void FileTest0General2_Medium() throws BoardCreationException, SudokuInputReadException {
		System.out.println("---------FileTest0General2_Medium-----------");
		EngineHelper("test0General2.txt", "test0General2Gold.txt");
	}
	
	
	@Test
	public void CancelTest() throws BoardCreationException, SudokuInputReadException, InterruptedException {
		System.out.println("---------CancelTest-----------");
		final Engine e = new EngineV1();
		final Board b = new Board(new BasicTextInput("test1Blank.txt"), 9);
		class xx extends Thread {
			@Override
			public void run() {
				Board solution = e.solve(b);
				assertNull(solution);
			}
		}
		new xx().start();
		e.cancel();
		Thread.sleep(500); // Give other thread a moment to do assertNull and terminate
	}
	
	/*@Test
	public void BlankTest() throws BoardCreationException, SudokuInputReadException {
		Board b = new Board(new BasicTextInput("test1Blank.txt"), 9);
		Engine e = new EngineV1();
		Board solution = e.solve(b);
		
		assertTrue(solution.isValid() == Validity.VALID_COMPLETE);
	}*/

}
