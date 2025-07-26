package xyz.itseve.flapper.observers;

public enum ObserverID {
    GROUND(1), PIPE(2);

    private final int id;
    public int id() {return id;}

    ObserverID(int id) {
        this.id = id;
    }
}
