package fourth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Rect[] rects;
    static boolean[] active;
    static Event[] events;

    public static void main(String[] args) throws Exception {

        long start = System.currentTimeMillis();

        //Scanner scanner = new Scanner(new File("tests/test.txt"));
        Scanner scanner = new Scanner("7\n" +
                "-10 -1 0 4\n" +
                "-2 -5 5 0\n" +
                "4 -8 6 11\n" +
                "-5 2 8 9\n" +
                "1 6 3 8\n" +
                "10 0 12 6\n" +
                "12 -4 14 2\n");

        int n = scanner.nextInt();

        rects = new Rect[n];
        active = new boolean[n];

        for (int i = 0; i < n; i++) {
            rects[i] = new Rect(
                    i,
                    scanner.nextInt(),
                    scanner.nextInt(),
                    scanner.nextInt(),
                    scanner.nextInt()
            );
        }

        events = new Event[n * 2];
        int i = 0;
        for (Rect rect : rects) {
            events[i++] = new Event(false, rect, EventType.x);
            events[i++] = new Event(true, rect, EventType.x);
        }

        Arrays.sort(events, Event::compare);

        int[] intervals = new int[2 * n];
        i = 0;
        for (Rect rect : rects) {
            intervals[i++] = rect.y1;
            intervals[i++] = rect.y2;
        }

        Arrays.sort(intervals);

        SegmentTree tree = new SegmentTree(intervals);
        int lastPoint = events[0].getPoint();
        int area = 0;
        int parameter = 0;

        for (Event event : events) {
            int currentX = event.getPoint();
            int dx = currentX - lastPoint;

            int y = tree.getSum();

            Rect r = event.rect;
            if (event.end) {
                tree.delete(r.y1, r.y2);
            } else {
                tree.insert(r.y1, r.y2);
            }

            area += y * dx;

            lastPoint = currentX;
        }
        parameter += tree.getD();
        ////////////////////////////////////////

        // get the rest of parameter

        for (Event event : events) {
            event.type = EventType.y;
        }

        Arrays.sort(events, Event::compare);

        i = 0;
        for (Rect rect : rects) {
            intervals[i++] = rect.x1;
            intervals[i++] = rect.x2;
        }

        Arrays.sort(intervals);

        tree = new SegmentTree(intervals);

        for (Event event : events) {
            Rect r = event.rect;
            if (event.end) {
                tree.delete(r.x1, r.x2);
            } else {
                tree.insert(r.x1, r.x2);
            }
        }
        parameter += tree.getD();

        ///////////////////////////////////////
        System.out.println(area);
        System.out.println(parameter);
        System.out.println(System.currentTimeMillis() - start);
    }

}

enum EventType {
    x, y
}

class Event {
    final boolean end;
    final Rect rect;
    EventType type;

    Event(boolean end, Rect rect, EventType type) {
        this.end = end;
        this.rect = rect;
        this.type = type;
    }

    int getPoint() {
        return type == EventType.x ?
                end ? rect.x2 : rect.x1 :
                end ? rect.y2 : rect.y1;
    }

    public static int compare(Event o1, Event o2) {
        int result = Integer.compare(o1.getPoint(), o2.getPoint());
        if (result == 0) {
            if (o1.end) return 1;
            if (o2.end) return -1;
        }
        return result;
    }
}

class Node {
    final int a, b;
    private int count = 0;
    int sum = 0;

    Node parent, left, right;

    Node(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public int getCount() {
        return count;
    }

    public int incrementCount(int i) {
        return this.count += i;
    }

    boolean active() {
        return count > 0;
    }

    boolean leaf() {
        return left == null && right == null;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", a, b);
    }

    public void updateSum() {
        if (active()) {
            sum = b - a;
        } else if (!leaf()) {
            sum = left.sum + right.sum;
        } else {
            sum = 0;
        }
    }
}

class SegmentTree {
    private Node root;

    public SegmentTree(int[] intervals) {
        intervals = purgeArray(intervals);
        root = new Node(intervals[0], intervals[intervals.length - 1]);
        build(root, intervals, 0, intervals.length - 1);
    }

    private void build(Node r, int[] arr, int s, int e) {
        if ((e - s <= 1) || r == null) return;
        int mid = (s + e) / 2;
        Node n1 = new Node(arr[s], arr[mid]);
        Node n2 = new Node(arr[mid], arr[e]);
        n1.parent = r;
        n2.parent = r;
        r.left = n1;
        r.right = n2;
        build(n1, arr, s, mid);
        build(n2, arr, mid, e);
    }

    void insert(int min, int max) {
        _edit(root, min, max, 1);
    }

    void delete(int min, int max) {
        _edit(root, min, max, -1);
    }

    private int d = 0;

    public int getD() {
        return d;
    }

    private void _edit(Node r, int min, int max, int c) {
        if (max <= r.a) return;
        if (r.a == min && r.b == max) {
            if (r.leaf()){
            int before = r.getCount();
            int after = r.incrementCount(c);

            r.updateSum();
            _update(r.parent);

            if (before==0 || after==0){
                d += r.b - r.a;
            }
            } else {
                _edit(r.left, r.left.a, r.left.b, c);
                _edit(r.right, r.right.a, r.right.b, c);
            }
        } else {
            if (min >= r.left.b) {
                _edit(r.right, min, max, c);
            } else if (max <= r.left.b) {
                _edit(r.left, min, max, c);
            } else {
                _edit(r.left, min, r.left.b, c);
                _edit(r.right, r.right.a, max, c);
            }
        }
    }

    private void _update(Node p) {
        if (p == null) return;
        p.updateSum();
        _update(p.parent);
    }


    int getSum() {
        return root.sum;
    }

    private int[] purgeArray(int[] arr) {
        int n = arr.length;
        if (n == 0 || n == 1) return arr;
        List<Integer> temp = new ArrayList<>();
        for (int i = 0; i < n - 1; i++) {
            if (arr[i] != arr[i + 1]) {
                temp.add(arr[i]);
            }
        }
        temp.add(arr[n - 1]);

        int[] array = new int[temp.size()];
        for (int i = 0; i < temp.size(); i++) {
            array[i] = temp.get(i);
        }

        return array;
    }

    @Override
    public String toString() {
        return root.toString();
    }
}

class Rect {
    final int index;
    final int x1, y1, x2, y2;

    Rect(int index, int x1, int y1, int x2, int y2) {
        this.index = index;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    public String toString() {
        return String.format("[%d, %d, %d, %d]", x1, y1, x2, y2);
    }
}