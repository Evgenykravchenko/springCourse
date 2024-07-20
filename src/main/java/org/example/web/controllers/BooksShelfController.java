package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.example.app.services.BookService;
import org.example.app.validators.BookValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/books")
public class BooksShelfController {

    private final BookService bookService;

    @Autowired
    public BooksShelfController(BookService bookService) {
        this.bookService = bookService;
    }

    private final Logger logger = Logger.getLogger(BooksShelfController.class);

    @GetMapping("/shelf")
    public String booksShelf(Model model) {
        logger.info("GET books shelf");

        model.addAttribute("book", new Book());
        model.addAttribute("bookList", bookService.getAllBooks());
        return "books_shelf";
    }

    @PostMapping("/save")
    public String saveBook(Book book, RedirectAttributes redirectAttributes) {
        logger.info("POST save book: " + book + " to books shelf");

        if (!BookValidator.isValid(book)) {
            redirectAttributes.addFlashAttribute("error", "At least one field (author, title, size) must be filled");
            return "redirect:/books/shelf";
        }
        bookService.saveBook(book);
        logger.info("Current repository size is: " + bookService.getAllBooks().size());

        return "redirect:/books/shelf";
    }

    @PostMapping("/remove")
    public String removeBook(@RequestParam("bookIdToRemove") Integer bookIdToRemove) {
        bookService.removeBookById(bookIdToRemove);

        return "redirect:/books/shelf";
    }

    @PostMapping("/removeByRegex")
    public String removeByRegex(@RequestParam("queryRegex") String queryRegex, RedirectAttributes redirectAttributes) {
        logger.info("POST remove books by regex: " + queryRegex);

        try {
            int removedCount = bookService.removeBooksByRegex(queryRegex);
            redirectAttributes.addFlashAttribute("message", "Removed " + removedCount + " books matching the criteria");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addAttribute("error", "Something goes wrong");
        }

        return "redirect:/books/shelf";
    }

}
