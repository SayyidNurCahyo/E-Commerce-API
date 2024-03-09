package com.enigma.wmbapi.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewMenuRequest {
    @NotBlank(message = "Name is Required")
    private String name;
    @NotNull(message = "Price is Required")
    @Min(value = 0, message = "Price Must Be Greater Than Or Equal 0")
    private Long price;
    @NotNull(message = "Menu Image is Required")
    private List<MultipartFile> images;
}
