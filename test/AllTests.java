import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({Sandbox.class, Derive.class, JavaCompilerEval.class, MachineEval.class, Simplify.class})
public class AllTests {

}
