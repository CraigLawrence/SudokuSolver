package sudoku.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import sudoku.model.Cell;
import sudoku.model.CellGroup;
import sudoku.model.CellGroup.CellGroupException;

public class CellTest {

	// Test creation
	@Test
	public void CreationTest() {
		Cell c = null;
		c = new Cell('0', false);
		assertNotNull(c);
	}
	
	// Test value and created at start
	@Test
	public void ValueTest1() {
		Cell c = new Cell('0', false);
		assertTrue(c.getValue() == '0');
	}
	
	@Test
	public void ValueTest2() {
		Cell c = new Cell('8', true);
		assertTrue(c.getValue() == '8');
	}
	
	@Test
	public void StartTest1() {
		Cell c = new Cell('0', false);
		assertFalse(c.wasDefinedAtStart());
	}
	
	@Test
	public void StartTest2() {
		Cell c = new Cell('8', true);
		assertTrue(c.wasDefinedAtStart());
	}
	
	// Test new value
	@Test
	public void NewValueTest() {
		Cell c = new Cell('0', false);
		assertTrue(c.getValue() == '0');
		c.changeValue('5');
		assertTrue(c.getValue() == '5');
	}
	
	
	// Test possible values
	@Test
	public void PossibleValues1() throws CellGroupException {
		Set<Character> numberSet = new HashSet<Character>(Arrays.asList('1','2','3','4','5','6','7','8','9'));
		CellGroup row = new CellGroup(numberSet, '0');
		CellGroup col = new CellGroup(numberSet, '0');
		CellGroup quad = new CellGroup(numberSet, '0');
		
		Cell mainCell = new Cell('0', false, row, col, quad);
		
		row.addCell(mainCell);
		col.addCell(mainCell);
		quad.addCell(mainCell);
		
		Set<Character> pvs = mainCell.possibleValues();
		assertTrue(pvs.size() == 9 && 
				pvs.contains('1') &&
				pvs.contains('2') &&
				pvs.contains('3') &&
				pvs.contains('4') &&
				pvs.contains('5') &&
				pvs.contains('6') &&
				pvs.contains('7') &&
				pvs.contains('8') &&
				pvs.contains('9'));
	}
	
	@Test
	public void PossibleValues2() throws CellGroupException {
		Set<Character> numberSet = new HashSet<Character>(Arrays.asList('1','2','3','4','5','6','7','8','9'));
		CellGroup row = new CellGroup(numberSet, '0');
		CellGroup col = new CellGroup(numberSet, '0');
		CellGroup quad = new CellGroup(numberSet, '0');
		
		Cell mainCell = new Cell('0', false, row, col, quad);
		Cell o1 = new Cell('1', true, col, quad);
		Cell o2 = new Cell('2', true, col, quad);
		Cell o3 = new Cell('3', true, row, quad);
		Cell o4 = new Cell('4', true, row, quad);
		
		row.addCell(mainCell); row.addCell(o3); row.addCell(o4);
 		col.addCell(mainCell); col.addCell(o1); col.addCell(o2);
		quad.addCell(mainCell); quad.addCell(o1); quad.addCell(o2); quad.addCell(o3); quad.addCell(o4);
		
		Set<Character> pvs = mainCell.possibleValues();
		assertTrue(pvs.size() == 5 && 
				pvs.contains('5') &&
				pvs.contains('6') &&
				pvs.contains('7') &&
				pvs.contains('8') &&
				pvs.contains('9'));
	}
	
