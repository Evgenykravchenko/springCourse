package org.example.app.validators;

import org.example.web.dto.Book;

public class BookValidator {
    public static boolean isValid(Book book) {
        return (book.getAuthor() != null && !book.getAuthor().isEmpty()) ||
                (book.getTitle() != null && !book.getTitle().isEmpty()) ||
                (book.getSize() != null);
    }
}
