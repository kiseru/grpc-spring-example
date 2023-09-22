package com.example.grpcclient.service;

import com.google.common.collect.Lists;
import io.grpc.stub.StreamObserver;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.spring.grpc.Author;
import ru.spring.grpc.AuthorId;
import ru.spring.grpc.Book;
import ru.spring.grpc.BookAuthorServiceGrpc;
import ru.spring.grpc.Empty;
import ru.spring.grpc.Gender;
import ru.spring.grpc.data.TempDB;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookAuthorClientService {

    @GrpcClient("my-grpc-blocking-client")
    BookAuthorServiceGrpc.BookAuthorServiceBlockingStub syncStub;

    @GrpcClient("my-grpc-async-client")
    BookAuthorServiceGrpc.BookAuthorServiceStub asyncStub;

    public Author getAuthorById(String authorId) {
        if (!StringUtils.hasText(authorId)) {
           throw new IllegalArgumentException("Error with authorId, authorId is empty or null");
        }

        // формируем сообщение
        AuthorId authorIdRequest = AuthorId.newBuilder().setAuthorId(Integer.parseInt(authorId)).build();
        // выполняем сам запрос и получаем ответ
        Author authorResponse = syncStub.getAuthor(authorIdRequest);

        return authorResponse;
//        return authorResponse.getAllFields();
    }

    public List<Book> getBooksByAuthor(String authorId) throws InterruptedException {
        if (!StringUtils.hasText(authorId)) {
            throw new IllegalArgumentException("Error with authorId, authorId is empty or null");
        }

        // формируем сообщение
        AuthorId authorIdRequest = AuthorId.newBuilder().setAuthorId(Integer.parseInt(authorId)).build();

        final List<Book> books = Lists.newArrayList();
        CountDownLatch countDownLatch = new CountDownLatch(1);

        // выполняем сам запрос и получаем ответ
        asyncStub.getBooksByAuthor(authorIdRequest, new StreamObserver<Book>() {
            @Override
            public void onNext(Book book) {
                books.add(book);
            }

            @Override
            public void onError(Throwable t) {
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                countDownLatch.countDown();
            }
        });

        boolean await = countDownLatch.await(1, TimeUnit.MINUTES);

        return await ? books : Collections.EMPTY_LIST;
    }

    public Book getMostExpansiveBook() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        var ref = new Object() {
            Book resultBook = null;
        };

        // выполняем сам запрос и получаем ответ
        StreamObserver<Book> mostExpansiveBookObserver = asyncStub.getMostExpansiveBook(new StreamObserver<Book>() {
            @Override
            public void onNext(Book book) {
                ref.resultBook = book;
            }

            @Override
            public void onError(Throwable t) {
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                countDownLatch.countDown();
            }
        });

        TempDB.getBook().stream()
                .forEach(mostExpansiveBookObserver::onNext);
        mostExpansiveBookObserver.onCompleted();

        boolean await = countDownLatch.await(1, TimeUnit.MINUTES);

        return await ? ref.resultBook : null;
    }

    public List<Book> getAllBooks() {
        // формируем сообщение
        Empty emptyRequest = Empty.newBuilder().build();

        final Iterator<Book> allBooks = syncStub.getAllBooks(emptyRequest);

        return Lists.newArrayList(allBooks);
    }

    public List<Book> getAllBooksRest() {
        return TempDB.getBook();
    }

    public List<Book> getBooksByGender(String gender) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        final List<Book> books = Lists.newArrayList();

        StreamObserver<AuthorId> bookByGenderObserver = asyncStub.getBookByGender(new StreamObserver<Book>() {
            @Override
            public void onNext(Book book) {
                books.add(book);
            }

            @Override
            public void onError(Throwable t) {
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                countDownLatch.countDown();
            }
        });

        TempDB.getAuthor().stream()
                .filter(author -> author.getGender().equals(Gender.valueOf(gender)))
                .forEach(author -> bookByGenderObserver.onNext(AuthorId.newBuilder().setAuthorId(author.getAuthorId()).build()));
        bookByGenderObserver.onCompleted();

        boolean await = countDownLatch.await(1, TimeUnit.MINUTES);

        return await ? books : Collections.EMPTY_LIST;
    }
}
