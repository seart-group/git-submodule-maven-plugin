package ch.usi.si.seart.maven.plugin.git;

import org.apache.maven.plugins.annotations.Mojo;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.submodule.SubmoduleStatus;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        Map<String, SubmoduleStatus> map = git.submoduleStatus().call();
        List<SubmoduleStatus> statuses = map.values().stream()
                .sorted(Comparator.comparing(SubmoduleStatus::getPath))
                .collect(Collectors.toList());
        for (SubmoduleStatus status : statuses) {
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
