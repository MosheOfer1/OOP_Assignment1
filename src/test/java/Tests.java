import observer.*;

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
        // The size that given is make sense
        assertTrue(totalSize > 0, "Unexpected total size: " + totalSize);

        // Log results for review
        logger.info(() -> "\nObject footprint: " + footprint);
        logger.info(() -> "\nTotal size: " + totalSize);
        logger.info(() -> "\nJVM info: " + jvmInfo);
    }
    @Test
    void testUpdate() {
        // Create a member named Yosi, and make him an attribute new Usb with a String
        ConcreteMember yosi = new ConcreteMember(new UndoableStringBuilder("hi yosi"));

        // Change Yosi's String
        yosi.getUsbSallowCopy().insert(3,"all, from ");
        // Create a new manager with different msg than Yosi's
        GroupAdmin manager = new GroupAdmin("hello from the manager");

        // Print Yosi's msg before register and after
        System.out.println(yosi.getUsbSallowCopy().toString());
        manager.register(yosi);
        System.out.println(yosi.getUsbSallowCopy().toString());
    }

    @Test
    void testDelete() {
        // Given
        GroupAdmin admin = new GroupAdmin("initial value");
        ConcreteMember member = new ConcreteMember(admin);
        admin.getMembers().add(member);

        // When
        admin.delete(4, 9);

        // Then
        assertEquals("initalue", admin.getUsb().toString());
        assertEquals("initalue", member.getUsbSallowCopy().toString());
    }

    @Test
    public void groupAdmin(){
        //In order to test I build Regular StringBuilder
        StringBuilder expected = new StringBuilder();
        //Creating a group that all has the same UndoableStringBuilder shallow copy as an attribute
        UndoableStringBuilder usb = new UndoableStringBuilder("hello");
        GroupAdmin groupAdmin = new GroupAdmin(usb);
        ConcreteMember Shoshi = new ConcreteMember(groupAdmin,usb);
        ConcreteMember Moshiko = new ConcreteMember(groupAdmin,usb);
        ConcreteMember Menachem = new ConcreteMember(groupAdmin,usb);
        //Change just Shoshi's data member
        Shoshi.update(usb.delete(2,3));
        System.out.println("Shoshi: "+Shoshi.getUsbSallowCopy().toString());
        System.out.println("Menachem: "+Menachem.getUsbSallowCopy().toString());
        //expected that Shoshi will have different str
        for (Member member:groupAdmin.getMembers()) {
            if(member instanceof ConcreteMember)
                expected.append(((ConcreteMember) member).getUsbSallowCopy().toString()).append(" ");
        }
        //Change the admins USB
        // and expect everyone else to have new data members identical to his
        groupAdmin.append(" world");
        groupAdmin.notifyObservers();
        expected.append('\n');
        for (Member member:groupAdmin.getMembers()) {
            if(member instanceof ConcreteMember)
                expected.append(((ConcreteMember) member).getUsbSallowCopy()).append(" ");
        }

        System.out.println(expected);
        //Test manually
        assertEquals("helo helo helo \nhelo world helo world helo world ",expected.toString());
    }

    @Test
    void testDoubleRegister() {
        GroupAdmin admin = new GroupAdmin("Abba");
        ConcreteMember member = new ConcreteMember(new UndoableStringBuilder("Amma"));

        // When register twice
        admin.register(member);
        admin.register(member);

        // Then the size should be 1
        assertEquals(1, admin.getMembers().size());
        assertEquals("Abba", member.getUsbSallowCopy().toString());
    }
    @Test
    void testUnregisterNotExistingMember() {
        GroupAdmin admin = new GroupAdmin("Abba");
        ConcreteMember member = new ConcreteMember(new UndoableStringBuilder("Amma"));

        // When remove a member that does not register before nothing happens
        admin.unregister(member);

        // Then
        assertTrue(admin.getMembers().isEmpty());
    }
    @Test
    void testModificationAndRegistration(){
        // Create a GroupAdmin with an initial value
        GroupAdmin admin = new GroupAdmin("abc");

        // Create a ConcreteMember and register it with the GroupAdmin
        ConcreteMember member1 = new ConcreteMember(admin);

        // Modify the UndoableStringBuilder object
        admin.insert(3, "def");
        admin.append("ghi");

        // The ConcreteMember's usbSallowCopy field should contain the modified value
        assertEquals("abcdefghi", member1.getUsbSallowCopy().toString());

        // Create another ConcreteMember and register it with the GroupAdmin
        ConcreteMember member2 = new ConcreteMember(admin);

        // The second ConcreteMember should also receive the update
        assertEquals("abcdefghi", member2.getUsbSallowCopy().toString());

        // Unregister the first ConcreteMember
        admin.unregister(member1);

        // Modify the UndoableStringBuilder object again
        admin.delete(0, 2);

        // The first ConcreteMember should not receive the update, because it is no longer registered
        assertEquals("abcdefghi", member1.getUsbSallowCopy().toString());
        // The second ConcreteMember should receive the update
        assertEquals("cdefghi", member2.getUsbSallowCopy().toString());

        // Try to unregister a ConcreteMember that is not registered
        admin.unregister(new ConcreteMember(admin));

        // The GroupAdmin's list of registered members should not change
        assertEquals(1, admin.getMembers().size());

    }
}
