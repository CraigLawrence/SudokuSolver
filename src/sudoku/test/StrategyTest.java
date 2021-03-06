package sudoku.test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

import sudoku.IO.*;
import sudoku.IO.SudokuInput.SudokuInputReadException;
import sudoku.model.*;
import sudoku.model.Board.*;
import sudoku.solve.*;

public class StrategyTest {
	
	@Test
	public void NakedSingleBranchTest1() throws BoardCreationException, SudokuInputReadException {
		GoldCompareHelper("test51NakedSingleTest.txt", new StrategyNakedSets(1, StrategyMode.BRANCHING), "test51NakedSingleTestGold.txt");
	}
	
	@Test
	public void NakedSingleBranchTest2() throws BoardCreationException, SudokuInputReadException {
		GoldCompareHelper("test52NakedSingleTest.txt", new StrategyNakedSets(1, StrategyMode.BRANCHING), "test52NakedSingleTestGold.txt");
	}
	
	@Test
	public void NakedPairBranchTest1() throws BoardCreationException, SudokuInputReadException {
		GoldCompareHelper("test71NakedPairTest.txt", new StrategyNakedSets(2, StrategyMode.BRANCHING), "test71NakedPairTestGold1.txt", "test71NakedPairTestGold2.txt");
	}
	
	@Test
	public void NakedPairBranchTest2() throws BoardCreationException, SudokuInputReadException {
		GoldCompareHelper("test72NakedPairTest.txt", new StrategyNakedSets(2, StrategyMode.BRANCHING));
	}
	
	@Test
	public void NakedTripleBranchTest1() throws BoardCreationException, SudokuInputReadException {
		GoldCompareHelper("test75NakedTripleTest.txt", new StrategyNakedSets(3, StrategyMode.BRANCHING), "test75NakedTripleTestGold1.txt", "test75NakedTripleTestGold2.txt", "test75NakedTripleTestGold3.txt");
	}
	
	/* Disabaled since test find another valid naked quad in the test data
	@Test
	public void NakedQuadBranchTest1() throws BoardCreationException, SudokuInputReadException {
		GoldCompareHelper("test77NakedQuadTest.txt", new StrategyNakedSets(4, StrategyMode.BRANCHING), "test77NakedQuadTestGold1.txt", "test77NakedQuadTestGold2.txt", "test77NakedQuadTestGold3.txt", "test77NakedQuadTestGold4.txt", "test77NakedQuadTestGold5.txt", "test77NakedQuadTestGold6.txt");
	}
	*/
	
	@Test
	public void NakedPairExcludeTest1() throws BoardCreationException, SudokuInputReadException {
		Board b = new Board(new BasicTextInput("test71NakedPairTest.txt"), 9);
		Strategy s = new StrategyNakedSets(2, StrategyMode.EXCLUDING);
		Set<Board> result = s.apply(b);
		assertTrue(result.size() == 1);
		result = s.apply(result.iterator().next());
		assertTrue(result.size() == 0);
	}
	
	// TODO: better testing of naked excluding

	@Test
	public void HiddenSingleBranchTest1() throws BoardCreationException, SudokuInputReadException {
		GoldCompareHelper("test61HiddenSingleTest.txt", new StrategyHiddenSets(1, StrategyMode.BRANCHING), "test61HiddenSingleTestGold.txt");
	}
	
	@Test
	public void HiddenPairBranchTest1() throws BoardCreationException, SudokuInputReadException {
		GoldCompareHelper("test64HiddenPairTest.txt", new StrategyHiddenSets(2, StrategyMode.BRANCHING), "test64HiddenPairTestGold1.txt", "test64HiddenPairTestGold2.txt");
	}
	
	@Test
	public void HiddenTripleBranchTest1() throws BoardCreationException, SudokuInputReadException {
		GoldCompareHelper("test66HiddenTripleTest.txt", new StrategyHiddenSets(3, StrategyMode.BRANCHING), "test66HiddenTripleTestGold1.txt", "test66HiddenTripleTestGold2.txt", "test66HiddenTripleTestGold3.txt");
	}
	
	// TODO: test hidden excluding
	
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
	
	private void GoldCompareHelper(String inputFile, Strategy s, String... goldFiles) throws BoardCreationException, SudokuInputReadException {
		Board b = new Board(new BasicTextInput(inputFile), 9);
		Set<Board> result = s.apply(b);
		assertTrue(result.size() == goldFiles.length);
		
		// Match each gold to a result
		Set<Board> unmatchedGolds = new HashSet<Board>();
		for (String i : goldFiles)
			unmatchedGolds.add(new Board(new BasicTextInput(i), 9));
		
		for (Board b1 : result){
			Iterator<Board> i = unmatchedGolds.iterator();
			boolean matched = false;
			while (i.hasNext()){
				if (b1.equals(i.next())){
					i.remove();
					matched = true;
				}
			}
			assertTrue(matched);
		}
	}
	
}
