package sudoku.solve;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sudoku.model.Board;
import sudoku.model.Validity;

public class EngineV1 implements Engine {
	
	private final ExecutorService boardPool;
	private final Solution solution;
	
	public EngineV1() {
		// Setup pool
		System.out.print("Setting up...");
		boardPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		solution = new Solution();
		System.out.println("Done");
	}

	@Override
	public Board solve(Board b) {

		// Seed with starting board
		boardPool.submit(new SolveHandler(b));
		
		// Start parallel processing
		System.out.println("Working...");
		
		// Wait for result or for pool to exhaust
		solution.startWaiting();
		
		// Return result
		return solution.getSolution();
		
		// TODO: some timeout
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
				solution.setSolution(board);
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
				break;
				
			default:
				break;
			}		
		}
		
	}
	
	class Solution {
		
		private Board finalBoard;
		private Object lock = new Object();
		
		public void startWaiting() {
			synchronized(lock){
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		public void setSolution(Board board){
			synchronized(lock){
				finalBoard = board;
				lock.notify();
			}
		}
		
		public Board getSolution(){
			synchronized(lock){
				return finalBoard;
			}
		}
		
	}

}
