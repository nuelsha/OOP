// Importing the AWT classes for UI components
import java.awt.*;
// Importing the ArrayList class from the java.util package
import java.util.ArrayList;
// Importing the Swing classes for UI components
import javax.swing.*;

// Class definition for the main application
public class App {
    // Static ArrayList to hold Contact objects
    private static ArrayList<Contact> contacts = new ArrayList<>();

    // Main method where the program starts execution
    public static void main(String[] args) {
        // Scheduling a job for the event-dispatching thread:
        // creating and showing this application's GUI
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI(); // Calling the method to create and show the GUI
        });
    }

    // Method to create and show the GUI
    private static void createAndShowGUI() {
        // Creating the main application frame
        JFrame frame = new JFrame("Contact Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Creating the main panel with a GridBagLayout
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        // Setting padding around the panel
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        // GridBagConstraints for layout management
        GridBagConstraints gbc = new GridBagConstraints();

        // Adding a heading label
        JLabel headingLabel = new JLabel("Contact Management System");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 20, 0);
        panel.add(headingLabel, gbc);

        // Resetting grid width and setting insets for buttons
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Adding "View Contacts" button
        JButton viewContactsButton = new JButton("View Contacts");
        viewContactsButton.setPreferredSize(new Dimension(150, 30));
        // Adding action listener to the button
        viewContactsButton.addActionListener(e -> viewContacts());
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(viewContactsButton, gbc);

        // Adding "Add Contact" button
        JButton addContactButton = new JButton("Add Contact");
        addContactButton.setPreferredSize(new Dimension(150, 30));
        // Adding action listener to the button
        addContactButton.addActionListener(e -> addContact());
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(addContactButton, gbc);

        // Adding "Delete Contact" button
        JButton deleteContactButton = new JButton("Delete Contact");
        deleteContactButton.setPreferredSize(new Dimension(150, 30));
        // Adding action listener to the button
        deleteContactButton.addActionListener(e -> deleteContact());
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(deleteContactButton, gbc);

        // Adding "Search Contact" button
        JButton searchContactButton = new JButton("Search Contact");
        searchContactButton.setPreferredSize(new Dimension(150, 30));
        // Adding action listener to the button
        searchContactButton.addActionListener(e -> searchContact());
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(searchContactButton, gbc);

        // Adding the panel to the frame and setting frame properties
        frame.getContentPane().add(panel);
        frame.pack();
        // Center the window on the screen
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Method to view contacts
    private static void viewContacts() {
        // Checking if the contacts list is empty
        if (contacts.isEmpty()) {
            showCustomMessageDialog("No contacts found.");
        } else {
            // Creating a custom dialog to display contacts
            JDialog dialog = new JDialog();
            dialog.setTitle("Contacts List");
            dialog.setLayout(new BorderLayout());
            dialog.setSize(300, 400);
            dialog.setModal(true);
            dialog.setLocationRelativeTo(null);

            // Creating a panel to hold the contacts
            JPanel contactPanel = new JPanel();
            contactPanel.setLayout(new BoxLayout(contactPanel, BoxLayout.Y_AXIS));
            // Adding border to the panel
            contactPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Adding each contact to the panel
            for (Contact contact : contacts) {
                JLabel contactLabel = new JLabel(contact.toString());
                contactLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                contactPanel.add(contactLabel);
                // Add space between contacts
                contactPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }

            // Adding a scroll pane to the panel
            JScrollPane scrollPane = new JScrollPane(contactPanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            dialog.add(scrollPane, BorderLayout.CENTER);

            // Adding a close button to the dialog
            JPanel buttonPanel = new JPanel();
            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(e -> dialog.dispose());
            buttonPanel.add(closeButton);
            dialog.add(buttonPanel, BorderLayout.SOUTH);

            // Making the dialog visible
            dialog.setVisible(true);
        }
    }

    // Method to add a contact
    private static void addContact() {
        // Prompting the user to enter the contact's name
        String name = JOptionPane.showInputDialog("Enter name:");
        if (name == null || name.trim().isEmpty()) {
            showCustomMessageDialog("Name cannot be empty.");
            return;
        }

        // Checking if the contact already exists
        for (Contact contact : contacts) {
            if (contact.getName().equalsIgnoreCase(name)) {
                int option = JOptionPane.showConfirmDialog(null, "Contact already exists. Do you want to overwrite it?", "Contact Exists", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.NO_OPTION) {
                    // User chose not to overwrite the existing contact
                    return;
                }
            }
        }

        // Prompting the user to enter the contact's phone number
        String phoneNumber = JOptionPane.showInputDialog("Enter phone number:");
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            showCustomMessageDialog("Phone number cannot be empty.");
            return;
        }

        // Creating and adding a new contact to the list
        Contact contact = new Contact(name, phoneNumber);
        contacts.add(contact);

        // Showing a confirmation message
        showCustomMessageDialog("Contact added successfully.");
    }

    // Method to
    // Method to delete a contact
    private static void deleteContact() {
        // Prompting the user to enter the name of the contact to delete
        String name = JOptionPane.showInputDialog("Enter name to delete:");
        if (name == null || name.trim().isEmpty()) {
            showCustomMessageDialog("Name cannot be empty.");
            return;
        }

        // Searching for the contact and removing it
        boolean contactFound = false;
        for (Contact contact : contacts) {
            if (contact.getName().equalsIgnoreCase(name)) {
                contacts.remove(contact);
                contactFound = true;
                showCustomMessageDialog("Contact deleted successfully.");
                break;
            }
        }

        // If contact not found, show a message
        if (!contactFound) {
            showCustomMessageDialog("Contact not found.");
        }
    }

    // Method to search for a contact
    private static void searchContact() {
        // Prompting the user to enter the name of the contact to search
        String name = JOptionPane.showInputDialog("Enter name to search:");
        if (name == null || name.trim().isEmpty()) {
            showCustomMessageDialog("Name cannot be empty.");
            return;
        }

        // Searching for the contact and showing it if found
        boolean contactFound = false;
        for (Contact contact : contacts) {
            if (contact.getName().equalsIgnoreCase(name)) {
                showCustomMessageDialog("Contact found:\n" + contact);
                contactFound = true;
                break;
            }
        }

        // If contact not found, show a message
        if (!contactFound) {
            showCustomMessageDialog("Contact not found.");
        }
    }

    // Method to display a custom message dialog
    private static void showCustomMessageDialog(String message) {
        // Creating a custom dialog for showing messages
        JDialog dialog = new JDialog();
        dialog.setTitle("Message");
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 150);
        dialog.setModal(true);
        dialog.setLocationRelativeTo(null);

        // Creating a panel to hold the message
        JPanel messagePanel = new JPanel();
        messagePanel.setBackground(new Color(230, 240, 255));
        messagePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>" + message.replaceAll("\n", "<br>") + "</div></html>");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        messagePanel.add(messageLabel);
        dialog.add(messagePanel, BorderLayout.CENTER);

        // Adding an OK button to the dialog
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(okButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Making the dialog visible
        dialog.setVisible(true);
    }
}

// Class representing a contact
class Contact {
    private String name;
    private String phoneNumber;

    // Constructor
    public Contact(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for phone number
    public String getPhoneNumber() {
        return phoneNumber;
    }

    // toString method to return a string representation of the contact
    @Override
    public String toString() {
        return "Name: " + name + ", Phone: " + phoneNumber;
    }
}
