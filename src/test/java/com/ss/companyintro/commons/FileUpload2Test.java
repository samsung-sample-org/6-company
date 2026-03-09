package com.ss.companyintro.commons;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

/**
 * Apache Commons FileUpload2 테스트.
 *
 * <p>ASIS: commons-fileupload 1.2 / 1.3.3 (javax.servlet 기반)<br>
 * TOBE: commons-fileupload2-jakarta-servlet6 2.0.0-M2 (jakarta.servlet 기반)</p>
 *
 * <p>전환 이유: Servlet 6(Jakarta) 호환 필요. 2.0.0-M2는 Milestone 릴리즈이므로
 * 안정 버전 출시까지 Spring Boot 내장 StandardServletMultipartResolver 사용을 권장한다.</p>
 *
 * <p>NOTE: commons-fileupload2-jakarta-servlet6 아티팩트가 Maven Central에
 * 안정적으로 배포될 때 테스트를 활성화한다.</p>
 */
class FileUpload2Test {

    @Test
    @Disabled("commons-fileupload2-jakarta-servlet6 2.0.0-M2 Milestone 릴리즈. " +
              "안정 버전 출시 시 활성화. Spring Boot MultipartFile로 대체 가능.")
    @DisplayName("[TOBE] commons-fileupload2 - Jakarta Servlet 6 호환 확인 (Milestone)")
    void fileUpload2JakartaCompatible() {
        // 안정 버전 출시 후 활성화:
        // DiskFileItemFactory factory = DiskFileItemFactory.builder().get();
        // assertNotNull(factory);
    }
}
