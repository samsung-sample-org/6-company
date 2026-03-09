package com.ss.companyintro.connpool;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Apache Commons DBCP2 커넥션 풀 테스트.
 *
 * <p>ASIS: commons-dbcp 1.2.2 (commons-dbcp:commons-dbcp)<br>
 * TOBE: commons-dbcp2 (org.apache.commons:commons-dbcp2, Boot 관리)</p>
 *
 * <p>전환 이유: dbcp 1.x EOL, 스레드 안전성 문제.
 * dbcp2로 artifactId 변경. Boot BOM 버전 관리.</p>
 */
class Dbcp2Test {

    @Test
    @DisplayName("[TOBE] commons-dbcp2 - BasicDataSource 생성 및 커넥션 획득 확인")
    void createAndConnect() throws SQLException {
        try (BasicDataSource ds = new BasicDataSource()) {
            ds.setDriverClassName("org.h2.Driver");
            ds.setUrl("jdbc:h2:mem:dbcp2test");
            ds.setUsername("sa");
            ds.setPassword("");
            ds.setMinIdle(1);
            ds.setMaxTotal(5);

            try (Connection conn = ds.getConnection()) {
                assertNotNull(conn, "Connection이 null이면 안 된다");
                assertFalse(conn.isClosed(), "Connection이 닫혀 있으면 안 된다");
            }
        }
    }
}
