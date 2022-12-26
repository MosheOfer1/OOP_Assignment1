# OOP_Assignment1
Observer Pattern Implementation
This project implements the Observer pattern in Java, using the ConcreteMember and GroupAdmin classes. The ConcreteMember class represents a member of a group, and the GroupAdmin class represents the group administrator who sends updates to the members.


## Class Definitions
The ConcreteMember class implements the Member interface, which defines a update() method that is called when the group administrator sends an update. 
The ConcreteMember class also has a usbSallowCopy field that stores a shallow copy of the UndoableStringBuilder object that is sent by the group administrator.

The GroupAdmin class implements the Sender interface, which defines methods for registering and unregistering members, and for modifying the UndoableStringBuilder object. The GroupAdmin class also has a members field that stores a list of registered members, and a usb field that stores the UndoableStringBuilder object.
## Example Usage
Here is an example of how to use the ConcreteMember and GroupAdmin classes:

```java
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
```
