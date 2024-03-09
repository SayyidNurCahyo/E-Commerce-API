package com.enigma.wmbapi.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor@NoArgsConstructor
@Builder
public class MenuResponse {
    private String menuId;
    private String menuName;
    private Long menuPrice;
    private List<ImageResponse> imageResponses;
}
