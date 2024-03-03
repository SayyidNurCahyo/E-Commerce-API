package com.enigma.wmbapi.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewDetailRequest {
    private String menuId;
    private Float menuQuantity;
}
