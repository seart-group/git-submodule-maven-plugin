package ch.usi.si.seart.maven.git;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.eclipse.jgit.api.Git;

/**
 * This goal is used to initialise and update all {@code git} submodules in a Maven project. It is a must when building
 * for release. Typically, the {@code maven-release-plugin} will not include submodules when cloning the repository in
 * the sandbox environment during release builds. With this plugin, the submodules will be updated at the beginning of
 * the build process. As a result, one doesn't need to resort to writing custom scripts to update submodules.
 *
 * @see <a href="https://git-scm.com/docs/git-submodule">Git Command Documentation</a>
 * @author Ozren Dabić
 */
@Mojo(name = "update", defaultPhase = LifecyclePhase.INITIALIZE)
public class GitSubmoduleUpdateMojo extends GitSubmoduleMojo {

    @Override
    public void execute(Git git) throws Exception {
        new RecursiveSubmoduleUpdater(git, getLog()).update();
    }
}
