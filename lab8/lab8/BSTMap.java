package lab8;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by XWEN on 3/9/2017.
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private K key;
    private V value;
    private BSTMap<K, V> parent = null;
    private BSTMap<K, V> left = null;
    private BSTMap<K, V> right = null;
    private int size;

    /* Empty BSTMap constructor */
    public BSTMap() {
        key = null;
        value = null;
        size = 0;
    }

    /* Creates BSTMap of size 1 with given key, value */
    public BSTMap(K key, V value) {
        this.key = key;
        this.value = value;
        size = 1;
    }

    /* Removes all of the mappings from this map. */
    public void clear() {
        key = null;
        value = null;
        left = null;
        right = null;
        size = 0;
    }

    /* Recursive helper for containsKey method */
    private boolean keyHelper(K ikey, BSTMap<K, V> bNode) {
        if (bNode == null) {
            return false;
        }
        if (ikey.equals(bNode.key)) {
            return true;
        } else if (ikey.compareTo(bNode.key) < 0) {
            return keyHelper(ikey, bNode.left);
        } else {
            return keyHelper(ikey, bNode.right);
        }
    }

    /* Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K ikey) {
        if (size == 0) {
            return false;
        }
        return keyHelper(ikey, this);
    }

    /* Recursive helper for get method */
    private V getHelper(K ikey, BSTMap<K, V> bNode) {
        if (bNode == null) {
            return null;
        } else if (ikey.equals(bNode.key)) {
            return bNode.value;
        } else if (ikey.compareTo(bNode.key) < 0) {
            return getHelper(ikey, bNode.left);
        } else {
            return getHelper(ikey, bNode.right);
        }
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    public V get(K ikey) {
        if (!containsKey(ikey)) {
            return null;
        }
        return getHelper(ikey, this);
    }

    /* Returns the number of key-value mappings in this map. */
    public int size() {
        return size;
    }

    /* Recursive helper for put method */
    private void putHelper(K ikey, V ivalue, BSTMap<K, V> bNode) {
        if (ikey.equals(bNode.key)) {
            bNode.value = ivalue;
        } else if (ikey.compareTo(bNode.key) < 0) {
            if (bNode.left == null) {
                bNode.left = new BSTMap<>(ikey, ivalue);
                bNode.left.parent = bNode;
                size += 1;
            } else {
                int prevSize = bNode.left.size();
                putHelper(ikey, ivalue, bNode.left);
                if (bNode.left.size() > prevSize) {
                    bNode.size += 1;
                }
            }
        } else {
            if (bNode.right == null) {
                bNode.right = new BSTMap<>(ikey, ivalue);
                bNode.right.parent = bNode;
                size += 1;
            } else {
                int prevSize = bNode.right.size();
                putHelper(ikey, ivalue, bNode.right);
                if (bNode.right.size() > prevSize) {
                    size += 1;
                }
            }
        }
    }

    /* Associates the specified value with the specified key in this map. */
    public void put(K ikey, V ivalue) {
        if (size == 0) {
            this.key = ikey;
            this.value = ivalue;
            size += 1;
        } else {
            putHelper(ikey, ivalue, this);
        }
    }

    /* Returns a Set view of the keys contained in this map. */
    public Set<K> keySet() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException. */
    public V remove(K ikey) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    public V remove(K ikey, V ivalue) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public Iterator<K> iterator() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /* Joins elements of BST in order as a StringBuilder */
    private void stringJoin(StringBuilder sb, BSTMap<K, V> map) {
        if (map == null) {
            return;
        } else {
            stringJoin(sb, map.left);
            sb.append(map.key.toString());
            sb.append(", ");
            stringJoin(sb, map.right);
        }
    }

    /* Prints key of BST in order */
    public void printInOrder() {
        StringBuilder sb = new StringBuilder();
        stringJoin(sb, this);
        System.out.println(sb.toString());
    }
}
