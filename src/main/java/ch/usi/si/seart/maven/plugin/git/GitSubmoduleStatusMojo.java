package ch.usi.si.seart.maven.plugin.git;

import org.apache.maven.plugins.annotations.Mojo;
import org.eclipse.jgit.api.Git;

/**
 * This goal is used to display the status of all {@code git} submodules in a Maven project.
 *
 * @see <a href="https://git-scm.com/docs/git-submodule">Git Command Documentation</a>
 * @author Ozren Dabić
 */
@Mojo(name = "status")
public class GitSubmoduleStatusMojo extends GitSubmoduleMojo {

    @Override
    protected void execute(Git git) throws Exception {
        new RecursiveSubmoduleStatusReporter(git, getLog()).status();
    }
}
