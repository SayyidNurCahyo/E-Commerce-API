package com.enigma.wmbapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^T\\d{2}$",message = "Table Format Incorrect, Should Be T**")
    private String name;
}
