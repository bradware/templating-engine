package plaid.templatingengine;

import com.sun.istack.internal.NotNull;

import static plaid.templatingengine.TemplatingEngineUtils.CLOSED_BRACKET;
import static plaid.templatingengine.TemplatingEngineUtils.EMPTY_STRING;
import static plaid.templatingengine.TemplatingEngineUtils.ENDIF_DIRECTIVE;
import static plaid.templatingengine.TemplatingEngineUtils.IF_DIRECTIVE;

import static plaid.templatingengine.TemplatingEngineUtils.log;

class BooleanDirectiveType implements DirectiveType {

  private boolean directiveValue;

  BooleanDirectiveType(final boolean value) {
    this.directiveValue = value;
  }

  public String render(@NotNull final String template, @NotNull final String directiveKey) {
    final String ifDirective = IF_DIRECTIVE + directiveKey + CLOSED_BRACKET;

    if (!template.contains(ifDirective)) {
      log("WARNING: Variable map contained key that was not a directive in the template.");
      return template;
    }

    if (directiveValue) {
      return template.replace(ifDirective, EMPTY_STRING);
    } else {
      final int startIndex = template.indexOf(ifDirective);
      final int endIndex = findEndIfIndex(template, startIndex);

      return template.replace(template.substring(startIndex, endIndex), EMPTY_STRING);
    }
  }

  private static int findEndIfIndex(@NotNull final String template, int currIndex) {
    int endIfIndex, startIfIndex;

    do {
      endIfIndex = template.indexOf(ENDIF_DIRECTIVE, currIndex + ENDIF_DIRECTIVE.length());
      startIfIndex = template.indexOf(IF_DIRECTIVE, currIndex + IF_DIRECTIVE.length());
      currIndex = endIfIndex;
    } while (startIfIndex != -1 && endIfIndex > startIfIndex);

    return endIfIndex;
  }
}
