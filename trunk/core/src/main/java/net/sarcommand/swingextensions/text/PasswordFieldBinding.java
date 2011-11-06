package net.sarcommand.swingextensions.text;

import net.sarcommand.swingextensions.binding.UpdatePolicy;
import net.sarcommand.swingextensions.internal.SwingExtLogger;
import net.sarcommand.swingextensions.internal.SwingExtLogging;

import javax.swing.*;
import static java.lang.String.format;

/**
 * <hr/> Copyright 2006-2009 Torsten Heup
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
public class PasswordFieldBinding extends AbstractTextComponentBinding<char[]> {
    private static final SwingExtLogger __log = SwingExtLogging.getLogger(PasswordFieldBinding.class);

    /**
     * The client property used to register this binding with the component.
     */
    public static final String CLIENT_PROPERTY = "swingext.PasswordFieldBindingClientProperty";

    public PasswordFieldBinding(final JPasswordField targetComponent, final Object targetBean, final String keypath,
                                final UpdatePolicy updatePolicy, final boolean ignoreAccessControl) {
        super(targetComponent, targetBean, keypath, updatePolicy, ignoreAccessControl);
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
