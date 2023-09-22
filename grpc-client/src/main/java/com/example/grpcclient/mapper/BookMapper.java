package com.example.grpcclient.mapper;

import com.example.grpcclient.dto.BookResponseDto;
import org.mapstruct.Mapper;
import ru.spring.grpc.Book;

@Mapper(componentModel = "spring")
public interface BookMapper {

    public BookResponseDto toDto(Book model);
}
