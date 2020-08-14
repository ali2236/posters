package solution;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    static Rect[] rects;
    static boolean[] active;
    static Event[] events;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        rects = new Rect[n];
        active = new boolean[n];
        for (int i = 0; i < n; i++)
            rects[i] = new Rect(i, scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
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
            if (event.end) tree.delete(r.y1, r.y2);
             else tree.insert(r.y1, r.y2);
            area += y * dx;
            lastPoint = currentX;
        }
        parameter += tree.d;
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
        parameter += tree.d;
        System.out.println(area);
        System.out.println(parameter);
    }
}
