package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by XWEN on 3/16/2017.
 */
public class MyHashMap<K, V> implements Map61B<K, V> {
    private Pair<K, V>[] buckets;
    private int size = 0;
    private double loadFactor = 2;
    private int initSize = 4;
    private HashSet<K> keys = new HashSet<>();

    public MyHashMap() {
        buckets = new Pair[initSize];
    }

    public MyHashMap(int initialSize) {
        initSize = initialSize;
        buckets = new Pair[initSize];
    }

    public MyHashMap(int initialSize, double loadFactor) {
        initSize = initialSize;
        buckets = new Pair[initSize];
        this.loadFactor = loadFactor;
    }

    static class Pair<K, V> {
        K key;
        V val;
        Pair<K, V> next;

        Pair(K k, V v, Pair<K, V> n) {
            key = k;
            val = v;
            next = n;
        }
    }

    /* Returns bucket index for input key. */
    private int hashFunction(K k, int s) {
        return Math.floorMod(k.hashCode(), s);
    }

    /* Removes all of the mappings from this map. */
    public void clear() {
        keys.clear();
        buckets = new Pair[initSize];
        size = 0;
    }

    /* Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K k) {
        return keys.contains(k);
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    public V get(K k) {
        if (!containsKey(k)) {
            return null;
        }
        Pair<K, V> p = buckets[hashFunction(k, buckets.length)];
        while (p != null) {
            if (k.equals(p.key)) {
                return p.val;
            }
            p = p.next;
        }
        return null;
    }

    /* Returns the number of key-value mappings in this map. */
    public int size() {
        return size;
    }

    private void resize() {
        Pair<K, V>[] temp = new Pair[size];
        for (int i = 0; i < buckets.length; i++) {
            for (Pair<K, V> p = buckets[i]; p != null; p = p.next) {
                int newIndex = hashFunction(p.key, size);
                temp[newIndex] = new Pair<>(p.key, p.val, temp[newIndex]);
            }
        }
        buckets = temp;
    }

    /* Associates the specified value with the specified key in this map. */
    public void put(K k, V v) {
        if (((double) size / buckets.length) >= loadFactor) {
            resize();
        }
        int index = hashFunction(k, buckets.length);
        if (!containsKey(k)) {
            buckets[index] = new Pair<>(k, v, buckets[index]);
            size += 1;
            keys.add(k);
        } else {
            for (Pair<K, V> p = buckets[index]; p != null; p = p.next) {
                if (k.equals(p.key)) {
                    p.val = v;
                }
            }
        }
    }

    /* Returns a Set view of the keys contained in this map. */
    public Set<K> keySet() {
        return keys;
    }

    public Iterator<K> iterator() {
        return keys.iterator();
    }

    public V remove(K k) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public V remove(K k, V v) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public static void main(String[] args) {
        MyHashMap<String, Integer> m = new MyHashMap<>();
        for (Pair p : m.buckets) {
            if (p == null) {
                System.out.println("hello");
            }
        }
    }
}
