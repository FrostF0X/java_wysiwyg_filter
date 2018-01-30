import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class WysiwygFilterTest {

    private final Map<String, Map<String, String>> configuration;
    private final String inputHtml;
    private final String expectedHtml;

    public WysiwygFilterTest(Map<String, Map<String, String>> configuration, String inputHtml, String expectedHtml){
        this.configuration = configuration;
        this.inputHtml = inputHtml;
        this.expectedHtml = expectedHtml;
    }

    @Test
    public void testFilter() {
        WysiwygFilter filter = new WysiwygFilter(configuration);
        assertEquals(expectedHtml, filter.filter(inputHtml));
    }


    @Parameterized.Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
                //Null configuration tests
                {nullConfiguration(), "", ""},
                {nullConfiguration(), "Test", "Test"},
                //Unwrap element
                {nullConfiguration(), "<p>Test</p>", "Test"},

                //Empty configuration test
                {emptyConfiguration(), "", ""},
                {emptyConfiguration(), "Test", "Test"},
                //Unwrap element
                {emptyConfiguration(), "<p>Test</p>", "Test"},

                //
                {standardConfiguration(), "", ""},
                {standardConfiguration(), "Test", "Test"},
                //Forced attribute
                {standardConfiguration(), "<p class=\"random\">Test</p>", "<p class=\"forced\">Test</p>"},
                //Forced attribute
                {standardConfiguration(), "<p class=\"forced\">Test</p>", "<p class=\"forced\">Test</p>"},
                //Allowed attribute
                {standardConfiguration(), "<p id=\"random\">Test</p>", "<p id=\"random\">Test</p>"},
                //Disallowed attribute
                {standardConfiguration(), "<p href=\"disallowed attribute value\">Test</p>", "<p>Test</p>"},
                //Unwrap element
                {standardConfiguration(), "<p>Test</p><div>Disallowed tag</div>", "<p>Test</p>Disallowed tag"},
                //Unwrap complex element
                {standardConfiguration(), "<div><p>Test</p></div>", "<p>Test</p>"},

                //Fixing markup tests
                {standardConfiguration(), "<p>Test", "<p>Test</p>"},
                {standardConfiguration(), "<p>Test phrase", "<p>Test phrase</p>"},
                {standardConfiguration(), "<p id=\"random\">Test", "<p id=\"random\">Test</p>"},
                {standardConfiguration(), "<p>Test</what?>", "<p>Test</p>"},
        });
    }

    private static Map<String, Map<String, String>> nullConfiguration(){
        return null;
    }

    private static Map<String, Map<String, String>> emptyConfiguration(){
        return new HashMap<String, Map<String, String>>();
    }

    private static Map<String, Map<String, String>> standardConfiguration(){
        Map<String, Map<String, String>> configuration = new HashMap<String, Map<String, String>>();
        Map<String, String> allowedAttributes = new HashMap<String, String>();

        allowedAttributes.put("class", "forced");
        allowedAttributes.put("id", null);
        configuration.put("p", allowedAttributes);

        return configuration;
    }
}
