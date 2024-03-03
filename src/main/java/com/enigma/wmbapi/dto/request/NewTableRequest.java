package com.enigma.wmbapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewTableRequest {
    @NotBlank(message = "Name is Required")
    @Pattern(regexp = "^T\\d{2}$",message = "Table Format Incorrect")
    private String name;
}
