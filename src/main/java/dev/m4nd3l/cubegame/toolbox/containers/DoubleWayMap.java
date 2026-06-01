package dev.m4nd3l.cubegame.toolbox.containers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class DoubleWayMap<K, V> {
    private Map<K, V> map;
    private Map<V, K> reversedMap;

    public DoubleWayMap() { this.map = new HashMap<>(); this.reversedMap = new HashMap<>(); }
    public DoubleWayMap(Map<K, V> map) {
        this();
        put(map);
    }

    public int size() { return map.size(); }
    public boolean isEmpty() { return map.isEmpty() && reversedMap.isEmpty(); }

    public V put(K key, V value) { map.put(key, value); reversedMap.put(value, key); return value; }
    public void put(Map<K, V> mapToAdd) {
        map.putAll(mapToAdd);
        mapToAdd.forEach((key, value) -> reversedMap.put(value, key));
    }

    public V getV(K key) { return map.get(key); }
    public V getV(K key, V defaultValue) { return map.getOrDefault(key, defaultValue); }

    public K getK(V value) { return reversedMap.get(value); }
    public K getK(V value, K defaultKey) { return reversedMap.getOrDefault(value, defaultKey); }

    public boolean containsKey(K key) { return map.containsKey(key); }
    public boolean containsValue(V value) { return map.containsValue(value); }
    public boolean contains(K key, V value) { return containsKey(key) && containsValue(value); }

    public Set<K> getKeySet() { return map.keySet(); }
    public Set<V> getValueSet() { return new HashSet<>(map.values()); }
    public Set<Map.Entry<K, V>> getEntrySet() { return map.entrySet(); }

    public Set<V> getReversedKeySet() { return reversedMap.keySet(); }
    public Set<K> getReversedValueSet() { return new HashSet<>(reversedMap.values()); }
    public Set<Map.Entry<V, K>> getReversedEntrySet() { return reversedMap.entrySet(); }

    public void actionOnKeys(ActionOnKeys<K> action) { map.forEach((key, _) -> action.invoke(key)); }
    public void actionOnValues(ActionOnValues<V> action) { map.forEach((_, value) -> action.invoke(value)); }
    public void action(BiConsumer<? super K, ? super V> action) { map.forEach((action)); }

    public void clear() { map.clear(); reversedMap.clear(); }
    public V removeK(K key) { V value = map.remove(key); reversedMap.remove(value); return value; }
    public K removeV(V value) { K key = reversedMap.remove(value); map.remove(key); return key; }
    public boolean remove(K key, V value) { return map.remove(key, value) && reversedMap.remove(value, key); }
}
