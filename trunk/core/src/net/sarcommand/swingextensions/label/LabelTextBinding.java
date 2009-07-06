package net.sarcommand.swingextensions.label;

import net.sarcommand.swingextensions.binding.Keypath;
import net.sarcommand.swingextensions.binding.KeypathObserver;
import net.sarcommand.swingextensions.binding.SwingBinding;
import net.sarcommand.swingextensions.internal.SwingExtLogger;
import net.sarcommand.swingextensions.internal.SwingExtLogging;

import javax.swing.*;
import static java.lang.String.format;

/**
 * <hr/> Copyright 2006-2008 Torsten Heup
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
public class LabelTextBinding extends SwingBinding {
    private static final SwingExtLogger __log = SwingExtLogging.getLogger(LabelTextBinding.class);

    public static final String CLIENT_PROPERTY = "swingext.LabelTextBindingClientProperty";

    protected Keypath<String> _path;
    protected KeypathObserver _observer;
    private final JLabel _targetLabel;
    private final Object _targetBean;

    public LabelTextBinding(final JLabel targetLabel, final Object targetBean, final String keypath,
                            final boolean ignoreAccessControl) {
        _targetLabel = targetLabel;
        _targetBean = targetBean;
        _path = new Keypath<String>(keypath, ignoreAccessControl);
        _observer = _path.createObserver(targetBean, getPropertyChangeListener());

        setClientPropertyOnTarget(targetLabel);
    }

    public LabelTextBinding(final JLabel targetLabel, final Object targetBean, final String keypath) {
        this(targetLabel, targetBean, keypath, true);
    }

    public void componentChanged() {
    }

    public void detach() {
        _observer.dispose();
    }

    protected void targetBeanChanged() {
        final String s = _path.get(_targetBean);
        __log.trace(format("%s: Target bean %s changed, new value: '%s'", this, _targetBean, s));
        final Runnable runnable = new Runnable() {
            public void run() {
                _targetLabel.setText(s);
            }
        };
        dispatchOnEDT(runnable);
    }

    protected String getClientProperty() {
        return CLIENT_PROPERTY;
    }
}
