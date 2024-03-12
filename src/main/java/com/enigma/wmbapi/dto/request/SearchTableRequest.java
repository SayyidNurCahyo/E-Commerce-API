package com.enigma.wmbapi.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchTableRequest {
    private Integer size;
    private Integer page;
    private String sortBy;
    private String direction;
    private String name;
}
