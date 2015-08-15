package sudoku.solve;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sudoku.model.Board;

public class EngineV1 implements Engine {
	
	private final ExecutorService boardPool;
	
	public EngineV1() {
		// Setup pool
		boardPool = Executors.newFixedThreadPool(8);
	}

	@Override
	public Board solve(Board b) {

		// Seed with starting board
		boardPool.submit(new SolveHandler(b));
		boardPool.submit(new SolveHandler(b));
		boardPool.submit(new SolveHandler(b));
		
		// Start parallel processing
		
		// Wait for result or for pool to exhaust
		
		// Return result
		
		return null;
	}
	
	class SolveHandler implements Runnable {
		private final Board board;
		SolveHandler(Board board) {
			this.board = board;
		}
	
		@Override
		public void run() {
			// Get the input board
			System.out.println("test");
			
			// Check the board
				// If valid and complete, offer as solution to pool and terminate
				// If invalid, log and terminate
				// If valid and incomplete, continue
			
			// Start executing strategies
				// If one succeeds with 1 possible change, go to top
				// If one succeeds with 1+ possible changes, add all to pool and terminate
				// If non succeed, terminate			
		}
		
	}

}
