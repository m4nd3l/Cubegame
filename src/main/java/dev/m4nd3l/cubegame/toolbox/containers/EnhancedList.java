package dev.m4nd3l.cubegame.toolbox.containers;

import dev.m4nd3l.cubegame.engine.coordinates.ChunkCoordinates;
import dev.m4nd3l.cubegame.game.world.chunks.Chunk;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class EnhancedList<E> implements Iterable<E> {
    private List<E> list;

    public EnhancedList() { this.list = new ArrayList<>(); }
    public EnhancedList(int size) { this.list = new ArrayList<>(size); }
    public EnhancedList(List<E> list) { this.list = list; }

    public int size() { return list.size(); }
    public boolean isEmpty() { return list.isEmpty(); }

    public boolean add(E element) { return list.add(element); }
    @SafeVarargs
    public final boolean add(E... elements) { return add(List.of(elements)); }
    public boolean add(Collection<E> elements) { return list.addAll(elements); }

    public boolean addIf(Predicate<E> condition, E element) { if (condition.test(element)) return add(element); else return false; }
    @SafeVarargs
    public final boolean addIf(Predicate<E> condition, E... elements) { return addIf(condition, List.of(elements)); }
    public boolean addIf(Predicate<E> condition, Collection<E> elements) {
        boolean returnValue = false;
        for (E element : elements) returnValue = returnValue || addIf(condition, element);
        return returnValue;
    }

    public void clear() { list.clear(); }
    public boolean remove(E element) { return list.remove(element); }
    @SafeVarargs
    public final boolean remove(E... elements) { return list.removeAll(List.of(elements)); }
    public boolean remove(Collection<E> elements) { return list.removeAll(elements); }
    public boolean removeIf(Predicate<E> condition, E element) { if (condition.test(element)) return remove(element); else return false; }
    @SafeVarargs
    public final boolean removeIf(Predicate<E> condition, E... elements) { return removeIf(condition, List.of(elements)); }
    public boolean removeIf(Predicate<E> condition, Collection<E> elements) {
        boolean returnValue = false;
        for (E element : elements) returnValue = returnValue || removeIf(condition, elements);
        return returnValue;
    }

    public boolean contains(E element) { return list.contains(element); }

    @NotNull
    @Override public Iterator<E> iterator() { return list.iterator(); }
    @Override public void forEach(Consumer<? super E> action) { list.forEach(action); }
    @Override public Spliterator<E> spliterator() { return list.spliterator(); }

    public static <T> boolean staticAddIf(Predicate<T> condition, Collection<T> list, T element) {
        if (condition.test(element)) return list.add(element);
        else return false;
    }
    @SafeVarargs
    public static <T> boolean staticAddIf(Predicate<T> condition, Collection<T> list, T... elements) {
        return staticAddIf(condition, list, List.of(elements));
    }
    public static <T> boolean staticAddIf(Predicate<T> condition, Collection<T> list, Collection<T> elements) {
        boolean returnValue = false;
        for (T element : elements) returnValue = returnValue || staticAddIf(condition, list, element);
        return returnValue;
    }
}
