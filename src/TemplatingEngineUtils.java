package plaid.templatingengine;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.Map;

class TemplatingEngineUtils {

  private static final String IF = "if ";
  private static final String ENDIF = "endif";

  static final String EMPTY_STRING = "";
  static final String OPEN_BRACKET = "{{";
  static final String CLOSED_BRACKET = "}}";

  static final String IF_DIRECTIVE = OPEN_BRACKET + IF;
  static final String ENDIF_DIRECTIVE = OPEN_BRACKET + ENDIF + CLOSED_BRACKET;

  static boolean emptyMap(@Nullable final Map<String, DirectiveType> map) {
    return map == null || map.isEmpty();
  }

  static boolean emptyString(@Nullable final String str) {
    return str == null || str.trim().isEmpty();
  }

  static void log(@NotNull final String message) {
    System.out.println(message);
  }
}
