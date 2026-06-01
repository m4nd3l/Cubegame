package dev.m4nd3l.cubegame.toolbox.containers;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

public class EnhancedMap<K, V> {
    private Map<K, V> map;

    public EnhancedMap() { this.map = new HashMap<>(); }
    public EnhancedMap(Map<K, V> map) { this.map = map; }

    public int size() { return map.size(); }
    public boolean isEmpty() { return map.isEmpty(); }

    public V put(K key, V value) { return map.put(key, value); }
    public void put(Map<K, V> mapToAdd) { map.putAll(mapToAdd); }

    public V get(K key) { return map.get(key); }
    public V get(K key, V defaultValue) { return map.getOrDefault(key, defaultValue); }

    public boolean containsKey(K key) { return map.containsKey(key); }
    public boolean containsValue(V value) { return map.containsValue(value); }
    public boolean contains(K key, V value) { return containsKey(key) && containsValue(value); }

    public Set<K> getKeySet() { return map.keySet(); }
    public Set<V> getValueSet() { return new HashSet<>(map.values()); }
    public Set<Entry<K, V>> getEntrySet() { return map.entrySet(); }

    public void actionOnKeys(ActionOnKeys<K> action) { map.forEach((key, _) -> action.invoke(key)); }
    public void actionOnValues(ActionOnValues<V> action) { map.forEach((_, value) -> action.invoke(value)); }
    public void action(BiConsumer<? super K, ? super V> action) { map.forEach((action)); }

    public void clear() { map.clear(); }
    public V remove(K key) { return map.remove(key); }
    public boolean remove(K key, V value) { return map.remove(key, value); }
}
