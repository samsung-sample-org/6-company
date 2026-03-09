package com.ss.companyintro.commons;

import org.apache.commons.validator.GenericValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Apache Commons Validator 테스트.
 *
 * <p>ASIS: commons-validator 1.1.3<br>
 * TOBE: commons-validator 1.9.0</p>
 *
 * <p>전환 이유: 이메일/URL/IP 검증 로직 보안 강화, 정규식 ReDoS 방어.
 * GenericValidator의 이메일 및 URL 검증이 정상 동작하는지 확인한다.</p>
 */
class ValidatorTest {

    @Test
    @DisplayName("[TOBE] commons-validator 1.9.0 - GenericValidator.isEmail 동작 확인")
    void isEmail() {
        // 유효한 이메일 주소
        assertTrue(GenericValidator.isEmail("user@example.com"),
                "올바른 이메일 형식이어야 한다");

        // 유효하지 않은 이메일 주소
        assertFalse(GenericValidator.isEmail("not-an-email"),
                "잘못된 이메일 형식은 false여야 한다");
        assertFalse(GenericValidator.isEmail(""),
                "빈 문자열은 유효한 이메일이 아니어야 한다");
    }

    @Test
    @DisplayName("[TOBE] commons-validator 1.9.0 - GenericValidator.isUrl 동작 확인")
    void isUrl() {
        // 유효한 URL
        assertTrue(GenericValidator.isUrl("https://www.example.com"),
                "올바른 URL 형식이어야 한다");
        assertTrue(GenericValidator.isUrl("http://www.example.com:8080/path"),
                "포트가 포함된 URL도 유효해야 한다");

        // 유효하지 않은 URL
        assertFalse(GenericValidator.isUrl("not-a-url"),
                "잘못된 URL 형식은 false여야 한다");
    }
}
