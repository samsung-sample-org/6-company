package com.ss.companyintro.commons;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Apache Commons Lang 2.x 테스트.
 *
 * <p>ASIS: commons-lang 2.1 (org.apache.commons.lang 패키지)<br>
 * TOBE: commons-lang 2.6 (기존 코드 호환 유지)</p>
 *
 * <p>전환 이유: 기존 코드에서 org.apache.commons.lang 패키지를 사용하는 경우
 * 버전업(2.6)으로 기존 import를 유지한다. commons-lang3과 병행 사용 가능.
 * 점진적으로 lang3(org.apache.commons.lang3)로 전환 권장.</p>
 */
class CommonsLang2Test {

    @Test
    @DisplayName("[TOBE] commons-lang 2.6 - StringUtils (lang 2.x 패키지) 동작 확인")
    void stringUtilsFromLang2() {
        // org.apache.commons.lang.StringUtils (2.x 패키지)가 동작하는지 확인
        assertTrue(StringUtils.isNotBlank("hello"), "hello는 blank가 아니어야 한다");
        assertTrue(StringUtils.isBlank(""), "빈 문자열은 blank여야 한다");
        assertTrue(StringUtils.isBlank(null), "null은 blank여야 한다");
        assertEquals("abc", StringUtils.trim("  abc  "), "양쪽 공백이 제거되어야 한다");
    }

    @Test
    @DisplayName("[TOBE] commons-lang 2.6 - NumberUtils (lang 2.x 패키지) 동작 확인")
    void numberUtilsFromLang2() {
        // org.apache.commons.lang.math.NumberUtils가 동작하는지 확인
        assertTrue(NumberUtils.isNumber("123"), "123은 숫자여야 한다");
        assertTrue(NumberUtils.isNumber("12.3"), "12.3은 숫자여야 한다");
        assertFalse(NumberUtils.isNumber("abc"), "abc는 숫자가 아니어야 한다");
    }
}
