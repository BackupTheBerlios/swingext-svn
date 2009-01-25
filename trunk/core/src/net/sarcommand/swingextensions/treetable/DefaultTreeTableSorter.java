package net.sarcommand.swingextensions.treetable;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.util.*;

/**
 * Default implementation for the TreeTableSorter interface.
 * <hr/>
 * Copyright 2006-2009 Torsten Heup
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class DefaultTreeTableSorter implements TreeTableSorter, Comparator<Object> {
    private HashMap<Integer, Comparator> _columnComparators;
    private LinkedHashMap<Class, Comparator> _classComparators;

    private List<RowSorter.SortKey> _sortKeys;
    private TreePath _path;
    private TreeTableModel _model;
    private boolean _usingToString;

    public DefaultTreeTableSorter() {
        _columnComparators = new HashMap<Integer, Comparator>(4);
        _classComparators = new LinkedHashMap<Class, Comparator>(4);
        setUsingToString(true);
    }

    public boolean isUsingToString() {
        return _usingToString;
    }

    public void setUsingToString(boolean usingToString) {
        _usingToString = usingToString;
    }

    public void setComparator(final int columnIndex, final Comparator c) {
        _columnComparators.put(columnIndex, c);
    }

    public List<Object> sort(TreePath path, TreeTableModel mdl, List<RowSorter.SortKey> sortKeys,
                             Collection<Object> values) {
        _path = path;
        _model = mdl;
        _sortKeys = sortKeys;
        final ArrayList<Object> sorted = new ArrayList<Object>(values.size());
        sorted.addAll(values);
        Collections.sort(sorted, this);
        return sorted;
    }

    @SuppressWarnings("unchecked")
    public int compare(Object o1, Object o2) {
        final RowSorter.SortKey key = _sortKeys.get(0);
        final int index = key.getColumn() - 1;
        final boolean ascend = key.getSortOrder() == SortOrder.ASCENDING;
        final Object v0 = _model.getValueAt(_path == null ?
                new TreePath(o1) : _path.pathByAddingChild(o1), index);
        final Object v1 = _model.getValueAt(_path == null ?
                new TreePath(o2) : _path.pathByAddingChild(o2), index);

        final int result;
        Comparator comp = _columnComparators.get(index);
        if (comp != null)
            result = comp.compare(v0, v1);
        else {
            if (v0 == null)
                result = v1 != null ? -1 : 0;
            else if (getComparator(v0.getClass()) != null)
                result = getComparator(v0.getClass()).compare(v0, v1);
            else if (v0 instanceof Comparable)
                result = ((Comparable) v0).compareTo(v1);
            else if (_usingToString)
                result = v0.toString().compareTo(v1.toString());
            else
                result = 0;
        }
        return ascend ? result : -result;
    }

    protected Comparator getComparator(final Class clazz) {
        if (_classComparators.get(clazz) != null)
            return _classComparators.get(clazz);
        for (Class c : _classComparators.keySet())
            if (c.isAssignableFrom(clazz))
                return _classComparators.get(c);
        return null;
    }
}
