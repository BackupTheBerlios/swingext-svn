package net.sarcommand.swingextensions.component;

import net.sarcommand.swingextensions.binding.SwingBinding;
import net.sarcommand.swingextensions.event.TableSelectionListenerGlue;
import net.sarcommand.swingextensions.internal.SwingExtLogger;
import net.sarcommand.swingextensions.internal.SwingExtLogging;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/**
 * Binds the enabled state of a component to the selection of a JList, JTable or JTree. One can determine whether the
 * target component should be enabled or disabled when a selection is present. You can use this class to auto-wire
 * context-specific buttons, e.g. a 'Remove' button which should only be useable when an according list/tree/table has a
 * value to remove in the first place.
 * <p/>
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
public class EnabledPropertyToSelectionPresentBinding extends SwingBinding {
    private static final SwingExtLogger __log = SwingExtLogging.getLogger(EnabledPropertyToSelectionPresentBinding.class);

    public static final String CLIENT_PROPERTY = "swingExt.EnabledPropertyToSelectionPresentBindingClientProperty";

    protected JComponent _targetComponent;
    protected JComponent _source;
    protected boolean _enableIfSelectionPresent;
    protected Object _observer;

    public EnabledPropertyToSelectionPresentBinding(final JComponent targetComponent, final JList list,
                                                    final boolean enableIfSelectionPresent) {
        initialize(targetComponent, list, enableIfSelectionPresent);
    }

    public EnabledPropertyToSelectionPresentBinding(final JComponent targetComponent, final JTable table,
                                                    final boolean enableIfSelectionPresent) {
        initialize(targetComponent, table, enableIfSelectionPresent);
    }

    public EnabledPropertyToSelectionPresentBinding(final JComponent targetComponent, final JTree tree,
                                                    final boolean enableIfSelectionPresent) {
        initialize(targetComponent, tree, enableIfSelectionPresent);
    }

    /**
     * Initializes this binding.
     *
     * @param targetComponent          The component to be enabled/disabled depending on the selection state.
     * @param sourceComponent          The component which's selection state should be monitored.
     * @param enableIfSelectionPresent If true, the target will be enabled if the source has a selection, if false, it
     *                                 will be disabled in this case.
     */
    protected void initialize(final JComponent targetComponent, final JComponent sourceComponent,
                              final boolean enableIfSelectionPresent) {
        _targetComponent = targetComponent;
        _source = sourceComponent;
        _enableIfSelectionPresent = enableIfSelectionPresent;

        addListener();

        setClientPropertyOnTarget(_targetComponent);
        targetBeanChanged();
    }

    /**
     * Adds a listener instance to the source component, monitoring its selection property for changes.
     */
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

    /**
     * @see net.sarcommand.swingextensions.binding.SwingBinding#targetBeanChanged()
     */
    protected void targetBeanChanged() {
        _targetComponent.setEnabled(isSelectionPresent() == _enableIfSelectionPresent);
    }

    /**
     * @see net.sarcommand.swingextensions.binding.SwingBinding#componentChanged()
     */
    public void componentChanged() {
    }

    /**
     * @see net.sarcommand.swingextensions.binding.SwingBinding#getClientProperty()
     */
    protected String getClientProperty() {
        return CLIENT_PROPERTY;
    }

    /**
     * Returns whether a selection is present on the source component.
     *
     * @return whether a selection is present on the source component.
     */
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

    /**
     * @see net.sarcommand.swingextensions.binding.SwingBinding#detach()
     */
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
