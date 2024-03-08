package com.enigma.wmbapi.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDetailResponse {
    private String detailId;
    private String menu;
    private Integer menuQuantity;
    private Long menuPrice;
}
