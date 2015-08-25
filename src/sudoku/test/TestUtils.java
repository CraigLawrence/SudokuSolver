package sudoku.test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import sudoku.model.Board;
import sudoku.model.Cell;
import sudoku.model.CellGroup;

public class TestUtils {
	
	public static boolean boardCompare(Board b, String goldFile) throws IOException {
		String resultBoard = "";
		Map<String, CellGroup> cgs = b.getCellGroups();
		for (int row=0; row<9; row++) {
			List<Cell> cells = cgs.get("row"+row).getCells();
			for (Cell c : cells) {
				resultBoard += c.getValue();
			}
		}
		
		BufferedReader br = new BufferedReader(new FileReader(goldFile));
		String goldBoard = "";
		while (br.ready()) {
			goldBoard += br.readLine();
		}
		
		return goldBoard.equals(resultBoard);		
	}

}
