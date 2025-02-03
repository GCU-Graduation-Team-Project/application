package com.example.myapplication.util;

import android.util.Patterns;

public class UserInputValidator {

    /**
     * 로그인 입력값 검증
     * @param email 이메일
     * @param password 비밀번호
     * @return 오류 메시지 (오류가 없으면 null 반환)
     */

    public static String validateLoginInput(String email, String password) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "사용할 수 없는 이메일입니다";
        }
        if (password.isEmpty()) {
            return "비밀번호를 입력해주세요";
        }
        return null;  // 검증 통과
    }


    /**
     * 회원가입 입력값 검증
     * @param name 이름
     * @param email 이메일
     * @param password 비밀번호
     * @param confirmPassword 비밀번호 확인
     * @return 오류 메시지 (오류가 없으면 null 반환)
     */

    public static String validateRegistrationInput(String name, String email, String password, String confirmPassword) {
        if (name.isEmpty()) {
            return "이름이 비어있습니다";
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "사용할 수 없는 이메일입니다";
        }
        if (password.isEmpty()) {
            return "비밀번호를 입력해주세요";
        }
        if (!password.equals(confirmPassword)) {
            return "비밀번호가 일치하지 않습니다";
        }
        return null;  // 검증 통과
    }
}