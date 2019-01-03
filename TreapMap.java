import java.util.*;

public class TreapMap<K extends Comparable<K>, V>
        implements Treap<K, V> {
    private TreapNode root = null;

    //Represents a node of the TreapMap
    private class TreapNode {

        //The key, value, and priority of the TreapNode
        private K key;
        private V value;
        private Integer priority =
                (int) (Math.random() * Treap.MAX_PRIORITY);

        //The left and right child pointers of the TreapNode
        private TreapNode left = null;
        private TreapNode right = null;

        //Pointer to the TreapNode's parent
        private TreapNode parent = null;

        private TreapNode(K key, V value){
            this.key = key;
            this.value = value;
        }

        private TreapNode(K key, V value, int priority){
            this.key = key;
            this.value = value;
            this.priority = priority;
        }

        //Set the left child of the TreapNode
        private void setLeft(TreapNode left){
            this.left = left;
            if(left != null){
                left.parent = this;
            }
        }

        //Set the right child of the TreapNode
        private void setRight(TreapNode right){
            this.right = right;
            if(right != null){
                right.parent = this;
            }
        }

        //Uses key to test equality
        @Override
        public boolean equals(Object other){
            if(!(other instanceof TreapMap.TreapNode)){
                return false;
            }
            return key.equals(((TreapMap.TreapNode) other).key);
        }

        //String representation of a single TreapNode
        //Used in the TreapMap toString method
        @Override
        public String toString(){
            return "[" + priority + "] <" + key + ", " + value + ">";
        }
    }

    //Default Constructor
    public TreapMap(){
    }

    //Contruct TreapMap with a given root
    public TreapMap(TreapNode root){
        this.root = root;
    }

    //Retrieves the value associated with a key in this dictionary.
    //If the key is null or the key is not present in this
    //dictionary, this method returns null.
    @Override
    public V lookup(K key) {
        if(key == null){
            return null;
        }
        return lookup(root, key);
    }

    //Recursive helper method for lookup
    private V lookup(TreapNode head, K key){
        //Base Case
        if(head == null){
            return null;
        }
        if(head.key.equals(key)){
            return head.value;
        }

        //Recursive Case
        //Call lookup on a child
        if(key.compareTo(head.key) < 0){
            return lookup(head.left, key);
        }
        return lookup(head.right, key);
    }

    //Adds a key-value pair to this dictionary.  Any old value
    //associated with the key is lost.  If the key or the value is
    //null, the pair is not added to the dictionary.
    @Override
    public void insert(K key, V value) {
        if (key == null || value == null){
            return;
        }
        if (root == null){
            //Empty Treap
            root = new TreapNode(key, value);
            return;
        }
        insert(root, key, value);
    }

    //Recursive helper method for insert
    private void insert(TreapNode head, K key, V value){
        //Base Case
        //Identical Key -> Replace Value
        if(head.key.equals(key)){
            head.value = value;
            return;
        }

        if(key.compareTo(head.key) < 0){
            //Base Case
            if(head.left == null) {
                head.setLeft(new TreapNode(key, value));
                restoreHeap(head.left);
                return;
            }
            //Recursive Case
            insert(head.left, key, value);
            return;
        }
        if(head.right == null){
            //Base Case
            head.setRight(new TreapNode(key, value));
            restoreHeap(head.right);
            return;
        }
        //Recursive Case
        insert(head.right, key, value);
    }

    //Recursive method to restore heap property
    private void restoreHeap(TreapNode head){
        //Base Case
        if(head.parent == null){
            return;
        }

        //Recursive Case
        //Parent has a higher priority than current node
        if(head.parent.priority.compareTo(head.priority) <= 0){
            if(head.parent.left != null &&
                    head.parent.left.equals(head)){
                rotateRight(head);
            } else {
                rotateLeft(head);
            }
            restoreHeap(head);
        }
    }

    //Rotate left the given pivot node
    private void rotateLeft(TreapNode pivot){
        TreapNode root = pivot.parent;
        TreapNode pivotLeft = pivot.left;

        if(root.parent == null){
            this.root = pivot;
            pivot.parent = null;
        } else if(root.parent.left != null &&
                root.parent.left.equals(root)){
            root.parent.setLeft(pivot);
        } else {
            root.parent.setRight(pivot);
        }

        pivot.setLeft(root);
        root.setRight(pivotLeft);
    }

    //Rotate right the given pivot node
    private void rotateRight(TreapNode pivot){
        TreapNode root = pivot.parent;
        TreapNode pivotRight = pivot.right;

        if(root.parent == null){
            this.root = pivot;
            pivot.parent = null;
        } else if(root.parent.right != null &&
                root.parent.right.equals(root)){
            root.parent.setRight(pivot);
        } else {
            root.parent.setLeft(pivot);
        }

        pivot.setRight(root);
        root.setLeft(pivotRight);
    }

    //Removes a key from this dictionary.  If the key is not present
    //in this dictionary, this method does nothing.  Returns the
    //value associated with the removed key, or null if the key
    //is not present.
    @Override
    public V remove(K key) {
        if(key == null){
            return null;
        }
        TreapNode node = lookupNode(key);
        if(node == null){
            return null;
        }
        return remove(node);
    }

    //Recursive helper method for remove
    private V remove(TreapNode node){
        V value = node.value;

        //Base Case
        //Node is a leaf
        if (node.left == null && node.right == null){
            if (node == root){
                root = null;
                return value;
            }
            if (node.parent.left != null &&
                    node.parent.left.equals(node)){
                node.parent.left = null;
            } else {
                node.parent.right = null;
            }
            node.parent = null;
            return value;
        }

        //Recursive Case
        if (node.left == null){
            rotateLeft(node.right);
        } else if (node.right == null) {
            rotateRight(node.left);
        } else if(node.left.priority.compareTo(node.right.priority) < 0){
            rotateLeft(node.right);
        } else {
            rotateRight(node.left);
        }
        return remove(node);
    }

    //Searches for a node that contains the given key
    private TreapNode lookupNode(K key){
        if(key == null){
            return null;
        }
        return lookupNode(root, key);
    }

    //Recursive helper method for lookupNode
    private TreapNode lookupNode(TreapNode head, K key){
        //Base Case
        if(head == null){
            return null;
        }
        if(head.key.equals(key)){
            return head;
        }

        //Recursive Case
        //Call lookupNode on a child
        if(key.compareTo(head.key) < 0){
            return lookupNode(head.left, key);
        }
        return lookupNode(head.right, key);
    }

    //Splits this treap into two treaps.  The left treap should contain
    //values less than the key, while the right treap should contain
    //values greater than or equal to the key.
    @Override
    public Treap<K, V>[] split(K key) {
        Treap<K, V> leftTreap;
        Treap<K, V> rightTreap;

        //If splitting an empty Treap, return two empty Treaps
        if(root == null) {
            leftTreap = new TreapMap<>();
            rightTreap = new TreapMap<>();
            return new Treap[] {leftTreap, rightTreap};
        }

        V value = lookup(key);
        if(value == null){
            //This Treap doesn't contain the given key
            insertHighPriority(key, root.value);
            leftTreap = new TreapMap<>(root.left);
            rightTreap = new TreapMap<>(root.right);
            if(root.left != null){
                root.left.parent = null;
            }
            if(root.right != null){
                root.right.parent = null;
            }
        } else {
            //This Treap contains the given key
            insertHighPriority(key, value);
            leftTreap = new TreapMap<>(root.left);
            if(root.left != null){
                root.left.parent = null;
            }
            root.left = null;
            rightTreap = new TreapMap<>(root);
        }

        return new Treap[] {leftTreap, rightTreap};
    }

    //Insert given key value pair into the root of the tree
    public void insertHighPriority(K key, V value) {
        if (key == null || value == null){
            return;
        }
        if (root == null){
            root = new TreapNode(key, value);
            return;
        }
        insertHighPriority(root, key, value);
    }

    //Recursive helper method for insertHighPriority
    private void insertHighPriority(TreapNode head, K key, V value){
        //Base Case
        if(head.key.equals(key)){
            head.value = value;
            head.priority = Treap.MAX_PRIORITY;
            restoreHeap(head);
            return;
        }

        if(key.compareTo(head.key) < 0){
            //Base Case
            if(head.left == null) {
                head.setLeft(new TreapNode(key, value, Treap.MAX_PRIORITY));
                restoreHeap(head.left);
                return;
            }
            //Recursive Case
            insertHighPriority(head.left, key, value);
            return;
        }
        //Base Case
        if(head.right == null){
            head.setRight(new TreapNode(key, value, Treap.MAX_PRIORITY));
            restoreHeap(head.right);
            return;
        }
        //Recursive Case
        insertHighPriority(head.right, key, value);
    }

    //Joins this treap with another treap.  If the other treap is not
    //an instance of the implementing class, both treaps are unmodified.
    //At the end of the join, this treap will contain the result.
    //This method may destructively modify both treaps.
    @Override
    public void join(Treap<K, V> t) {
        if(!(t instanceof TreapMap)){
            return;
        }
        TreapMap<K, V> otherTreap = (TreapMap<K, V>) t;

        //If one of the Treaps is empty, return other Treap
        if(otherTreap.root == null){
            return;
        }
        if(root == null){
            root = otherTreap.root;
            return;
        }

        TreapNode tempRoot = root;
        root = new TreapNode(root.key, root.value);

        //Other Treap has greater keys
        if(otherTreap.root.key.compareTo(root.key) > 0){
            root.setLeft(tempRoot);
            root.setRight(otherTreap.root);
        //This Treap has greater keys
        } else {
            root.setLeft(otherTreap.root);
            root.setRight(tempRoot);
        }
        remove(root);
    }

    //Returns a fresh iterator that points to the first element in
    //this Treap and iterates in sorted order.
    @Override
    public Iterator<K> iterator() {
        class TreapIterator implements Iterator<K>{
            //The next node to be iterated over
            //Null if there are no more key-value pairs left
            private TreapNode node;

            private TreapIterator(){
                if(root != null){
                    smallestNode(root);
                }
            }

            @Override
            public boolean hasNext() {
                return node != null;
            }

            //Assigns the smallest node of the given subtree to the node field.
            private void smallestNode(TreapNode head){
                if (head.left != null){
                    smallestNode(head.left);
                } else {
                    node = head;
                }
            }

            //Determines whether the given node is a left or right child.
            private boolean isLeftChild(TreapNode child){
                if(child.parent == null || child.parent.left == null){
                    return false;
                }
                return child.parent.left.equals(child);
            }

            //Returns the next key
            //Moves the node field to the next node to iterate over
            //If the iterator has finished iterating, node becomes null
            @Override
            public K next() {
                if(!hasNext()){
                    throw new NoSuchElementException();
                }
                K key = node.key;

                if(node.right != null){
                    node = node.right;
                    smallestNode(node);
                } else if(isLeftChild(node)){
                    node = node.parent;
                } else if(!isLeftChild(node)){
                    node = node.parent;
                    while(node != null && !isLeftChild(node)){
                        node = node.parent;
                    }
                    if(node != null){
                        node = node.parent;
                    }
                }

                return key;
            }

        }

        return new TreapIterator();
    }

    //Build a human-readable version of the treap.
    //Each node in the treap will be represented as
    //
    //    [priority] <key, value>\n
    //
    //Subtreaps are indented one tab over from their parent for
    //printing.  This method prints out the string representations
    //of key and value using the object's toString(). Treaps should
    //be printed in pre-order traversal fashion.
    @Override
    public String toString(){
        if(root == null){
            return "";
        }
        String output = toString(root, 0);
        if(output.charAt(output.length() - 1) == '\n'){
            return output.substring(0, output.length() - 1);
        }
        return output;
    }

    //Recursive helper method for toString
    private String toString(TreapNode node, int tabs){
        //Base Case
        String output = "";
        for(int i = 0; i < tabs; i++){
            output += "\t";
        }
        output += node.toString();
        output += "\n";

        //Recursive Case
        if(node.left != null){
            output += toString(node.left, tabs + 1);
        }
        if(node.right != null){
            output += toString(node.right, tabs + 1);
        }
        return output;
    }


    //Returns the balance factor of the treap.  The balance factor
    //is the height of the treap divided by the minimum possible
    //height of a treap of this size.  A perfectly balanced treap
    //has a balance factor of 1.0.  If this treap does not support
    //balance statistics, throw an exception.
    @Override
    public double balanceFactor() {
        return (double) height() / minHeight();
    }

    private int height(){
        if(root == null){
            return 0;
        }
        return height(root);
    }

    private int height(TreapNode node){
        if(node.left == null && node.right == null){
            return 0;
        }
        if(node.left == null){
            return height(node.right) + 1;
        }
        if(node.right == null){
            return height(node.left) + 1;
        }
        return Math.max(height(node.left), height(node.right)) + 1;
    }

    private int minHeight(){
        return (int) Math.ceil((Math.log(size() + 1) / Math.log(2)) - 1);
    }

    private int size(){
        Iterator<K> it = iterator();
        int size = 0;
        while(it.hasNext()){
            size++;
            it.next();
        }
        return size;
    }
}
