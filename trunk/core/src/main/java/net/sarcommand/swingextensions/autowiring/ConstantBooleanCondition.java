package net.sarcommand.swingextensions.autowiring;

/**
 * A BooleanCondition that always returns a predefined value. Note that for testing purposes, this class may also return
 * null if it was initialized with this value. Obviously, this condition will never fire any update events.
 *
 * @author Torsten Heup <torsten.heup@fit.fraunhofer.de>
 */
public class ConstantBooleanCondition extends BooleanCondition {
    /**
     * The constant state to be returned by this condition.
     */
    protected Boolean _state;

    /**
     * Creates a new condition that always returns the same, constant state.
     *
     * @param state The state to return from this condition.
     */
    public ConstantBooleanCondition(final Boolean state) {
        _state = state;
    }

    @Override
    public Boolean getState() {
        return _state;
    }
}
