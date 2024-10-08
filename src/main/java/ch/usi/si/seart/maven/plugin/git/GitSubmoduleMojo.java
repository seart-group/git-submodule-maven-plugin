package ch.usi.si.seart.maven.plugin.git;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.jgit.api.Git;

import java.io.File;

abstract class GitSubmoduleMojo extends AbstractMojo {

    /**
     * When set to {@code true} the plugin execution will completely skip. This is useful for profile-activated plugin
     * invocations or to use properties to enable/disable pom features.
     * <p>
     * By default, the execution is not skipped (set to {@code false}).
     * <p>
     * Example:
     * <pre>{@code
     * <skip>false</skip>
     * }</pre>
     */
    @Parameter(defaultValue = "false")
    boolean skip;

    /**
     * When set to {@code true} the plugin will produce execution output. By default, the output is <strong>not</strong>
     * verbose. Note that some goals (such as {@link GitSubmoduleStatusMojo status}) may override this setting, since
     * execution without any output is pointless.
     * <p>
     * Example:
     * <pre>{@code
     * <verbose>false</verbose>
     * }</pre>
     */
    @Parameter(defaultValue = "false")
    boolean verbose;

    /**
     * Configuration used to indicate to the plugin about the root directory of the {@code git} repository we want to
     * check. By default, uses {@code ${project.basedir}/.git}. Should work for most single-module projects. For Maven
     * modules, use `../` to get higher up in the directory tree (for example {@code ${project.basedir}/../.git}).
     * <p>
     * Example:
     * <pre>{@code
     * <dotGitDirectory>
     *   ${project.basedir}/.git
     * </dotGitDirectory>
     * }</pre>
     */
    @Parameter(defaultValue = "${project.basedir}/.git")
    File dotGitDirectory;

    /**
     * @return {@code true} if the goal is executed in verbose mode, {@code false} otherwise.
     */
    public boolean isVerbose() {
        return verbose;
    }

    @Override
    public final void execute() throws MojoExecutionException {
        Log log = getLog();

        if (skip) {
            if (verbose) log.info("Skipping execution of plugin.");
            return;
        }

        if (!dotGitDirectory.exists()) {
            if (verbose) log.info(
                    "No .git directory found at: " +
                    dotGitDirectory.getParent() +
                    ". Skipping execution of plugin."
            );
            return;
        }

        try (Git git = Git.open(dotGitDirectory)) {
            execute(git);
        } catch (Exception ex) {
            throw new MojoExecutionException(ex.getMessage(), ex);
        }
    }

    /**
     * Perform the actual work of a plugin goal. Implementors overriding this method shouldn't concern themselves with
     * closing the {@link Git} instance, as this is automatically performed once this method returns.
     *
     * @param git the {@code git} repository to work with.
     * @throws Exception if an error occurs during execution.
     */
    protected abstract void execute(Git git) throws Exception;
}
