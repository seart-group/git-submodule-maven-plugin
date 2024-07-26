package ch.usi.si.seart.maven.plugin.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.submodule.SubmoduleStatusType;

import java.io.IOException;

final class GitSubmoduleUtil {

    private GitSubmoduleUtil() {
    }

    static String describe(Repository repository, ObjectId target) throws GitAPIException, IOException {
        String result = Git.wrap(repository)
                .describe()
                .setTarget(target)
                .setTags(true)
                .call();
        String branch = repository.getBranch();
        boolean detached = ObjectId.isId(branch);
        String fallback = detached ? target.abbreviate(7).name() : "heads/" + branch;
        return result != null ? result : fallback;
    }

    static char getPrefix(SubmoduleStatusType type) {
        switch (type) {
            case INITIALIZED:
                return ' ';
            case UNINITIALIZED:
                return '-';
            case REV_CHECKED_OUT:
                return '+';
            default:
                throw new IllegalStateException("Unexpected submodule status: " + type.name().toLowerCase());
        }
    }

    static boolean hasConflicts(Repository repository) throws GitAPIException {
        if (repository == null) return false;
        return !Git.wrap(repository)
                .status()
                .call()
                .getConflicting()
                .isEmpty();
    }
}
