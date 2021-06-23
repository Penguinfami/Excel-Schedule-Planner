/**
 * SimpleLinkedList.java
 *
 * @author Christine Zhao
 * Jan 2021
 * A linked list with methods to manage it
 */

/**
 * Create a linked list
 * @param <E> The object class
 */
public class SimpleLinkedList<E> {

    // class fields
    private Node<E> head;
    private Node<E> tail;

    public SimpleLinkedList() {
        head = null;
        tail = null;
    }

    /** add
     * Add an item to the end of the list
     * @param item The item to add
     */
    public void add(E item) {
        if (tail == null) {
            head = new Node<E>(item, null, null);
            tail = head;
            return;
        }
        tail.setNext(new Node<E>(item, null, tail));
        tail = tail.getNext();

    }


    /** get
     * Get the object from the node at a specific index
     * @param index The index of the node's position in the list
     * @return The item associated with the node
     */
    public E get(int index) {
        Node<E> currentNode = head;
        if (index < 0) {
            return null;
        }
        while (index > 0) {
            if (currentNode == null) { // if there are no nodes to get / list is empty
                return null;
            }
            currentNode = currentNode.getNext();
            index--; // Count down to the index's Node position
        }
        if (currentNode != null) {
            return currentNode.getItem();
        } else {
            return null;
        }
    } // end get

    /** get
     * Set an item for the node at a specific index
     * @param index The index of the node's position in the list
     */
    public void set(E item, int index) {
        Node<E> currentNode = head;
        while (index > 0) {
            if (currentNode == null) { // if there are no nodes to get / list is empty
                return;
            }
            currentNode = currentNode.getNext();
            index--; // Count down to the index's Node position
        }
        currentNode.setItem(item);
    } // end get

    /** delete
     * Remove a node from the list
     * @param index The index position of the node that is to be removed
     */
    public E delete(int index) {
        Node<E> currentNode = head;

        while (index > 0 && currentNode != null) {
            currentNode = currentNode.getNext();
            index--; // count down to the node's index position
        }

        if (head == tail) { // if there is only 1 or less nodes in the list
            head = null; // make the list empty, if it isn't already
            tail = null;
            return null;
        } else { // if there is more than 1 node in the list
            Node<E> previousNode = currentNode.getPrevious(); // get the nodes around the current node
            Node<E> nextNode = currentNode.getNext();
            if (currentNode == head) { // if the head is getting removed, set the new head as the next node
                head = currentNode.getNext();
            }
            if (currentNode == tail) { // if the tail is getting removed, set the new tail as the previous node
                tail = currentNode.getPrevious();
            }

            // remove references to the current node by having the previous and next nodes link to each other instead
            if (previousNode != null) {
                previousNode.setNext(nextNode);
            }
            if (nextNode != null) {
                nextNode.setPrevious(previousNode);
            }
        }
        return currentNode.getItem();
    } // end delete

    /** delete
     * Delete the first instance of an item
     * @param item The object to remove
     * @return The object that was removed
     */
    public E delete(E item) {
        if (head == null) { // if the list is empty
            return null;
        }
        Node<E> currentNode = head;
        Node<E> nextNode;
        Node<E> previousNode;
        while (currentNode != null) {
            if (currentNode.getItem() == item) { // if the node contains the item
                previousNode = currentNode.getPrevious();
                nextNode = currentNode.getNext();
                if (previousNode != null) { // have the next and previous nodes link to each other instead
                    previousNode.setNext(nextNode);
                }
                if (nextNode != null) {
                    nextNode.setPrevious(previousNode);
                }
                return currentNode.getItem(); // return, only remove the 1st instance
            }
            currentNode = currentNode.getNext(); //move through the list
        }
        return null;
    }

