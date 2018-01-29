# Wysiwyg Filter

Simple Java library for filtering html tags an attributes according to provided configuration

## Usage

```java
import static org.junit.Assert.assertEquals;

public class WysiwygFilterTest {

    public void testFilter() {
        String inputHtml = "<p id=\"allowed\" class=\"disallowed attribute\">Test</p><div>Disallowedtag</div>";
        String expectedHtml = "<p id=\"allowed\">Test</p>";
        WysiwygFilter filter = new WysiwygFilter(standardConfiguration());
        assertEquals(filter.filter(inputHtml), expectedHtml);
    }

    private Map<String, Map<String, String>> standardConfiguration(){
        Map<String, Map<String, String>> configuration = new HashMap<String, Map<String, String>>();
        Map<String, String> allowedAttributes = new HashMap<String, String>();

        allowedAttributes.put("id", "allowed");
        configuration.put("p", allowedAttributes);

        return configuration;
    }
}
```
 
 