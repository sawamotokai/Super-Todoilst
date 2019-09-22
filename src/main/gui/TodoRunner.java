package gui;

import gui.menus.TopMenu;
import model.user.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TodoRunner extends JFrame implements ActionListener {
    JFrame mainFrame;
    Container contentPane;
    JTextField field;
    JPanel btnPanel;
    JButton startBtn;
    JLabel label;

    User user;

    public static void main(String[] args) {
        new TodoRunner();
    }

    TodoRunner() {
        super("Awesome Todo List");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));

        setLayout(new FlowLayout());
        JButton btn = new JButton("Start");
        btn.setActionCommand("start");
        btn.addActionListener(this); //sets "this" class as an action listener for btn.
        //that means that when the btn is clicked,
        //this.actionPerformed(ActionEvent e) will be called.
        //You could also set a different class, if you wanted
        //to capture the response behaviour elsewhere
        label = new JLabel("What is your name?");
        field = new JTextField(5);
        add(field);
        add(btn);
        add(label);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(true);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("start")) {
            this.user = new User(field.getText());
            // instantiate map and lists from username
            Util.tryLoad(user.getTodoListWithDueDate());
            Util.tryLoad(user.getRecurringTodoList());
            TopMenu topMenu = new TopMenu(user);
            topMenu.setVisible(true);
            this.setVisible(false);

        }
    }



}
