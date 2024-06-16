package com.jejeong.authplatform.dto.sign;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class SignUpRequest {

  public SignUpRequest() {
  }


  @Email(message = "이메일 형식에 맞춰주세요")
  @NotBlank(message = "이메일을 입력하세요.")
  private String email;

  @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
      message = "비밀번호는 최소 8자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.")
  @NotBlank(message = "비밀번호를 입력하세요")
  private String password;

  @Size(min = 2)
  @NotBlank(message = "사용자 이름을 입력하세요.")
  @Pattern(regexp = "^[A-Za-z가-힣]+$", message = "사용자 이름은 한글 또는 알파벳만 입력해주세요.")
  private String username;

  @Size(min = 2)
  @NotBlank(message = "닉네임을 입력하세요.")
  @Pattern(regexp = "^[A-Za-z가-힣]+$", message = "닉네임은 한글 또는 알파벳만 입력해주세요.")
  private String nickname;

}
