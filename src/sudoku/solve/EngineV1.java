package sudoku.solve;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import sudoku.model.Board;
import sudoku.model.Validity;

public class EngineV1 implements Engine {
	private static final Logger LOGGER = Logger.getLogger( EngineV1.class.getName() );
	
	private final ThreadPoolExecutor boardPool;
	private final Solution solution;
	private final AtomicBoolean cancelled;
	private final Date startTime;

	public EngineV1() {		
		// Setup pool
		LOGGER.log(Level.INFO, "Setting up...");
		int poolSize = Runtime.getRuntime().availableProcessors();
		boardPool = new ThreadPoolExecutor(0, poolSize, 0, TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>());

		solution = new Solution();
		cancelled = new AtomicBoolean(); cancelled.set(false);
		LOGGER.log(Level.INFO, "Done setting up");

		startTime = new Date();
	}

	@Override
	public Board solve(Board b) throws EngineExhaustedException, EngineCancelledException {
		
		// Setup engine
		solution.startSolving();		

		// Seed with starting board
		boardPool.execute(new SolveHandler(b));
		
		// Start parallel processing
		LOGGER.log(Level.INFO, "Working...");
		
		while (true) {
			if (solution.getSolution() != null) {
				// Solution found
				long runTime = (new Date()).getTime() - startTime.getTime();
				LOGGER.log(Level.INFO, "Solution found in {0}ms!", new Object[]{runTime});
				boardPool.shutdownNow();
				return solution.getSolution();
			} else if (cancelled.get()) {
				// Cancel initiated
				LOGGER.log(Level.INFO, "Cancelled");
				boardPool.shutdownNow();
				throw new EngineCancelledException("Cancelled");
			} else if ((boardPool.getPoolSize() + boardPool.getQueue().size() == 0) && solution.getSolution() == null) {
				// Pool has exhausted
				LOGGER.log(Level.INFO, "No solution found");
				boardPool.shutdownNow();
				throw new EngineExhaustedException("No solution found");
			}
		}
	}
	
	@Override
	public void cancel() {
		cancelled.set(true);
	}
	
	class SolveHandler implements Runnable, Comparable<SolveHandler> {
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
				// TODO: add more strategies											// Max Branches
				strategies.add(new StrategyNakedSets(1, StrategyMode.BRANCHING));		// 1
				strategies.add(new StrategyHiddenSets(1, StrategyMode.BRANCHING));	// 1
				strategies.add(new StrategyNakedSets(2, StrategyMode.EXCLUDING));		// 1
				strategies.add(new StrategyNakedSets(2, StrategyMode.BRANCHING)); 	// 2
				strategies.add(new StrategyScatterShot());
				
				for (Strategy s : strategies){
					candidates = s.apply(board);
					// If one succeeds with 1 or more possible changes, add all to pool and terminate
					if (candidates.size() > 0) {
						for (Board b : candidates) {
							try {
								boardPool.execute(new SolveHandler(b));
							}
							catch (RejectedExecutionException e) {
								if (!Pattern.matches("^.*Shutting down.*$", e.getMessage())){
									/* This may come up if a solution is found (and the pool is shutting down)
									 * while this thread is still solving.
									 * So only throw if something else has gone wrong.
									 */
									throw e;
								}
							}
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

		@Override
		/* The PriorityBlockingQueue orders items from 'least' to 'most' value.
		 * Since I want the most complete boards processed with highest priority the natural
		 * ordering provided by the Board class needs to be reversed.
		 */
		public int compareTo(SolveHandler arg0) {
			return arg0.board.compareTo(this.board);
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
