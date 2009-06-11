package net.sarcommand.swingextensions.text;

import net.sarcommand.swingextensions.binding.UpdatePolicy;
import net.sarcommand.swingextensions.internal.SwingExtLogger;
import net.sarcommand.swingextensions.internal.SwingExtLogging;

import javax.swing.*;
import static java.lang.String.format;

/**
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public class PasswordFieldBinding extends AbstractTextComponentBinding<char[]> {
    private static final SwingExtLogger __log = SwingExtLogging.getLogger(PasswordFieldBinding.class);

    /**
     * The client property used to register this binding with the component.
     */
    public static final String CLIENT_PROPERTY = "swingext.PasswordFieldBindingClientProperty";

    public PasswordFieldBinding(final JPasswordField targetComponent, final Object targetBean, final String keypath,
                                final boolean continuousUpdates, final boolean ignoreAccessControl) {
        super(targetComponent, targetBean, keypath, continuousUpdates, ignoreAccessControl);
    }

    public PasswordFieldBinding(final JPasswordField targetComponent, final Object targetBean, final String keypath) {
        super(targetComponent, targetBean, keypath);
    }

    public PasswordFieldBinding(final JPasswordField targetComponent, final Object targetBean, final String keypath,
                                final UpdatePolicy updatePolicy) {
        super(targetComponent, targetBean, keypath, updatePolicy);
    }

    @Override
    public void componentChanged() {
        if (_ownUpdate)
            return;
        final JPasswordField component = (JPasswordField) _component;
        final char[] password = component.getPassword();
        __log.trace(format("%s: Target component changed", this));
        _ownUpdate = true;
        _path.set(_targetBean, password);
        _ownUpdate = false;
    }

    protected String getClientProperty() {
        return CLIENT_PROPERTY;
    }

    protected void adaptComponent(final boolean canResolve, final boolean canSet, final char[] newValue) {
        Runnable runnable = new Runnable() {
            public void run() {
                _ownUpdate = true;
                _component.setText(newValue == null ? "" : new String(newValue));
                _component.setEnabled(canResolve);
                _component.setEditable(canSet);
                _ownUpdate = false;
            }
        };
        dispatchOnEDT(runnable);
    }
}
