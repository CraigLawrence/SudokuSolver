package sudoku.IO;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class BasicTextInput implements SudokuInput {
	
	BufferedReader br;
	
	public BasicTextInput(String file) throws SudokuInputReadException{
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			throw new SudokuInputReadException("The file wasn't found");
		}
	}

	@Override
	public boolean hasNext() throws SudokuInputReadException {
		try {
			return br.ready();
		} catch (IOException e) {
			throw new SudokuInputReadException("The input read failed");
		}
	}

	@Override
	public int getNext() throws SudokuInputReadException {
		try {
			int readValue;
			while ((readValue = br.read()) < 33); // Ignore space, newline, etc
			return readValue;
		} catch (IOException e) {
			throw new SudokuInputReadException("The input read failed");
		}
	}

}
