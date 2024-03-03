package com.enigma.wmbapi.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableResponse {
    private String tableId;
    private String tableName;
}
