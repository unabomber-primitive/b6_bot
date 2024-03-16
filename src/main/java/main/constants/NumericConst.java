package main.constants;

public enum NumericConst {
    DEPARTURE_INPUT_ROWS(1),
    GUEST_INPUT_ROWS(8);

    private int count;

    NumericConst(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
