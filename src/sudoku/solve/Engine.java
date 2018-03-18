package sudoku.solve;

import sudoku.model.Board;

public interface Engine {

	public abstract Board solve(Board b) throws EngineExhaustedException, EngineCancelledException;
	
	public abstract void cancel();

	/*
	 * Exceptions
	 */

	public class EngineCancelledException extends Exception {
		public EngineCancelledException(String message) {
			super(message);
		}
	}

	public class EngineExhaustedException extends Exception {
		public EngineExhaustedException(String message) {
			super(message);
		}
	}
}
