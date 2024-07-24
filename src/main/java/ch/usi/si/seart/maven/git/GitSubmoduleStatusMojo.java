package ch.usi.si.seart.maven.git;

import org.apache.maven.plugins.annotations.Mojo;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.submodule.SubmoduleStatus;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

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
        Repository repository = git.getRepository();
        Path root = repository.getWorkTree().toPath().toAbsolutePath();
        Map<String, SubmoduleStatus> statuses = git.submoduleStatus().call();
        for (SubmoduleStatus status : statuses.values()) {
            Path absolute = Paths.get(status.getPath()).toAbsolutePath();
            Path relative = root.relativize(absolute);
            String message = formatSHA(status) + " " + relative;
            getLog().info(message);
        }
    }

    private static String formatSHA(SubmoduleStatus status) {
        String sha = status.getHeadId().getName();
        switch (status.getType()) {
            case UNINITIALIZED:
                return "-" + sha;
            case REV_CHECKED_OUT:
                return "+" + sha;
            default:
                return " " + sha;
        }
    }
}
