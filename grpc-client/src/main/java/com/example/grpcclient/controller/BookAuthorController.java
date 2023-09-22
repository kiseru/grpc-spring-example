package com.example.grpcclient.controller;

import com.example.grpcclient.dto.AuthorResponseDto;
import com.example.grpcclient.dto.BookResponseDto;
import com.example.grpcclient.mapper.AuthorMapper;
import com.example.grpcclient.mapper.BookMapper;
import com.example.grpcclient.service.BookAuthorClientService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces = "application/json")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookAuthorController {
    BookAuthorClientService bookAuthorClientService;
    AuthorMapper authorMapper;
    BookMapper bookMapper;

    @GetMapping("/author/{authorId}")
    public AuthorResponseDto getAuthorById(@PathVariable String authorId) {
        return authorMapper.toDto(bookAuthorClientService.getAuthorById(authorId));
    }

    @GetMapping("/books/{authorId}")
    public List<BookResponseDto> getBooksByAuthor(@PathVariable String authorId) throws InterruptedException {
        return bookAuthorClientService.getBooksByAuthor(authorId).stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/book/expansive")
    public BookResponseDto getBooksByAuthor() throws InterruptedException {
        return bookMapper.toDto(bookAuthorClientService.getMostExpansiveBook());
    }

    @GetMapping("/books")
    public List<BookResponseDto> getAllBooks() {
        return bookAuthorClientService.getAllBooks().stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/books/rest")
    public List<BookResponseDto> getAllBooksRest() {
        return bookAuthorClientService.getAllBooksRest().stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/books/by/{gender}")
    public List<BookResponseDto> getBooksByGender(@PathVariable String gender) throws InterruptedException {
        return bookAuthorClientService.getBooksByGender(gender).stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }
}
