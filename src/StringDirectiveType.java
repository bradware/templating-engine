package plaid.templatingengine;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import static plaid.templatingengine.TemplatingEngineUtils.CLOSED_BRACKET;
import static plaid.templatingengine.TemplatingEngineUtils.EMPTY_STRING;
import static plaid.templatingengine.TemplatingEngineUtils.OPEN_BRACKET;

import static plaid.templatingengine.TemplatingEngineUtils.log;

class StringDirectiveType implements DirectiveType {

  @NotNull private String directiveValue;

  StringDirectiveType(@Nullable final String value) {
    this.directiveValue = value != null ? value : EMPTY_STRING;
  }

  public String render(@NotNull final String template, @NotNull final String directiveKey) {
    if (!template.contains(directiveKey)) {
      log("WARNING: Variable map contained key that was not a directive in the template.");
      return template;
    }

    return template.replace(OPEN_BRACKET + directiveKey + CLOSED_BRACKET, directiveValue);
  }
}
