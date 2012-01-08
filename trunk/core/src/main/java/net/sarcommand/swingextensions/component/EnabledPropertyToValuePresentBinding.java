package net.sarcommand.swingextensions.component;

import net.sarcommand.swingextensions.binding.Keypath;
import net.sarcommand.swingextensions.binding.KeypathObserver;
import net.sarcommand.swingextensions.binding.SwingBinding;
import net.sarcommand.swingextensions.internal.SwingExtLogger;
import net.sarcommand.swingextensions.internal.SwingExtLogging;

import javax.swing.*;

/**
 * <hr/> Copyright 2006-2012 Torsten Heup
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
public class EnabledPropertyToValuePresentBinding extends SwingBinding {
    private static final SwingExtLogger __log = SwingExtLogging.getLogger(EnabledPropertyToValuePresentBinding.class);
    public static final String CLIENT_PROPERTY = "swingExt.EnabledPropertyToValuePresentBindingClientProperty";

    private JComponent _component;
    private Object _targetBean;
    private boolean _enableIfValuePresent;

    private Keypath _keypath;
    private KeypathObserver _observer;

    public EnabledPropertyToValuePresentBinding(final JComponent component, final Object targetBean,
                                                final String keypath, final boolean enableIfValuePresent) {
        _component = component;
        _targetBean = targetBean;
        _enableIfValuePresent = enableIfValuePresent;

        _keypath = new Keypath(keypath);
        _observer = _keypath.createObserver(targetBean, getPropertyChangeListener());

        setClientPropertyOnTarget(component);

        targetBeanChanged();
    }

    protected void targetBeanChanged() {
        final Object value = _keypath.get(_targetBean);
        if (__log.isTraceEnabled())
            __log.trace(String.format("%s: Target bean %s's value changed to %s", this, _targetBean, value));
        _component.setEnabled((value != null) == _enableIfValuePresent);
    }

    public void componentChanged() {
    }

    public void detach() {
        if (__log.isTraceEnabled())
            __log.debug(String.format("Detaching %s from %s", this, _component));
        _observer.dispose();
        _component.putClientProperty(EnabledPropertyToValuePresentBinding.CLIENT_PROPERTY, null);
    }

    protected String getClientProperty() {
        return CLIENT_PROPERTY;
    }
}
