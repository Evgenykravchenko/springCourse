package org.example.app.services;

import org.example.web.dto.Book;

import org.example.app.repositories.ProjectRepository;
import org.example.app.validators.BookValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final ProjectRepository<Book> booksRepository;

    @Autowired
    public BookService(ProjectRepository<Book> booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> getAllBooks() {
        return booksRepository.retrieveAll();
    }

    public void saveBook(Book book) {
        if (!BookValidator.isValid(book)) {
            throw new IllegalArgumentException("At least one field (author, title, size) must be filled");
        }

        booksRepository.store(book);
    }

    public boolean removeBookById(Integer bookIdToRemove) {
        return booksRepository.removeItemById(bookIdToRemove);
    }

    public int removeBooksByRegex(String queryRegex) {
        return booksRepository.removeItemsByRegex(queryRegex);
    }
}
