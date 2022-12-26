package observer;

public class ConcreteMember implements Member{
    private Sender sender;
    private UndoableStringBuilder usbSallowCopy;

    public ConcreteMember(UndoableStringBuilder usb) {
        this.usbSallowCopy = usb;
    }
    public ConcreteMember(Sender sender) {
        this.sender = sender;
        this.sender.register(this);
    }

    public ConcreteMember(Sender sender,UndoableStringBuilder usb) {
        this.sender = sender;
        this.usbSallowCopy = usb;
        sender.register(this);
    }
    @Override
    public void update(UndoableStringBuilder usb) {
        this.usbSallowCopy = usb;
    }
    public UndoableStringBuilder getUsbSallowCopy() {
        return this.usbSallowCopy;
    }

    public void setUsbSallowCopy(UndoableStringBuilder usbSallowCopy) {
        this.usbSallowCopy = usbSallowCopy;
    }
}
