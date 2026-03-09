package com.ss.companyintro.commons;

import org.apache.commons.digester3.Digester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Apache Commons Digester3 XML 파싱 테스트.
 *
 * <p>ASIS: commons-digester 1.7 (commons-digester:commons-digester)<br>
 * TOBE: commons-digester3 3.2 (org.apache.commons:commons-digester3)</p>
 *
 * <p>전환 이유: Digester 1.x → 3.x로 메이저 업그레이드.
 * groupId/artifactId가 변경되었으며, 규칙 기반 XML 파싱 API가 개선되었다.</p>
 */
class DigesterTest {

    /**
     * XML에서 파싱할 항목 클래스.
     */
    public static class Item {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Test
    @DisplayName("[TOBE] commons-digester3 3.2 - XML 파싱 동작 확인")
    void parseSimpleXml() throws Exception {
        // given: 간단한 XML 문자열
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<items>"
                + "  <item name=\"사과\"/>"
                + "  <item name=\"바나나\"/>"
                + "</items>";

        // Digester 설정
        Digester digester = new Digester();
        digester.setValidating(false);

        // 루트에 ArrayList 스택 push
        List<Item> items = new ArrayList<>();
        digester.push(items);

        // items/item 패턴을 만나면 Item 객체 생성
        digester.addObjectCreate("items/item", Item.class);
        digester.addSetProperties("items/item");
        digester.addSetNext("items/item", "add");

        // when: XML 파싱
        ByteArrayInputStream input = new ByteArrayInputStream(
                xml.getBytes(StandardCharsets.UTF_8));
        digester.parse(input);

        // then: 파싱 결과 확인
        assertFalse(items.isEmpty(), "파싱 결과가 비어 있으면 안 된다");
        assertEquals(2, items.size(), "항목이 2개여야 한다");
        assertEquals("사과", items.get(0).getName(), "첫 번째 항목 이름이 일치해야 한다");
        assertEquals("바나나", items.get(1).getName(), "두 번째 항목 이름이 일치해야 한다");
    }
}
