package solution;

import java.util.ArrayList;
import java.util.List;

class Node {
    final int a, b;
    int count = 0;
    int sum = 0;

    Node parent, left, right;

    Node(int a, int b) {
        this.a = a;
        this.b = b;
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

class Utils {
    static int[] purgeArray(int[] arr) {
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
}