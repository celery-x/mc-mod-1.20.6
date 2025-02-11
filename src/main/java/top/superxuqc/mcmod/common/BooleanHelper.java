package top.superxuqc.mcmod.common;

public class BooleanHelper {
    private boolean b;

    public BooleanHelper(boolean b) {
        this.b = b;
    }

    public boolean isB() {
        return b;
    }

    public void setB(boolean b) {
        this.b = b;
    }

    public void opposite() {
        b = !b;
    }

    public void setFalse(){
        b = false;
    }

    public void setTrue() {
        b = true;
    }
}
