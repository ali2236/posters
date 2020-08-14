package solution;

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