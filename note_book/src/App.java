import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class App {
    private List<Note> notes = new ArrayList<>();
    private JFrame frame;
    private JTextField searchField;
    private DefaultListModel<String> noteListModel;
    private JList<String> noteList;
    private JLabel statusBar;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::new);
    }

    public App() {
        frame = new JFrame("Note Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem addNoteMenuItem = new JMenuItem("Add Note");
        addNoteMenuItem.addActionListener(e -> showNoteDialog(null));
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));
        fileMenu.add(addNoteMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        // Search bar
        searchField = new JTextField();
        searchField.setToolTipText("Search notes by title or content");
        
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchNotes());
        searchButton.setToolTipText("Search notes");

        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // Note list
        noteListModel = new DefaultListModel<>();
        noteList = new JList<>(noteListModel);
        noteList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        noteList.addListSelectionListener(e -> displayNoteDetails());
        JScrollPane noteListScrollPane = new JScrollPane(noteList);

        // Buttons
        JButton addButton = new JButton("Add Note");
        addButton.addActionListener(e -> showNoteDialog(null));
        addButton.setToolTipText("Add a new note");

        JButton editButton = new JButton("Edit Note");
        editButton.addActionListener(e -> editSelectedNote());
        editButton.setToolTipText("Edit the selected note");

        // Layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(noteListScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Status bar
        statusBar = new JLabel("Welcome to Note Manager!");
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        frame.add(statusBar, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void searchNotes() {
        String query = searchField.getText().toLowerCase();
        noteListModel.clear();
        for (Note note : notes) {
            if (note.getTitle().toLowerCase().contains(query) || note.getContent().toLowerCase().contains(query)) {
                noteListModel.addElement(note.getTitle());
            }
        }
        statusBar.setText("Search completed.");
    }

    private void displayNoteDetails() {
        int index = noteList.getSelectedIndex();
        if (index >= 0) {
            Note note = notes.get(index);
            JOptionPane.showMessageDialog(frame, note.toString(), "Note Details", JOptionPane.INFORMATION_MESSAGE);
            statusBar.setText("Displayed details for: " + note.getTitle());
        }
    }

    private void showNoteDialog(Note noteToEdit) {
        JTextField titleField = new JTextField();
        JTextArea contentArea = new JTextArea(10, 30);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        JScrollPane contentScrollPane = new JScrollPane(contentArea);
        JTextField categoryField = new JTextField();

        if (noteToEdit != null) {
            titleField.setText(noteToEdit.getTitle());
            contentArea.setText(noteToEdit.getContent());
            categoryField.setText(noteToEdit.getCategory());
        }

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        panel.add(titleField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Content:"), gbc);
        gbc.gridx = 1;
        gbc.gridheight = 3;
        panel.add(contentScrollPane, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridheight = 1;
        panel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        panel.add(categoryField, gbc);

        int result = JOptionPane.showConfirmDialog(frame, panel, noteToEdit == null ? "Add Note" : "Edit Note",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText();
            String content = contentArea.getText();
            String category = categoryField.getText();

            if (noteToEdit == null) {
                Note note = new Note(title, content, category);
                notes.add(note);
                noteListModel.addElement(title);
                statusBar.setText("Added new note: " + title);
            } else {
                noteToEdit.setTitle(title);
                noteToEdit.setContent(content);
                noteToEdit.setCategory(category);
                refreshNoteList();
                statusBar.setText("Edited note: " + title);
            }
        }
    }

    private void editSelectedNote() {
        int index = noteList.getSelectedIndex();
        if (index >= 0) {
            Note note = notes.get(index);
            showNoteDialog(note);
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a note to edit.", "Edit Note", JOptionPane.WARNING_MESSAGE);
            statusBar.setText("No note selected for editing.");
        }
    }

    private void refreshNoteList() {
        noteListModel.clear();
        for (Note note : notes) {
            noteListModel.addElement(note.getTitle());
        }
    }

    public class Note {
        private String title;
        private String content;
        private String category;

        public Note(String title, String content, String category) {
            this.title = title;
            this.content = content;
            this.category = category;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        @Override
        public String toString() {
            return "Title: " + title + "\n" +
                    "Content: " + content + "\n" +
                    "Category: " + category;
        }
    }
}
