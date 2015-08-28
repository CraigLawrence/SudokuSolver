package sudoku.IO;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import sudoku.model.Board;
import sudoku.model.Cell;
import sudoku.model.CellGroup;

public class FileOutput implements SudokuOutput {
	
	BufferedWriter bw = null;
	String file = null;
	
	public FileOutput(String file){
		this.file = file;
	}

	@Override
	public void outputBoard(Board b) {
		try {
			// Open file
			bw = new BufferedWriter(new FileWriter(file));
			
			// Write file
			bw.write(b.toString());
			
			// Close file
			bw.close();
		}
		catch (IOException e) {
			// TODO: Do Something
		}
	}

}
