package dev.m4nd3l.cubegame.toolbox.util;

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
}
