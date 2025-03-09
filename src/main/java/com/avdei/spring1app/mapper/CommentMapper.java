package com.avdei.spring1app.mapper;

import com.avdei.spring1app.dto.CommentDTO;
import com.avdei.spring1app.model.Comment;
import org.mapstruct.*;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class CommentMapper {
    public abstract void update(CommentDTO commentDTO, @MappingTarget Comment comment);
    public abstract Comment map(CommentDTO commentDTO);
    public abstract CommentDTO map(Comment comment);
}
