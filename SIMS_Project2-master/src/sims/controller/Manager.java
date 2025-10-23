package sims.controller;

public interface Manager<T> {
     void add(T item);
     void delete(String id);
     void update(T item);
     T search(String id);
     void displayAll();

}
