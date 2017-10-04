package sudoku.test;

import static org.junit.Assert.*;

import org.junit.Test;

import sudoku.IO.BasicTextInput;
import sudoku.IO.SudokuInput.SudokuInputReadException;
import sudoku.model.Board;
import sudoku.model.Board.BoardCreationException;
import sudoku.model.Cell;
import sudoku.model.CellGroup;
import sudoku.model.Validity;

public class BoardTest {

	// Basic Creation Test
	@Test
	public void creationTest() throws BoardCreationException, SudokuInputReadException {
		Board b = new Board(new BasicTextInput("test1Blank.txt"), 9);
		for (CellGroup cg : b.getCellGroups().values()){
			for (Cell c : cg.getCells()){
				assertTrue(c.getValue() == '0');
			}
		}
	}
	
	// Copy Creation Test
	@Test
	public void copyTest() throws BoardCreationException, SudokuInputReadException {
		Board b = new Board(new BasicTextInput("test1Blank.txt"), 9);		
		Board b2 = b.copyBoard();
		
		for (CellGroup cg : b2.getCellGroups().values()){
			for (Cell c : cg.getCells()){
				c.changeValue('5');
			}
		}
		
		for (CellGroup cg : b.getCellGroups().values()){
			for (Cell c : cg.getCells()){
				assertTrue(c.getValue() == '0');
			}
		}
		
		for (CellGroup cg : b2.getCellGroups().values()){
			for (Cell c : cg.getCells()){
				assertTrue(c.getValue() == '5');
			}
		}
	}
	
	// Validity Tests
	@Test
	public void validtyTestInvalid() throws BoardCreationException, SudokuInputReadException {
		Board b = new Board(new BasicTextInput("test2Invalid.txt"), 9);
		assertTrue(b.isValid() == Validity.INVALID);
	}
	
	@Test
	public void validtyTestValidComp() throws BoardCreationException, SudokuInputReadException {
		Board b = new Board(new BasicTextInput("test3ValidComp.txt"), 9);
		assertTrue(b.isValid() == Validity.VALID_COMPLETE);
	}

	@Test
	public void validtyTestValidIncomp() throws BoardCreationException, SudokuInputReadException {
		Board b = new Board(new BasicTextInput("test4ValidIncomp.txt"), 9);
		assertTrue(b.isValid() == Validity.VALID_INCOMPLETE);
	}
	
	// Cells Fraction Filled Tests
	@Test
	public void fractionFilledTestBlank() throws BoardCreationException, SudokuInputReadException{
		Board b = new Board(new BasicTextInput("test1Blank.txt"), 9);
		assertTrue(b.getFractionComplete() == 0.0);
	}
	
	@Test
	public void fractionFilledTestFull() throws BoardCreationException, SudokuInputReadException{
		Board b = new Board(new BasicTextInput("test3ValidComp.txt"), 9);
		assertTrue(b.getFractionComplete() == 1.0);
	}
	
	@Test
	public void fractionFilledTest1() throws BoardCreationException, SudokuInputReadException{
		Board b = new Board(new BasicTextInput("test51NakedSingleTest.txt"), 9);
		assertTrue(b.getFractionComplete() == 80.0/81.0);
	}
	
	@Test
	public void fractionFilledTest2() throws BoardCreationException, SudokuInputReadException{
		Board b = new Board(new BasicTextInput("test0General1.txt"), 9);
		assertTrue(b.getFractionComplete() == 36.0/81.0);
	}
	
	// Exception Tests
	// TODO
}
