package com.pragma.personmonolith.mapper;

import com.pragma.personmonolith.dto.ImageDto;
import com.pragma.personmonolith.model.Image;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {PersonMapper.class})
public interface ImageMapper extends EntityMapper<ImageDto, Image>{
}
