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

}
