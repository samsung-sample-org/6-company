# 6. 회사소개 시스템 - JDK 17 마이그레이션 라이브러리 검증

## 목차

1. [개요](#1-개요)
2. [기술 스택](#2-기술-스택)
3. [라이브러리 전환 현황 (ASIS → TOBE)](#3-라이브러리-전환-현황-asis--tobe)
   - [전환 원칙](#전환-원칙)
   - [전환 요약](#전환-요약)
   - [전체 라이브러리 매트릭스](#전체-라이브러리-매트릭스)
4. [솔루션/자체 라이브러리](#4-솔루션자체-라이브러리)
5. [주요 결정 사항과 근거](#5-주요-결정-사항과-근거)
6. [프로젝트 구조](#6-프로젝트-구조)
7. [실행 방법](#7-실행-방법)
8. [테스트 실행](#8-테스트-실행)
9. [알려진 제약사항](#9-알려진-제약사항)

---

## 1. 개요

기존 회사소개 시스템(Spring Framework 3.2.2, JDK 8)을 JDK 17 + Spring Boot 3.5.11로 전환하기 위한 **라이브러리 호환성 검증 프로젝트**이다.

원본 시스템에서 사용 중인 오픈소스 라이브러리를 분석하여, Spring Boot 3 환경에서의 호환 여부를 Docker 기반으로 실증 검증한다. 각 라이브러리에 대해 단위 테스트를 작성하고, JDK 17 + Jakarta EE 10 환경에서 정상 동작함을 확인하는 것이 목적이다.

---

## 2. 기술 스택

| 항목 | 선택 | 선택 이유 |
|------|------|-----------|
| JDK | 17 (Adoptium Temurin) | Spring Boot 3의 최소 요구사항. LTS 버전으로 장기 지원 보장 |
| Framework | Spring Boot 3.5.11 | 3.2.x 최신 안정 버전. Jakarta EE 10 기반, Spring Framework 6.1 내장 |
| 빌드 도구 | Maven | 기존 시스템과 동일한 빌드 도구를 유지하여 전환 비용 최소화 |
| View | **JSP** (기존 유지) | 기존 JSP 기반 화면을 최대한 유지. tomcat-embed-jasper + JSTL 추가 |
| 컨테이너 OS | Docker (CentOS 7) | JDK 6/7/8/17 모두 지원 가능. 레거시 환경 재현에 적합 |
| DB (Docker) | Oracle XE 21c | 원본 시스템 Oracle 사용. gvenzl/oracle-xe 경량 이미지 활용 |
| DB (로컬) | H2 인메모리 | Oracle 없이 로컬에서 빠른 테스트 가능 |

---

## 3. 라이브러리 전환 현황 (ASIS → TOBE)

### 전환 원칙

1. **기존 기술 최대 유지**: 있던 라이브러리를 그대로 가져간다. 마이그레이션 공수를 최소화한다.
2. **버전업 우선**: 동일 라이브러리의 최신 버전으로 업그레이드하는 것을 1순위로 한다.
3. **교체는 불가피한 경우만**: 버전업만으로 대응할 수 없을 때(프로젝트 폐기, Jakarta 미호환 등)에만 대체 라이브러리를 고려한다.
4. **JSP 유지**: 기존 JSP 기반 화면을 유지한다. Tiles는 Spring 6에서 지원이 완전 제거되어 교체가 불가피하다.

### 전환 요약

| 전환 방식 | 건수 | 설명 |
|----------|------|------|
| Boot 내장 | 15건 | Spring Boot Starter/BOM에 포함. 별도 의존성 관리 불필요 |
| 버전업 | 23건 | 동일 계열 라이브러리 최신 버전으로 업그레이드 |
| 교체 | 4건 | 버전업 불가, 대체 라이브러리로 전환 (불가피) |
| 제거 | 6건 | 프로젝트 폐기/완전 EOL로 버전업 자체 불가 |
| 신규 | 1건 | 기존 EOL 라이브러리의 현대적 대안으로 추가 |
| **합계** | **49건** | 솔루션/자체 라이브러리 10건 별도 |

### 전체 라이브러리 매트릭스

> **범례** — Boot 내장: Spring Boot Starter가 관리 | 버전업: 동일 계열 최신 버전 | 교체: 대체 라이브러리 전환(불가피) | 제거: EOL/폐기/불필요 | 신규: 새로 추가

#### Framework / Core

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 1 | Spring Framework 3.2.2 (spring-aop, beans, context, core, webmvc) | Spring 6.1+ | Boot 내장 | starter-web. javax→jakarta 전환 |
| 2 | Hibernate 3.1 | Hibernate 6.x | Boot 내장 | starter-data-jpa. javax.persistence→jakarta.persistence |
| 3 | Hibernate Validator 4.1 | Validator 8.x | Boot 내장 | starter-validation. javax.validation→jakarta.validation |
| 4 | aopalliance 1.0 | Spring AOP 내장 | Boot 내장 | starter-aop에 포함. 별도 추가 시 충돌 |
| 5 | AspectJ 1.6.8 (rt/tools/weaver) | AspectJ 1.9.21+ | Boot 내장 | starter-aop. JDK 17 호환 버전으로 자동 관리 |
| 6 | cglib-nodep 2.2 | ByteBuddy | Boot 내장 | Spring 6이 프록시 생성기로 ByteBuddy 사용 |
| 7 | asm 2.2.2 (+commons, util) | ASM 9.x | Boot 내장 | Spring 6 내장. 직접 선언 시 버전 충돌 위험 |
| 8 | antlr 2.7.5 | ANTLR 4 | Boot 내장 | Hibernate 6 내장. 직접 사용 시 antlr4-runtime 전환 |

#### JSON

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 9 | Jackson 1.9.7 (core-asl, mapper-asl) | Jackson 2.17+ | Boot 내장 | starter-web. org.codehaus→com.fasterxml 패키지 변경 |

#### Logging

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 10 | SLF4J 1.6.4 | SLF4J 2.0+ | Boot 내장 | starter-logging. fluent API 추가 |
| 11 | commons-logging 1.0.4 | spring-jcl 브릿지 | Boot 내장 | Spring Boot에 브릿지 내장. 직접 의존 불필요 |
| 12 | Log4j 1.2.16 | **log4j-1.2-api** (Log4j2 브릿지) | 교체 | Log4j 1.x EOL(CVE-2019-17571). 1.x API를 유지하면서 Logback으로 라우팅 |
| 13 | slf4j-log4j12 1.6.4 | (제거) | 제거 | SLF4J 2.x에서 바인딩 구조가 ServiceProvider 방식으로 변경되어 1.x용 slf4j-log4j12 호환 불가. Boot 기본 Logback 바인딩과 충돌. Log4j 1.x 자체도 CVE-2019-17571 보안 취약점 존재. 기존 org.apache.log4j API는 log4j-1.2-api 브릿지로 유지 |

#### Testing

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 14 | JUnit 4.9 | JUnit 5 (Jupiter) | Boot 내장 | starter-test. @RunWith→@ExtendWith 전환 |
| 15 | cactus 1.7.2 | (제거) | 제거 | Apache Cactus 프로젝트 완전 폐기. Spring Boot Test로 대체 |

#### View / Template

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 16 | **JSP** | **JSP (유지)** | 버전업 | tomcat-embed-jasper + JSTL 추가. **기존 JSP 화면 그대로 사용** |
| 17 | Tiles 2.2.2 | JSP include / Sitemesh | 교체 | **Spring 6에서 Tiles 지원 완전 제거**. 버전업 불가 |
| 18 | Velocity 1.7 | velocity-engine-core 2.4.1 | 버전업 | artifactId 변경. Boot 3에 auto-config 없음, 수동 Bean 필요 |
| 19 | displaytag 1.0 | (제거) | 제거 | JSP 태그 라이브러리 프로젝트 폐기. JSTL로 대체 |
| 20 | Thymeleaf | (제거) | 제거 | 기존 시스템에 없던 신규 라이브러리. JSP 유지 원칙에 따라 불필요 |

#### DB / Connection Pool

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 21 | ojdbc14 | ojdbc11 | 버전업 | JDK 17 호환 드라이버. Maven Central 공식 배포 |
| 22 | c3p0 0.9.1 | c3p0 0.9.5.5 | 버전업 | JDK 17 호환. Boot 기본 HikariCP와 병행 검증 |
| 23 | commons-dbcp 1.2.2 | commons-dbcp2 | 버전업 | artifactId 변경(dbcp→dbcp2). Boot BOM 관리 |

#### Cache

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 24 | ehcache 1.2.3 / 2.5.0 | ehcache 3.10.8 | 버전업 | **classifier=jakarta 필수**. JCache(JSR-107) 표준 |

#### Scheduler

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 25 | quartz 1.6.1 | Quartz 2.3.2+ | 버전업 | starter-quartz. Boot 자동 구성으로 설정 간소화 |

#### HTTP / SOAP

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 26 | httpclient 4.3 | httpclient5 | 버전업 | groupId 완전 변경. org.apache.http→org.apache.hc 패키지 |
| 27 | axis 1.4 | (교체 필요) | 교체 | Jakarta 미호환. **Apache CXF 또는 REST 전환 권장** |

#### Security

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 28 | antisamy 1.4.4 | antisamy 1.7.6 | 버전업 | XSS 필터 CVE 다수 패치. groupId 변경(org.owasp.antisamy) |

#### Office

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 29 | poi 3.0.1 | poi 5.3.0 + poi-ooxml | 버전업 | .xlsx OOXML 지원 추가. XXE 취약점 패치 |

#### XML

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 30 | dom4j 1.6.1 | dom4j 2.1.4 | 버전업 | groupId 변경(dom4j→org.dom4j). XXE 취약점 패치 |
| 31 | xalan 2.7.0 | xalan 2.7.3 + serializer | 버전업 | XSLT 처리 보안 패치. serializer 모듈 별도 추가 필요 |
| 32 | xercesImpl 2.11.0 | xercesImpl 2.12.2 | 버전업 | XML 파싱 보안 패치 |

#### SVG

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 33 | batik 1.7 (css, util) | batik-all 1.17 | 버전업 | 통합 모듈. SVG 렌더링 SSRF 방어 강화 |

#### Apache Commons

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 34 | commons-beanutils 1.8.0 | commons-beanutils 1.9.4 | 버전업 | CVE-2014-0114 패치 |
| 35 | commons-codec 1.3 | commons-codec 1.16+ | Boot 내장 | Boot BOM 버전 자동 관리 |
| 36 | commons-collections 3.2 | commons-collections4 4.5.0 | 버전업 | **RCE 취약점(CVE-2015-6420) 패치 필수**. artifactId 변경 |
| 37 | commons-digester 1.7 | commons-digester3 3.2 | 버전업 | groupId/artifactId 변경 |
| 38 | commons-fileupload 1.2/1.3 | commons-fileupload2-jakarta 2.0.0-M2 | 버전업 | Jakarta Servlet 6 호환. API 변경 있음 |
| 39 | commons-io 1.4 | commons-io 2.16.1 | 버전업 | JDK 8+ NIO 통합 지원 |
| 40 | commons-lang 2.1 | commons-lang 2.6 | 버전업 | **기존 코드 호환 유지** (org.apache.commons.lang 패키지 유지) |
| 41 | commons-lang3 3.1 | commons-lang3 3.14+ | Boot 내장 | Boot BOM 버전 자동 관리. lang 2.x와 병행 사용 가능 |
| 42 | commons-net 2.0 | commons-net 3.11.1 | 버전업 | FTP/FTPS TLS 1.2+ 지원 |
| 43 | commons-pool 1.5.3 | commons-pool2 | Boot 내장 | artifactId 변경. Boot BOM 관리 |
| 44 | commons-validator 1.1.3 | commons-validator 1.9.0 | 버전업 | 이메일/URL/IP 검증 보안 강화 |
| 45 | commons-vfs 1.0 | commons-vfs2 2.9.0 | 버전업 | artifactId 변경(vfs→vfs2). API 재설계 |

#### Utility

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 46 | activation 1.1 (JAF) | jakarta.activation-api | 교체 | JDK 17에서 javax.activation 제거. jakarta 네임스페이스 전환 |
| 47 | joda-time 1.6.2 | joda-time 2.12.7 | 버전업 | **기존 코드 유지**. java.time 전환은 점진적으로 권장 |
| 48 | jsoup (신규) | jsoup 1.18.1 | 신규 | nekohtml 대체. HTML 파싱 사실상 표준 |

#### 제거 (프로젝트 폐기)

| # | ASIS | 제거 사유 | 대체 방안 |
|---|------|----------|----------|
| 49 | avalon-framework 4.0 | Apache Avalon 프로젝트 완전 폐기 | 의존 코드 리팩토링 |
| 50 | commons-discovery 0.2 | Axis 종속 라이브러리, EOL | Spring DI |

---

## 4. 솔루션/자체 라이브러리

바이너리가 Maven Central에 배포되어 있지 않거나, 소스 코드를 확보할 수 없는 라이브러리이다. 별도 절차를 통해 JDK 17 호환 여부를 확인해야 한다.

| 파일명 | 추정 용도 | 향후 조치 |
|--------|----------|----------|
| ozenc_utf8.jar | 인코딩/보안 모듈 | 담당 부서에 JDK 17 호환 버전 요청 |
| queryapi500.jar / queryapi530.jar | 쿼리 API | 담당 부서에 업데이트 버전 요청 |
| RpsMerge.jar | 리포트 병합 | 벤더사에 JDK 17 호환 확인 |
| rptcertapi.jar | 리포트 인증 | 벤더사에 JDK 17 호환 확인 |
| sciSecu_v2.jar / sciSecuPCC.jar | 보안 모듈 | 보안팀에 JDK 17 호환 확인 |
| seedx.jar | SEED 암호화 | 암호화 모듈 JDK 17 호환 테스트 필요 |
| anyframe-* | 삼성SDS Anyframe 프레임워크 | SDS에 Spring Boot 3 호환 버전 문의 |
| transkey-* | 키보드 보안 | 벤더사에 JDK 17/Jakarta EE 호환 문의 |
| ojdbc14 | Oracle JDBC (JDK 1.4용) | **ojdbc11로 대체 완료** (Maven Central 공식 배포) |

---

## 5. 주요 결정 사항과 근거

### 5.1 JSP 유지 + Tiles 교체 불가피

기존 시스템은 JSP + Tiles 2.2.2 기반이다. **JSP는 Spring Boot 3에서도 지원**되므로 기존 JSP 파일을 그대로 사용한다. `tomcat-embed-jasper`와 JSTL을 추가하면 된다.

단, **Tiles는 Spring 6에서 지원이 완전 제거**되었다(`org.springframework.web.servlet.view.tiles3` 패키지 삭제). 이는 버전업으로 해결할 수 없는 문제이므로 교체가 불가피하다.

Tiles 교체 옵션:
- **JSP include 방식** (권장): `<%@ include %>` 또는 `<jsp:include>`로 레이아웃 구성. 코드 변경 최소
- **Sitemesh 3**: Tiles와 유사한 데코레이터 패턴. 별도 라이브러리 추가 필요

### 5.2 CentOS 7 선택 (Docker 런타임 OS)

CentOS 7은 JDK 6, 7, 8, 17을 모두 설치할 수 있어 레거시 환경에서 신규 환경까지 검증하기에 적합하다. Adoptium Temurin RPM 저장소를 통해 JDK 17을 공식 설치한다.

> **주의**: CentOS 7은 2024년 6월 EOL. 운영 환경에서는 Rocky Linux 9 / AlmaLinux 9 사용 권장.

### 5.3 Ehcache 3 jakarta classifier

Spring Boot 3은 Jakarta EE 10 기반이므로 Ehcache 3의 `<classifier>jakarta</classifier>` 지정이 필수이다. 누락 시 `javax.cache` 패키지를 찾을 수 없어 `ClassNotFoundException`이 발생한다.

### 5.4 Log4j 1.x → log4j-1.2-api 브릿지

Log4j 1.x는 CVE-2019-17571 등 심각한 취약점이 존재하여 그대로 사용할 수 없다. 기존 코드에서 `org.apache.log4j.Logger`를 사용하는 경우, **Log4j2의 `log4j-1.2-api` 브릿지**를 통해 API 호환성을 유지하면서 실제 로깅은 Logback(Boot 기본)으로 라우팅할 수 있다. 이 방식은 기존 코드 변경 없이 보안 문제를 해결한다.

### 5.5 joda-time 유지 (버전업)

기존 코드에서 `org.joda.time.DateTime` 등을 사용하는 경우, 즉시 `java.time`으로 전환하면 공수가 크다. joda-time 2.12.7은 JDK 17에서 정상 동작하므로 **버전업으로 기존 코드를 유지**하고, `java.time` 전환은 점진적으로 진행한다.

### 5.6 commons-collections 3 → 4 필수 전환 (RCE 취약점)

Apache Commons Collections 3.x에는 역직렬화를 통한 원격 코드 실행(RCE) 취약점(CVE-2015-6420)이 존재한다. 패키지가 `org.apache.commons.collections4`로 변경되어 import 수정이 필요하지만, **보안상 전환이 필수**이다.

### 5.7 c3p0 / commons-dbcp 유지 (버전업)

기존 커넥션 풀을 버전업하여 JDK 17 호환성을 검증한다. Spring Boot 기본 HikariCP와 병행 사용 가능하며, 기존 설정을 유지하려는 경우 c3p0 0.9.5.5 또는 commons-dbcp2로 버전업한다.

### 5.8 Oracle XE Docker

원본 시스템이 Oracle을 사용하므로 `gvenzl/oracle-xe:21-slim` 경량 이미지로 검증한다. ojdbc14(JDK 1.4용) → ojdbc11(JDK 17 호환)로 드라이버를 전환한다.

---

## 6. 프로젝트 구조

```
6-company-intro/
├── Dockerfile                          # Multi-stage 빌드 (Maven → CentOS 7 + JDK 17)
├── docker-compose.yml                  # 앱 + Oracle XE 21c 구성
├── pom.xml                             # 전체 의존성 정의 (ASIS→TOBE 주석 포함)
├── README.md
└── src/
    ├── main/
    │   ├── java/com/ss/companyintro/
    │   │   ├── CompanyIntroApplication.java
    │   │   ├── config/
    │   │   │   ├── CacheConfig.java            # Ehcache 3 JCache
    │   │   │   ├── HttpClientConfig.java        # HttpClient 5 Bean
    │   │   │   └── QuartzConfig.java           # Quartz 스케줄러
    │   │   ├── controller/
    │   │   │   └── HealthCheckController.java  # REST + JSP 뷰
    │   │   ├── entity/
    │   │   │   └── SampleEntity.java           # JPA (Hibernate 6)
    │   │   ├── job/
    │   │   │   └── SampleQuartzJob.java
    │   │   └── repository/
    │   │       └── SampleRepository.java
    │   ├── resources/
    │   │   ├── application.yml                 # H2 기본 프로파일
    │   │   ├── application-docker.yml          # Oracle XE 프로파일
    │   │   ├── ehcache.xml
    │   │   └── logback-spring.xml
    │   └── webapp/WEB-INF/jsp/
    │       ├── index.jsp                       # 메인 페이지 JSP
    │       └── hello.jsp                       # JSP 뷰 (기존 기술 유지 검증)
    └── test/java/com/ss/companyintro/
        ├── boot/                               # Spring Boot 컨텍스트, JPA
        ├── cache/                              # Ehcache 3
        ├── commons/                            # Apache Commons 계열 (10종)
        ├── connpool/                           # c3p0, DBCP2 커넥션 풀
        ├── http/                               # HttpClient 5
        ├── logging/                            # Log4j 브릿지
        ├── office/                             # Apache POI
        ├── removed/                            # 제거 라이브러리 부재 확인
        ├── scheduler/                          # Quartz
        ├── security/                           # AntiSamy
        ├── svg/                                # Batik
        ├── template/                           # Velocity
        ├── util/                               # joda-time, activation, JSP
        └── xml/                                # dom4j, Xalan, Xerces
```

---

## 7. 실행 방법

### 로컬 실행 (H2 인메모리 DB)

```bash
cd 6-company-intro
mvn spring-boot:run
```

- 애플리케이션: http://localhost:8080
- H2 콘솔: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:testdb`)

### Docker 실행 (Oracle XE)

```bash
cd 6-company-intro
docker-compose up -d
```

- 애플리케이션: http://localhost:8080
- Oracle DB: `localhost:1521/XE` (system / oracle)

---

## 8. 테스트 실행

```bash
cd 6-company-intro
mvn clean test
```

### 테스트 그룹별 검증 대상

| 테스트 그룹 | 검증 대상 라이브러리 |
|------------|-------------------|
| boot/ | Spring Boot 3.5.11 컨텍스트 로딩, Hibernate 6 + JPA CRUD |
| cache/ | Ehcache 3.10.8 (JCache, jakarta classifier) |
| commons/ | beanutils, codec, collections4, digester3, io, lang 2.6, lang3, net, pool2, validator, vfs2 |
| connpool/ | c3p0 0.9.5.5, commons-dbcp2 |
| http/ | HttpClient 5 (클라이언트 생성, 요청 객체) |
| logging/ | Log4j 1.x API 브릿지 (log4j-1.2-api) |
| office/ | Apache POI 5.3.0 (Excel .xlsx 읽기/쓰기) |
| removed/ | 제거 라이브러리 ClassPath 부재 확인 (Log4j, cglib, Tiles, joda-time 등) |
| scheduler/ | Quartz 2.3.2+ (Boot 자동 구성) |
| security/ | AntiSamy 1.7.6 (XSS 필터) |
| svg/ | Batik 1.17 (SVG 렌더링) |
| template/ | Velocity 2.4.1 |
| util/ | joda-time 2.12.7, jakarta.activation-api, JSP 뷰 |
| xml/ | dom4j 2.1.4, Xalan 2.7.3, Xerces 2.12.2 |

---

## 9. 알려진 제약사항

1. **솔루션/자체 라이브러리 미검증**: anyframe, transkey, sciSecu 등 10건은 바이너리 미확보로 본 프로젝트에 포함되지 않았다. 각 담당 부서/벤더사에 JDK 17 호환 버전을 별도 요청해야 한다.

2. **Tiles 교체 불가피**: Spring 6에서 Tiles 지원이 완전 제거되어 기존 Tiles 레이아웃은 JSP include 방식 또는 Sitemesh로 전환해야 한다. 기존 JSP 화면은 그대로 유지하되, 레이아웃 구성 코드만 수정이 필요하다.

3. **Axis 교체 불가피**: Apache Axis 1.4는 Jakarta EE 미호환이며 프로젝트가 사실상 폐기 상태이다. SOAP 통신이 필요한 경우 Apache CXF 또는 Spring Web Services로 전환해야 한다.

4. **commons-fileupload2 미안정 버전**: Jakarta Servlet 6 호환 commons-fileupload2는 아직 Milestone 릴리즈(M2) 단계이다. 안정 버전 출시까지 Spring Boot 내장 `StandardServletMultipartResolver` 사용을 권장한다.

5. **CentOS 7 EOL**: Docker 런타임 CentOS 7은 2024년 6월 지원 종료. 운영 환경에서는 Rocky Linux 9 / AlmaLinux 9 권장.

6. **JSP 패키징 주의**: Spring Boot의 `jar` 패키징에서는 JSP 사용에 제한이 있다. 운영 환경에서는 `war` 패키징을 사용해야 한다.

7. **batik과 xalan/xerces 간 transitive dependency 충돌 가능성**: `mvn dependency:tree`로 실제 해석 버전을 확인하고, 필요 시 `<exclusions>` 적용.
