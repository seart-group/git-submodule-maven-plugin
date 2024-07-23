package ch.usi.si.seart.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.submodule.SubmoduleWalk;

import java.io.File;
import java.io.IOException;

/**
 * This plugin is used to update all {@code git} submodules in a Maven project. It is a must when building for release.
 * Typically, the {@code maven-release-plugin} will not initialise submodules when building the release in a sandbox.
 * With this plugin, the submodules will be updated at the beginning of the build process. As a result, one doesn't need
 * to resort to writing custom scripts to update submodules.
 *
 * @author Ozren Dabić
 */
@Mojo(name = "update", defaultPhase = LifecyclePhase.INITIALIZE)
public class UpdateGitSubmodulesMojo extends AbstractMojo {

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

    @Override
    public void execute() throws MojoExecutionException {
        if (skip) {
            getLog().info("Skipping execution of plugin.");
            return;
        }

        if (!dotGitDirectory.exists()) {
            String parent = dotGitDirectory.getParent();
            getLog().info("No .git directory found at: " + parent + ". Skipping execution of plugin.");
            return;
        }

        try (Git git = Git.open(dotGitDirectory)) {
            Repository repository = git.getRepository();
            git.submoduleInit().call();
            git.submoduleUpdate().call();
            traverse(repository);
        } catch (GitAPIException | IOException ex) {
            throw new MojoExecutionException(ex.getMessage(), ex);
        }
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
