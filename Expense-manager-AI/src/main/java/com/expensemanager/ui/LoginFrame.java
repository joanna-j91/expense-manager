package com.expensemanager.ui;

import com.expensemanager.dao.UserDAO;
import com.expensemanager.models.User;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.InputStream;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private final UserDAO userDAO;
    private JPanel mainPanel;
    private JPanel loginPanel;
    private JPanel registerPanel;
    private CardLayout cardLayout;
    private Timer shakeTimer;
    private int shakeCount;
    private final int SHAKE_DISTANCE = 10;
    private Font iconFont;

    public LoginFrame() {
        this.userDAO = new UserDAO();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Expense Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 400, 600, 20, 20));

        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);

        createLoginPanel();
        createRegisterPanel();

        mainPanel.add(loginPanel, "login");
        mainPanel.add(registerPanel, "register");

        add(mainPanel);

        shakeTimer = new Timer(50, e -> {
            if (shakeCount < 10) {
                Point p = getLocation();
                setLocation(p.x + (shakeCount % 2 == 0 ? SHAKE_DISTANCE : -SHAKE_DISTANCE), p.y);
                shakeCount++;
            } else {
                shakeTimer.stop();
                setLocation(getX() + SHAKE_DISTANCE, getY());
                shakeCount = 0;
            }
        });

        addDraggableMouseListener();

        loadCustomFont();

        setupIcons();
    }

    private void createLoginPanel() {
        loginPanel = new JPanel(null);
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        JButton closeButton = createIconButton("×", 360, 10, 30, 30);
        closeButton.addActionListener(e -> System.exit(0));
        loginPanel.add(closeButton);

        JLabel iconLabel = new JLabel("💰", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setBounds(150, 50, 100, 60);
        loginPanel.add(iconLabel);

        JLabel titleLabel = new JLabel("Expense Manager", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(52, 152, 219));
        titleLabel.setBounds(100, 120, 200, 30);
        loginPanel.add(titleLabel);

        JLabel userLabel = new JLabel("👤 Username");
        userLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        userLabel.setBounds(50, 180, 300, 20);
        loginPanel.add(userLabel);

        usernameField = createStyledTextField();
        usernameField.setBounds(50, 205, 300, 40);
        loginPanel.add(usernameField);

        JLabel passLabel = new JLabel("🔒 Password");
        passLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        passLabel.setBounds(50, 260, 300, 20);
        loginPanel.add(passLabel);

        passwordField = new JPasswordField();
        styleTextField(passwordField);
        passwordField.setBounds(50, 285, 300, 40);
        loginPanel.add(passwordField);

        loginButton = createStyledButton("LOGIN", new Color(52, 152, 219));
        loginButton.setBounds(50, 350, 300, 45);
        loginButton.addActionListener(e -> handleLogin());
        loginPanel.add(loginButton);

        JLabel registerLabel = new JLabel("Don't have an account? Register here", SwingConstants.CENTER);
        registerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        registerLabel.setForeground(new Color(52, 152, 219));
        registerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLabel.setBounds(50, 410, 300, 30);
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                animateTransition("register");
            }
        });
        loginPanel.add(registerLabel);
    }

    private void createRegisterPanel() {
        registerPanel = new JPanel(null);
        registerPanel.setBackground(Color.WHITE);
        registerPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        //close btn
        JButton closeButton = createIconButton("×", 360, 10, 30, 30);
        closeButton.addActionListener(e -> System.exit(0));
        registerPanel.add(closeButton);

        //back btm
        JButton backButton = createIconButton("←", 10, 10, 30, 30);
        backButton.addActionListener(e -> animateTransition("login"));
        registerPanel.add(backButton);

        //title
        JLabel titleLabel = new JLabel("Create Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(52, 152, 219));
        titleLabel.setBounds(100, 40, 200, 30);
        registerPanel.add(titleLabel);

        //fiels
        JTextField regUsernameField = createStyledTextField();
        JTextField regEmailField = createStyledTextField();
        JTextField regFullNameField = createStyledTextField();
        JPasswordField regPasswordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();
        styleTextField(regPasswordField);
        styleTextField(confirmPasswordField);


        JLabel fullNameLabel = new JLabel("Full Name");
        fullNameLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        fullNameLabel.setBounds(50, 90, 300, 20);
        registerPanel.add(fullNameLabel);
        regFullNameField.setBounds(50, 115, 300, 40);
        registerPanel.add(regFullNameField);


        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        userLabel.setBounds(50, 165, 300, 20);
        registerPanel.add(userLabel);
        regUsernameField.setBounds(50, 190, 300, 40);
        registerPanel.add(regUsernameField);


        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        emailLabel.setBounds(50, 240, 300, 20);
        registerPanel.add(emailLabel);
        regEmailField.setBounds(50, 265, 300, 40);
        registerPanel.add(regEmailField);


        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        passLabel.setBounds(50, 315, 300, 20);
        registerPanel.add(passLabel);
        regPasswordField.setBounds(50, 340, 300, 40);
        registerPanel.add(regPasswordField);


        JLabel confirmLabel = new JLabel("Confirm Password");
        confirmLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        confirmLabel.setBounds(50, 390, 300, 20);
        registerPanel.add(confirmLabel);
        confirmPasswordField.setBounds(50, 415, 300, 40);
        registerPanel.add(confirmPasswordField);

        registerButton = createStyledButton("REGISTER", new Color(46, 204, 113));
        registerButton.setBounds(50, 475, 300, 45);  // Changed Y position
        registerButton.addActionListener(e -> {
            String username = regUsernameField.getText().trim();
            String email = regEmailField.getText().trim();
            String fullName = regFullNameField.getText().trim();
            String password = new String(regPasswordField.getPassword());
            String confirmPass = new String(confirmPasswordField.getPassword());

            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username is required!");
                return;
            }

            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Email is required!");
                return;
            }

            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                JOptionPane.showMessageDialog(this, "Invalid email format!");
                return;
            }

            if (fullName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Full name is required!");
                return;
            }

            if (password.isEmpty() || confirmPass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Password fields cannot be empty!");
                return;
            }

            if (password.length() < 6) {
                JOptionPane.showMessageDialog(this, "Password must be at least 6 characters!");
                return;
            }

            if (password.equals(confirmPass)) {
                try {
                    userDAO.createUser(username, password, email, fullName);
                    JOptionPane.showMessageDialog(this, "Registration successful!");

                    regUsernameField.setText("");
                    regEmailField.setText("");
                    regFullNameField.setText("");
                    regPasswordField.setText("");
                    confirmPasswordField.setText("");

                    animateTransition("login");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Registration failed: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Passwords don't match!");
            }
        });
        registerPanel.add(registerButton);
    }

    private void handleLogin() {
        try {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (userDAO.authenticate(username, password)) {
                User user = userDAO.findByUsername(username);
                dispose();
                new DashboardFrame(user);
            } else {
                shakeTimer.start();
                JOptionPane.showMessageDialog(this, "Invalid username or password!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Login error: " + e.getMessage());
        }
    }

    private void animateTransition(String targetCard) {
        cardLayout.show(mainPanel, targetCard);
    }


    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        styleTextField(field);
        return field;
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setBackground(Color.WHITE);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });

        return button;
    }

    private JButton createIconButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        button.setForeground(new Color(52, 152, 219));
        button.setBackground(Color.WHITE);
        button.setBorder(null);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(new Color(41, 128, 185));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(new Color(52, 152, 219));
            }
        });

        return button;
    }

    private void addDraggableMouseListener() {
        Point offset = new Point();
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                offset.setLocation(e.getX(), e.getY());
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point p = getLocation();
                setLocation(p.x + e.getX() - offset.x, p.y + e.getY() - offset.y);
            }
        });
    }

    private void loadCustomFont() {
        try {
            InputStream is = getClass().getResourceAsStream("/fonts/FontAwesome6Free-Solid-900.otf");
            iconFont = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(iconFont);
        } catch (Exception e) {
            e.printStackTrace();
            iconFont = new Font("Arial", Font.PLAIN, 24); // Fallback font
        }
    }

    private void setupIcons() {}
} 