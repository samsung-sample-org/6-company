package com.ss.companyintro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 회사 소개 애플리케이션 진입점.
 *
 * <p>본 프로젝트는 JDK 17 + Spring Boot 3 환경에서
 * 기존(ASIS) 라이브러리들의 TOBE 전환 호환성을 검증하기 위한 샘플 애플리케이션이다.</p>
 *
 * <p>검증 대상 라이브러리:</p>
 * <ul>
 *   <li>Hibernate (javax.persistence → jakarta.persistence)</li>
 *   <li>Ehcache (2.x → 3.10.x, JCache 기반)</li>
 *   <li>Apache HttpClient (4.x → 5.x)</li>
 *   <li>Quartz Scheduler (1.6.x → 2.3.x, spring-boot-starter-quartz)</li>
 *   <li>JSP (기존 뷰 기술 유지)</li>
 * </ul>
 *
 * @author SS Sample
 */
@SpringBootApplication
public class CompanyIntroApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompanyIntroApplication.class, args);
    }
}
