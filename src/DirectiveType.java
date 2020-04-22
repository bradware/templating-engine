package plaid.templatingengine;

interface DirectiveType {

  String render(final String template, final String directiveKey);
}