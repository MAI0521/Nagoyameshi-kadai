package com.example.nagoyameshi.form;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryRegisterForm {
    
    @NotNull(message = "登録したいカテゴリー名を入力してください。")
    private String name;
        
}