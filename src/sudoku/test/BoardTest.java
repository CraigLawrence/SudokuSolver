package sudoku.test;

import static org.junit.Assert.*;

import org.junit.Test;

import sudoku.IO.BasicTextInput;
import sudoku.IO.SudokuInput.SudokuInputReadException;
import sudoku.model.Board;
import sudoku.model.Board.BoardCreationException;
import sudoku.model.Cell;
import sudoku.model.CellGroup;

public class BoardTest {

	// Basic Creation Test
	@Test
	public void creationTest() throws BoardCreationException, SudokuInputReadException {
		Board b = new Board(new BasicTextInput("test1.txt"), 9);
		for (CellGroup cg : b.getCellGroups().values()){
			for (Cell c : cg.getCells()){
				assertTrue(c.getValue() == '0');
			}
		}
	}
	
	// Copy Creation Test
	@Test
	public void copyTest() throws BoardCreationException, SudokuInputReadException {
		Board b = new Board(new BasicTextInput("test1.txt"), 9);		
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
	// TODO

	// Exception Tests
	// TODO
}
