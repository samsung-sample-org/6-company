package com.ss.companyintro.xml;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Xalan XSLT 변환 테스트.
 *
 * <p>ASIS: xalan 2.7.0<br>
 * TOBE: xalan 2.7.3</p>
 *
 * <p>전환 이유: XSLT 처리 보안 패치 적용. Xalan을 TransformerFactory로 등록하여
 * XSLT 변환이 정상적으로 동작하는지 확인한다.</p>
 */
class XalanTest {

    /**
     * Identity XSLT: 입력 XML을 그대로 출력한다.
     */
    private static final String IDENTITY_XSLT =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">"
            + "  <xsl:template match=\"@*|node()\">"
            + "    <xsl:copy>"
            + "      <xsl:apply-templates select=\"@*|node()\"/>"
            + "    </xsl:copy>"
            + "  </xsl:template>"
            + "</xsl:stylesheet>";

    private static final String INPUT_XML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<greeting>Hello, Xalan!</greeting>";

    @Test
    @DisplayName("[TOBE] xalan 2.7.3 - TransformerFactory 생성 확인")
    void createTransformerFactory() {
        // TransformerFactory 인스턴스가 정상 생성되는지 확인한다
        TransformerFactory factory = TransformerFactory.newInstance();
        assertNotNull(factory, "TransformerFactory가 null이면 안 된다");
    }

    @Test
    @DisplayName("[TOBE] xalan 2.7.3 - Identity XSLT 변환 확인")
    void identityTransform() throws Exception {
        // Identity XSLT를 적용하여 입력 XML이 그대로 출력되는지 확인한다
        TransformerFactory factory = TransformerFactory.newInstance();
        StreamSource xsltSource = new StreamSource(new StringReader(IDENTITY_XSLT));
        Transformer transformer = factory.newTransformer(xsltSource);
        assertNotNull(transformer, "Transformer가 null이면 안 된다");

        StreamSource inputSource = new StreamSource(new StringReader(INPUT_XML));
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);

        transformer.transform(inputSource, result);

        String output = writer.toString();
        assertTrue(output.contains("Hello, Xalan!"),
                "변환 결과에 원본 텍스트가 포함되어야 한다");
    }
}
