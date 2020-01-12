package third;

import java.util.*;

public class Main {

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

        Rect[] rects = new Rect[n];

        for (int i = 0; i < n; i++) {
            rects[i] = new Rect(
                    scanner.nextInt(),
                    scanner.nextInt(),
                    scanner.nextInt(),
                    scanner.nextInt()
            );
        }

        int area = 0;
        int parameter = 0;

        // X - intervals
        int[] xIntervals = new int[2 * n];
        int p = 0;
        for (Rect r : rects) {
            xIntervals[p++] = r.x1;
            xIntervals[p++] = r.x2;
        }

        Arrays.sort(xIntervals);

        Tree xTree = new Tree(xIntervals);

        // Y - intervals
        int[] yIntervals = new int[2 * n];
        p = 0;
        for (Rect r : rects) {
            yIntervals[p++] = r.y1;
            yIntervals[p++] = r.y2;
        }

        Arrays.sort(yIntervals);

        Tree yTree = new Tree(yIntervals);

        // add bounds

        for (Rect r : rects){
            xTree.addBound(r.x1,r.x2);
            yTree.addBound(r.y1,r.y2);
        }

        // sweep horizontal

        System.out.println(xTree);
    }
}

class Node {
    final int i;
    final int a, b;
    int count = 0;

    Node(int i,int a, int b) {
        this.i = i;
        this.a = a;
        this.b = b;
    }

    @Override
    public String toString() {
        return "("+a+", "+b+")";
    }
}

class Tree {
    public Node[] nodes;

    public Tree(int[] intervals) {
        int[] set = arraySet(intervals);
        int space = set.length+1;
        nodes = new Node[space+1];
        build(set);
    }

    private void build(int[] interval) {
        for (int i = 0; i < interval.length - 1; i++) {

        }
    }

    int[] arraySet(int[] arr){
        int n = arr.length;
        if (n==0 || n==1) return arr;
        List<Integer> temp = new ArrayList<>();
        for (int i=0; i<n-1; i++){
            if (arr[i] != arr[i+1]){
                temp.add(arr[i]);
            }
        }
        temp.add(arr[n-1]);

        int[] array = new int[temp.size()];
        for (int i = 0; i < temp.size(); i++) {
            array[i] = temp.get(i);
        }

        return array;
    }

    private Node root(){
        return nodes[1];
    }

    private Node left(Node r){
        return nodes[r.i*2];
    }

    private Node right(Node r){
        return nodes[r.i*2+1];
    }

    public void addBound(int min, int max) {
        _addBound(root(),min,max);
    }

    private void _addBound(Node r,int min, int max){
        try {
            if (min == r.a && (r.b - max) <= 1) {
                r.count++;
                return;
            }
            int mid = left(r).b;
            if (min >= r.a && max <= r.b) {
                if (max <= mid) {
                    _addBound(left(r), min, max);
                } else /*max >= mid+1*/ {
                    if (min > mid) {
                        _addBound(right(r), min, max);
                    } else { // split
                        _addBound(left(r), min, mid);
                        _addBound(right(r), mid + 1, max);
                    }
                }
            }
        } catch (Exception e){
            System.out.println(e);
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
