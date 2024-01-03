package com.umc.StudyFlexBE.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;


@Getter
@Setter
@NoArgsConstructor
public class SignUpOAuthDto {

    @JsonProperty
    @NotNull
    @Size(min = 3 , max = 100, message = "이메일은 3글자 이상 100글자 이하여야 합니다.")
    private String email;
    @JsonProperty
    @NotNull
    @Size(min =1, max = 50, message = "이름은 1글자 이상 50글자 이하여야 합니다.")
    private String name;

    @JsonProperty
    @Size(min =3 , max = 100, message = "학교 이름은 3글자 이상 100글자 이하여야 합니다.")
    private String school;
}
