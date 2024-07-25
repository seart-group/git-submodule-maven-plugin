package ch.usi.si.seart.maven.plugin.git;

import org.apache.maven.plugin.logging.Log;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.submodule.SubmoduleStatus;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

final class RecursiveSubmoduleStatusReporter {

    private final Git git;
    private final Log log;
    private final Path root;

    RecursiveSubmoduleStatusReporter(Git git, Log log) {
        this.git = git;
        this.log = log;
        this.root = git.getRepository().getWorkTree().toPath().toAbsolutePath();
    }

    void status() throws GitAPIException {
        status(git);
    }

    private void status(Git git) throws GitAPIException {
        git.submoduleStatus().call().values().stream()
                .sorted(Comparator.comparing(SubmoduleStatus::getPath))
                .forEach(this::status);
    }

    private void status(SubmoduleStatus status) {
        Path absolute = Paths.get(status.getPath()).toAbsolutePath();
        Path relative = root.relativize(absolute);
        String message = formatSHA(status) + " " + relative;
        log.info(message);
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
