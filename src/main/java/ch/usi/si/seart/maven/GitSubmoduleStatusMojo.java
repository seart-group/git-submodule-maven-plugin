package ch.usi.si.seart.maven;

import org.apache.maven.plugins.annotations.Mojo;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.submodule.SubmoduleStatus;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

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
            String sha = status.getHeadId().getName();
            String message = String.format("%s\t%s", sha, relative);
            getLog().info(message);
        }
    }
}
