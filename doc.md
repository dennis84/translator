# API Classes

## UserAPI

    # On a project secured admin page on every request this method will called
    # and the user by project will get determined.
    def by(project: Project): Option[User]
    
    # On a project secured admin page on every request this method will called
    # and the user by project will get determined.
    def by(username: String): Option[User]

    # Lists the users by project in application.
    def contributors(p: Project): List[User]

    # Creates a new user in application.
    def create(
      project: Project,
      username: String,
      password: String,
      roles: List[String]): Option[User]

    # Updates a user password in application.
    def update(
      before: User,
      password: String): User

## LanguageAPI

    # @FIXME
    def by(id: ObjectId, p: Project): Option[Language]

    # To list the languages in application admin.
    def list(project: Project): List[Language]

    # @FIXME
    def first(project: Project): Option[Language]

    # @FIXME
    def code(project: Project, code: String): Option[String]

    # To create a language in application admin.
    def create(code: String, name: String, project: Project): Option[Language]

    # To update a language in application admin.
    def update(lang: Language, code: String, name: String): Language

## ProjectAPI

    # @FIXME
    def by(token: String)

    # @FIXME
    def listMine(user: User)

    # To creates another project in application admin.
    def create(name: String, u: User)

    # To signup a new project in application frontend.
    def signup(projectName: String, username: String, password: String)

## TranslationAPI

    # @FIXME
    def by(id: ObjectId)

    # To fetch the updated translation in application.
    def entry(id: ObjectId, project: Project)

    # @FIXME
    def export(project: Project, c: String): List[(String, String)]

    # To list all master translations in application.
    def entries(filter: Filter)(implicit ctx: ProjectContext[_])

    # @FIXME
    def list(project: Project)

    # To list all translations by master translation in application.
    def list(project: Project, name: String)

    # @FIXME
    def activatable(project: Project, name: String)

    # @FIXME
    def search(project: Project, term: String)

    # Creates a new translation in application.
    def create(c: String, name: String, text: String)(implicit ctx: ProjectContext[_])

    # Updated a translation in application.
    def update(before: Translation, text: String)

    # Activates a translation in application and removes the last activated.
    def switch(user: User, project: Project, id: ObjectId)

    # Deletes a translation in application.
    def delete(project: Project, id: ObjectId)

    # @FIXME
    def imports(project: Project, user: User, content: String, t: String, code: String)

