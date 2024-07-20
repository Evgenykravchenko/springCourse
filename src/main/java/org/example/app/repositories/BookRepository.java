package org.example.app.repositories;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.example.app.validators.BookValidator;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Repository
public class BookRepository implements ProjectRepository<Book> {

    private final Logger logger = Logger.getLogger(BookRepository.class);
    private final List<Book> repository = new ArrayList<>();

    @Override
    public List<Book> retrieveAll() {
        return new ArrayList<>(repository);
    }

    @Override
    public void store(Book book) {
        if (!BookValidator.isValid(book)) {
            throw new IllegalArgumentException("At least one field (author, title, size) must be filled");
        }

        book.setId(book.hashCode());
        logger.info("Store new book: " + book);

        repository.add(book);
    }

    @Override
    public boolean removeItemById(Integer bookIdToRemove) {
        for (Book book: repository) {
            if (book.getId().equals(bookIdToRemove)) {
                logger.info("Remove book completed: " + book);
                return repository.remove(book);
            }
        }

        logger.info("Book was not found");

        return false;
    }

    @Override
    public int removeItemsByRegex(String queryRegex) {
        Pattern pattern = Pattern.compile(queryRegex);
        List<Book> booksToRemove = repository.stream()
                .filter(book -> pattern.matcher(book.getAuthor()).find()
                        || pattern.matcher(book.getTitle()).find()
                        || pattern.matcher(String.valueOf(book.getSize())).find())
                .collect(Collectors.toList());

        booksToRemove.forEach(book -> {
            logger.info("Remove book by regex: " + book);
            repository.remove(book);
        });

        return booksToRemove.size();
    }
}
