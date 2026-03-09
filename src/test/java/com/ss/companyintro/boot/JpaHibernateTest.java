package com.ss.companyintro.boot;

import com.ss.companyintro.entity.SampleEntity;
import com.ss.companyintro.repository.SampleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * JPA + Hibernate 통합 테스트.
 *
 * <p>ASIS: Hibernate 3.1 + javax.persistence (JPA 2.x, 수동 SessionFactory 설정)<br>
 * TOBE: Hibernate 6.x + jakarta.persistence (JPA 3.1, Spring Boot 3 자동 설정)</p>
 *
 * <p>전환 이유: Hibernate 3.x EOL. Spring Boot 3은 Jakarta EE 9+ 네임스페이스를
 * 사용하므로 javax.persistence → jakarta.persistence 전환이 필수이다.
 * {@code @DataJpaTest}는 H2 인메모리 DB를 기본으로 사용하여 별도 설정이 불필요하다.</p>
 */
@DataJpaTest
class JpaHibernateTest {

    @Autowired
    private SampleRepository sampleRepository;

    @Test
    @DisplayName("[TOBE] Hibernate 6.x + jakarta.persistence - save 및 findByName 동작 확인")
    void saveAndFindByName() {
        // given: 샘플 엔티티 저장
        SampleEntity entity = new SampleEntity("테스트", "설명", LocalDateTime.now());
        sampleRepository.save(entity);

        // when: 이름으로 조회
        List<SampleEntity> result = sampleRepository.findByName("테스트");

        // then: 저장한 엔티티가 조회되어야 한다
        assertFalse(result.isEmpty(), "조회 결과가 비어 있으면 안 된다");
        assertEquals("테스트", result.get(0).getName(), "이름이 일치해야 한다");
    }
}
