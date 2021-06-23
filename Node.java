/**
 * Node.java
 *
 * @param <T> The item type
 *            A node that holds an item
 * @author Christine Zhao
 */
public class Node<T> {

    // class fields
    private Node<T> next;
    private Node<T> previous;
    private T item;

    /**
     * Node
     * Construct a node
     *
     * @param item     The item to contain
     * @param next     The next node in the list
     * @param previous The previous node in the list
     */
    public Node(T item, Node<T> next, Node<T> previous) {
        this.item = item;
        this.next = next;
        this.previous = previous;
    } // end Node

    /**
     * getItem
     * Get the item in the node
     *
     * @return The item
     */
    public T getItem() {
        return this.item;
    } // end getItem

    /**
     * getNext
     * Get the next node in the list
     *
     * @return The next node
     */
    public Node<T> getNext() {
        return next;
    } // end getNext

    /**
     * getPrevious
     * Get the previous node in the list
     *
     * @return The previous node
     */
    public Node<T> getPrevious() {
        return previous;
    } // end getPrevious

    /**
     * setNext
     * Set the next node in the list
     *
     * @param next The next node
     */
    public void setNext(Node<T> next) {
        this.next = next;
    } // end setNext

    /**
     * setPrevious
     * Set the previous node in the list
     *
     * @param previous The next node
     */
    public void setPrevious(Node<T> previous) {
        this.previous = previous;
    } // end setPrevious

    /**
     * setItem
     * Set the item to hold
     *
     * @param item The item
     */
    public void setItem(T item) {
        this.item = item;
    } // end setItem
} // end Node
