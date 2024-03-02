package com.enigma.wmbapi.dto.request;

import com.enigma.wmbapi.constant.TransTypeId;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetTransTypeRequest {
    @NotBlank(message = "Id is Required")
    private TransTypeId id;
    @NotBlank(message = "Description is Required")
    private String description;
}
