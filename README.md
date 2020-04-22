# Templating Engine

Given input returns a rendered output based on a set of rules.

## Dependencies

- Java 8 (1.8.0_111)
- JUnit 4 (4.12)
- Hamcrest Core (1.3)

## Template Error Handling (Bullet Point #1)

I decided to throw an exception if the template had any remaining directives after the render process was complete. I made my own custom exception called `TemplateStateException`.

However, I decided not to throw an exception if the map had extra variables that weren't defined in the template. I didn't think an exception was necessary in that case as the rendered template was still valid, and would be better to show than throwing an exception.

I did however log those scenarios as I think that edge case is important to document. Would be very happy to talk through the trade-offs of both options and why I came to this decision if you want further details or insights!

## Generic EndIf Directive (Bullet Point #2)

I updated the render function to properly parse a template with the generic endif directive - `{{endif}}`.

I however also made the assumption our templating engine would need to handle nested if directives. Here's a really simple example:

`{{if isBirthday}}Happy Birthday{{if isFriend}} friend{{endif}}!{{endif}}`

`isBirthday = true, isFriend = true` renders `Happy Birthday friend!`

`isBirthday = true, isFriend = false` renders `Happy Birthday!`

`isBirthday = false, isFriend = true` renders `''`

`isBirthday = false, isFriend = false` also renders `''`

I added in support for nested if directives to my templating engine even though that wasn't technically required. It seemed like a must have feature for a templating language and tons of UI logic operates on dependency tree of markup.

## Logging

I added in a few log (simple print in this case) statements so that I can understand how my code is behaving during the test cases. I think this is extremely useful if we actually tested this templating engine in the wild with the amount of edge and corner cases that come up.

## Next Steps

Obviously this solution is not anywhere complete. Michael said to spend about an hour on it so that's what I aimed for. Here's what my next steps would be though if I was to continue working on the templating engine:

- Add way more test cases
- Evaluate the runtime and performance of the current solution
- Parse the template (compile step) in a much more efficient manner before rendering logic is applied
- Add in more robust logging
- Look into adding more feature support:
  - Supporting more types of Directives
  - Supporting complex markup and template logic
  - Documenting clearly the directive structure supported

This just scratches the surface of the work that could be put into this project.
