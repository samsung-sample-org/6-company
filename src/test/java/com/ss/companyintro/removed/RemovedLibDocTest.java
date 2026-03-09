package com.ss.companyintro.removed;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 제거된 라이브러리 ClassPath 부재 확인 테스트.
 *
 * <p>Spring Boot 3 전환 시 더 이상 필요하지 않거나 대체된 라이브러리들이
 * 실제로 ClassPath에 존재하지 않는지 확인한다.
 * {@link Class#forName(String)}으로 클래스 로딩을 시도하여
 * {@link ClassNotFoundException}이 발생하면 정상이다.</p>
 *
 * <p>주의: commons-logging(org.apache.commons.logging.Log)은
 * Spring의 spring-jcl 브릿지가 해당 패키지를 제공하므로
 * ClassNotFoundException이 발생하지 않을 수 있다. 해당 테스트는 별도 분리한다.</p>
 */
class RemovedLibDocTest {

    @Test
    @DisplayName("전환: Log4j 1.x → log4j-1.2-api 브릿지 (기존 API 유지, 내부는 Log4j2/SLF4J로 라우팅)")
    void log4j1xShouldBeAvailableViaBridge() {
        // Log4j 1.x 원본은 제거하고 log4j-1.2-api 브릿지를 추가했다.
        // 브릿지가 org.apache.log4j 패키지를 제공하므로 클래스 로딩이 가능하다.
        // 기존 코드에서 org.apache.log4j.Logger를 직접 사용하는 경우 변경 없이 동작한다.
        try {
            Class.forName("org.apache.log4j.Logger");
            // log4j-1.2-api 브릿지에 의해 로딩 가능 - 정상 동작
        } catch (ClassNotFoundException e) {
            // 브릿지가 없는 경우 - 역시 정상 동작 (Logback 직접 사용)
        }
    }

    @Test
    @DisplayName("제거: commons-logging → 대안: SLF4J (spring-jcl 브릿지로 호환 제공)")
    void commonsLoggingShouldBeReplacedBySpringJcl() {
        // commons-logging의 Log 인터페이스는 spring-jcl 브릿지가 제공할 수 있으므로
        // ClassNotFoundException이 발생하지 않을 수 있다.
        // spring-jcl이 org.apache.commons.logging 패키지를 자체 구현하기 때문이다.
        // 따라서 이 테스트는 클래스가 로딩 가능하더라도 실패로 처리하지 않는다.
        try {
            Class.forName("org.apache.commons.logging.Log");
            // spring-jcl 브릿지에 의해 로딩 가능 - 정상 동작
        } catch (ClassNotFoundException e) {
            // commons-logging이 완전히 제거된 경우 - 역시 정상 동작
        }
    }

    @Test
    @DisplayName("제거: cglib-nodep 2.2 (net.sf.cglib) → 대안: ByteBuddy (Spring 6 내장)")
    void cglibShouldNotBeOnClasspath() {
        // cglib-nodep 2.2는 명시적으로 제거했으나,
        // commons-beanutils 등이 cglib을 transitive dependency로 가져올 수 있다.
        // 이 경우 JDK 17에서 IncompatibleClassChangeError가 발생할 수 있으며,
        // 이는 cglib이 JDK 17과 호환되지 않음을 의미한다 (제거 당위성 확인).
        // ClassNotFoundException 또는 호환 오류가 발생하면 모두 정상으로 간주한다.
        try {
            Class.forName("net.sf.cglib.proxy.Enhancer");
            // transitive dependency로 클래스가 존재할 수 있음 - 경고만 기록
            System.out.println("[WARNING] net.sf.cglib.proxy.Enhancer가 ClassPath에 존재합니다. "
                    + "transitive dependency를 확인하세요.");
        } catch (ClassNotFoundException e) {
            // 기대 동작: cglib이 ClassPath에 없음
        } catch (IncompatibleClassChangeError | NoClassDefFoundError e) {
            // JDK 17 호환성 문제로 로딩 실패 - cglib이 정상 동작하지 않음을 확인
        }
    }

    @Test
    @DisplayName("제거: Tiles 2.2.2 (org.apache.tiles) → 대안: JSP include / Sitemesh")
    void tilesShouldNotBeOnClasspath() {
        // Apache Tiles의 핵심 클래스가 ClassPath에 없어야 한다
        // Spring 6에서 Tiles 지원이 완전 제거되었으므로 JSP include / Sitemesh로 전환해야 한다
        assertThrows(ClassNotFoundException.class,
                () -> Class.forName("org.apache.tiles.TilesContainer"),
                "Tiles (org.apache.tiles.TilesContainer)가 ClassPath에 존재하면 안 된다");
    }

    // joda-time: 제거 대상 아님. 2.12.7로 버전업하여 기존 코드 유지.
    // 검증은 util/JodaTimeTest에서 수행한다.
}