    /** deleteLast
     * Delete the last instance of an item
     * @param item The object to remove
     * @return The object that was removed
     */
    public E deleteLast(E item) {
        if (head == null) { // if the list is empty
            return null;
        }
        Node<E> currentNode = tail;
        Node<E> nextNode;
        Node<E> previousNode;
        while (currentNode != null) {
            if (currentNode.getItem() == item) { // if the node contains the item
                System.out.println("deletelast");
                if (size() == 1) {
                    clear();
                    return item;
                }
                previousNode = currentNode.getPrevious();
                nextNode = currentNode.getNext();
                if (currentNode == tail) {
                    tail = previousNode;
                }
                if (currentNode == head) {
                    head = nextNode;
                }
                if (previousNode != null) { // have the next and previous nodes link to each other instead
                    previousNode.setNext(nextNode);
                }
                if (nextNode != null) {
                    nextNode.setPrevious(previousNode);
                }
                return currentNode.getItem(); // return, only remove the 1st instance
            }
            currentNode = currentNode.getPrevious(); //move backwards through the list
        }
        return null;
    }

    /** deleteAll
     * Delete the all instances of an item
     * @param item The object to remove
     * @return The object that was removed
     */
    public E deleteAll(E item) {
        if (head == null) { // if the list is empty
            return null;
        }
        Node<E> currentNode = head;
        Node<E> nextNode;
        Node<E> previousNode;
        while (currentNode != null) {
            if (currentNode.getItem() == item) { // if the node contains the item
                if (size() == 1) {
                    clear(); // empty the list
                    return item;
                } else {
                    previousNode = currentNode.getPrevious();
                    nextNode = currentNode.getNext();
                    if (currentNode == tail) {
                        tail = previousNode;
                    }
                    if (currentNode == head) {
                        head = nextNode;
                    }
                    if (previousNode != null) { // have the next and previous nodes link to each other instead
                        previousNode.setNext(nextNode);
                    }
                    if (nextNode != null) {
                        nextNode.setPrevious(previousNode);
                    }
                }
            }

            currentNode = currentNode.getNext(); //move backwards through the list
        }
        return item;
    }

    /** contains
     * Checks if the list contains an item
     * @param item The item to check for
     * @return True of False, if the list contains the item or not
     */
    public boolean contains(E item) {
        Node<E> currentNode = head;
        while (currentNode != null) {
            if (currentNode.getItem() == item) { // if the item exists in this node
                return true;
            }
            currentNode = currentNode.getNext(); // move through the list
        }
        return false;
    } // end contains

    /** indexOf
     * Find the index of an item in the list
     * @param item The item to search for
     * @return The index position of the first occurence of the item, or -1 if the item does not exist in the list.
     */
    public int indexOf(E item) {
        int index = 0;
        Node<E> currentNode = head;
        while (currentNode != null) {
            if (currentNode.getItem() == item) {
                return index;
            }
            index++;
            currentNode = currentNode.getNext();
        }
        return -1;
    }

    /** indexOf
     * Find the index of an item in the list
     * @param item The item to search for
     * @param startingIndex The index to start at
     * @return The index position of the first occurence of the item, or -1 if the item does not exist in the list.
     */
    public int indexOf(E item, int startingIndex) {
        int index = 0;
        Node<E> currentNode = head;
        while (currentNode != null) {
            if (currentNode.getItem() == item && index >= startingIndex) {
                return index;
            }
            index++;
            currentNode = currentNode.getNext();
        }
        return -1;
    }

    /** indexOf
     * Find the number of times an item appears
     * @param item The item to search for
     * @return The number of occurences.
     */
    public int numInstancesOf(E item) {

        Node<E> currentNode = head;
        int numOccurences = 0;
        while (currentNode != null) {
            if (currentNode.getItem() == item) {
                numOccurences++;
            }
            currentNode = currentNode.getNext();
        }
        return numOccurences;
    }

    /** size
     * Get the size of the list
     * @return The number of nodes in the list
     */
    public int size() {
        Node<E> currentNode = head;
        int size = 0;
        while (currentNode != null) { // while the loop hasn't reached the end yet, keep going through the list
            currentNode = currentNode.getNext();
            size++;
        }
        return size;
    } //total number of items in list

    /** clear
     * Clear the list
     */
    public void clear() {
        Node<E> prevNode = head;
        Node<E> currentNode = head;
        while (prevNode != null) {
            prevNode.setNext(null);
            prevNode = currentNode.getNext();
            currentNode = currentNode.getNext();
        }
        head = null;
        tail = null;
    }


} // end SimpleLinkedList

