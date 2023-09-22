package com.example.grpcserver.service;

import com.google.common.collect.Lists;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.spring.grpc.Author;
import ru.spring.grpc.AuthorId;
import ru.spring.grpc.Book;
import ru.spring.grpc.BookAuthorServiceGrpc;
import ru.spring.grpc.Empty;
import ru.spring.grpc.data.TempDB;

import java.util.List;

@GrpcService
public class BookAuthorService extends BookAuthorServiceGrpc.BookAuthorServiceImplBase {
    @Override
    public void getAuthor(AuthorId request, StreamObserver<Author> responseObserver) {
        TempDB.getAuthor().stream()
                .filter(author -> author.getAuthorId() == request.getAuthorId())
                .findFirst()
                .ifPresent((author) -> {
                    responseObserver.onNext(author);
                    responseObserver.onCompleted();
                });
    }

    @Override
    public void getBooksByAuthor(AuthorId request, StreamObserver<Book> responseObserver) {
        TempDB.getBook().stream()
                .filter(book -> book.getAuthorId() == request.getAuthorId())
                .forEach(responseObserver::onNext);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<Book> getMostExpansiveBook(StreamObserver<Book> responseObserver) {
        return new StreamObserver<Book>() {
            float bestPrice = 0;
            Book mostExpansiveBook = null;

            @Override
            public void onNext(Book book) {
                if (bestPrice < book.getPrice()) {
                    bestPrice = book.getPrice();
                    mostExpansiveBook = book;
                }
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(mostExpansiveBook);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void getAllBooks(Empty request, StreamObserver<Book> responseObserver) {
        TempDB.getBook().stream()
                .forEach(responseObserver::onNext);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<AuthorId> getBookByGender(StreamObserver<Book> responseObserver) {
        List<Book> books = Lists.newArrayList();
        return new StreamObserver<AuthorId>() {
            @Override
            public void onNext(AuthorId authorId) {
                TempDB.getBook().stream()
                        .filter(b -> b.getAuthorId() == authorId.getAuthorId())
                        .forEach(books::add);
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                books.forEach(responseObserver::onNext);
                responseObserver.onCompleted();
            }
        };
    }
}
