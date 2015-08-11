package sudoku.solve;

import java.util.Set;

import sudoku.model.Board;

public interface Strategy {
	
	public abstract Set<Board> apply(Board b);
	
}
