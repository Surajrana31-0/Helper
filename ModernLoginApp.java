import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import java.awt.geom.RoundRectangle2D;

public class ModernLoginApp extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private Font fontAwesome;

    // Modern color scheme matching the image
    private Color primaryBlue = new Color(29, 161, 242); // Bright blue
    private Color backgroundColor = new Color(29, 161, 242); // Background blue
    private Color cardColor = Color.WHITE;
    private Color textColor = new Color(102, 102, 102); // Gray text
    private Color linkColor = new Color(29, 161, 242); // Link blue

    private JTextField signupNameField, signupUsernameField, signupEmailField;
    private JPasswordField signupPasswordField;
    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;

    public ModernLoginApp() {
        // Load Font Awesome
        try {
            InputStream is = getClass().getResourceAsStream("/fonts/fontawesome-webfont.ttf");
            fontAwesome = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(fontAwesome);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Modern Login");
        setSize(1600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(backgroundColor);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(backgroundColor);

        cardPanel.add(createSignupPanel(), "signup");
        cardPanel.add(createLoginPanel(), "login");

        add(createHeaderPanel(), BorderLayout.NORTH); // Add the header panel at the top
        add(cardPanel, BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH); // Add the footer panel at the bottom
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(backgroundColor);

        // Application name
        JLabel appNameLabel = new JLabel("ChatX");
        appNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        appNameLabel.setHorizontalAlignment(JLabel.CENTER);
        appNameLabel.setForeground(Color.WHITE);
        headerPanel.add(appNameLabel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(backgroundColor);

        // Left-aligned panel for enquiry, contact, and social media icons
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(backgroundColor);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Enquiry label with email icon
        JPanel enquiryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        enquiryPanel.setBackground(backgroundColor);
        enquiryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel emailIcon = new JLabel("\uf0e0"); // Font Awesome email icon
        emailIcon.setFont(fontAwesome.deriveFont(20f));
        emailIcon.setForeground(Color.BLACK);

        JLabel enquiryLabel = new JLabel("Enquiry: Jp98064scoot@gmail.com");
        enquiryLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        enquiryLabel.setForeground(Color.BLACK);

        enquiryPanel.add(emailIcon);
        enquiryPanel.add(enquiryLabel);

        // Contact label with phone icon
        JPanel contactPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        contactPanel.setBackground(backgroundColor);
        contactPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel contactIcon = new JLabel("\uf095"); // Font Awesome phone icon
        contactIcon.setFont(fontAwesome.deriveFont(20f));
        contactIcon.setForeground(Color.BLACK);

        JLabel contactLabel = new JLabel("Contact: 9810005000");
        contactLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        contactLabel.setForeground(Color.BLACK);

        contactPanel.add(contactIcon);
        contactPanel.add(contactLabel);

        // Social media icons
        JPanel socialPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        socialPanel.setBackground(backgroundColor);
        socialPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel facebookLabel = new JLabel("\uf09a"); // Font Awesome Facebook icon
        facebookLabel.setFont(fontAwesome.deriveFont(16f));
        facebookLabel.setForeground(Color.BLACK);

        JLabel instagramLabel = new JLabel("\uf16d"); // Font Awesome Instagram icon
        instagramLabel.setFont(fontAwesome.deriveFont(20f));
        instagramLabel.setForeground(Color.BLACK);

        JLabel twitterLabel = new JLabel("\uf099"); // Font Awesome Twitter icon
        twitterLabel.setFont(fontAwesome.deriveFont(20f));
        twitterLabel.setForeground(Color.BLACK);

        socialPanel.add(facebookLabel);
        socialPanel.add(instagramLabel);
        socialPanel.add(twitterLabel);

        // Right-aligned panel for copyright and version information
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(backgroundColor);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rightPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel copyrightLabel = new JLabel("© 2025 Coder Technologies, Inc.");
        copyrightLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        copyrightLabel.setForeground(Color.WHITE);
        copyrightLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel versionLabel = new JLabel("v2.18.4-devel+4872d14e5");
        versionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        versionLabel.setForeground(Color.WHITE);
        versionLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        rightPanel.add(copyrightLabel);
        rightPanel.add(versionLabel);

        footerPanel.add(leftPanel, BorderLayout.WEST);
        footerPanel.add(rightPanel, BorderLayout.EAST);

        // Add enquiry, contact, and social media icons to the left panel
        leftPanel.add(enquiryPanel);
        leftPanel.add(contactPanel);
        leftPanel.add(socialPanel);

        return footerPanel;
    }

    private JPanel createSignupPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(backgroundColor);

        // Create card panel with rounded corners
        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(cardColor);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.dispose();
            }
        };
        cardPanel.setLayout(new GridBagLayout());
        cardPanel.setBackground(cardColor);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);

        // Title
        JLabel titleLabel = new JLabel("Sign Up");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        cardPanel.add(titleLabel, gbc);

        // Name field with icon
        JPanel namePanel = createInputPanel("\uf007", "Name");
        signupNameField = (JTextField) namePanel.getComponent(1);
        cardPanel.add(namePanel, gbc);

        // Username field with icon
        JPanel usernamePanel = createInputPanel("\uf007", "Username");
        signupUsernameField = (JTextField) usernamePanel.getComponent(1);
        cardPanel.add(usernamePanel, gbc);

        // Email field with icon
        JPanel emailPanel = createInputPanel("\uf0e0", "Email");
        signupEmailField = (JTextField) emailPanel.getComponent(1);
        cardPanel.add(emailPanel, gbc);

        // Password field with icon
        JPanel passwordPanel = createPasswordPanel("\uf023", "Password");
        signupPasswordField = (JPasswordField) passwordPanel.getComponent(1);
        cardPanel.add(passwordPanel, gbc);

        // Signup button
        JButton signupButton = new JButton("SIGNUP");
        styleButton(signupButton);
        signupButton.addActionListener(e -> registerUser());
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)), gbc);
        cardPanel.add(signupButton, gbc);

        // Login link
        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        linkPanel.setBackground(cardColor);
        JLabel loginText = new JLabel("Already have an account? ");
        JLabel loginLink = new JLabel("Login");
        loginLink.setForeground(linkColor);
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        linkPanel.add(loginText);
        linkPanel.add(loginLink);

        loginLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(ModernLoginApp.this.cardPanel, "login");
            }
        });

        cardPanel.add(linkPanel, gbc);

        // Add card to main panel
        mainPanel.add(cardPanel);

        return mainPanel;
    }

    private JPanel createLoginPanel() {
        // Similar to signup panel but with fewer fields
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(backgroundColor);

        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(cardColor);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.dispose();
            }
        };
        cardPanel.setLayout(new GridBagLayout());
        cardPanel.setBackground(cardColor);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);

        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        cardPanel.add(titleLabel, gbc);

        JPanel usernamePanel = createInputPanel("\uf007", "Username");
        loginUsernameField = (JTextField) usernamePanel.getComponent(1);
        cardPanel.add(usernamePanel, gbc);

        JPanel passwordPanel = createPasswordPanel("\uf023", "Password");
        loginPasswordField = (JPasswordField) passwordPanel.getComponent(1);
        cardPanel.add(passwordPanel, gbc);

        JButton loginButton = new JButton("LOGIN");
        styleButton(loginButton);
        loginButton.addActionListener(e -> loginUser());
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)), gbc);
        cardPanel.add(loginButton, gbc);

        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        linkPanel.setBackground(cardColor);
        JLabel signupText = new JLabel("Don't have an account? ");
        JLabel signupLink = new JLabel("Sign Up");
        signupLink.setForeground(linkColor);
        signupLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        linkPanel.add(signupText);
        linkPanel.add(signupLink);

        signupLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(ModernLoginApp.this.cardPanel, "signup");
            }
        });

        cardPanel.add(linkPanel, gbc);
        mainPanel.add(cardPanel);

        return mainPanel;
    }

    private JPanel createInputPanel(String icon, String placeholder) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 0));
        panel.setBackground(cardColor);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(fontAwesome.deriveFont(16f));
        iconLabel.setForeground(textColor);

        JTextField textField = new JTextField(20);
        textField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        textField.setForeground(textColor);

        // Placeholder text
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(textColor);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Color.GRAY);
                }
            }
        });

        panel.add(iconLabel, BorderLayout.WEST);
        panel.add(textField, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createPasswordPanel(String icon, String placeholder) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 0));
        panel.setBackground(cardColor);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(fontAwesome.deriveFont(16f));
        iconLabel.setForeground(textColor);

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        passwordField.setForeground(textColor);

        // Show/Hide password toggle
        JLabel toggleLabel = new JLabel("\uf06e"); // Eye icon
        toggleLabel.setFont(fontAwesome.deriveFont(16f));
        toggleLabel.setForeground(textColor);
        toggleLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleLabel.addMouseListener(new MouseAdapter() {
            boolean isVisible = false;

            @Override
            public void mouseClicked(MouseEvent e) {
                isVisible = !isVisible;
                passwordField.setEchoChar(isVisible ? (char) 0 : '•');
                toggleLabel.setText(isVisible ? "\uf070" : "\uf06e"); // Toggle between eye and eye-slash
            }
        });

        // Placeholder text
        passwordField.setEchoChar((char) 0);
        passwordField.setText(placeholder);
        passwordField.setForeground(Color.GRAY);
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).equals(placeholder)) {
                    passwordField.setText("");
                    passwordField.setEchoChar('•');
                    passwordField.setForeground(textColor);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).isEmpty()) {
                    passwordField.setEchoChar((char) 0);
                    passwordField.setText(placeholder);
                    passwordField.setForeground(Color.GRAY);
                }
            }
        });

        panel.add(iconLabel, BorderLayout.WEST);
        panel.add(passwordField, BorderLayout.CENTER);
        panel.add(toggleLabel, BorderLayout.EAST);

        return panel;
    }

    private void styleButton(JButton button) {
        button.setBackground(primaryBlue);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14)); // Keep Arial font for buttons
        button.setPreferredSize(new Dimension(200, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(primaryBlue.darker());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(primaryBlue);
            }
        });
    }

    private void registerUser() {
        String name = signupNameField.getText().trim();
        String username = signupUsernameField.getText().trim();
        String email = signupEmailField.getText().trim();
        String password = new String(signupPasswordField.getPassword()).trim();

        if (name.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() ||
                name.equals("Name") || username.equals("Username") || email.equals("Email")
                || password.equals("Password")) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

       UserDao userDao = new UserDao();
        boolean isRegistered = userDao.registerUser (username, password, name, "", email);

        if (isRegistered) {
            JOptionPane.showMessageDialog(this, "Registration Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(cardPanel, "login");
        } else {
            JOptionPane.showMessageDialog(this, "Registration Failed", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loginUser() {
        String username = loginUsernameField.getText().trim();
        String password = new String(loginPasswordField.getPassword()).trim();

        System.out.println("username: " + username);
        System.out.println("password: " + password);

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        UserDao userDao = new UserDao();
        boolean isValidUser = userDao.validateUser(username, password);

        if (isValidUser) {
            JOptionPane.showMessageDialog(this, "Login Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Close the login window

            // Start the ChatClient

            SwingUtilities.invokeLater(() -> {
                ChatClient client = new ChatClient(username); // Pass the username to ChatClient
                client.connectToServer(); // Connect to the server
            });

        } else {
            JOptionPane.showMessageDialog(this, "Invalid Username or Password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            ModernLoginApp app = new ModernLoginApp();
            app.setVisible(true);
        });
    }
}