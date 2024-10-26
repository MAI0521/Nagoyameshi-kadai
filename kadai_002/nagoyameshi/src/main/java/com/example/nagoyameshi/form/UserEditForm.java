package com.example.nagoyameshi.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserEditForm {
    @NotNull
    private Integer id;
    
    @NotBlank(message = "氏名を入力してください。")
    private String fullName;
    
    @NotBlank(message = "フリガナを入力してください。")
    private String kana;
    
    @NotBlank(message = "メールアドレスを入力してください。")
    private String email;
}
