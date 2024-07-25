# Git Submodule Maven Plugin &middot; [![GitHub Workflow Status (with event)](https://img.shields.io/github/actions/workflow/status/seart-group/git-submodule-maven-plugin/package.yml)](https://github.com/seart-group/git-submodule-maven-plugin/actions/workflows/package.yml) [![Maven Central](https://img.shields.io/maven-central/v/ch.usi.si.seart/git-submodule-maven-plugin)](https://central.sonatype.com/artifact/ch.usi.si.seart/git-submodule-maven-plugin) [![javadoc](https://javadoc.io/badge2/ch.usi.si.seart/git-submodule-maven-plugin/javadoc.svg)](https://javadoc.io/doc/ch.usi.si.seart/git-submodule-maven-plugin) [![MIT license](https://img.shields.io/github/license/seart-group/git-submodule-maven-plugin)](https://github.com/seart-group/git-submodule-maven-plugin/blob/master/LICENSE)

This plugin is used to initialise `git` submodules in Maven projects. An absolute life-saver when performing releases.

## Rationale

When working with Maven projects that have `git` submodules, it is often necessary to clone the submodules before
building. This is especially true when performing releases, as the submodules aren't initialised by the
`maven-release-plugin` when building in a sandbox. This is a problem we've faced regularly in the past, most notably
with our [java-tree-sitter](https://github.com/seart-group/java-tree-sitter) project, and more recently with
[jcloc](https://github.com/seart-group/jcloc). The only [solution](https://stackoverflow.com/q/6938142/17173324)
proposed by the community involved the use of the `exec-maven-plugin` to run a recursive initialisation and update of
submodules within a project. Although viable, frequently copying and pasting the same configuration across projects
tends to get annoying after the first few times. This is without mentioning the technical debt that would be incurred by
having to maintain the same configuration in multiple projects. This project was born out a necessity to provide a more
Maven-like, drop-in solution to the described problem.

## Requirements

* Java ${java.version} or later
* Maven 3.6.3 or later

> [!NOTE]
> Although it operates on Git submodules, this plugin doesn't require `git` to be installed on the system. Instead, it
> uses the Eclipse [JGit](https://www.eclipse.org/jgit/) library to interact with the VCS through Java. As a result, the
> plugin will still work in containerised environments such as Docker containers and CI/CD pipelines.

## Usage

Add the following to the `plugins` section of your `pom.xml`:

```xml
<plugin>
  <groupId>${groupId}</groupId>
  <artifactId>${artifactId}</artifactId>
  <version>${project.version}</version>
  <!-- configurations and executions go here -->  
</plugin>
```

## Goals

### `status`

Equivalent to running `git submodule status`. This goal is intended primarily for debugging purposes. You can execute it
directly from the command line:

```shell
mvn ${name}:status
```

### `update`

Equivalent to running `git submodule update --init --recursive`. Recursively initialises and updates all submodules in
the project. To use this goal in your build, write your plugin definition as follows:

```xml
<plugin>
  <groupId>${groupId}</groupId>
  <artifactId>${artifactId}</artifactId>
  <version>${project.version}</version>
  <executions>
    <execution>
      <goals>
        <goal>update</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```

The default execution `phase` of this goal is `initialize`, but you can change it according to your needs.

## Configuration

At the moment, the plugin supports the following configuration options:

| Configuration     | Default Value             | Description                                      |
|-------------------|---------------------------|--------------------------------------------------|
| `skip`            | `false`                   | Skips the execution of the plugin.               |
| `verbose`         | `false`                   | Print additional execution information.          |
| `dotGitDirectory` | `\${project.basedir}/.git` | The path to the `.git` directory in the project. |

## FAQ

### How can I request a feature or ask a question?

If you have ideas for a feature that you would like to see implemented or if you have any questions, we encourage you to
create a new [discussion](${url}/discussions). By initiating a discussion, you can engage with the community and our
team, and we will respond promptly to address your queries or consider your feature requests.

### How can I report a bug?

To report any issues or bugs you encounter, create a [new issue](${url}/issues). Providing detailed information about
the problem you're facing will help us understand and address it more effectively. Rest assured, we're committed to
promptly reviewing and responding to the issues you raise, working collaboratively to resolve any bugs and improve the
overall user experience.

### How do I contribute to the project?

Refer to [CONTRIBUTING.md](/CONTRIBUTING.md) for more information.
