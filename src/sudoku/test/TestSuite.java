package sudoku.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
   BoardTest.class,
   CellGroupTest.class,
   CellTest.class,
   EngineTest.class,
   IOTest.class,
   StrategyTest.class
})

public class TestSuite {   
}  	