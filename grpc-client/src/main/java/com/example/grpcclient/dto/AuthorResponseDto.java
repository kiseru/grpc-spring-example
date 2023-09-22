package com.example.grpcclient.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthorResponseDto {
    int authorId;
    String firstName;
    String lastName;
    String gender;
    int bookId;
}
