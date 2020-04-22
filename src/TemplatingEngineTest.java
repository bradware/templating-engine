package plaid.templatingengine;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static plaid.templatingengine.TemplatingEngineUtils.EMPTY_STRING;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static plaid.templatingengine.TemplatingEngine.render;

public class TemplatingEngineTest {

  @Test(expected = TemplateStateException.class)
  public void testNullMap() {
    final String template = "{{name}}{{if title}}Dummy Title{{endif}}{{if title}}Dummy Title{{endif}}" +
            "{{if holiday}} Happy {{holidayName}}{{endif}}";

    render(template, null);
  }

  @Test(expected = TemplateStateException.class)
  public void testEmptyMap() {
    final String template = "{{name}}{{if title}}Dummy Title{{endif}}{{if title}}Dummy Title{{endif}}" +
            "{{if holiday}} Happy {{holidayName}}{{endif}}";

    final Map<String, DirectiveType> map = new HashMap<String, DirectiveType>();
    map.put("name", new StringDirectiveType("Brad -"));
    map.put("holidayName", new StringDirectiveType("July 4th!"));

    render(template, map);
  }

  @Test(expected = TemplateStateException.class)
  public void testMapWithLessVariables() {
    final String template = "{{name}}{{if title}}Dummy Title{{endif}}{{if title}}Dummy Title{{endif}}" +
            "{{if holiday}} Happy {{holidayName}}{{endif}}";

    final Map<String, DirectiveType> map = new HashMap<String, DirectiveType>();
    map.put("name", new StringDirectiveType("Brad -"));

    render(template, map);
  }

  @Test
  public void testMapWithNullTemplate() {
    final Map<String, DirectiveType> map = new HashMap<String, DirectiveType>();
    map.put("name", new StringDirectiveType("Brad -"));
    map.put("title", new BooleanDirectiveType(false));
    map.put("holiday", new BooleanDirectiveType(true));
    map.put("holidayName", new StringDirectiveType("July 4th!"));

    assertNull(render(null, map));
  }

  @Test
  public void testMapWithEmptyTemplate() {
    final Map<String, DirectiveType> map = new HashMap<String, DirectiveType>();
    map.put("name", new StringDirectiveType("Brad -"));
    map.put("title", new BooleanDirectiveType(false));
    map.put("holiday", new BooleanDirectiveType(true));
    map.put("holidayName", new StringDirectiveType("July 4th!"));

    assertEquals(EMPTY_STRING, render(EMPTY_STRING, map));
  }

  @Test
  public void testMapWithEmptyTemplate2() {
    final Map<String, DirectiveType> map = new HashMap<String, DirectiveType>();
    map.put("name", new StringDirectiveType("Brad -"));
    map.put("title", new BooleanDirectiveType(false));
    map.put("holiday", new BooleanDirectiveType(true));
    map.put("holidayName", new StringDirectiveType("July 4th!"));

    assertEquals("   ", render("   ", map));
  }

  @Test
  public void testMapWithInvalidArguments() {
    final String template = "Hello World!";

    final Map<String, DirectiveType> map = new HashMap<String, DirectiveType>();
    map.put("name", new StringDirectiveType("  "));
    map.put("holidayName", new StringDirectiveType(null));

    assertEquals(template, render(template, map));
  }

  @Test(expected = TemplateStateException.class)
  public void testMapWithInvalidArguments2() {
    final String template = "{{name}}{{if title}}Dummy Title{{endif}}{{if title}}Dummy Title{{endif}}" +
            "{{if holiday}} Happy {{holidayName}}{{endif}}";

    final Map<String, DirectiveType> map = new HashMap<String, DirectiveType>();
    map.put("name", new StringDirectiveType("  "));
    map.put("holidayName", new StringDirectiveType(null));

    assertEquals(template, render(template, map));
  }

  @Test
  public void testSimpleCase(){
    final String template = "{{name}} -{{if title}}Dummy Title{{endif}}{{if title}}Dummy Title{{endif}}" +
            "{{if holiday}} Happy {{holidayName}}{{endif}}";

    final Map<String, DirectiveType> map = new HashMap<String, DirectiveType>();
    map.put("name", new StringDirectiveType("Brad"));
    map.put("title", new BooleanDirectiveType(false));
    map.put("holiday", new BooleanDirectiveType(true));
    map.put("holidayName", new StringDirectiveType("July 4th!"));

    assertEquals("Brad - Happy July 4th!", render(template, map));
  }

