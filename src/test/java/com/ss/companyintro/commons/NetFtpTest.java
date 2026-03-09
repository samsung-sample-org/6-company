package com.ss.companyintro.commons;

import org.apache.commons.net.ftp.FTPClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Apache Commons Net FTP 클라이언트 테스트.
 *
 * <p>ASIS: commons-net-ftp 2.0<br>
 * TOBE: commons-net 3.11.1</p>
 *
 * <p>전환 이유: FTP/SMTP 클라이언트 보안 강화, TLS 1.2+ 지원.
 * FTPClient 객체가 정상적으로 생성되는지 확인한다(실제 FTP 서버 연결 불필요).</p>
 */
class NetFtpTest {

    @Test
    @DisplayName("[TOBE] commons-net 3.11.1 - FTPClient 객체 생성 확인")
    void createFtpClient() {
        // when: FTPClient 인스턴스 생성
        FTPClient ftpClient = new FTPClient();

        // then: 객체가 null이 아니어야 한다
        assertNotNull(ftpClient, "FTPClient 객체가 정상적으로 생성되어야 한다");
    }
}
