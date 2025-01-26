import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.Base64;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private JFrame frame;
    private JTextPane chatArea;
    private JTextField messageField;
    private JComboBox<String> userComboBox;
    private DefaultComboBoxModel<String> comboBoxModel;
    private JList<String> userList;
    private DefaultListModel<String> listModel;
    private SimpleDateFormat timeFormat;
    private String username;
    private static final Color PRIMARY_COLOR = new Color(0, 128, 105);
    private static final Color SECONDARY_COLOR = new Color(240, 240, 240);
    private static final Color BUTTON_TEXT_COLOR = new Color(0, 255, 0); // Green color for button text
    private static final String RECEIVED_PATH = "received_files";

    public ChatClient(String username) {
        this.username = username;
        timeFormat = new SimpleDateFormat("HH:mm:ss");
        listModel = new DefaultListModel<>();
        comboBoxModel = new DefaultComboBoxModel<>();
        createAndShowGUI();
        new File(RECEIVED_PATH).mkdirs();
    }

    private void createAndShowGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame = new JFrame("Chat Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(800, 600));

        createComponents();
        layoutComponents();

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createComponents() {
        chatArea = new JTextPane();
        chatArea.setEditable(false);
        chatArea.setBackground(SECONDARY_COLOR);
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        messageField = new JTextField();
        messageField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageField.setEnabled(false);

        userList = new JList<>(listModel) {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (int i = 0; i < getModel().getSize(); i++) {
                    String user = getModel().getElementAt(i);
                    if (user.equals(username)) {
                        Font boldFont = getFont().deriveFont(Font.BOLD);
                        setFont(boldFont);
                    } else {
                        Font plainFont = getFont().deriveFont(Font.PLAIN);
                        setFont(plainFont);
                    }
                }
            }
        };
        userList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userList.setFixedCellHeight(30);
        userList.setBackground(Color.WHITE);

        userComboBox = new JComboBox<>(comboBoxModel);
        userComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userComboBox.setEnabled(false);
    }

    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        JLabel titleLabel = new JLabel("Chat Client", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Chat Panel
        JPanel chatPanel = new JPanel(new BorderLayout(5, 5));
        JScrollPane chatScroll = new JScrollPane(chatArea);
        chatScroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // Input Panel
        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        JButton sendButton = new JButton("Send");
        JButton attachButton = new JButton("Attach");
        styleButton(sendButton);
        styleButton(attachButton);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 5, 0));
        buttonPanel.add(attachButton);
        buttonPanel.add(userComboBox);
        buttonPanel.add(sendButton);

        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.EAST);

        chatPanel.add(chatScroll, BorderLayout.CENTER);
        chatPanel.add(inputPanel, BorderLayout.SOUTH);

        // Users Panel
        JPanel usersPanel = new JPanel(new BorderLayout());
        JLabel usersLabel = new JLabel("Active Users", SwingConstants.CENTER);
        usersLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        usersPanel.add(usersLabel, BorderLayout.NORTH);
        usersPanel.add(new JScrollPane(userList), BorderLayout.CENTER);
        usersPanel.setPreferredSize(new Dimension(200, 0));

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(chatPanel, BorderLayout.CENTER);
        mainPanel.add(usersPanel, BorderLayout.EAST);

        frame.add(mainPanel);

        // Add listeners
        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());
        attachButton.addActionListener(e -> sendFile());
    }

    private void styleButton(JButton button) {
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(BUTTON_TEXT_COLOR); // Set text color to green
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
    }

    private void sendMessage() {
        String recipient = (String) userComboBox.getSelectedItem();
        String message = messageField.getText().trim();
        if (!message.isEmpty() && out != null) {
            if (recipient != null && !recipient.equals("Everyone")) {
                out.println("@" + recipient + " " + message);
            } else {
                out.println(message);
            }
            messageField.setText("");
        }
    }

    private void sendFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                byte[] fileData = Files.readAllBytes(file.toPath());
                String encodedFile = Base64.getEncoder().encodeToString(fileData);
                out.println("FILE " + file.getName() + " " + encodedFile);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Error sending file: " + e.getMessage());
            }
        }
    }

    private void appendToChat(String message) {
        SwingUtilities.invokeLater(() -> {
            try {
                StyledDocument doc = chatArea.getStyledDocument();
                String timestamp = "[" + timeFormat.format(new Date()) + "] ";
                
                SimpleAttributeSet attrs = new SimpleAttributeSet();
                if (message.startsWith(username + ": ")) {
                    StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_RIGHT);
                    StyleConstants.setBackground(attrs, PRIMARY_COLOR);
                    StyleConstants.setForeground(attrs, Color.WHITE);
                } else {
                    StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_LEFT);
                    StyleConstants.setForeground(attrs, Color.BLACK);
                }
                
                doc.insertString(doc.getLength(), timestamp + message + "\n", attrs);
                chatArea.setCaretPosition(doc.getLength());
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        });
    }

    public void connectToServer() {
        String serverAddress = JOptionPane.showInputDialog(
                frame,
                "Enter server IP address:",
                "localhost"
        );

        if (serverAddress == null || serverAddress.trim().isEmpty()) {
            System.exit(0);
        }

        try {
            socket = new Socket(serverAddress, 5000);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            new Thread(() -> {
                try {
                    while (true) {
                        String line = in.readLine();
                        if (line.startsWith("SUBMITNAME")) {
                            out.println(username);
                        } else if (line.startsWith("NAMEACCEPTED")) {
                            messageField.setEnabled(true);
                            userComboBox.setEnabled(true);
                            frame.setTitle("Chat Client - " + username);
                        } else if (line.startsWith("MESSAGE")) {
                            appendToChat(line.substring(8));
                        } else if (line.startsWith("FILE")) {
                            handleFileReceived(line);
                        } else if (line.startsWith("USERLIST")) {
                            updateUserList(line.substring(9));
                        }
                    }
                } catch (IOException e) {
                    appendToChat("Lost connection to server!");
                    messageField.setEnabled(false);
                    userComboBox.setEnabled(false);
                }
            }).start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error connecting to server: " + e.getMessage());
            System.exit(1);
        }
    }

    private void handleFileReceived(String line) {
        try {
            String[] parts = line.split(" ", 3);
            if (parts.length == 3) {
                String fileName = parts[1];
                byte[] fileData = Base64.getDecoder().decode(parts[2]);
                Path filePath = Paths.get(RECEIVED_PATH, fileName);
                Files.write(filePath, fileData);
                appendToChat("Received file: " + fileName);
            }
        } catch (IOException e) {
            appendToChat("Error receiving file: " + e.getMessage());
        }
    }

    private void updateUserList(String users) {
        SwingUtilities.invokeLater(() -> {
            listModel.clear();
            comboBoxModel.removeAllElements();
            comboBoxModel.addElement("Everyone");
            String[] userArray = users.split(",");
            for (String user : userArray) {
                if (!user.trim().isEmpty()) {
                    listModel.addElement(user.trim());
                    comboBoxModel.addElement(user.trim());
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChatClient client = new ChatClient("Rev_G");
            client.connectToServer();
        });
    }
}