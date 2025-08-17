package br.ifg.gymbro.output;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class ConsoleWindow extends JFrame {
    private JTextArea outputArea;
    private JTextField inputField;
    private PipedOutputStream pipedOut;
    private PipedInputStream pipedIn;
    private PrintStream printStream;

    public ConsoleWindow() {
        super("Gymbro Console");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the window

        // Set up the output area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setBackground(Color.BLACK);
        outputArea.setForeground(Color.WHITE);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);

        // Auto-scroll to the bottom
        DefaultCaret caret = (DefaultCaret) outputArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove border

        // Set up the input field
        inputField = new JTextField();
        inputField.setBackground(Color.BLACK);
        inputField.setForeground(Color.WHITE);
        inputField.setCaretColor(Color.WHITE); // Set caret color to white
        inputField.setFont(new Font("Monospaced", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY)); // Subtle border

        // Set up piped streams for input/output redirection
        pipedOut = new PipedOutputStream();
        pipedIn = new PipedInputStream();
        try {
            pipedOut.connect(pipedIn);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a custom PrintStream to redirect System.out and System.err
        printStream = new PrintStream(pipedOut) {
            @Override
            public void write(byte[] buf, int off, int len) {
                super.write(buf, off, len);
                String text = new String(buf, off, len);
                SwingUtilities.invokeLater(() -> outputArea.append(text));
            }
        };

        // Add action listener for input field
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = inputField.getText() + "\n"; // Add newline to simulate Enter key
                outputArea.append("> " + input); // Echo input to output area
                try {
                    pipedOut.write(input.getBytes());
                    pipedOut.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                inputField.setText(""); // Clear input field
            }
        });

        // Layout components
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(inputField, BorderLayout.SOUTH);
    }

    public PrintStream getPrintStream() {
        return printStream;
    }

    public InputStream getInputStream() {
        return pipedIn;
    }
}
