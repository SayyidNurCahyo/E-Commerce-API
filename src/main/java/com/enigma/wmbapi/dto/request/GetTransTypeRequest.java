package com.enigma.wmbapi.dto.request;

import com.enigma.wmbapi.constant.TransTypeId;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetTransTypeRequest {
    @NotBlank(message = "Id is Required")
    @Enumerated(EnumType.STRING)
    private TransTypeId id;
//    private List<TransTypeId> id;
    @NotBlank(message = "Description is Required")
    private String description;
}
