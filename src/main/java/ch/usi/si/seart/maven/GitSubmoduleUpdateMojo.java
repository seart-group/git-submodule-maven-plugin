package ch.usi.si.seart.maven;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.submodule.SubmoduleWalk;

import java.io.IOException;

/**
 * This goal is used to initialise and update all {@code git} submodules in a Maven project. It is a must when building
 * for release. Typically, the {@code maven-release-plugin} will not include submodules when cloning the repository in
 * the sandbox environment during release builds. With this plugin, the submodules will be updated at the beginning of
 * the build process. As a result, one doesn't need to resort to writing custom scripts to update submodules.
 *
 * @author Ozren Dabić
 */
@Mojo(name = "update", defaultPhase = LifecyclePhase.INITIALIZE)
public class GitSubmoduleUpdateMojo extends GitSubmoduleMojo {

    @Override
    public void execute(Git git) throws Exception {
        Repository repository = git.getRepository();
        git.submoduleInit().call();
        git.submoduleUpdate().call();
        traverse(repository);
    }

    private void traverse(Repository root) throws GitAPIException, IOException {
        SubmoduleWalk walk = SubmoduleWalk.forIndex(root);
        while (walk.next()) {
            Repository submodule = walk.getRepository();
            String name = submodule.getDirectory().getName();
            getLog().info("Entering '" + name + "'");
            Git git = Git.wrap(submodule);
            git.submoduleInit().call();
            git.submoduleUpdate().call();
            traverse(submodule);
        }
    }
}
