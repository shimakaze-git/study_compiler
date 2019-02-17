public class IntStack {
    private int[] data = new int[256];
    // 256個の要素が入るint型配列

    private int sp = 0;
    // スタックポインタ

    public int pop() {
        System.out.println(data);
        return data[--sp];
    }

    public void push(int value) {
        data[sp++] = value;
    }
}