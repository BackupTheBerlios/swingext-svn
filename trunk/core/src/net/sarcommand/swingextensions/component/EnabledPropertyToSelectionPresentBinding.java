package net.sarcommand.swingextensions.component;

import net.sarcommand.swingextensions.binding.SwingBinding;
import net.sarcommand.swingextensions.internal.SwingExtLogger;
import net.sarcommand.swingextensions.internal.SwingExtLogging;
import net.sarcommand.swingextensions.table.TableSelectionListenerGlue;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/**
 */
public class EnabledPropertyToSelectionPresentBinding extends SwingBinding {
    private static final SwingExtLogger __log = SwingExtLogging.getLogger(EnabledPropertyToSelectionPresentBinding.class);

    public static final String CLIENT_PROPERTY = "swingExt.EnabledPropertyToSelectionPresentBindingClientProperty";

    protected JComponent _targetComponent;
    protected JComponent _source;
    protected boolean _enableIfSelectionPresent;
    protected Object _observer;

    public EnabledPropertyToSelectionPresentBinding(final JComponent targetComponent, final JList list, final boolean enableIfSelectionPresent) {
        initialize(targetComponent, list, enableIfSelectionPresent);
    }

    public EnabledPropertyToSelectionPresentBinding(final JComponent targetComponent, final JTable table, final boolean enableIfSelectionPresent) {
        initialize(targetComponent, table, enableIfSelectionPresent);
    }

    public EnabledPropertyToSelectionPresentBinding(final JComponent targetComponent, final JTree tree, final boolean enableIfSelectionPresent) {
        initialize(targetComponent, tree, enableIfSelectionPresent);
    }

    protected void initialize(final JComponent targetComponent, final JComponent sourceComponent, final boolean enableIfSelectionPresent) {
        _targetComponent = targetComponent;
        _source = sourceComponent;
        _enableIfSelectionPresent = enableIfSelectionPresent;

        addListener();

        setClientPropertyOnTarget(_targetComponent);
        targetBeanChanged();
    }

    protected void addListener() {
        if (_source instanceof JList) {
            final ListSelectionListener listener = new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    targetBeanChanged();
                }
            };
            ((JList) _source).addListSelectionListener(listener);
            _observer = listener;
        } else if (_source instanceof JTable) {
            final ListSelectionListener listener = new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    targetBeanChanged();
                }
            };
            _observer = new TableSelectionListenerGlue((JTable) _source, listener);
        } else if (_source instanceof JTree) {
            final TreeSelectionListener listener = new TreeSelectionListener() {
                public void valueChanged(TreeSelectionEvent e) {
                    targetBeanChanged();
                }
            };
            ((JTree) _source).addTreeSelectionListener(listener);
            _observer = listener;
        }

    }

    protected void targetBeanChanged() {
        _targetComponent.setEnabled(isSelectionPresent() == _enableIfSelectionPresent);
    }

    public void componentChanged() {
    }

    protected String getClientProperty() {
        return CLIENT_PROPERTY;
    }

    protected boolean isSelectionPresent() {
        if (_source instanceof JList)
            return ((JList) _source).getSelectedIndex() >= 0;
        if (_source instanceof JTable)
            return ((JTable) _source).getSelectedRow() >= 0;
        if (_source instanceof JTree)
            return ((JTree) _source).getSelectionCount() > 0;

        if (__log.isDebugEnabled())
            __log.debug(String.format("%s: Unknown class for field 'source': %s", this, _source));
        return false;
    }

    public void detach() {
        if (__log.isDebugEnabled())
            __log.debug(String.format("%s detaching from %s", this, _targetComponent));
        if (_source instanceof JList) {
            ((JList) _source).removeListSelectionListener((ListSelectionListener) _observer);
            _targetComponent.putClientProperty(CLIENT_PROPERTY, null);
        } else if (_source instanceof JTable) {
            ((TableSelectionListenerGlue) _observer).dispose();
            _targetComponent.putClientProperty(CLIENT_PROPERTY, null);
        } else if (_source instanceof JTree) {
            ((JTree) _source).removeTreeSelectionListener((TreeSelectionListener) _observer);
            _targetComponent.putClientProperty(CLIENT_PROPERTY, null);
        }
    }
}
