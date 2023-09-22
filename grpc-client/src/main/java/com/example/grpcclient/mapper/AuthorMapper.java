package com.example.grpcclient.mapper;

import com.example.grpcclient.dto.AuthorResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.spring.grpc.Author;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    @Mapping(target = "gender", expression = "java(model.getGender().name())")
    AuthorResponseDto toDto(Author model);
}
