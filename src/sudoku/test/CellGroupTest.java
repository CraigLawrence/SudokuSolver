package sudoku.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import sudoku.IO.SudokuInput.SudokuInputReadException;
import sudoku.model.Cell;
import sudoku.model.CellGroup;
import sudoku.model.CellGroup.CellGroupException;
import sudoku.model.Validity;

public class CellGroupTest {
	
	// Test creation
	@Test
	public void creationTest() {
		CellGroup cg = null;
		Set<Character> numberSet = new HashSet<Character>(Arrays.asList('1','2','3','4','5','6','7','8','9'));
		
		try {
			cg = new CellGroup(numberSet, '0');
		} catch (Exception e) {
			cg = null;
		}
		assertNotNull(cg);
	}
	
	@Test
	public void nullCreationTest() {
		CellGroup cg = null;
		Exception e = null;
		try {
			cg = new CellGroup(null, '0');
		} catch (Exception e1) {
			e = e1;
		}
		assertTrue(e instanceof CellGroupException);
	}
	
	
	// Test addition and getting of cells
	@Test
	public void addOne() throws CellGroupException {		
		Set<Character> numberSet = new HashSet<Character>(Arrays.asList('1','2','3','4','5','6','7','8','9'));
		CellGroup cg = new CellGroup(numberSet, '0');
		
		createGroup(cg, '5');
		
		List<Cell> cells = cg.getCells();
		assertTrue(cells.size() == 1 && cells.get(0).getValue() == '5');
	}
	
	@Test
	public void addNine() throws CellGroupException {		
		Set<Character> numberSet = new HashSet<Character>(Arrays.asList('1','2','3','4','5','6','7','8','9'));
		CellGroup cg = new CellGroup(numberSet, '0');
		
		createGroup(cg, '1','2','3','4','5','6','7','8','9');
		
		List<Cell> cells = cg.getCells();
		assertTrue(cells.size() == 9 && 
				cells.get(0).getValue() == '1' &&
				cells.get(1).getValue() == '2' &&
				cells.get(2).getValue() == '3' &&
				cells.get(3).getValue() == '4' &&
				cells.get(4).getValue() == '5' &&
				cells.get(5).getValue() == '6' &&
				cells.get(6).getValue() == '7' &&
				cells.get(7).getValue() == '8' &&
				cells.get(8).getValue() == '9');
	}
	
	
	// Test validity check
	@Test
	public void validCompleteTest() throws CellGroupException {
		Set<Character> numberSet = new HashSet<Character>(Arrays.asList('1','2','3','4','5','6','7','8','9'));
		CellGroup cg = new CellGroup(numberSet, '0');
		
		createGroup(cg, '1','2','3','4','5','6','7','8','9');
		
		assertTrue(cg.isValid() == Validity.VALID_COMPLETE);
	}
	
	@Test
	public void validInCompleteTest() throws CellGroupException {
		Set<Character> numberSet = new HashSet<Character>(Arrays.asList('1','2','3','4','5','6','7','8','9'));
		CellGroup cg = new CellGroup(numberSet, '0');
		
		createGroup(cg, '1','2','3','4','0','6','7','0','9');
		
		assertTrue(cg.isValid() == Validity.VALID_INCOMPLETE);
	}
	
	@Test
	public void InvalidTest() throws CellGroupException {
		Set<Character> numberSet = new HashSet<Character>(Arrays.asList('1','2','3','4','5','6','7','8','9'));
		CellGroup cg = new CellGroup(numberSet, '0');
		
		createGroup(cg, '1','2','3','4','5','6','7','5','9');
		
		assertTrue(cg.isValid() == Validity.INVALID);
	}
	

	// Test missing values
	@Test
	public void OneMissingTest() throws CellGroupException {
		Set<Character> numberSet = new HashSet<Character>(Arrays.asList('1','2','3','4','5','6','7','8','9'));
		CellGroup cg = new CellGroup(numberSet, '0');
		
		createGroup(cg, '1','2','3','4','5','6','7','0','9');
		
		Set<Character> missing = cg.missingValues();
		assertTrue(missing.size() == 1 && missing.contains('8'));
	}
	
	@Test
	public void SomeMissingTest() throws CellGroupException {
		Set<Character> numberSet = new HashSet<Character>(Arrays.asList('1','2','3','4','5','6','7','8','9'));
		CellGroup cg = new CellGroup(numberSet, '0');
		
		createGroup(cg, '1','0','0','3','5','6','9','0','7');
		
		Set<Character> missing = cg.missingValues();
		assertTrue(missing.size() == 3 && 
				missing.contains('2') &&
				missing.contains('4') &&
				missing.contains('8'));
	}
	
	@Test
	public void AllMissingTest() throws CellGroupException {
		Set<Character> numberSet = new HashSet<Character>(Arrays.asList('1','2','3','4','5','6','7','8','9'));
		CellGroup cg = new CellGroup(numberSet, '0');
		
		createGroup(cg, '0','0','0','0','0','0','0','0','0');
		
		Set<Character> missing = cg.missingValues();
		assertTrue(missing.size() == 9 && 
				missing.contains('1') &&
				missing.contains('2') &&
				missing.contains('3') &&
				missing.contains('4') &&
				missing.contains('5') &&
				missing.contains('6') &&
				missing.contains('7') &&
				missing.contains('8') &&
				missing.contains('9'));
	}
	
	// Test possible cells
	@Test
	public void PossibleCellsTest() throws CellGroupException {
		Set<Character> numberSet = new HashSet<Character>(Arrays.asList('1','2','3','4','5','6','7','8','9'));
		CellGroup cg = new CellGroup(numberSet, '0');
		
		Cell cell1 = new Cell('1', true, cg); cg.addCell(cell1);
		Cell cell2 = new Cell('2', true, cg); cg.addCell(cell2);
		Cell cell3 = new Cell('3', true, cg); cg.addCell(cell3);
		Cell cell4 = new Cell('4', true, cg); cg.addCell(cell4);
		Cell cell5 = new Cell('0', true, cg); cg.addCell(cell5);
		Cell cell6 = new Cell('0', true, cg); cg.addCell(cell6);
		Cell cell7 = new Cell('0', true, cg); cg.addCell(cell7);
		Cell cell8 = new Cell('0', true, cg); cg.addCell(cell8);
		Cell cell9 = new Cell('0', true, cg); cg.addCell(cell9);
		
		Set<Cell> missing = cg.possibleCells('5');
		assertTrue(missing.size() == 5 && 
				missing.contains(cell5) &&
				missing.contains(cell6) &&
				missing.contains(cell7) &&
				missing.contains(cell8) &&
				missing.contains(cell9));
	}
	
	
	// Helper methods
	private void createGroup(CellGroup cg, char... nums) throws CellGroupException {		
		for (char i : nums) {
			Cell cell = new Cell(i, true, cg);
			cg.addCell(cell);
		}
	}

}