	@Test
	public void PossibleValues3() throws CellGroupException {
		Set<Character> numberSet = new HashSet<Character>(Arrays.asList('1','2','3','4','5','6','7','8','9'));
		CellGroup row = new CellGroup(numberSet, '0');
		CellGroup col = new CellGroup(numberSet, '0');
		CellGroup quad = new CellGroup(numberSet, '0');
		
		Cell mainCell = new Cell('0', false, row, col, quad);
		Cell o1 = new Cell('1', true, col, quad);
		Cell o2 = new Cell('2', true, col, quad);
		Cell o3 = new Cell('1', true, row);
		Cell o4 = new Cell('2', true, row);
		
		row.addCell(mainCell); row.addCell(o3); row.addCell(o4);
 		col.addCell(mainCell); col.addCell(o1); col.addCell(o2);
		quad.addCell(mainCell); quad.addCell(o1); quad.addCell(o2);
		
		Set<Character> pvs = mainCell.possibleValues();
		assertTrue(pvs.size() == 7 && 
				pvs.contains('3') &&
				pvs.contains('4') &&
				pvs.contains('5') &&
				pvs.contains('6') &&
				pvs.contains('7') &&
				pvs.contains('8') &&
				pvs.contains('9'));
	}
	
	@Test
	public void PossibleValues4() throws CellGroupException {
		Set<Character> numberSet = new HashSet<Character>(Arrays.asList('1','2','3','4','5','6','7','8','9'));
		CellGroup row = new CellGroup(numberSet, '0');
		CellGroup col = new CellGroup(numberSet, '0');
		CellGroup quad = new CellGroup(numberSet, '0');
		
		Cell mainCell = new Cell('0', false, row, col, quad);
		Cell o1 = new Cell('1', true, col, quad);
		Cell o2 = new Cell('2', true, col, quad);
		Cell o3 = new Cell('3', true, col);
		Cell o4 = new Cell('4', true, col);
		Cell o5 = new Cell('5', true, col);
		Cell o6 = new Cell('6', true, col);
		Cell o7 = new Cell('7', true, col);
		Cell o8 = new Cell('8', true, col);
		Cell o9 = new Cell('1', true, row);
		Cell o10 = new Cell('2', true, row);
		Cell o11 = new Cell('3', true, row, quad);
		Cell o12 = new Cell('4', true, row, quad);
		Cell o13 = new Cell('5', true, row);
		Cell o14 = new Cell('6', true, row);
		Cell o15 = new Cell('7', true, row);
		Cell o16 = new Cell('8', true, row);
		Cell o17 = new Cell('5', true, quad);
		Cell o18 = new Cell('6', true, quad);
		Cell o19 = new Cell('7', true, quad);
		Cell o20 = new Cell('8', true, quad);
		
		row.addCell(mainCell); row.addCell(o9); row.addCell(o10); row.addCell(o11); row.addCell(o12); row.addCell(o13); row.addCell(o14); row.addCell(o15); row.addCell(o16);
 		col.addCell(mainCell); col.addCell(o1); col.addCell(o2); col.addCell(o3); col.addCell(o4); col.addCell(o5); col.addCell(o6); col.addCell(o7); col.addCell(o8);
 		quad.addCell(mainCell); quad.addCell(o1); quad.addCell(o2); quad.addCell(o11); quad.addCell(o12); quad.addCell(o17); quad.addCell(o18); quad.addCell(o19); quad.addCell(o20); 
		
		Set<Character> pvs = mainCell.possibleValues();
		assertTrue(pvs.size() == 1 && 
				pvs.contains('9'));
	}
	
	@Test
	public void PossibleValueWithExclusion() throws CellGroupException {
		Set<Character> numberSet = new HashSet<Character>(Arrays.asList('1','2','3','4','5','6','7','8','9'));
		CellGroup row = new CellGroup(numberSet, '0');
		CellGroup col = new CellGroup(numberSet, '0');
		CellGroup quad = new CellGroup(numberSet, '0');
		
		Cell mainCell = new Cell('0', false, row, col, quad);
		
		row.addCell(mainCell);
		col.addCell(mainCell);
		quad.addCell(mainCell);
		
		mainCell.addPossibleValueExclusion('2');
		mainCell.addPossibleValueExclusion('7');
		
		Set<Character> pvs = mainCell.possibleValues();
		assertTrue(pvs.size() == 7 && 
				pvs.contains('1') &&
				pvs.contains('3') &&
				pvs.contains('4') &&
				pvs.contains('5') &&
				pvs.contains('6') &&
				pvs.contains('8') &&
				pvs.contains('9'));
	}
}
