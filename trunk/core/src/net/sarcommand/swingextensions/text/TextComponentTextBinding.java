package net.sarcommand.swingextensions.text;

import net.sarcommand.swingextensions.binding.Keypath;
import net.sarcommand.swingextensions.binding.KeypathObserver;
import net.sarcommand.swingextensions.binding.SwingBinding;
import net.sarcommand.swingextensions.event.DocumentAdapter;
import net.sarcommand.swingextensions.internal.SwingExtLogger;
import net.sarcommand.swingextensions.internal.SwingExtLogging;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import static java.lang.String.format;

/**
 * SwingBinding that will connect a generic JTextComponent to an arbitrary java bean. This implementation offers two
 * 'modes' for the event updates to be fired: <li>Normally, the bean will be updated whenever the user leaves the text
 * component. In case of a JTextField, firing an actionPerformed(...) event (for instance by pressing 'enter') will also
 * cause an update</li> <li>Alternatively, you can choose 'continuousUpdates'. In this case, a DocumentListener will be
 * installed and the java bean will be updated on every keystroke.</li>
 * <p/>
 * See the JavaDoc for the SwingBinding class for details on how the binding is applied. <hr/> Copyright 2006-2008
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
public class TextComponentTextBinding extends SwingBinding {
    private static final SwingExtLogger __log = SwingExtLogging.getLogger(TextComponentTextBinding.class);

    /**
     * The client property used to register this binding with the component.
     */
    public static final String CLIENT_PROPERTY = "swingext.TextComponentTextBindingClientProperty";

    /**
     * The observer used to track changes on the bean.
     */
    protected KeypathObserver _observer;

    /**
     * The Keypath instance used for getting and setting values from/to the bean.
     */
    protected Keypath<String> _path;

    /**
     * The component this binding is applied to.
     */
    protected final JTextComponent _component;

    /**
     * The java bean this binding is applied to.
     */
    protected final Object _targetBean;

    /**
     * An (optional) action listener that will be installed onto JTextFields.
     */
    protected ActionListener _actionListener;

    /**
     * An (optional) document listner that will be installed if 'continuousUpdate' has been selected.
     */
    protected DocumentAdapter _documentAdapter;

    /**
     * An (optional) focus listener which will be notified whenever the user leaves the text component.
     */
    protected FocusAdapter _focusAdapter;

    /**
     * An (optional) property change listener which will be notified whenever the text component's document is
     * exchanged.
     */
    protected PropertyChangeListener _documentChangeListener;

    /**
     * Volatile flag used to determine wheter an update to the bean was self-induced.
     */
    protected volatile boolean _ownUpdate;

    /**
     * Creates a new TextComponentTextBinding.
     *
     * @param targetComponent     The text component this binding applies to. Non-null.
     * @param targetBean          The java bean this binding applies to. Non-null.
     * @param keypath             The keypath used to obtain the java bean's bound value. Non-null.
     * @param continuousUpdates   A flag determining whether or not all updates to the text component should update the
     *                            target bean as well. If set to false, the bean will be updated whenever the text
     *                            component loses the focus. Default: false.
     * @param ignoreAccessControl Determines whether normal java access control should be ignored. Default: false.
     */
    public TextComponentTextBinding(final JTextComponent targetComponent, final Object targetBean,
                                    final String keypath, final boolean continuousUpdates,
                                    final boolean ignoreAccessControl) {
        if (targetComponent == null)
            throw new IllegalArgumentException("Parameter 'targetComponent' must not be null!");
        if (targetBean == null)
            throw new IllegalArgumentException("Parameter 'targetBean' must not be null!");
        if (keypath == null)
            throw new IllegalArgumentException("Parameter 'keypath' must not be null!");

        _component = targetComponent;
        _targetBean = targetBean;
        _path = new Keypath<String>(keypath, ignoreAccessControl);
        _observer = _path.createObserver(targetBean, getPropertyChangeListener());

        if (continuousUpdates)
            installContinuousUpdateListeners();
        else
            installDefaultListeners();

        adaptComponent(_path.canResolve(_targetBean), _path.canSet(_targetBean), _path.get(_targetBean));

        _component.putClientProperty(CLIENT_PROPERTY, this);
    }

    /**
     * Creates a new TextComponentTextBinding. 'continuousUpdates' and 'ignoreAccessControl' will both be set to false.
     *
     * @param targetComponent The text component this binding applies to. Non-null.
     * @param targetBean      The java bean this binding applies to. Non-null.
     * @param keypath         The keypath used to obtain the java bean's bound value. Non-null.
     */
    public TextComponentTextBinding(final JTextComponent targetComponent, final Object targetBean,
                                    final String keypath) {
        this(targetComponent, targetBean, keypath, false, false);
    }

    /**
     * Creates a new TextComponentTextBinding. 'ignoreAccessControl' will be set to false.
     *
     * @param targetComponent   The text component this binding applies to. Non-null.
     * @param targetBean        The java bean this binding applies to. Non-null.
     * @param keypath           The keypath used to obtain the java bean's bound value. Non-null.
     * @param continuousUpdates A flag determining whether or not all updates to the text component should update the
     *                          target bean as well. If set to false, the bean will be updated whenever the text
     *                          component loses the focus. Default: false.
     */
    public TextComponentTextBinding(final JTextComponent targetComponent, final Object targetBean,
                                    final String keypath, final boolean continuousUpdates) {
        this(targetComponent, targetBean, keypath, continuousUpdates, false);
    }

    /**
     * Installs the required listeners if 'continuousUpdates' is set to false.
     */
    protected void installDefaultListeners() {
        _focusAdapter = new FocusAdapter() {
            @Override
            public void focusLost(final FocusEvent e) {
                componentChanged();
            }
        };
        _component.addFocusListener(_focusAdapter);

        if (_component instanceof JTextField) {
            _actionListener = new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    componentChanged();
                }
            };
            ((JTextField) _component).addActionListener(_actionListener);
        }
    }

    /**
     * Installs the required listeners if 'continuousUpdates' is set to true.
     */
    protected void installContinuousUpdateListeners() {
        _documentAdapter = new DocumentAdapter() {
            @Override
            public void documentChanged(final DocumentEvent e) {
                componentChanged();
            }
        };

        _documentChangeListener = new PropertyChangeListener() {
            public void propertyChange(final PropertyChangeEvent evt) {
                documentChanged((Document) evt.getOldValue(), (Document) evt.getNewValue());
            }
        };

        _component.addPropertyChangeListener("document", _documentChangeListener);
        documentChanged(null, _component.getDocument());
    }

    /**
     * Invoked whenever the text component's document is exchanged. Will only be invoked if 'continuousUpdates' is set
     * to true.
     *
     * @param previous The previous Document instance.
     * @param current  The new Document instance.
     */
    protected void documentChanged(final Document previous, final Document current) {
        if (previous != null)
            previous.removeDocumentListener(_documentAdapter);
        if (current != null)
            current.addDocumentListener(_documentAdapter);
    }

    /**
     * @see net.sarcommand.swingextensions.binding.SwingBinding#targetBeanChanged()
     */
    protected void targetBeanChanged() {
        if (_ownUpdate)
            return;
        final boolean canResolve = _path.canResolve(_targetBean);
        final boolean canSet = _path.canSet(_targetBean);
        final String newValue = _path.get(_targetBean);

        __log.trace(format("%s: Target bean %s changed, canResolve(): %b, canSet():%b, new value: '%s'", this,
                _targetBean, canResolve, canSet, newValue));

        adaptComponent(canResolve, canSet, newValue);
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
     * @see net.sarcommand.swingextensions.binding.SwingBinding#detach()
     */
    public void detach() {
        __log.debug(format("Detaching %s from %s", this, _component));
        _observer.dispose();
        if (_focusAdapter != null)
            _component.removeFocusListener(_focusAdapter);
        if (_actionListener != null)
            ((JTextField) _component).removeActionListener(_actionListener);
        if (_documentAdapter != null)
            _component.getDocument().removeDocumentListener(_documentAdapter);
        _component.putClientProperty(CLIENT_PROPERTY, null);
    }
}
