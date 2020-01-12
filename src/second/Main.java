package second;

import java.util.*;

public class Main {

    static Node[] tree = new Node[100_000];
    static int[] interval = new int[100_000];

    public static void main(String[] args) {
        Scanner scanner = new Scanner("7\n" +
                "-10 -1 0 4\n" +
                "-2 -5 5 0\n" +
                "4 -8 6 11\n" +
                "-5 2 8 9\n" +
                "1 6 3 8\n" +
                "10 0 12 6\n" +
                "12 -4 14 2\n");
        int n = scanner.nextInt();
        for(int j=1;j<=n;j++){
            rects.add(new Rect(
                    scanner.nextInt(),
                    scanner.nextInt(),
                    scanner.nextInt(),
                    scanner.nextInt()
                    ));
        }
        compressncalc();
        Sweep();

        System.out.println(ans);
    }

    static void build(int x, int a, int b) {
        tree[x] = new Node(0, 0, 0);
        if (a == b) {
            tree[x].sum += interval[a];
            return;
        }
        build(x * 2, a, (a + b) / 2);
        build(x * 2 + 1, (a + b) / 2 + 1, b);
        tree[x].sum = tree[x * 2].sum + tree[x * 2 + 1].sum;
    }

    static int ask(int x) {
        if (tree[x].prob != 0) return tree[x].sum;
        return tree[x].ans;
    }

    static int st, en, V;

    static void update(int x, int a, int b) {
        if (st > b || en < a) return;
        if (a >= st && b <= en) {
            tree[x].prob += V;
            return;
        }
        update(x * 2, a, (a + b) / 2);
        update(x * 2 + 1, (a + b) / 2 + 1, b);
        tree[x].ans = ask(x * 2) + ask(x * 2 + 1);
    }

    static List<Rect> rects = new ArrayList<>();
    static List<Integer> sorted = new ArrayList<>();
    static List<Event> sweep = new ArrayList<>();

    static void compressncalc(){
        sweep.clear();
        sorted.clear();
        for(Rect R : rects){
            sorted.add(R.y1);
            sorted.add(R.y2);
        }
        sorted.sort(Integer::compareTo);
        int sz = sorted.size();
        for(int j=0;j<sorted.size() - 1;j++)
            interval[j+1] = sorted.get(j + 1) - sorted.get(j);
        for(Rect R : rects){
            sweep.add(new Event(R.x1 , R.y1 , R.y2 , 1));
            sweep.add(new Event(R.x2 , R.y1 , R.y2 , -1));
        }
        sweep.sort(Event::compare);
        build(1,1,sz-1);
    }

    static long ans;
    static void Sweep(){
        ans=0;
        if(sorted.isEmpty() || sweep.isEmpty()) return;
        int last = 0 , sz_ = sorted.size();
        for(int j=0;j<sweep.size();j++){
            ans+= (long) (sweep.get(j).x - last) * ask(1);
            last = sweep.get(j).x;
            V = sweep.get(j).type;
            st = sorted.stream().min(Integer::compareTo).get() - sorted.get(0) + 1;
            en = sorted.stream().min(Integer::compareTo).get() - sorted.get(0);
            update(1 , 1 , sz_-1);
        }
    }

}

class Rect {
    final int x1, y1, x2, y2;

    Rect(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    Rect intersection(Rect other) {
        return new Rect(
                Math.max(this.x1, other.x1),
                Math.max(this.y1, other.y1),
                Math.min(this.x2, other.x2),
                Math.min(this.y2, other.y2)
        );
    }

    boolean intersects(Rect other) {
        return other.x1 <= x2 &&
                other.y1 <= y2 &&
                other.x2 >= x1 &&
                other.y2 >= y1;
    }

    int getArea() {
        return (x2 - x1) * (y2 - y1);
    }

    int getPerimeter() {
        return 2 * ((x2 - x1) + (y2 - y1));
    }

    @Override
    public String toString() {
        return String.format("[%d, %d, %d, %d]", x1, y1, x2, y2);
    }
}

class Event {
    final int x, y1, y2, type;

    Event(int x, int y1, int y2, int type) {
        this.x = x;
        this.y1 = y1;
        this.y2 = y2;
        this.type = type;
    }

    static public int compare(Event o1, Event o2) {
        return Integer.compare(o1.x, o2.x);
    }
}

class Node {
    int prob, sum, ans;

    public Node(int prob, int sum, int ans) {
        this.prob = prob;
        this.sum = sum;
        this.ans = ans;
    }
}

class IntervalTree {
    private List<Node> nodes;

    public IntervalTree(int count) {
        nodes = new ArrayList<>(count);
    }


}