package plaid.templatingengine;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import java.util.Map;
import java.util.regex.Pattern;

import static plaid.templatingengine.TemplatingEngineUtils.CLOSED_BRACKET;
import static plaid.templatingengine.TemplatingEngineUtils.ENDIF_DIRECTIVE;
import static plaid.templatingengine.TemplatingEngineUtils.EMPTY_STRING;
import static plaid.templatingengine.TemplatingEngineUtils.OPEN_BRACKET;

import static plaid.templatingengine.TemplatingEngineUtils.emptyMap;
import static plaid.templatingengine.TemplatingEngineUtils.emptyString;
import static plaid.templatingengine.TemplatingEngineUtils.log;

public class TemplatingEngine {

  public static void main(final String[] args) {
    log("RUNNING JUNIT TESTS");
    log("===================");

    final JUnitCore junit = new JUnitCore();
    final Result result = junit.run(TemplatingEngineTest.class);

    log(EMPTY_STRING);

    if (result.wasSuccessful()) {
      log("NO TEST FAILURES WOOOO!");
    } else {
      log("There were " + result.getFailureCount()  + " failed tests. Quick find the bugs!");
    }
  }

  public static String render(@Nullable final String template, @Nullable final Map<String, DirectiveType> map) {
    if (emptyString(template)) {
      return template;
    }

    final String renderedTemplate =
            emptyMap(map) || noTemplateDirectives(template) ? template : renderTemplate(template, map);
    return handleRenderedTemplate(renderedTemplate);
  }

  private static boolean noTemplateDirectives(@NotNull final String template) {
    return !template.contains(OPEN_BRACKET) || !template.contains(CLOSED_BRACKET);
  }

  private static String renderTemplate(@NotNull final String template, @NotNull final Map<String, DirectiveType> map) {
    String currTemplate = template;

    for (final String key : map.keySet()) {
      if (emptyString(key)) {
        log("WARNING: Variable map contained key that was invalid.");
        continue;
      }

      final DirectiveType value = map.get(key);
      if (value != null) {
        currTemplate = value.render(currTemplate, key);
      }
    }

    return currTemplate;
  }

  private static String handleRenderedTemplate(@NotNull final String template) {
    final String finalTemplate = template.replaceAll(Pattern.quote(ENDIF_DIRECTIVE), EMPTY_STRING);

    if (noTemplateDirectives(finalTemplate)) {
      log("SUCCESS: Rendering Complete");
      return finalTemplate;
    } else {
      throw new TemplateStateException();
    }
  }
}
