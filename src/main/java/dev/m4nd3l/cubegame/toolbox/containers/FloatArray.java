package dev.m4nd3l.cubegame.toolbox.containers;

public class FloatArray {
    private float[] array;
    private int size;

    private static final float[] EMPTY = {};

    public FloatArray() { this(10); }
    public FloatArray(int capacity) {
        if (capacity < 0) throw new IllegalArgumentException("Illegal capacity: " + capacity);
        this.array = capacity == 0 ? EMPTY : new float[capacity];
        this.size = 0;
    }

    public FloatArray(float[] toAdd) {
        if (toAdd == null) throw new IllegalArgumentException("Array cannot be null");
        this.array = new float[toAdd.length];
        System.arraycopy(toAdd, 0, this.array, 0, toAdd.length);
        this.size = toAdd.length;
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    public void put(float toAdd) {
        if (size == array.length) grow(array.length == 0 ? 10 : array.length * 2);
        array[size++] = toAdd;
    }

    public float get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index '" + index + "' out of bound " + size);
        return array[index];
    }

    public void clear() { array = EMPTY; }
    public void clear(int capacity) { array = new float[capacity]; }

    public void trim() {
        if (size == array.length) return;
        float[] trimmedArray = new float[size];
        System.arraycopy(array, 0, trimmedArray, 0, size);
        array = trimmedArray;
    }

    private void grow(int minCapacity) {
        float[] newArray = new float[minCapacity];
        System.arraycopy(array, 0, newArray, 0, size);
        array = newArray;
    }

    public void forEach(java.util.function.Consumer<? super Float> action) { for (int i = 0; i < size; i++) action.accept(array[i]); }
    public float[] getArray() { return array; }
}