package net.sarcommand.swingextensions.actions;

/**
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public interface ActionProvider {
    public ManagedAction createAction(final Object identifier);
}
