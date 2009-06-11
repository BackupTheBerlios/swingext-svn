package net.sarcommand.swingextensions.text;

import net.sarcommand.swingextensions.binding.Keypath;
import net.sarcommand.swingextensions.binding.KeypathObserver;
import net.sarcommand.swingextensions.binding.SwingBinding;
import net.sarcommand.swingextensions.binding.UpdatePolicy;
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

public abstract class AbstractTextComponentBinding<T> extends SwingBinding {
    private static final SwingExtLogger __log = SwingExtLogging.getLogger(AbstractTextComponentBinding.class);

    /**
     * The observer used to track changes on the bean.
     */
    protected KeypathObserver _observer;
    /**
     * The Keypath instance used for getting and setting values from/to the bean.
     */
    protected Keypath<T> _path;
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
    protected boolean _ownUpdate;
    private final UpdatePolicy _updatePolicy;

    /**
     * Creates a new TextComponentTextBinding.
     *
     * @param targetComponent     The text component this binding applies to. Non-null.
     * @param targetBean          The java bean this binding applies to. Non-null.
     * @param keypath             The keypath used to obtain the java bean's bound value. Non-null.
     * @param updatePolicy        A flag determining when the component should update the bean and vice versa.
     * @param ignoreAccessControl Determines whether normal java access control should be ignored. Default: false.
     */
    public AbstractTextComponentBinding(final JTextComponent targetComponent, final Object targetBean,
                                        final String keypath, final UpdatePolicy updatePolicy,
                                        final boolean ignoreAccessControl) {
        if (targetComponent == null)
            throw new IllegalArgumentException("Parameter 'targetComponent' must not be null!");
        if (targetBean == null)
            throw new IllegalArgumentException("Parameter 'targetBean' must not be null!");
        if (keypath == null)
            throw new IllegalArgumentException("Parameter 'keypath' must not be null!");

        _component = targetComponent;
        _targetBean = targetBean;
        _path = new Keypath<T>(keypath, ignoreAccessControl);
        _observer = _path.createObserver(targetBean, getPropertyChangeListener());
        _updatePolicy = updatePolicy;

        installListeners();

        adaptComponent(_path.canResolve(_targetBean), _path.canSet(_targetBean), _path.get(_targetBean));

        _component.putClientProperty(getClientProperty(), this);
    }

    protected void installListeners() {
        switch (_updatePolicy) {
            case ON_CHANGE:
                installOnChangeListeners();
                break;
            case ON_FOCUS_LOST:
                installOnFocusLostListeners();
                break;
            default:
                /* Don't install any listeners */
        }
    }

    /**
     * Creates a new TextComponentTextBinding. 'continuousUpdates' and 'ignoreAccessControl' will both be set to false.
     *
     * @param targetComponent The text component this binding applies to. Non-null.
     * @param targetBean      The java bean this binding applies to. Non-null.
     * @param keypath         The keypath used to obtain the java bean's bound value. Non-null.
     */
    public AbstractTextComponentBinding(final JTextComponent targetComponent, final Object targetBean,
                                        final String keypath) {
        this(targetComponent, targetBean, keypath, UpdatePolicy.ON_FOCUS_LOST, false);
    }

    /**
     * Creates a new TextComponentTextBinding. 'ignoreAccessControl' will be set to false.
     *
     * @param targetComponent The text component this binding applies to. Non-null.
     * @param targetBean      The java bean this binding applies to. Non-null.
     * @param keypath         The keypath used to obtain the java bean's bound value. Non-null.
     * @param updatePolicy    A flag determining when the component should update the bean and vice versa.
     */
    public AbstractTextComponentBinding(final JTextComponent targetComponent, final Object targetBean,
                                        final String keypath, final UpdatePolicy updatePolicy) {
        this(targetComponent, targetBean, keypath, updatePolicy, true);
    }

    /**
     * Installs the required listeners if 'continuousUpdates' is set to false.
     */
    protected void installOnFocusLostListeners() {
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
    protected void installOnChangeListeners() {
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
     * @see net.sarcommand.swingextensions.binding.SwingBinding#targetBeanChanged()
     */
    protected void targetBeanChanged() {
        if (_ownUpdate)
            return;
        final boolean canResolve = _path.canResolve(_targetBean);
        final boolean canSet = _path.canSet(_targetBean);
        final T newValue = _path.get(_targetBean);

        __log.trace(String.format("%s: Target bean %s changed, canResolve(): %b, canSet():%b, new value: '%s'",
                this, _targetBean, canResolve, canSet, newValue));

        adaptComponent(canResolve, canSet, newValue);
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
     * @see net.sarcommand.swingextensions.binding.SwingBinding#detach()
     */
    public void detach() {
        __log.debug(String.format("Detaching %s from %s", this, _component));
        _observer.dispose();
        if (_focusAdapter != null)
            _component.removeFocusListener(_focusAdapter);
        if (_actionListener != null)
            ((JTextField) _component).removeActionListener(_actionListener);
        if (_documentAdapter != null)
            _component.getDocument().removeDocumentListener(_documentAdapter);
        _component.putClientProperty(TextComponentTextBinding.CLIENT_PROPERTY, null);
    }

    protected JTextComponent getComponent() {
        return _component;
    }

    protected abstract String getClientProperty();

    public abstract void componentChanged();

    protected abstract void adaptComponent(final boolean canResolve, final boolean canSet, final T newValue);
}