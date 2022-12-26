import observer.ConcreteMember;
import observer.GroupAdmin;
import observer.JvmUtilities;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.openjdk.jol.info.GraphLayout;

class Tests {
    // Logger for logging test output and status
    private static final Logger logger = LoggerFactory.getLogger(Tests.class);

    /*
    The test creates a GroupAdmin object and 1000 ConcreteMember objects,
    and then uses the objectFootprint(), objectTotalSize(), and jvmInfo()
    methods to retrieve information about the memory usage and the JVM.
    It then adds assertions to verify that the returned values are as expected.
    Finally, it logs the results for review.
    Because we used Shallow copy, the memory should be used efficiently.
    Which means that we will see use of just one UndoableStringBuilder,Stack and StringBuilder, and not 1000.
     */

    @Test
    public void testMemoryUsage() {
        GroupAdmin admin = new GroupAdmin("Some initial string");
        for (int i = 0; i < 1000; i++) {
            new ConcreteMember(admin);
        }

        String footprint = JvmUtilities.objectFootprint(admin);
        long totalSize = GraphLayout.parseInstance(admin).totalSize();
        String jvmInfo = JvmUtilities.jvmInfo();

        // Verify that there is no unexpected object footprint
        assertTrue(footprint.contains("java.util.ArrayList"), "Unexpected object footprint: " + footprint);
        // The size is given
        assertTrue(totalSize > 0, "Unexpected total size: " + totalSize);

        // Log results for review
        logger.info(() -> "\nObject footprint: " + footprint);
        logger.info(() -> "\nTotal size: " + totalSize);
        logger.info(() -> "\nJVM info: " + jvmInfo);
    }
}
