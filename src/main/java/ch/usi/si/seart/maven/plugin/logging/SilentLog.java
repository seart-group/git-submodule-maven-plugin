package ch.usi.si.seart.maven.plugin.logging;

import org.apache.maven.plugin.logging.Log;

/**
 * A logger that does nothing.
 *
 * @author Ozren Dabić
 */
public class SilentLog implements Log {

    /**
     * @return <code>false</code>
     * @see org.apache.maven.plugin.logging.Log#isDebugEnabled()
     */
    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    /**
     * By default, do nothing.
     *
     * @see org.apache.maven.plugin.logging.Log#debug(java.lang.CharSequence)
     */
    @Override
    public void debug(CharSequence content) {
    }

    /**
     * By default, do nothing.
     *
     * @see org.apache.maven.plugin.logging.Log#debug(java.lang.CharSequence, java.lang.Throwable)
     */
    @Override
    public void debug(CharSequence content, Throwable error) {
    }

    /**
     * By default, do nothing.
     *
     * @see org.apache.maven.plugin.logging.Log#debug(java.lang.Throwable)
     */
    @Override
    public void debug(Throwable error) {
    }

    /**
     * @return <code>false</code>
     * @see org.apache.maven.plugin.logging.Log#isInfoEnabled()
     */
    @Override
    public boolean isInfoEnabled() {
        return false;
    }

    /**
     * By default, do nothing.
     *
     * @see org.apache.maven.plugin.logging.Log#info(java.lang.CharSequence)
     */
    @Override
    public void info(CharSequence content) {
    }

    /**
     * By default, do nothing.
     *
     * @see org.apache.maven.plugin.logging.Log#info(java.lang.CharSequence, java.lang.Throwable)
     */
    @Override
    public void info(CharSequence content, Throwable error) {
    }

    /**
     * By default, do nothing.
     *
     * @see org.apache.maven.plugin.logging.Log#info(java.lang.Throwable)
     */
    @Override
    public void info(Throwable error) {
    }

    /**
     * By default, do nothing.
     *
     * @see org.apache.maven.plugin.logging.Log#isWarnEnabled()
     */
    @Override
    public boolean isWarnEnabled() {
        // nop
        return false;
    }

    /**
     * By default, do nothing.
     *
     * @see org.apache.maven.plugin.logging.Log#warn(java.lang.CharSequence)
     */
    @Override
    public void warn(CharSequence content) {
    }

    /**
     * By default, do nothing.
     *
     * @see org.apache.maven.plugin.logging.Log#warn(java.lang.CharSequence, java.lang.Throwable)
     */
    @Override
    public void warn(CharSequence content, Throwable error) {
    }

    /**
     * By default, do nothing.
     *
     * @see org.apache.maven.plugin.logging.Log#warn(java.lang.Throwable)
     */
    @Override
    public void warn(Throwable error) {
    }

    /**
     * @return <code>false</code>
     * @see org.apache.maven.plugin.logging.Log#isErrorEnabled()
     */
    @Override
    public boolean isErrorEnabled() {
        return false;
    }

    /**
     * By default, do nothing.
     *
     * @see org.apache.maven.plugin.logging.Log#error(java.lang.CharSequence)
     */
    @Override
    public void error(CharSequence content) {
    }

    /**
     * By default, do nothing.
     *
     * @see org.apache.maven.plugin.logging.Log#error(java.lang.CharSequence, java.lang.Throwable)
     */
    @Override
    public void error(CharSequence content, Throwable error) {
    }

    /**
     * By default, do nothing.
     *
     * @see org.apache.maven.plugin.logging.Log#error(java.lang.Throwable)
     */
    @Override
    public void error(Throwable error) {
    }
}
