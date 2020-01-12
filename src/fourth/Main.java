package fourth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Rect[] rects;
    static boolean[] active;
    static Event[] events;

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
            events[i++] = new Event(false, rect,EventType.x);
            events[i++] = new Event(true, rect,EventType.x);
        }

        Arrays.sort(events, Event::compare);

        int[] ys = new int[2 * n];
        i = 0;
        for (Rect rect : rects) {
            ys[i++] = rect.y1;
            ys[i++] = rect.y2;
        }

        Arrays.sort(ys);

        SegmentTree tree = new SegmentTree(ys);
        int lastX = events[0].getPoint();
        int area = 0;
        int parameter = 0;

        for (Event event : events) {
            int currentX = event.getPoint();
            int dx = currentX - lastX;

            int y = tree.getSum();
            int gaps = tree.getGaps();

            Rect r = event.rect;
            if (event.end) {
                tree.delete(r.y1, r.y2);
            } else {
                tree.insert(r.y1, r.y2);
            }

            area += y * dx;
            parameter += (dx * 2) * (1 + gaps);

            lastX = currentX;
        }

        ////////////////////////////////////////


        ///////////////////////////////////////

        System.out.println("area = " + area);
        System.out.println("parameter = " + parameter);
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
    final int min, max;
    int count = 0;
    int sum = 0;
    int gaps = 0;


    Node parent, left, right;

    Node(int min, int max) {
        this.min = min;
        this.max = max;
    }

    boolean active(){
        return count > 0;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", min, max);
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

    private void _edit(Node r, int min, int max, int c) {
        if (max <= r.min) return;
        if (r.min == min && r.max == max) {
            if (r.right!=null && r.left!=null){
                _edit(r.right,r.right.min,r.right.max,c);
                _edit(r.left,r.left.min,r.left.max,c);
            } else {
                r.count += c;
            }
        } else {
            if (min >= r.left.max) {
                _edit(r.right, min, max, c);
            } else if (max <= r.left.max) {
                _edit(r.left, min, max, c);
            } else {
                _edit(r.left, min, r.left.max, c);
                _edit(r.right, r.right.min, max, c);
            }
        }
    }

    int getSum() {
        return _getSum(root);
    }

    int getGaps() {
        return root.gaps;
    }

    private int _getSum(Node r){
        if (r==null) return 0;
         if (r.active()){
             return r.max - r.min;
         } else if(r.left!=null) {
             return _getSum(r.left) + _getSum(r.right);
         } else {
             return 0;
         }
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