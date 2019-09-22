package gui.menus.recurringlist;

import gui.Util;
import model.item.RecurringItem;
import model.user.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class EditTodoGetIdRecurring extends JFrame implements ActionListener {
    User user;
    JLabel msg;
    JTextField id;
    JButton submit;
    JButton back;


    public EditTodoGetIdRecurring(User user) {


        super("Awesome Todo List");
        this.user = user;
        this.msg = new JLabel("Enter the Id of Todo You Want to Edit");
        id = new JTextField("id");
        submit = new JButton("Edit");
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

        for (RecurringItem i : user.getRecurringTodoList()) {
            JLabel item = new JLabel(i.getRepr());
            items.add(item);
        }
        add(items);


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
                setVisible(false);
                new EditTodoGetIdRecurring(user);
                return;
                // if user input cannot be parsed to int, set it to -1 so it never matches with id of any item.
            }
            // turn isDone prop if id matches
            new EditRecurringTodo(user, idInt);
            Util.trySaving(user.getTodoListWithDueDate());
            setVisible(false);
        } else if (e.getActionCommand().equals("back")) {
            windowBack();
        }
    }

    private void windowBack() {
        new MenuRecurringList(user);
        setVisible(false);
    }
}
