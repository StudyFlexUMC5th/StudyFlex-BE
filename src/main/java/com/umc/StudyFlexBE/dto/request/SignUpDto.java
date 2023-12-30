package com.umc.StudyFlexBE.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@NoArgsConstructor
public class SignUpDto {

    @JsonProperty
    @NotNull
    @Size(min = 3 , max = 100, message = "이메일은 3글자 이상 100글자 이하여야 합니다.")
    private String email;


    @JsonProperty
    @NotNull
    @Size(min = 3, max = 100, message = "비밀번호는 3글자 이상 100글자 이하여야 합니다.")
    private String password;






}
