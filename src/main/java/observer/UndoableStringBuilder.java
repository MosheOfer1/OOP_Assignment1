package observer;
import java.util.EmptyStackException;
import java.util.Stack;
/**
 * Constructs a string builder initialized to the contents of the specified string. This class supports the undo operation
 * @Params:
 * str – the initial contents of the buffer.
 */
public class UndoableStringBuilder {

    private Stack<String> memory = new Stack<>();
    private StringBuilder string;
    public UndoableStringBuilder() {
        this.string = new StringBuilder("");
    }
    public UndoableStringBuilder(String str) {
        this.string = new StringBuilder(str);
    }

    /**
     * Appends the specified string to this character sequence.
     * The characters of the String argument are appended, in order,
     * increasing the length of this sequence by the length of the argument.
     * @param str
     * @return a reference to this object.
     */
    public UndoableStringBuilder append(String str){
        //save the last string into the Stack called memory and add the str
        memory.push(this.string.toString());
        this.string.append(str);
        return this;
    }

    /**
     * Removes the characters in a substring of this sequence. The substring begins
     * at the specified start and extends to the character at index
     * end - 1 or to the end of the sequence if no such character exists.
     * If start is equal to end, no changes are made.
     * @param start
     * @param end
     * @return This object
     * @throws StringIndexOutOfBoundsException – if start is negative, greater than length(), or greater than end.
     */
    public UndoableStringBuilder delete(int start, int end){
        if (start == end)
            return this;
        //firstly check if the start and end pointers are illegals or not
        if (start<0 || start > end || start >= this.string.length())
            throw new StringIndexOutOfBoundsException("One of the pointers is not in the right value");
        //if it's fine, save the last string into the Stack called memory
        memory.push(this.string.toString());
        //simply remove
        this.string.delete(start,end);
        return this;
    }

    /**
     * Inserts the string into this character sequence.
     * @param offset
     * @param str
     * @throws StringIndexOutOfBoundsException – if offset is negative or greater than length().
     * @return This object
     */
    public UndoableStringBuilder insert(int offset, String str){
        //check
        if (offset > this.string.length() || offset < 0)
            throw new StringIndexOutOfBoundsException("not able to insert, because the offset is out of range");

        memory.push(this.string.toString());
        this.string = this.string.insert(offset,str);
        return this;
    }

    /**
     * Replaces the characters in a substring of this sequence with characters in
     * the specified String. The substring begins at the specified start and
     * extends to the character at index end - 1 or to the end of the sequence if
     * no such character exists. First the characters in the substring are removed
     * and then the specified String is inserted at start. (This sequence will be
     * lengthened to accommodate the specified String if necessary).
     *
     * @return This object.
     * @Overrides replace in class AbstractStringBuilder
     * @param start: The beginning index, inclusive.
     * @param end The ending index, exclusive.
     * @param str String that will replace previous contents.
     * @Returns: This object.
     * @Throws StringIndexOutOfBoundsException – if start is negative, greater than length(), or greater than end.
     */
    public UndoableStringBuilder replace(int start, int end, String str){
        //if no need te delete just insert
        if (start == end)
            this.insert(start,str);
        //check
        if (start > end || start >= this.string.length() || start < 0)
            throw new StringIndexOutOfBoundsException("One of the pointers is not in the right value");
        else if (str.equals(null)) {
            throw new NullPointerException("Cannot invoke 'String.length()' because 'str' is null");
        }
        //save
        memory.push(this.string.toString());
        //simply replace
        this.string.delete(start,end);
        this.string = new StringBuilder(this.string.substring(0, start) + str + this.string.substring(start));
        return this;
    }
    /**
     * Causes this character sequence to be replaced by the reverse of the
     * sequence.
     * @return: a reference to this object.
     * @Overridess reverse in class AbstractStringBuilder
     */
    public UndoableStringBuilder reverse(){
        memory.push(this.string.toString());
        this.string = this.string.reverse();
        return this;
    }
    /**
     * undo the last operation
     * @return the previous String
     */
    public UndoableStringBuilder undo(){
        try {
            this.string = new StringBuilder(memory.pop());
        }catch (EmptyStackException e){
            System.out.println("nothing to undo!");
        }
        return this;
    }

    @Override
    public String toString() {
        return this.string.toString();
    }
}
