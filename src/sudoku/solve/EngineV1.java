package sudoku.solve;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import sudoku.model.Board;
import sudoku.model.Validity;

public class EngineV1 implements Engine {
	
	private final ExecutorService boardPool;
	private final Solution solution;
	private final AtomicBoolean cancelled;
	
	public EngineV1() {
		// Setup pool
		System.out.print("Setting up...");
		boardPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		solution = new Solution();
		cancelled = new AtomicBoolean(); cancelled.set(false);
		System.out.println("Done");
	}

	@Override
	public Board solve(Board b) {
		
		// Setup engine
		solution.startSolving();		

		// Seed with starting board
		boardPool.submit(new SolveHandler(b));
		
		// Start parallel processing
		System.out.println("Working...");
		
		while (true) {
			if (solution.getSolution() ==  null) {
				// No solution found yet
				// Has the solve been cancelled?
				if (cancelled.get()){
					System.out.println("Cancelled\n");
					boardPool.shutdownNow();
					return null;
				}
			}
			else {
				// Solution found, 
				System.out.println("Solution Found!\n");
				boardPool.shutdownNow();
				return solution.getSolution();
			}
		}
		
		// TODO: some timeout, handle case where pool exhausts
		
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
			//System.out.println(board.toString()); // <-- Uncomment for desperate debugging
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
				List<Strategy> strategies = new ArrayList<Strategy>();
				// TODO: add more strategies										// Max Branches
				strategies.add(new StrategyOnePossibleValue());						// 1
				strategies.add(new StrategyOnePossibleCellInGroup());				// 1
				strategies.add(new StrategyNakedPairs(StrategyMode.EXCLUDING));		// 1
				strategies.add(new StrategyNakedPairs(StrategyMode.BRANCHING)); 	// 2
				strategies.add(new StrategyScatterShot());
				
				for (Strategy s : strategies){
					candidates = s.apply(board);
					// If one succeeds with 1 or more possible changes, add all to pool and terminate
					if (candidates.size() > 0) {
						for (Board b : candidates) {
							boardPool.submit(new SolveHandler(b));
						}
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
