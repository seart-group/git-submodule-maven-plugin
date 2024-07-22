package ch.usi.si.seart;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.junit.jupiter.api.Assertions;

public class HelloWorldMojoTest extends AbstractMojoTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void test() {
        new HelloWorldMojo().execute();
        Assertions.assertTrue(true);
    }
}
