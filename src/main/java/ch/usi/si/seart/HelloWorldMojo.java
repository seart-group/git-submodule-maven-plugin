package ch.usi.si.seart;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "hello-world")
public class HelloWorldMojo extends AbstractMojo {

    @Override
    public void execute() {
        getLog().info("Hello World!");
    }
}
