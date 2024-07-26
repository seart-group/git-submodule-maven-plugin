package ch.usi.si.seart.maven.plugin.git;

import org.apache.maven.plugin.logging.Log;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.submodule.SubmoduleStatus;
import org.eclipse.jgit.submodule.SubmoduleStatusType;
import org.eclipse.jgit.submodule.SubmoduleWalk;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

final class RecursiveSubmoduleStatusReporter {

    private final Git git;
    private final Log log;

    RecursiveSubmoduleStatusReporter(Git git, Log log) {
        this.git = git;
        this.log = log;
    }

    void status() throws GitAPIException, IOException {
        status("", git);
    }

    private void status(String parent, Git git) throws GitAPIException, IOException {
        Repository repository = git.getRepository();
        List<SubmoduleStatus> statuses = git.submoduleStatus().call().values().stream()
                .sorted(Comparator.comparing(SubmoduleStatus::getPath))
                .collect(Collectors.toList());
        for (SubmoduleStatus status : statuses) {
            String path = status.getPath();
            ObjectId head = status.getHeadId();
            String sha = head.getName();
            SubmoduleStatusType type = status.getType();
            try (Repository submodule = SubmoduleWalk.getSubmoduleRepository(repository, path)) {
                Path relative = Paths.get(parent, path);
                String description = GitSubmoduleUtil.describe(submodule, head);
                char prefix = GitSubmoduleUtil.hasConflicts(submodule) ? 'U' : GitSubmoduleUtil.getPrefix(type);
                String message = String.format("%c%s %s (%s)", prefix, sha, relative, description);
                log.info(message);
                status(path, Git.wrap(submodule));
            }
        }
    }
}
