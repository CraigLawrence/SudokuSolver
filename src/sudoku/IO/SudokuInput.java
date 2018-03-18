package sudoku.IO;

@SuppressWarnings("serial")
public interface SudokuInput {

	public abstract boolean hasNext() throws SudokuInputReadException;
	
	public abstract int getNext() throws SudokuInputReadException;
	
	public class SudokuInputReadException extends Exception {
		public SudokuInputReadException(String message) {
			super(message);
		}
	}
	
}
