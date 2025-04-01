package com.waris.graphQl_qbe.book;

import com.waris.graphQl_qbe.qbe.book.Book;
import com.waris.graphQl_qbe.qbe.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureHttpGraphQlTester
@TestPropertySource(locations = "classpath:application.properties")  // Assure qu'on utilise bien H2

public class BookGraphQlTest {

    @Autowired
    private HttpGraphQlTester graphQlTester;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll(); // Nettoyer la base avant chaque test
        bookRepository.saveAll(List.of(
                new Book(null, "The Alchemist", "Paulo Coelho", 1988),
                new Book(null, "Sapiens: A Brief History of Humankind", "Yuval Noah Harari", 2011),
                new Book(null, "The Silent Patient", "Alex Michaelides", 2019),
                new Book(null, "Where the Crawdads Sing", "Delia Owens", 2018),
                new Book(null, "Atomic Habits", "James Clear", 2018),
                new Book(null, "The Midnight Library", "Matt Haig", 2020),
                new Book(null, "Project Hail Mary", "Andy Weir", 2021),
                new Book(null, "The Song of Achilles", "Madeline Miller", 2011),
                new Book(null, "Circe", "Madeline Miller", 2018),
                new Book(null, "It Ends with Us", "Colleen Hoover", 2016)
        ));
    }

    @Test
    void testFindAll() {
        String query = """
            query {
                books {
                    id
                    title
                    author
                    publishedYear
                }
            }
        """;

        graphQlTester.document(query)
                .execute()
                .path("data.books")
                .entityList(Book.class)
                .hasSize(10); // Vérifie que tous les livres sont bien insérés
    }

    @Test
    void testFindById() {
        Book savedBook = bookRepository.findAll().get(0);

        String query = """
            query($id: ID!) {
                book(id: $id) {
                    id
                    title
                    author
                    publishedYear
                }
            }
        """;

        graphQlTester.document(query)
                .variable("id", savedBook.getId().toString())
                .execute()
                .path("data.book")
                .entity(Book.class)
                .satisfies(book -> {
                    assert book.getId().equals(savedBook.getId());
                    assert book.getTitle().equals(savedBook.getTitle());
                });
    }

    @Test
    void testFindByExample_ExactTitle() {
        String document = """
            query($bookInput: BookInput!) {
                books(book: $bookInput) {
                    id
                    title
                    author
                    publishedYear
                }
            }
        """;

        graphQlTester.document(document)
                .variable("bookInput", Map.of("title", "The Alchemist"))
                .execute()
                .path("data.books")
                .entityList(Book.class)
                .hasSize(1)
                .satisfies(books -> {
                    assert books.get(0).getTitle().equals("The Alchemist");
                });
    }

    @Test
    void testFindByExample_PublishedYearAndAuthorPattern() {
        String document = """
            query($bookInput: BookInput!) {
                books(book: $bookInput) {
                    id
                    title
                    author
                    publishedYear
                }
            }
        """;

        graphQlTester.document(document)
                .variable("bookInput", Map.of("publishedYear", 2011, "author", "Yuval Noah Harari"))
                .execute()
                .path("data.books")
                .entityList(Book.class)
                .hasSize(1)
                .satisfies(books -> {
                    assert books.get(0).getPublishedYear().equals(2011);
                    assert books.get(0).getAuthor().contains("Yuval Noah Harari");
                    assert books.get(0).getTitle().equals("Sapiens: A Brief History of Humankind");
                });
    }

    @Test
    void testFindByExample_NoMatch() {
        String document = """
            query($bookInput: BookInput!) {
                books(book: $bookInput) {
                    id
                    title
                    author
                    publishedYear
                }
            }
        """;

        graphQlTester.document(document)
                .variable("bookInput", Map.of("title", "Non Existent Book"))
                .execute()
                .path("data.books")
                .entityList(Book.class)
                .hasSize(0);
    }
}
