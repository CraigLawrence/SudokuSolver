package sudoku.solve;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import sudoku.model.Board;
import sudoku.model.Validity;

public class EngineV1 implements Engine {
	private static final Logger LOGGER = Logger.getLogger( EngineV1.class.getName() );
	
	private final ThreadPoolExecutor boardPool;
	private final Solution solution;
	private final AtomicBoolean cancelled;
	
	public EngineV1() {		
		// Setup pool
		LOGGER.log(Level.INFO, "Setting up...");
		int poolSize = Runtime.getRuntime().availableProcessors();
		boardPool = new ThreadPoolExecutor(0, poolSize, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		
		solution = new Solution();
		cancelled = new AtomicBoolean(); cancelled.set(false);
		LOGGER.log(Level.INFO, "Done setting up");
	}

	@Override
	public Board solve(Board b) {
		
		// Setup engine
		solution.startSolving();		

		// Seed with starting board
		boardPool.submit(new SolveHandler(b));
		
		// Start parallel processing
		LOGGER.log(Level.INFO, "Working...");
		
		while (true) {
			if (solution.getSolution() ==  null) {
				// No solution found yet
				// Has the solve been cancelled?
				if (cancelled.get()){
					LOGGER.log(Level.INFO, "Cancelled");
					boardPool.shutdownNow();
					return null;
				}
				if (boardPool.getPoolSize() + boardPool.getQueue().size() == 0){
					// TODO: This still need proper testing
					// Pool has exhausted
					LOGGER.log(Level.INFO, "No solution found");
					boardPool.shutdownNow();
					return null;
				}
			}
			else {
				// Solution found, 
				LOGGER.log(Level.INFO, "Solution found!");
				boardPool.shutdownNow();
				return solution.getSolution();
			}
		}
		
		// TODO: consider handling case where cancel is called really quickly after starting. 
	}
	
	@Override
	public void cancel() {
		cancelled.set(true);
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
				LOGGER.log(Level.WARNING, "WARNING: An invalid board was detected in the pool.");
				break;
			case VALID_COMPLETE:
				// If valid and complete, offer as solution to pool and terminate
				solution.setSolution(board);
				break;
			case VALID_INCOMPLETE:
				// If valid and incomplete, start executing strategies
				Set<Board> candidates = null;
				List<Strategy> strategies = new ArrayList<Strategy>();
				// TODO: add more strategies										// Max Branches
				strategies.add(new StrategyNakedSets(1, StrategyMode.BRANCHING));	// 1
				strategies.add(new StrategyHiddenSets(1, StrategyMode.BRANCHING));	// 1
				strategies.add(new StrategyNakedSets(2, StrategyMode.EXCLUDING));	// 1
				strategies.add(new StrategyNakedSets(2, StrategyMode.BRANCHING)); 	// 2
				strategies.add(new StrategyScatterShot());
				
				for (Strategy s : strategies){
					candidates = s.apply(board);
					// If one succeeds with 1 or more possible changes, add all to pool and terminate
					if (candidates.size() > 0) {
						for (Board b : candidates) {
							boardPool.submit(new SolveHandler(b));
						}
						LOGGER.log(Level.INFO, "Thread: {0}\nBoard:\n{1}\nExecuted: {2}\nPool size: {3}\nQueue size: {4}\n", new Object[]{
								Thread.currentThread().getName(),
								board.toString(),
								s.getClass().getName(),
								boardPool.getPoolSize(),
								boardPool.getQueue().size()});
						break;
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
		private Semaphore sem;
		
		public void startSolving(){
			sem = new Semaphore(1);
			try {
				sem.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void setSolution(Board board){
			if (sem.availablePermits() == 0) {
				finalBoard = board;
				sem.release();
			}
		}
		
		public Board getSolution(){
			return sem.availablePermits() == 1 ? finalBoard : null;
		}
		
	}

}
