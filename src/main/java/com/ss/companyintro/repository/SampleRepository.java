package com.ss.companyintro.repository;

import com.ss.companyintro.entity.SampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 샘플 엔티티 리포지토리.
 *
 * <p>ASIS: Spring Data JPA 1.x (Hibernate 3.1 + javax.persistence)<br>
 * TOBE: Spring Data JPA 3.x (Hibernate 6.x + jakarta.persistence)</p>
 *
 * <p>전환 이유: Spring Boot 3의 spring-boot-starter-data-jpa는
 * Spring Data JPA 3.x를 포함하며, 내부적으로 jakarta.persistence를 사용한다.
 * 인터페이스 시그니처 자체는 변경 없이 호환되지만,
 * 엔티티 클래스의 임포트가 jakarta로 변경되어야 정상 동작한다.</p>
 */
public interface SampleRepository extends JpaRepository<SampleEntity, Long> {

    /**
     * 이름으로 샘플 엔티티 목록을 조회한다.
     *
     * @param name 조회할 이름
     * @return 해당 이름을 가진 엔티티 목록
     */
    List<SampleEntity> findByName(String name);
}
