package com.ss.companyintro.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JSP 뷰 해석 통합 테스트.
 *
 * <p>ASIS: JSP + Tiles 2.2.2 (javax.servlet.jsp 기반)<br>
 * TOBE: JSP (jakarta.servlet.jsp 기반, tomcat-embed-jasper)</p>
 *
 * <p>전환 이유: 기존 JSP 화면을 그대로 유지한다. Tiles는 Spring 6에서
 * 지원 제거되었으나 JSP 자체는 정상 사용 가능하다.
 * JSP 뷰 리졸버 설정(prefix=/WEB-INF/jsp/, suffix=.jsp)이
 * 올바르게 구성되었는지 확인한다.</p>
 *
 * <p>JSP 관련 클래스 로딩과 설정 존재 여부를 검증한다.</p>
 */
@SpringBootTest
class JspViewTest {

    @Test
    @DisplayName("[TOBE] JSP - tomcat-embed-jasper 클래스 로딩 확인")
    void jasperClassLoadable() throws ClassNotFoundException {
        // tomcat-embed-jasper가 ClassPath에 존재하는지 확인한다
        Class<?> jasperClass = Class.forName("org.apache.jasper.servlet.JspServlet");
        assertNotNull(jasperClass, "JspServlet 클래스가 로딩되어야 한다");
    }

    @Test
    @DisplayName("[TOBE] JSP - JSTL (jakarta.tags.core) 구현체 클래스 로딩 확인")
    void jstlClassLoadable() throws ClassNotFoundException {
        // JSTL 구현체(org.apache.taglibs.standard)가 ClassPath에 존재하는지 확인한다
        // c:out, c:forEach 등 JSTL 코어 태그를 처리하는 클래스이다
        Class<?> jstlClass = Class.forName("org.apache.taglibs.standard.tag.common.core.OutSupport");
        assertNotNull(jstlClass, "JSTL OutSupport 클래스가 로딩되어야 한다");
    }

    @Test
    @DisplayName("[TOBE] JSP - InternalResourceViewResolver 설정 확인")
    void jspViewResolverConfigured(@Autowired org.springframework.context.ApplicationContext ctx) {
        // Spring MVC의 InternalResourceViewResolver Bean이 등록되어 있는지 확인한다
        // application.yml의 spring.mvc.view.prefix/suffix 설정에 의해 자동 구성된다
        String[] resolverBeans = ctx.getBeanNamesForType(
                org.springframework.web.servlet.view.InternalResourceViewResolver.class);
        assertTrue(resolverBeans.length > 0,
                "InternalResourceViewResolver가 등록되어 있어야 한다");
    }
}
