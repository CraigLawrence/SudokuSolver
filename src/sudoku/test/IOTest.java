package sudoku.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import sudoku.IO.*;
import sudoku.IO.SudokuInput.SudokuInputReadException;
import sudoku.model.Board;
import sudoku.model.Cell;
import sudoku.model.CellGroup;
import sudoku.model.Board.BoardCreationException;

public class IOTest {

	@Test
	public void successfulFileOpen() {
		SudokuInput si = null;
		try {
			si = new BasicTextInput("test1Blank.txt");
		} catch (SudokuInputReadException e) {
			si = null;
		}
		assertNotNull(si);
	}
	
	@Test
	public void failedFileOpen() {
		SudokuInput si = null;
		Exception ee = null;
		try {
			si = new BasicTextInput("fakeFile.txt");
		} catch (SudokuInputReadException e) {
			ee = e;
		}
		assertTrue(ee instanceof SudokuInputReadException);
	}

	@Test
	public void basicBoardConstruction() {
		SudokuInput si = null;
		try {
			si = new BasicTextInput("test1Blank.txt");
		} catch (SudokuInputReadException e) {
			si = null;
		}
		
		Board b = null;
		try {
			b = new Board(si);
		} catch (BoardCreationException e) {
			e.printStackTrace();
		}
		
		SudokuOutput so = new TestOutput("test1Blank.txt");
		so.outputBoard(b);
		
	}
	
	private class TestOutput implements SudokuOutput {
		
		private String compareFile;
		private BufferedReader br;
		
		public TestOutput(String file) {
			compareFile = file;
		}

		@Override
		public void outputBoard(Board b) {
			try {
				// Open
				br = new BufferedReader(new FileReader(compareFile));
				
				Map<String, CellGroup> cgs = b.getCellGroups();
				for (int row=0; row<9; row++) {
					List<Cell> cells = cgs.get("row"+row).getCells();
					for (Cell c : cells) {
						
						// Read expected from file (ignoring newline, carriage return)
						int readValue;
						while ((readValue = br.read()) < 33); // Ignore space, newline, etc
						// Get actual from board
						int actualValue = c.getValue();
						// Assert that match
						assertTrue(readValue == actualValue);
						
					}
				}	
							
				// Close
				br.close();
			}
			catch (IOException e){
				//TODO: something
			}
		}
		
	}
}
