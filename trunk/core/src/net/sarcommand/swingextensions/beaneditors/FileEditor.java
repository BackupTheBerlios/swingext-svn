package net.sarcommand.swingextensions.beaneditors;

import net.sarcommand.swingextensions.resources.SwingExtResources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.prefs.Preferences;

/**
 * A simple BeanEditor implementation handling file properties. It consists of a text field holding the file name and a
 * 'search' button which triggers a JFileChooser dialog.
 */
public class FileEditor extends BeanEditor<File, JPanel> {
    public static final String ICON_SEARCH = "FileEditor.iconSearch";

    private JPanel _editor;
    private JTextField _fileTF;
    private JButton _searchButton;

    private JFileChooser _fileChooser;

    public FileEditor(final Object targetBean, final String property) {
        super(targetBean, property);
        initialize();
    }

    public FileEditor(final Object targetBean, final String property, final String prefKey) {
        super(targetBean, property, prefKey);
        initialize();
    }

    public FileEditor(final Object targetBean, final String property, final String prefKey, final Preferences prefs) {
        super(targetBean, property, prefKey, prefs);
        initialize();
    }

    protected void initialize() {
        initComponents();
        initLayout();
        setupEventHandlers();
        super.initialize();
    }

    protected void initComponents() {
        _editor = new JPanel();
        _fileTF = new JTextField(20) {
            public String getToolTipText() {
                return getText();
            }
        };
        _fileChooser = new JFileChooser();
        _searchButton = new JButton(SwingExtResources.getIconResource(ICON_SEARCH));
        ToolTipManager.sharedInstance().registerComponent(_fileTF);
    }

    protected void initLayout() {
        _editor.setLayout(new GridBagLayout());
        _editor.add(_fileTF, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        _editor.add(_searchButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

        setLayout(new GridLayout(1, 1));
        add(_editor);
    }

    protected void setupEventHandlers() {
        _searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                searchFile();
            }
        });

        _fileTF.addFocusListener(new FocusListener() {
            public void focusGained(final FocusEvent e) {
            }

            public void focusLost(final FocusEvent e) {
                final File f = new File(_fileTF.getText());
                setValue(f);
            }
        });
    }

    protected void beanValueUpdated() {
        final File value = getValue();
        _fileTF.setText(value == null ? "" : value.getAbsolutePath());
    }

    public JPanel getEditor() {
        return _editor;
    }

    public Class<File> getValueClass() {
        return File.class;
    }

    protected void searchFile() {
        if (_fileChooser.showOpenDialog(_editor) != JFileChooser.APPROVE_OPTION)
            return;

        setValue(_fileChooser.getSelectedFile());
    }

    public JFileChooser getFileChooser() {
        return _fileChooser;
    }

    public void setFileChooser(final JFileChooser fileChooser) {
        _fileChooser = fileChooser;
    }
}
