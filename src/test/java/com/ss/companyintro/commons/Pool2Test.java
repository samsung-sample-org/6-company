package com.ss.companyintro.commons;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Apache Commons Pool2 테스트.
 *
 * <p>ASIS: commons-pool 1.5.3 (commons-pool:commons-pool)<br>
 * TOBE: commons-pool2 (Boot 관리 버전, 2.12.x, org.apache.commons:commons-pool2)</p>
 *
 * <p>전환 이유: pool 1.x EOL, 2.x에서 GenericObjectPool 완전 재설계.
 * 객체 풀에서 borrow/return이 정상 동작하는지 확인한다.</p>
 */
class Pool2Test {

    /**
     * 테스트용 문자열 객체 팩토리.
     */
    static class StringPoolFactory extends BasePooledObjectFactory<String> {

        @Override
        public String create() {
            return "pooled-object";
        }

        @Override
        public PooledObject<String> wrap(String obj) {
            return new DefaultPooledObject<>(obj);
        }
    }

    @Test
    @DisplayName("[TOBE] commons-pool2 Boot 관리 버전 - GenericObjectPool borrow/return 동작 확인")
    void borrowAndReturn() throws Exception {
        // given: 풀 설정 및 생성
        GenericObjectPoolConfig<String> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(5);

        try (GenericObjectPool<String> pool = new GenericObjectPool<>(
                new StringPoolFactory(), config)) {

            // when: 풀에서 객체를 빌려온다
            String borrowed = pool.borrowObject();

            // then: 빌려온 객체가 유효해야 한다
            assertNotNull(borrowed, "빌려온 객체가 null이면 안 된다");
            assertEquals("pooled-object", borrowed, "빌려온 객체 값이 일치해야 한다");
            assertEquals(1, pool.getNumActive(), "활성 객체 수가 1이어야 한다");

            // when: 객체를 풀에 반환한다
            pool.returnObject(borrowed);

            // then: 활성 객체 수가 0이어야 한다
            assertEquals(0, pool.getNumActive(), "반환 후 활성 객체 수가 0이어야 한다");
            assertEquals(1, pool.getNumIdle(), "유휴 객체 수가 1이어야 한다");
        }
    }
}
