package com.enigma.wmbapi.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor@NoArgsConstructor
@Builder
public class MenuResponse {
    private String menuId;
    private String menuName;
    private Long menuPrice;
}
