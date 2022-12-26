package observer;

import java.util.ArrayList;

public class GroupAdmin implements Sender {
    private UndoableStringBuilder usb;
    private ArrayList<Member> members = new ArrayList<Member>();

    public GroupAdmin(String str) {
        this.usb = new UndoableStringBuilder(str);
    }

    public GroupAdmin(UndoableStringBuilder usb) {
        this.usb = usb;
    }

    public UndoableStringBuilder getUsb() {
        return usb;
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<Member> members) {
        this.members = members;
    }

    public void notifyObservers() {
        for (Member member:members) {
            member.update(usb);
        }
    }

    public void setUsb(UndoableStringBuilder usb) {
        this.usb = usb;
    }

    @Override
    public void register(Member member) {
        //check if he already registered in the past
        if (!members.contains(member)) {
            members.add(member);
            member.update(usb);
        }
        else
            System.err.println("Could not register twice");
    }

    @Override
    public void unregister(Member member) {
        members.remove(member);
    }

    @Override
    public void insert(int offset, String str) {
        usb.insert(offset,str);
        notifyObservers();
    }

    @Override
    public void append(String str) {
        usb.append(str);
        notifyObservers();
    }

    @Override
    public void delete(int start, int end) {
        usb.delete(start,end);
        notifyObservers();
    }

    @Override
    public void undo() {
        usb.undo();
        notifyObservers();
    }
}
