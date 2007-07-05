package net.sarcommand.swingextensions.actions;

import javax.swing.*;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public class SimpleActionState implements ActionState {
    private HashMap<Action, Boolean> _mapping;

    public SimpleActionState() {
        _mapping = new HashMap<Action, Boolean>(4);
    }

    public SimpleActionState(final Map<Action, Boolean> mapping) {
        this();
        _mapping.putAll(mapping);
    }

    public Collection<Action> getActions() {
        return _mapping.keySet();
    }

    public Map<Action, Boolean> getEnabledStates() {
        return Collections.synchronizedMap(_mapping);
    }

    public boolean isEnabled(final Action action) {
        final Boolean result = _mapping.get(action);
        return result == null || result;
    }

    public void setEnabled(final Action action, final boolean enabled) {
        if (action == null)
            throw new IllegalArgumentException("Parameter 'action' must not be null!");
        _mapping.put(action, enabled);
    }

    public void setEnabled(final Collection<Action> actions, final boolean enabled) {
        if (actions == null)
            throw new IllegalArgumentException("Parameter 'actions' must not be null!");

        for (Action a : actions)
            _mapping.put(a, enabled);
    }
}
