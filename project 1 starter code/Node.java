public class Node {
    private int g;
    private int h;
    private int f;
    private Node previous;
    private Point current;

    public Node(int g, int h, int f, Node previous, Point current){
        this.g = g;
        this.h = h;
        this.f = f;
        this.previous = previous;
        this.current = current;
    }

    public int getF() {
        return f;
    }
    public int getG() {
        return g;
    }
    public int getH() {
        return h;
    }
    public Node getPrevious() {
        return previous;
    }
    public Point getCurrent() {
        return current;
    }

    public void setG(int g) {
        this.g = g;
    }

    public void setH(int h) {
        this.h = h;
    }

    public void setF(int f) {
        this.f = f;
    }

    public void setPrevious(Node previous) {
        this.previous = previous;
    }

    public void setCurrent(Point current) {
        this.current = current;
    }
}
