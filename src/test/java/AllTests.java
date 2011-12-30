import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({
  Sandbox.class,
  TestPrinter.class,
  TestParser.class,
  TestDerive.class,
  TestJavaCompilerEval.class,
  TestMachineEval.class,
  TestSimplify.class,
  TestComplex.class,
  TestIntervals.class,
  TestSolve.class
})
public class AllTests {

}