  @Test
  public void testRegularCase() {
    final String template = "{{if greeting}}Hello there, {{name}}!!{{endif}}";

    final Map<String, DirectiveType> map = new HashMap<String, DirectiveType>();
    map.put("greeting", new BooleanDirectiveType(true));
    map.put("name", new StringDirectiveType("Brad"));

    assertEquals("Hello there, Brad!!", render(template, map));
  }

  @Test
  public void testRegularCase2() {
    final String template = "{{if greeting}}Hello there, {{name}}!!{{endif}}";

    final Map<String, DirectiveType> map = new HashMap<String, DirectiveType>();
    map.put("greeting", new BooleanDirectiveType(false));
    map.put("name", new StringDirectiveType("Brad"));

    assertEquals(EMPTY_STRING, render(template, map));
  }

  @Test
  public void testRegularCase3() {
    final String template = "{{if greeting}}Hello there, {{name}}!!{{endif}}";

    final Map<String, DirectiveType> map = new HashMap<String, DirectiveType>();
    map.put("greeting", new BooleanDirectiveType(false));
    map.put("name", new StringDirectiveType("     "));

    assertEquals(EMPTY_STRING, render(template, map));
  }

  @Test
  public void testRegularCase4() {
    final String template = "{{if greeting}}Hello there, {{name}}!!{{endif}}";

    final Map<String, DirectiveType> map = new HashMap<String, DirectiveType>();
    map.put("greeting", new BooleanDirectiveType(false));
    map.put("name", null);

    assertEquals(EMPTY_STRING, render(template, map));
  }

  @Test
  public void testRegularCase5() {
    final String template = "{{if greeting}}Hello there, {{name}}!!{{endif}}";

    final Map<String, DirectiveType> map = new HashMap<String, DirectiveType>();
    map.put("greeting", new BooleanDirectiveType(true));
    map.put("name", new StringDirectiveType(EMPTY_STRING));

    assertEquals("Hello there, !!", render(template, map));
  }

  @Test
  public void testRegularCase6() {
    final String template = "{{if greeting}}Hello there, {{name}}!!{{endif}}";

    final Map<String, DirectiveType> map = new HashMap<String, DirectiveType>();
    map.put("greeting", new BooleanDirectiveType(true));
    map.put("name", new StringDirectiveType(null));

    assertEquals("Hello there, !!", render(template, map));
  }

  @Test
  public void testEmbeddedIfCase1() {
    final String template = "{{if isBirthday}}Happy Birthday{{if isFriend}} friend{{endif}}!{{endif}}";

    final Map<String, DirectiveType> map = new HashMap<String, DirectiveType>();
    map.put("isBirthday", new BooleanDirectiveType(false));
    map.put("isFriend", new BooleanDirectiveType(true));

    assertEquals(EMPTY_STRING, render(template, map));
  }

  @Test
  public void testEmbeddedIfCase2() {
    final String template = "{{if isBirthday}}Happy Birthday{{if isFriend}} friend{{endif}}!{{endif}}";

    final Map<String, DirectiveType> map = new HashMap<String, DirectiveType>();
    map.put("isBirthday", new BooleanDirectiveType(false));
    map.put("isFriend", new BooleanDirectiveType(false));

    assertEquals(EMPTY_STRING, render(template, map));
  }

  @Test
  public void testEmbeddedIfCase3() {
    final String template = "{{if isBirthday}}Happy Birthday{{if isFriend}} friend{{endif}}!{{endif}}";

    final Map<String, DirectiveType> map = new HashMap<String, DirectiveType>();
    map.put("isBirthday", new BooleanDirectiveType(true));
    map.put("isFriend", new BooleanDirectiveType(true));

    assertEquals("Happy Birthday friend!", render(template, map));
  }

  @Test
  public void testEmbeddedIfCase4() {
    final String template = "{{if isBirthday}}Happy Birthday{{if isFriend}} friend{{endif}}!{{endif}}";

    final Map<String, DirectiveType> map = new HashMap<String, DirectiveType>();
    map.put("isBirthday", new BooleanDirectiveType(true));
    map.put("isFriend", new BooleanDirectiveType(false));

    assertEquals("Happy Birthday!", render(template, map));
  }

