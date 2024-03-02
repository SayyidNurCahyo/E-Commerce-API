package com.enigma.wmbapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateTableRequest {
    @NotBlank(message = "Id is Required")
    private String id;
    @NotBlank(message = "Name is Required")
    private String name;
}
