package main.utils;

public record Interval(int s, int e) {
    public int length(){
        return e - s +1;
    }

    public boolean isEmpty(){
        return e < s;
    }

    public Interval intersect(Interval o){
        return new Interval(Math.max(s, o.s), Math.min(e, o.e));
    }
}
