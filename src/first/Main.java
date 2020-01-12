package first;

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

        List<Event> events = new ArrayList<>(2 * n);
        for (Rect rect : rects) {
            events.add(new Event(rect.x1, rect, false));
            events.add(new Event(rect.x2, rect, true));
        }

        events.sort(Event::compare);

        List<Rect> active = new ArrayList<>();

        List<Rect> intersections = new ArrayList<>();

        for (Event event : events) {
            if (!event.end) {
                active.add(event.r);
            } else {
                for (Rect rect : active){
                    if (rect != event.r) {
                        if (rect.intersects(event.r)) {
                            Rect union = event.r.intersection(rect);
                            intersections.add(union);
                        }
                    }
                }
                active.remove(event.r);
            }
        }

        int area = 0;
        int perimeter = 0;

        for (Rect rect : rects) {
            area += rect.getArea();
            perimeter += (rect.y2 - rect.y1) * 2;//rect.getPerimeter();
        }

        for (Rect rect : intersections) {
            area -= rect.getArea();
            perimeter -= (rect.y2 - rect.y1) * 2;//rect.getPerimeter();
        }

        System.out.println("area = " + area);
        System.out.println("perimeter = " + perimeter);
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

    Rect intersection(Rect other){
        return new Rect(
                Math.max(this.x1,other.x1),
                Math.max(this.y1,other.y1),
                Math.min(this.x2,other.x2),
                Math.min(this.y2,other.y2)
        );
    }

    boolean intersects(Rect other) {
        return other.x1 <= x2 &&
                other.y1 <= y2 &&
                other.x2 >= x1 &&
                other.y2 >= y1;
    }

    int getArea(){
        return (x2 - x1) * (y2 - y1);
    }

    int getPerimeter(){
        return 2 * ((x2-x1) + (y2-y1));
    }

    @Override
    public String toString() {
        return String.format("[%d, %d, %d, %d]", x1, y1, x2, y2);
    }
}

class Event {
    final int p;
    final Rect r;
    final boolean end;

    Event(int p, Rect r, boolean end) {
        this.p = p;
        this.r = r;
        this.end = end;
    }

    public static int compare(Event o1, Event o2) {
        int result = Integer.compare(o1.p, o2.p);
        if (result==0){
            if (o1.end) return 1;
            if (o2.end) return -1;
        }
        return result;
    }
}