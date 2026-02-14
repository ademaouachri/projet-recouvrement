package com.example.backend.Mapper;

import com.example.backend.DTO.PalierDto;
import com.example.backend.Model.Palier;

import java.util.List;
import java.util.stream.Collectors;

public class GeneralMapper {

    public PalierDto toDto(Palier palier) {
        return PalierDto.builder()
                .id(palier.getId())
                .code(palier.getCode())
                .label(palier.getLabel())
                .build();
    }

    public List<PalierDto> toDtoList(List<Palier> paliers) {
        return paliers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
