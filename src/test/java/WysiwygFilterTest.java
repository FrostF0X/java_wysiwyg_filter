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
        assertEquals(filter.filter(inputHtml), expectedHtml);
    }


    @Parameterized.Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
                //Null configuration tests
                {nullConfiguration(), "", ""},
                {nullConfiguration(), "Test", "Test"},
                {nullConfiguration(), "<p class=\"random\">Test", ""},

                //Empty configuration test
                {emptyConfiguration(), "", ""},
                {emptyConfiguration(), "Test", "Test"},
                {emptyConfiguration(), "<p class=\"random\">Test", ""},

                //Standard configuration test
                {standardConfiguration(), "", ""},
                {standardConfiguration(), "Test", "Test"},
                {standardConfiguration(), "<p id=\"allowed\">Test</p>", "<p id=\"allowed\">Test</p>"},
                //Disallowed attribute
                {standardConfiguration(), "<p id=\"dissallowed\">Test</p>", "<p>Test</p>"},
                //Disallowed attribute value
                {standardConfiguration(), "<p class=\"random and disallowed attribute\">Test</p>", "<p>Test</p>"},
                //Disallowed tag
                {standardConfiguration(), "<p class=\"random\">Test<div>Disallowed tag</div>", "<p>Test</p>"},


                //Fixing markup test
                {standardConfiguration(), "<p>Test", "<p>Test</p>"},
                {standardConfiguration(), "<p id=\"allowed\">Test", "<p id=\"allowed\">Test</p>"},
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

        allowedAttributes.put("id", "allowed");
        configuration.put("p", allowedAttributes);

        return configuration;
    }
}
