package sudoku.solve;

import sudoku.model.Board;

public interface Engine {

	public abstract Board solve(Board b);
	
	public abstract void cancel();
	
}
