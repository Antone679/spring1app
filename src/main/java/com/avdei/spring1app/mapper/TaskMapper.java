package com.avdei.spring1app.mapper;

import com.avdei.spring1app.dto.TaskCreateDTO;
import com.avdei.spring1app.dto.TaskDTO;
import com.avdei.spring1app.dto.TaskUpdateDTO;
import com.avdei.spring1app.model.Task;
import org.mapstruct.*;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class TaskMapper {

    public abstract void update(TaskUpdateDTO updateDTO, @MappingTarget Task task);
    public abstract Task map(TaskCreateDTO createDTO);
    public abstract TaskDTO map(Task task);
    public abstract TaskUpdateDTO mapToUpdateDTO(TaskDTO taskDTO);
}
