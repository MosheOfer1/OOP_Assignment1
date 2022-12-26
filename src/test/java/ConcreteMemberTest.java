import observer.ConcreteMember;
import observer.GroupAdmin;
import observer.UndoableStringBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConcreteMemberTest {

    @Test
    void update() {
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
}