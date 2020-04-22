package plaid.templatingengine;

class TemplateStateException extends IllegalStateException {

  TemplateStateException() {
    super("Template is invalid to render as it still contains directives." + System.lineSeparator()
            + " Please make sure the map contains variables that correspond to a directive and that the template has valid directive markup");
    }
}
