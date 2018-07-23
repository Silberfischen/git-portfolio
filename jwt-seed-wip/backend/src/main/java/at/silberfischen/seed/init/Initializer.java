package at.silberfischen.seed.init;

public abstract class Initializer {

    public void execute() {
        if (shouldInit()) {
            init();
        }
    }

    protected abstract void init();

    protected boolean shouldInit() {
        return true;
    }

}
