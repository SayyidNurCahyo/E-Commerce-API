package com.enigma.wmbapi.dto.response;

import com.enigma.wmbapi.constant.TransTypeId;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransTypeResponse {
    @Enumerated(EnumType.STRING)
    private TransTypeId transTypeId;
    private String transTypeDescription;
}
