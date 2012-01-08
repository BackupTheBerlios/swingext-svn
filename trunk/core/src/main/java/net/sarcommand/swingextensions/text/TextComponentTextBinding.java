package net.sarcommand.swingextensions.text;

import net.sarcommand.swingextensions.binding.UpdatePolicy;
import net.sarcommand.swingextensions.internal.SwingExtLogger;
import net.sarcommand.swingextensions.internal.SwingExtLogging;

import javax.swing.text.JTextComponent;

import static java.lang.String.format;

/**
 * SwingBinding that will connect a generic JTextComponent to an arbitrary java bean. This implementation offers two
 * 'modes' for the event updates to be fired: <li>Normally, the bean will be updated whenever the user leaves the text
 * component. In case of a JTextField, firing an actionPerformed(...) event (for instance by pressing 'enter') will also
 * cause an update</li> <li>Alternatively, you can choose 'continuousUpdates'. In this case, a DocumentListener will be
 * installed and the java bean will be updated on every keystroke.</li>
 * <p/>
 * See the JavaDoc for the SwingBinding class for details on how the binding is applied. <hr/> Copyright 2006-2012
 * Torsten Heup
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * @see net.sarcommand.swingextensions.binding.SwingBinding
 */
public class TextComponentTextBinding extends AbstractTextComponentBinding<String> {
    private static final SwingExtLogger __log = SwingExtLogging.getLogger(TextComponentTextBinding.class);

    /**
     * The client property used to register this binding with the component.
     */
    public static final String CLIENT_PROPERTY = "swingext.TextComponentTextBindingClientProperty";

    /**
     * Creates a new TextComponentTextBinding.
     *
     * @param targetComponent     The text component this binding applies to. Non-null.
     * @param targetBean          The java bean this binding applies to. Non-null.
     * @param keypath             The keypath used to obtain the java bean's bound value. Non-null.
     * @param updatePolicy        A flag determining when the component should update the bean and vice versa.
     * @param ignoreAccessControl Determines whether normal java access control should be ignored. Default: false.
     */
    public TextComponentTextBinding(final JTextComponent targetComponent, final Object targetBean,
                                    final String keypath, final UpdatePolicy updatePolicy,
                                    final boolean ignoreAccessControl) {
        super(targetComponent, targetBean, keypath, updatePolicy, ignoreAccessControl);
    }

    /**
     * Creates a new TextComponentTextBinding. It will update the bean whenever the component loses focus.
     * 'ignoreAccessControl' will be set to false.
     *
     * @param targetComponent The text component this binding applies to. Non-null.
     * @param targetBean      The java bean this binding applies to. Non-null.
     * @param keypath         The keypath used to obtain the java bean's bound value. Non-null.
     */
    public TextComponentTextBinding(final JTextComponent targetComponent, final Object targetBean,
                                    final String keypath) {
        super(targetComponent, targetBean, keypath, UpdatePolicy.ON_FOCUS_LOST, false);
    }

    /**
     * Creates a new TextComponentTextBinding. 'ignoreAccessControl' will be set to false.
     *
     * @param targetComponent The text component this binding applies to. Non-null.
     * @param targetBean      The java bean this binding applies to. Non-null.
     * @param keypath         The keypath used to obtain the java bean's bound value. Non-null.
     * @param updatePolicy    A flag determining when the component should update the bean and vice versa.
     */
    public TextComponentTextBinding(final JTextComponent targetComponent, final Object targetBean,
                                    final String keypath, final UpdatePolicy updatePolicy) {
        super(targetComponent, targetBean, keypath, updatePolicy, true);
    }

    /**
     * Changes the text component according to the results obtained by the underlying keypath.
     *
     * @param canResolve Whether or not the keypath was resolvable. If false, the component will become disabled.
     * @param canSet     Whether or not the keypath supports the set operation. If false, the component will be
     *                   uneditable.
     * @param newValue   The new text value to set to the component.
     */
    protected void adaptComponent(final boolean canResolve, final boolean canSet, final String newValue) {
        Runnable runnable = new Runnable() {
            public void run() {
                _ownUpdate = true;
                _component.setText(newValue);
                _component.setEnabled(canResolve);
                _component.setEditable(canSet);
                _ownUpdate = false;
            }
        };
        dispatchOnEDT(runnable);
    }

    /**
     * @see net.sarcommand.swingextensions.binding.SwingBinding#componentChanged()
     */
    public void componentChanged() {
        if (_ownUpdate)
            return;
        final String text = _component.getText();
        __log.trace(format("%s: Target component changed, new value is %s", this, text));
        _ownUpdate = true;
        _path.set(_targetBean, text);
        _ownUpdate = false;
    }

    /**
     * Returns the client property under which this binding registers.
     *
     * @return the client property under which this binding registers.
     */
    protected String getClientProperty() {
        return CLIENT_PROPERTY;
    }
}
