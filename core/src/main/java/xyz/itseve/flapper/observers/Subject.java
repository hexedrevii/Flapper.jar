package xyz.itseve.flapper.observers;

public interface Subject {
    void pushObserver(Observer o);
    void notify(int id);
}
