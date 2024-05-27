package com.example.vedioservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoFileDTO {

    private Long id;

    private String name;

    private URI uri;


}
