package ch.usi.si.seart.maven.git;

import org.apache.maven.plugin.logging.Log;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.submodule.SubmoduleWalk;

import java.io.IOException;
import java.nio.file.Path;

final class RecursiveSubmoduleUpdater {

    private final Git git;
    private final Log log;
    private final Path root;

    RecursiveSubmoduleUpdater(Git git, Log log) {
        this.git = git;
        this.log = log;
        this.root = git.getRepository().getWorkTree().toPath().toAbsolutePath();
    }

    void update() throws GitAPIException, IOException {
        Repository repository = git.getRepository();
        git.submoduleInit().call();
        git.submoduleUpdate().call();
        update(repository);
    }

    private void update(Repository repository) throws GitAPIException, IOException {
        try (SubmoduleWalk walk = SubmoduleWalk.forIndex(repository)) {
            while (walk.next()) {
                try (Repository submodule = walk.getRepository()) {
                    Path absolute = submodule.getWorkTree().toPath().toAbsolutePath();
                    log.info("Entering '" + root.relativize(absolute) + "'");
                    Git git = Git.wrap(submodule);
                    git.submoduleInit().call();
                    git.submoduleUpdate().call();
                    update(submodule);
                }
            }
        }
    }
}
