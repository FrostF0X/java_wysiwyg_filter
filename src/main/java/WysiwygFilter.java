import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WysiwygFilter {

    private final Map<String, Map<String, String>> configuration;

    WysiwygFilter(Map<String, Map<String, String>> configuration) {
        if(configuration == null){
            configuration = new HashMap<String, Map<String, String>>();
        }
        this.configuration = configuration;
        configuration.put("#root", null);
        configuration.put("html", null);
        configuration.put("body", null);
        configuration.put("head", null);
    }

    public String filter(String html) {
        Document document = Jsoup.parseBodyFragment(html);
        List<Element> elements = document.getAllElements();

        for (Element element : elements) {

            if (!configuration.containsKey(element.tagName())) {
                element.remove();
            }

            leaveAllowedAttributes(element);
        }

        return document.body().html();
    }


    private void leaveAllowedAttributes(Element element) {
        Map<String, String> allowedAttributes = configuration.get(element.tagName());

        if(allowedAttributes == null){
            allowedAttributes = new HashMap<String, String>();
        }

        for (Attribute attribute : element.attributes()) {
            String value = allowedAttributes.get(attribute.getKey());

            if (value == null || !value.equals(attribute.getValue())) {
                element.removeAttr(attribute.getKey());
            }
        }
    }
}
