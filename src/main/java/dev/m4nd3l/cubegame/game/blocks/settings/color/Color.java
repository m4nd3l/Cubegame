package dev.m4nd3l.cubegame.game.blocks.settings.color;

public class Color {
    private float r, g, b, a;

    public Color(float r, float g, float b, float a) { setAll(r, g, b, a); }

    public float getA() { return a; }
    public float getB() { return b; }
    public float getG() { return g; }
    public float getR() { return r; }

    public Color copy(Color from) { setAll(from.getR(), from.getG(), from.getB(), from.getA()); return this; }
    public Color setR(float r) { this.r = r; return this; }
    public Color setG(float g) { this.g = g; return this; }
    public Color setB(float b) { this.b = b; return this; }
    public Color setA(float a) { this.a = a; return this; }
    public Color setAll(float r, float g, float b, float a) {
        setA(a); setB(b); setG(g); setR(r);
        return this;
    }
}
