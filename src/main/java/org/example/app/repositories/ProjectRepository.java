package org.example.app.repositories;

import java.util.List;

public interface ProjectRepository<T> {
    List<T> retrieveAll();
    void store(T element);
    boolean removeItemById(Integer bookIdToRemove);
    int removeItemsByRegex(String queryRegex);
}
