package com.fakecompany.personmonolith.mapper;

import com.fakecompany.personmonolith.dto.ImageDto;
import com.fakecompany.personmonolith.model.Image;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {PersonMapper.class})
public interface ImageMapper extends EntityMapper<ImageDto, Image>{
}