  @Test
  public void testLongCase() {
    final String template = "Hello World! I am {{name}} and I wrote this sexy template rendering engine." +
            "{{if background}} I went to GT and now I work at {{company}}.{{endif}}{{if story}} Here's a nice " +
            "short little story: {{if mainStory}}It all started in a galaxy far, far, away...{{endif}} {{if otherStory}}" +
            "Nah jk no story for ya{{endif}}{{endif}} Goodbye!";

    final Map<String, DirectiveType> map = new HashMap<String, DirectiveType>();
    map.put("name", new StringDirectiveType("Brad Ware"));
    map.put("company", new StringDirectiveType("Facebook"));
    map.put("background", new BooleanDirectiveType(true));
    map.put("story", new BooleanDirectiveType(true));
    map.put("mainStory", new BooleanDirectiveType(true));
    map.put("otherStory", new BooleanDirectiveType(true));

    assertEquals("Hello World! I am Brad Ware and I wrote this sexy template rendering engine. " +
            "I went to GT and now I work at Facebook. Here's a nice short little story: It all started in a galaxy far," +
            " far, away... Nah jk no story for ya Goodbye!",
            render(template, map));
  }

  @Test
  public void testLongCase2() {
    final String template = "Hello World! I am {{name}} and I wrote this sexy template rendering engine." +
            "{{if background}} I went to GT and now I work at {{company}}.{{endif}}{{if story}} Here's a nice " +
            "short little story: {{if mainStory}}It all started in a galaxy far, far, away...{{endif}} {{if otherStory}}" +
            "Nah jk no story for ya{{endif}}{{endif}} Goodbye!";

    final Map<String, DirectiveType> map = new HashMap<String, DirectiveType>();
    map.put("name", new StringDirectiveType("Brad Ware"));
    map.put("company", new StringDirectiveType("Facebook"));
    map.put("background", new BooleanDirectiveType(false));
    map.put("story", new BooleanDirectiveType(true));
    map.put("mainStory", new BooleanDirectiveType(true));
    map.put("otherStory", new BooleanDirectiveType(true));

    assertEquals("Hello World! I am Brad Ware and I wrote this sexy template rendering engine. " +
            "Here's a nice short little story: It all started in a galaxy far," +
            " far, away... Nah jk no story for ya Goodbye!",
            render(template, map));
  }

  @Test
  public void testLongCase3() {
    final String template = "Hello World! I am {{name}} and I wrote this sexy template rendering engine." +
            "{{if background}} I went to GT and now I work at {{company}}.{{endif}}{{if story}} Here's a nice " +
            "short little story: {{if mainStory}}It all started in a galaxy far, far, away...{{endif}} {{if otherStory}}" +
            "Nah jk no story for ya{{endif}}{{endif}} Goodbye!";

    final Map<String, DirectiveType> map = new HashMap<String, DirectiveType>();
    map.put("name", new StringDirectiveType("Brad Ware"));
    map.put("company", new StringDirectiveType("Woo"));
    map.put("background", new BooleanDirectiveType(true));
    map.put("story", new BooleanDirectiveType(false));
    map.put("mainStory", new BooleanDirectiveType(false));
    map.put("otherStory", new BooleanDirectiveType(false));

    assertEquals("Hello World! I am Brad Ware and I wrote this sexy template rendering engine." +
            " I went to GT and now I work at Woo.  Goodbye!",
            render(template, map));
  }

  @Test
  public void testLongCase4() {
    final String template = "Hello World! I am {{name}} and I wrote this sexy template rendering engine." +
            "{{if background}} I went to GT and now I work at {{company}}.{{endif}}{{if story}} Here's a nice " +
            "short little story: {{if mainStory}}It all started in a galaxy far, far, away...{{endif}} {{if otherStory}}" +
            "Nah jk no story for ya{{endif}}{{endif}} Goodbye!";

    final Map<String, DirectiveType> map = new HashMap<String, DirectiveType>();
    map.put("name", new StringDirectiveType("Brad Ware"));
    map.put("company", new StringDirectiveType(null));
    map.put("background", new BooleanDirectiveType(false));
    map.put("story", new BooleanDirectiveType(true));
    map.put("mainStory", new BooleanDirectiveType(false));
    map.put("otherStory", new BooleanDirectiveType(true));

    assertEquals(
        "Hello World! I am Brad Ware and I wrote this sexy template rendering engine. Here's a nice short " +
                "little story:  Nah jk no story for ya Goodbye!",
            render(template, map));
  }

