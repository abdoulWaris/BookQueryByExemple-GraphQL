package com.waris.graphQl_qbe.qbe.common;

import com.waris.graphQl_qbe.qbe.book.Book;
import com.waris.graphQl_qbe.qbe.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {
    private final BookRepository bookRepository;

    public DataLoader(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    @Override
    public void run(String... args) throws Exception {
        // Supprimer les anciens livres pour éviter les doublons
        bookRepository.deleteAll();

        List<Book> books = List.of(
                new Book(null, "1984", "George Orwell", 1949),
                new Book(null, "Brave New World", "Aldous Huxley", 1932),
                new Book(null, "Fahrenheit 451", "Ray Bradbury", 1953),
                new Book(null, "To Kill a Mockingbird", "Harper Lee", 1960),
                new Book(null, "The Catcher in the Rye", "J.D. Salinger", 1951)
        );
        bookRepository.saveAll(books);

        System.out.println("📚 Données initiales insérées avec succès ! good luck");
    }
}
