package sudoku.solve;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sudoku.model.Board;
import sudoku.model.Validity;

public class EngineV1 implements Engine {
	
	private final ExecutorService boardPool;
	
	public EngineV1() {
		// Setup pool
		System.out.print("Setting up...");
		boardPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		System.out.println("Done");
	}

	@Override
	public Board solve(Board b) {

		// Seed with starting board
		boardPool.submit(new SolveHandler(b));
		
		// Start parallel processing
		System.out.println("Working...");
		
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
			// Check the board
			Validity valid = board.isValid();
			switch (valid) {
			case INVALID:
				// If invalid, log and terminate
				System.out.println("WARNING: An invalid board was detected in the pool.");
				break;
			case VALID_COMPLETE:
				// If valid and complete, offer as solution to pool and terminate
				break;
			case VALID_INCOMPLETE:
				// If valid and incomplete, start executing strategies
				Set<Board> candidates = null;
				Set<Strategy> strategies = new HashSet<Strategy>();
				// TODO: add strategies
				
				for (Strategy s : strategies){
					candidates = s.apply(board);
					// If one succeeds with 1 or more possible changes, add all to pool and terminate
					if (candidates.size() > 0) {
						for (Board b : candidates) {
							boardPool.submit(new SolveHandler(b));
						}
					}
				}
				// If not succeed, terminate
				break;
			default:
				break;
			}		
		}
		
	}

}
