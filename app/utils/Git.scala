package translator.utils

import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepository
import translator.ProjectContext

object git {

  def WithRepo(ctx: ProjectContext[_])(f: Git => String): String = {
    val repo = new FileRepository("/home/dennis/projects/foo/.git")
    val git = new Git(repo, true)

    f(git)
  }

  class Git(repo: Repository, debug: Boolean = false) {

    import org.eclipse.jgit.api._

    val api = new org.eclipse.jgit.api.Git(repo)

    def currentBranch =
      cleanupBranch(repo.getFullBranch)

    def checkout(branch: String, create: Boolean = false) =
      api.checkout.setName(branch).setCreateBranch(create).call

    def add(pattern: String) =
      api.add.addFilepattern(pattern).call

    def commit(message: String) =
      api.commit.setMessage(message).call

    private def cleanupBranch(branch: String) =
      branch.replace("refs/heads/", "")
  }
}
