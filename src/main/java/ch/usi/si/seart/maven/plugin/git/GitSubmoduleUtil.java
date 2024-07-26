package ch.usi.si.seart.maven.plugin.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;

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
}
