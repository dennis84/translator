# What is git-database
> The git-database is our backup system. And gives the contributors a secure way
  to make changes.

# Where the files will saved?
> In your Github Account. We don't host git repos.

# When the files will saved?
> Git is not our major database. It's to sync the translations on a remote
  server and the admins can look in their data. An other feature is the version
  handling for contributors.

# Version handling
> Make tags to create a version of all translations.

# Contributor Workflow
  1. A user can start a new version.
    1.1 Make a fork if not exists.
    1.2 Make a new branch

  2. He makes his changes

  3. He Stops the version
    3.1 Adds, Commits and Pushs his changes.

  4. The admin can view a diff of all changes in translation app in our style.

  5. The admin can accept now
    5.a In the translation app
      5.a.1 Merge the branch
      5.a.2 Import the translations in db. All translations will set the status
            to active.
    5.b In github
      5.a.1 Merge the branch
      5.b.1 Manually import the latest git translations

# An schema example
> The saved schema for translations can looks like the following json. One pro
  is the easy style you can look in one translation and you will see all other
  translations for the different languages. One con is this format is not usable
  for translation readers, this files must be exported by translation app.
  Another pro is that format can save the count of files, git will get very slow
  if the files increases too much. One file for all makes the chance for merge
  conflicts higher.

    /acme/translations/hello_world.json
    {
      "hello_world": [
        {
          "code": "en",
          "text": "Hello World",
          "author": "d.dietrich@gmail.com",
          "status": "active"
        },
        {
          "code": "de",
          "text": "Hallo Welt",
          "author": "d.dietrich@gmail.com",
          "status": "active"
        }
      ]
    }
