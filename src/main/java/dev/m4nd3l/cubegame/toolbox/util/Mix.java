package dev.m4nd3l.cubegame.toolbox.util;

import java.util.Objects;

public class Mix<T1, T2> {
    private T1 t1;
    private T2 t2;

    public Mix(T1 t1, T2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public T1 get1() { return t1; }
    public T2 get2() { return t2; }

    public Mix<T1, T2> set1(T1 type1) { this.t1 = t1; return this; }
    public Mix<T1, T2> set2(T2 type2) { this.t2 = t2; return this; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mix<?, ?> mix = (Mix<?, ?>) o;
        return Objects.equals(t1, mix.t1) && Objects.equals(t2, mix.t2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(t1, t2);
    }
}
