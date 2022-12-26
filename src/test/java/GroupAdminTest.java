import observer.ConcreteMember;
import observer.GroupAdmin;
import observer.Member;
import observer.UndoableStringBuilder;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class GroupAdminTest {
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
    void testRegister() {
        // Test that when register the attribute chanced
        GroupAdmin admin = new GroupAdmin("Abba");
        ConcreteMember member = new ConcreteMember(new UndoableStringBuilder("Amma"));


        admin.register(member);

        // Assert size and value
        assertEquals(1, admin.getMembers().size());
        assertEquals("Abba", member.getUsbSallowCopy().toString());
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