  @Test
  public void testLongCase5() {
    final String template = "Hello World! I am {{name}} and I wrote this sexy template rendering engine." +
            "{{if background}} I went to GT and now I work at {{company}}.{{endif}}{{if story}} Here's a nice " +
            "short little story: {{if mainStory}}It all started in a galaxy far, far, away...{{endif}} {{if otherStory}}" +
            "Nah jk no story for ya{{endif}}{{endif}} Goodbye!";

    final Map<String, DirectiveType> map = new HashMap<String, DirectiveType>();
    map.put("name", new StringDirectiveType("  "));
    map.put("company", new StringDirectiveType(null));
    map.put("background", new BooleanDirectiveType(false));
    map.put("story", new BooleanDirectiveType(true));
    map.put("mainStory", new BooleanDirectiveType(true));
    map.put("otherStory", new BooleanDirectiveType(false));

    assertEquals(
            "Hello World! I am    and I wrote this sexy template rendering engine. Here's a nice short " +
                    "little story: It all started in a galaxy far, far, away...  Goodbye!",
            render(template, map));
  }

  @Test
  public void testLongCase6() {
    final String template = "Hello World! I am {{name}} and I wrote this sexy template rendering engine." +
            "{{if background}} I went to GT and now I work at {{company}}.{{endif}}{{if story}} Here's a nice " +
            "short little story: {{if mainStory}}It all started in a galaxy far, far, away...{{endif}} {{if otherStory}}" +
            "Nah jk no story for ya{{endif}}{{endif}} Goodbye!";

    final Map<String, DirectiveType> map = new HashMap<String, DirectiveType>();
    map.put("name", new StringDirectiveType(EMPTY_STRING));
    map.put("company", new StringDirectiveType("FB"));
    map.put("background", new BooleanDirectiveType(true));
    map.put("story", new BooleanDirectiveType(true));
    map.put("mainStory", new BooleanDirectiveType(false));
    map.put("otherStory", new BooleanDirectiveType(false));

    assertEquals(
            "Hello World! I am  and I wrote this sexy template rendering engine. I went to GT and now I work at FB. Here's a nice short " +
                    "little story:   Goodbye!",
            render(template, map));
  }

  @Test(expected = TemplateStateException.class)
  public void testLongCase7() {
    final String template = "Hello World! I am {{name}} and I wrote this sexy template rendering engine." +
            "{{if background}} I went to GT and now I work at {{company}}.{{endif}}{{if story}} Here's a nice " +
            "short little story: {{if mainStory}}It all started in a galaxy far, far, away...{{endif}} {{if otherStory}}" +
            "Nah jk no story for ya{{endif}}{{endif}} Goodbye!";

    final Map<String, DirectiveType> map = new HashMap<String, DirectiveType>();
    map.put("name", new StringDirectiveType(EMPTY_STRING));
    map.put("background", new BooleanDirectiveType(true));
    map.put("otherStory", new BooleanDirectiveType(false));

    assertEquals(
            "Hello World! I am  and I wrote this sexy template rendering engine. I went to GT and now I work at FB. Here's a nice short " +
                    "little story:   Goodbye!",
            render(template, map));
  }

  @Test(expected = TemplateStateException.class)
  public void testLongCase8() {
    final String template = "Hello World! I am {{name}} and I wrote this sexy template rendering engine." +
            "{{if background}} I went to GT and now I work at {{company}}.{{endif}}{{if story}} Here's a nice " +
            "short little story: {{if mainStory}}It all started in a galaxy far, far, away...{{endif}} {{if otherStory}}" +
            "Nah jk no story for ya{{endif}}{{endif}} Goodbye!";

    final Map<String, DirectiveType> map = new HashMap<String, DirectiveType>();
    map.put("name", new StringDirectiveType("Brad Ware"));
    map.put("company", null);
    map.put("background", new BooleanDirectiveType(true));
    map.put("story", new BooleanDirectiveType(true));
    map.put("mainStory", new BooleanDirectiveType(true));
    map.put("otherStory", new BooleanDirectiveType(true));

    assertEquals("Hello World! I am Brad Ware and I wrote this sexy template rendering engine. " +
            "I went to GT and now I work at Facebook. Here's a nice short little story: It all started in a galaxy far," +
            " far, away... Nah jk no story for ya Goodbye!",
            render(template, map));
  }
}
