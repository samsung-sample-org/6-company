package com.ss.companyintro.util;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.MimeType;
import jakarta.activation.MimeTypeParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Jakarta Activation API 테스트.
 *
 * <p>ASIS: activation 1.1 (javax.activation, JDK 8까지 포함)<br>
 * TOBE: jakarta.activation-api (jakarta.activation, Boot 관리)</p>
 *
 * <p>전환 이유: JDK 17에서 javax.activation 모듈 제거됨.
 * jakarta 네임스페이스로 전환 필수.</p>
 */
class ActivationTest {

    @Test
    @DisplayName("[TOBE] jakarta.activation-api - MimeType 파싱 확인")
    void mimeTypeParsing() throws MimeTypeParseException {
        MimeType mimeType = new MimeType("text/html");
        assertEquals("text", mimeType.getPrimaryType(), "primaryType이 text여야 한다");
        assertEquals("html", mimeType.getSubType(), "subType이 html이어야 한다");
    }

    @Test
    @DisplayName("[TOBE] jakarta.activation-api - DataHandler 생성 확인")
    void dataHandlerCreation() {
        DataHandler handler = new DataHandler("테스트 데이터", "text/plain");
        assertNotNull(handler, "DataHandler가 null이면 안 된다");
        assertEquals("text/plain", handler.getContentType(), "ContentType이 일치해야 한다");
    }
}
