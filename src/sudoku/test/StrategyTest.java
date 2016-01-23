package sudoku.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Set;

import org.junit.Test;

import sudoku.IO.*;
import sudoku.IO.SudokuInput.SudokuInputReadException;
import sudoku.model.*;
import sudoku.model.Board.*;
import sudoku.solve.*;

public class StrategyTest {
	
	private void OnePossibleValueHelper(String inputFile, String goldFile) throws BoardCreationException, SudokuInputReadException {
		Board b = new Board(new BasicTextInput(inputFile), 9);
		Strategy s = new StrategyOnePossibleValue();
		Set<Board> result = s.apply(b);
		assertTrue(result.size() == 1);
		
		boolean CompareResult = false;
		try {
			CompareResult = TestUtils.boardCompare((Board) result.toArray()[0], goldFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		assertTrue(CompareResult);
	}

	@Test
	public void OnePossibleValueTest1() throws BoardCreationException, SudokuInputReadException {
		OnePossibleValueHelper("test51OnePossValueTest.txt", "test51OnePossValueTestGold.txt");
	}
	
	@Test
	public void OnePossibleValueTest2() throws BoardCreationException, SudokuInputReadException {
		OnePossibleValueHelper("test52OnePossValueTest.txt", "test52OnePossValueTestGold.txt");
	}
	
	
	private void OnePossibleCellHelper(String inputFile, String goldFile) throws BoardCreationException, SudokuInputReadException {
		Board b = new Board(new BasicTextInput(inputFile), 9);
		Strategy s = new StrategyOnePossibleCellInGroup();
		Set<Board> result = s.apply(b);
		assertTrue(result.size() == 1);
		
		boolean CompareResult = false;
		try {
			CompareResult = TestUtils.boardCompare((Board) result.toArray()[0], goldFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		assertTrue(CompareResult);
	}

	@Test
	public void OnePossibleCellTest1() throws BoardCreationException, SudokuInputReadException {
		OnePossibleCellHelper("test61OnePossCellTest.txt", "test61OnePossCellTestGold.txt");
	}
	
	
	private void NakedPairHelperBranching(String inputFile, String goldFile1, String goldFile2) throws BoardCreationException, SudokuInputReadException {
		Board b = new Board(new BasicTextInput(inputFile), 9);
		Strategy s = new StrategyNakedPairs(StrategyMode.BRANCHING);
		Set<Board> result = s.apply(b);
		assertTrue(result.size() == 2);
		
		for (Board b1 : result){
			try {
				assertTrue(TestUtils.boardCompare(b1, goldFile1) || TestUtils.boardCompare(b1, goldFile2));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void NakedPairTest1() throws BoardCreationException, SudokuInputReadException {
		NakedPairHelperBranching("test71NakedPairTest.txt", "test71NakedPairTestGold1.txt", "test71NakedPairTestGold2.txt");
	}
	
	@Test
	public void NakedPairTest2() throws BoardCreationException, SudokuInputReadException {
		Board b = new Board(new BasicTextInput("test72NakedPairTest.txt"), 9);
		Strategy s = new StrategyNakedPairs(StrategyMode.BRANCHING);
		Set<Board> result = s.apply(b);
		assertTrue(result.size() == 0);
	}
	
	@Test
	public void NakedPairTest3() throws BoardCreationException, SudokuInputReadException {
		Board b = new Board(new BasicTextInput("test71NakedPairTest.txt"), 9);
		Strategy s = new StrategyNakedPairs(StrategyMode.EXCLUDING);
		Set<Board> result = s.apply(b);
		assertTrue(result.size() == 1);
		result = s.apply(result.iterator().next());
		assertTrue(result.size() == 0);
	}
	
	
	@Test
	public void ScatterShotTest1() throws BoardCreationException, SudokuInputReadException {
		Board b = new Board(new BasicTextInput("test1Blank.txt"), 9);
		Strategy s = new StrategyScatterShot();
		Set<Board> result = s.apply(b);
		
		assertTrue(result.size() == 9);
		for (Board b2 : result) {
			assertTrue(b2.isValid() == Validity.VALID_COMPLETE || b2.isValid() == Validity.VALID_INCOMPLETE);
		}
	}
	
}
