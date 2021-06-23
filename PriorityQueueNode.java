/**
 * PriorityQueueNode.java
 *
 * @author Christine ZHao
 * Node for a priority queue
 */
public class PriorityQueueNode<T> {
    private PriorityQueueNode<T> next;
    private PriorityQueueNode<T> previous;
    private T item;
    private final int  priority;

    /**
     * PriorityQueueNode
     * Construct a priority queue node
     *
     * @param item     The item to  hold
     * @param priority The priority
     * @param next     The next node in the list
     * @param previous The previous node in the list
     */
    public PriorityQueueNode(T item, int priority, PriorityQueueNode<T> next, PriorityQueueNode<T> previous) {
        this.item = item;
        this.next = next;
        this.previous = previous;
        this.priority = priority;
    } // end PriorityQueueNode

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
    public PriorityQueueNode<T> getNext() {
        return next;
    } // end getNext

    /**
     * getPrevious
     * Get the previous node in the list
     *
     * @return The previous node
     */
    public PriorityQueueNode<T> getPrevious() {
        return previous;
    } // end getPrevious

    /**
     * setNext
     * Set the next node in the queue
     *
     * @param next The next node
     */
    public void setNext(PriorityQueueNode<T> next) {
        this.next = next;
    } // end setNext

    /**
     * setPrevious
     * Set the previous node in the queue
     *
     * @param previous The previous node
     */
    public void setPrevious(PriorityQueueNode<T> previous) {
        this.previous = previous;
    } // end setPrevious

    /**
     * setItem
     * Set the item in the node
     *
     * @param item The node
     */
    public void setItem(T item) {
        this.item = item;
    } // end setItem

    public int getPriority() {
        return priority;
    } // end getPriority


} // end PriorityQueueNode
