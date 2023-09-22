package com.example.grpcclient.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookResponseDto {
    int bookId;
    String title;
    float price;
    int authorId;
}
