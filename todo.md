! Important
? Is this needed?
* Must not have
$ Code mistakes
~ Optional

----

$ Prevent for js object zombies. Make a view renderer which removes the current views before render the new one.
$ TranslationAPI.entry: Do not load all translation, only the childs of translation by id
~ Scala 2.10 (Better wait for full Salat support)
! version 1.0 bugfixing
? If a entry with existig name will created then the new one will pasted to the existing collection as an inactive. In the view the translation will dropped under the other entries thats a bug in all cases.
! Update project if navigating to. The statistic changes will not shown if navigate back to project.
