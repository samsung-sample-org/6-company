package com.ss.companyintro.commons;

import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Apache Commons VFS2 테스트.
 *
 * <p>ASIS: commons-vfs 1.0 (org.apache.commons:commons-vfs)<br>
 * TOBE: commons-vfs2 2.9.0 (org.apache.commons:commons-vfs2)</p>
 *
 * <p>전환 이유: VFS 1.x EOL, 2.x에서 artifactId 변경 및 API 재설계.
 * VFS.getManager()로 FileSystemManager가 정상 생성되는지 확인한다.</p>
 */
class Vfs2Test {

    @Test
    @DisplayName("[TOBE] commons-vfs2 2.9.0 - FileSystemManager 생성 확인")
    void getFileSystemManager() throws Exception {
        // when: VFS 매니저 생성
        FileSystemManager fsManager = VFS.getManager();

        // then: FileSystemManager가 null이 아니어야 한다
        assertNotNull(fsManager, "FileSystemManager가 정상적으로 생성되어야 한다");
    }
}
