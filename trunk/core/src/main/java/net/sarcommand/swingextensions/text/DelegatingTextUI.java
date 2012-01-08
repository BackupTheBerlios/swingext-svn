package net.sarcommand.swingextensions.text;

import javax.accessibility.Accessible;
import javax.swing.*;
import javax.swing.plaf.TextUI;
import javax.swing.text.*;
import java.awt.*;

/**
 * A TextUI implementation which will delegate all calls to a given instance. Used as abstract super class for various
 * implementations.
 * <p/>
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
public abstract class DelegatingTextUI extends TextUI {
    protected TextUI _delegate;

    protected DelegatingTextUI(final TextUI delegate) {
        _delegate = delegate;
    }

    public void damageRange(final JTextComponent t, final int p0, final int p1) {
        _delegate.damageRange(t, p0, p1);
    }

    public void damageRange(final JTextComponent t, final int p0, final int p1, final Position.Bias firstBias,
                            final Position.Bias secondBias) {
        _delegate.damageRange(t, p0, p1, firstBias, secondBias);
    }

    public EditorKit getEditorKit(final JTextComponent t) {
        return _delegate.getEditorKit(t);
    }

    public int getNextVisualPositionFrom(final JTextComponent t, final int pos, final Position.Bias b,
                                         final int direction, final Position.Bias[] biasRet) throws
            BadLocationException {
        return _delegate.getNextVisualPositionFrom(t, pos, b, direction, biasRet);
    }

    public View getRootView(final JTextComponent t) {
        return _delegate.getRootView(t);
    }

    public String getToolTipText(final JTextComponent t, final Point pt) {
        return _delegate.getToolTipText(t, pt);
    }

    public Rectangle modelToView(final JTextComponent t, final int pos) throws BadLocationException {
        return _delegate.modelToView(t, pos);
    }

    public Rectangle modelToView(final JTextComponent t, final int pos, final Position.Bias bias) throws
            BadLocationException {
        return _delegate.modelToView(t, pos, bias);
    }

    public int viewToModel(final JTextComponent t, final Point pt) {
        return _delegate.viewToModel(t, pt);
    }

    public int viewToModel(final JTextComponent t, final Point pt, final Position.Bias[] biasReturn) {
        return _delegate.viewToModel(t, pt, biasReturn);
    }

    public boolean contains(final JComponent c, final int x, final int y) {
        return _delegate.contains(c, x, y);
    }

    public Accessible getAccessibleChild(final JComponent c, final int i) {
        return _delegate.getAccessibleChild(c, i);
    }

    public int getAccessibleChildrenCount(final JComponent c) {
        return _delegate.getAccessibleChildrenCount(c);
    }

    public int getBaseline(final JComponent c, final int width, final int height) {
        return _delegate.getBaseline(c, width, height);
    }

    public Component.BaselineResizeBehavior getBaselineResizeBehavior(final JComponent c) {
        return _delegate.getBaselineResizeBehavior(c);
    }

    public Dimension getMaximumSize(final JComponent c) {
        return _delegate.getMaximumSize(c);
    }

    public Dimension getMinimumSize(final JComponent c) {
        return _delegate.getMinimumSize(c);
    }

    public Dimension getPreferredSize(final JComponent c) {
        return _delegate.getPreferredSize(c);
    }

    public void installUI(final JComponent c) {
        _delegate.installUI(c);
    }

    public void paint(final Graphics g, final JComponent c) {
        _delegate.paint(g, c);
    }

    public void uninstallUI(final JComponent c) {
        _delegate.uninstallUI(c);
    }

    public void update(final Graphics g, final JComponent c) {
        _delegate.update(g, c);
    }
}
