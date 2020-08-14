package solution;

class SegmentTree {
    private Node root;

    public SegmentTree(int[] intervals) {
        intervals = Utils.purgeArray(intervals);
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

    int d = 0;

    private void _edit(Node r, int min, int max, int c) {
        if (max <= r.a) return;
        if (r.a == min && r.b == max) {
            r.count += c;
            _update(r);
        } else {
            if (min >= r.left.b) {
                _edit(r.right, min, max, c);
            } else if (max <= r.left.b) {
                _edit(r.left, min, max, c);
            } else { // fork
                _edit(r.left, min, r.left.b, c);
                _edit(r.right, r.right.a, max, c);
            }
        }
    }

    private void _update(Node p) {
        if (p == root) {
            int oldSum = p.sum;
            p.updateSum();
            int newSum = p.sum;
            int dt = Math.abs(oldSum - newSum);
            d += dt;
        } else {
            p.updateSum();
            _update(p.parent);
        }
    }

    int getSum() { return root.sum; }
}