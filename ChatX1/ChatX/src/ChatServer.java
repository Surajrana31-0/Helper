import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ChatServer {
    private static final int PORT = 5000;
    private static Set<String> userNames = new HashSet<>();
    private static Set<ClientHandler> clients = new HashSet<>();
    private static JFrame frame;
    private static JTextPane chatArea;
    private static JTextField messageField;
    private static DefaultListModel<String> listModel;
    private static SimpleDateFormat timeFormat;
    private static final Color PRIMARY_COLOR = new Color(0, 128, 105);
    private static final Color SECONDARY_COLOR = new Color(240, 240, 240);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatServer::createAndShowGUI);
        setupServer();
    }

    public static void createAndShowGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        timeFormat = new SimpleDateFormat("HH:mm:ss");
        listModel = new DefaultListModel<>();

        frame = new JFrame("Chat Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(800, 600));

        createComponents();
        layoutComponents();

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void createComponents() {
        chatArea = new JTextPane();
        chatArea.setEditable(false);
        chatArea.setBackground(SECONDARY_COLOR);
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        messageField = new JTextField();
        messageField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // User list panel
        JList<String> userList = new JList<>(listModel);
        userList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userList.setFixedCellHeight(30);
        userList.setBackground(Color.WHITE);
    }

    private static void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        JLabel titleLabel = new JLabel("Chat Server", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        JPanel chatPanel = new JPanel(new BorderLayout(5, 5));
        JScrollPane chatScroll = new JScrollPane(chatArea);
        chatScroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        JButton sendButton = new JButton("Send");
        styleButton(sendButton);

        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        chatPanel.add(chatScroll, BorderLayout.CENTER);
        chatPanel.add(inputPanel, BorderLayout.SOUTH);

        JPanel usersPanel = new JPanel(new BorderLayout());
        JLabel usersLabel = new JLabel("Active Users", SwingConstants.CENTER);
        usersLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        usersPanel.add(usersLabel, BorderLayout.NORTH);
        usersPanel.add(new JScrollPane(new JList<>(listModel)), BorderLayout.CENTER);
        usersPanel.setPreferredSize(new Dimension(200, 0));

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(chatPanel, BorderLayout.CENTER);
        mainPanel.add(usersPanel, BorderLayout.EAST);

        frame.add(mainPanel);

        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());
    }

    private static void styleButton(JButton button) {
        button.setForeground(Color.GREEN);
        button.setBackground(PRIMARY_COLOR);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(PRIMARY_COLOR.darker());
                button.setForeground(Color.GREEN.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
                button.setForeground(Color.GREEN);
            }
        });
    }

    private static void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            broadcastMessage("SERVER: " + message);
            appendToChat("SERVER", message);
            messageField.setText("");
        }
    }

    private static void appendToChat(String sender, String message) {
        SwingUtilities.invokeLater(() -> {
            try {
                String timestamp = "[" + timeFormat.format(new Date()) + "] ";
                String fullMessage = timestamp + sender + ": " + message + "\n";
                chatArea.getDocument().insertString(chatArea.getDocument().getLength(), fullMessage, null);
                chatArea.setCaretPosition(chatArea.getDocument().getLength());
            } catch (javax.swing.text.BadLocationException e) {
                e.printStackTrace();
            }
        });
    }

    public static void setupServer() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                appendToChat("System", "Server is running on port " + PORT);

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    clients.add(clientHandler);
                    clientHandler.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void broadcastMessage(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage("MESSAGE " + message);
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    out.println("SUBMITNAME");
                    username = in.readLine();
                    if (username == null) {
                        return;
                    }
                    synchronized (userNames) {
                        if (!username.isEmpty() && !userNames.contains(username)) {
                            userNames.add(username);
                            updateUserList();
                            break;
                        }
                    }
                }

                out.println("NAMEACCEPTED " + username);
                broadcastMessage(username + " has joined the chat.");
                appendToChat("System", username + " has joined the chat.");

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("FILE")) {
                        handleFileReceived(message);
                    } else if (message.startsWith("@")) {
                        handlePrivateMessage(message);
                    } else {
                        broadcastMessage(username + ": " + message);
                        appendToChat(username, message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                disconnect();
            }
        }

        private void handleFileReceived(String message) {
            // File handling logic here
        }

        private void handlePrivateMessage(String message) {
            // Private message handling logic here
        }

        private void disconnect() {
            if (username != null) {
                userNames.remove(username);
                updateUserList();
                broadcastMessage(username + " has left the chat.");
                appendToChat("System", username + " has left the chat.");
            }
            clients.remove(this);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendMessage(String message) {
            if (out != null) {
                out.println(message);
            }
        }
    }

    private static void updateUserList() {
        SwingUtilities.invokeLater(() -> {
            listModel.clear();
            for (String user : userNames) {
                listModel.addElement(user);
            }
        });
    }
}