package gui.menus.duedatelist;

import gui.Util;
import model.item.ItemWithDueDate;
import model.user.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CrossOff extends JFrame implements ActionListener {

    User user;
    JLabel msg;
    JTextField id;
    JButton submit;
    JButton back;


    public CrossOff(User user) {
        super("Awesome Todo List");
        this.user = user;
        this.msg = new JLabel("Enter the Id of the Todo You Want to Cross Off");
        id = new JTextField("id");
        submit = new JButton("Cross Off");
        submit.setActionCommand("submit");
        submit.addActionListener(this);
        back = new JButton("Back");
        back.setActionCommand("back");
        back.addActionListener(this);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));

        setLayout(new FlowLayout());

        JPanel items = new JPanel();
        items.setLayout(new BoxLayout(items, BoxLayout.Y_AXIS));

        for (ItemWithDueDate i : user.getTodoListWithDueDate()) {
            JLabel item = new JLabel(i.getRepr());
            items.add(item);
        }
        add(items);

        add(msg);
        add(id);
        add(submit);
        pack();
        add(back);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("submit")) {
            int idInt;
            try {
                idInt  = Integer.parseInt(id.getText());
            } catch (Exception exc) {
                idInt = -1;
                // if user input cannot be parsed to int, set it to -1 so it never matches with id of any item.
            }
            // turn isDone prop if id matches
            Util.tryCrossOff(user.getTodoListWithDueDate(), idInt);
            Util.trySaving(user.getTodoListWithDueDate());

            new MenuTodoListWithDueDate(user);
            setVisible(false);
        } else if (e.getActionCommand().equals("back")) {
            new MenuTodoListWithDueDate(user);
            setVisible(false);
        }
    }
}
