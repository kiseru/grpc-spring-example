package ru.spring.grpc.data;

import ru.spring.grpc.Author;
import ru.spring.grpc.Book;
import ru.spring.grpc.Gender;

import java.util.List;

public class TempDB {

    public static List<Author> getAuthor() {
        return List.of(
                Author.newBuilder().setAuthorId(1).setFirstName("Tom").setLastName("Nikolson").setGender(Gender.male).setBookId(2).build(),
                Author.newBuilder().setAuthorId(2).setFirstName("Ann").setLastName("Stoylashova").setGender(Gender.female).setBookId(1).build(),
                Author.newBuilder().setAuthorId(3).setFirstName("Hugo").setLastName("Bolinson").setGender(Gender.male).setBookId(5).build(),
                Author.newBuilder().setAuthorId(4).setFirstName("Takoi").setLastName("Shabano").setGender(Gender.male).setBookId(4).build(),
                Author.newBuilder().setAuthorId(5).setFirstName("Takoi").setLastName("Shabano").setGender(Gender.male).setBookId(6).build(),
                Author.newBuilder().setAuthorId(6).setFirstName("Bob").setLastName("Alean").setGender(Gender.male).setBookId(7).build(),
                Author.newBuilder().setAuthorId(7).setFirstName("Kate").setLastName("Stokman").setGender(Gender.female).setBookId(8).build()
        );
    }

    public static List<Book> getBook() {
        return List.of(
                Book.newBuilder().setBookId(1).setTitle("Story1").setPrice(123.23f).setAuthorId(1).build(),
                Book.newBuilder().setBookId(2).setTitle("Story2").setPrice(234.45f).setAuthorId(1).build(),
                Book.newBuilder().setBookId(3).setTitle("Story3").setPrice(512.5f).setAuthorId(2).build(),
                Book.newBuilder().setBookId(4).setTitle("Story4").setPrice(23.67f).setAuthorId(5).build(),
                Book.newBuilder().setBookId(5).setTitle("Story5").setPrice(67.23f).setAuthorId(4).build(),
                Book.newBuilder().setBookId(6).setTitle("Story6").setPrice(789.90f).setAuthorId(6).build(),
                Book.newBuilder().setBookId(7).setTitle("Story7").setPrice(345.67f).setAuthorId(6).build(),
                Book.newBuilder().setBookId(8).setTitle("Story8").setPrice(45.67f).setAuthorId(7).build()
        );
    }
}
