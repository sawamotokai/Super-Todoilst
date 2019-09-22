package gui.menus.recurringlist;

import gui.Util;
import model.item.RecurringItem;
import model.user.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditRecurringTodo extends JFrame implements ActionListener {
    JTextField taskName;
    JComboBox intervalDropDown;

    JTextField location;
    JButton edit;
    JButton back;
    User user;
    int id;
    RecurringItem item;

    public EditRecurringTodo(User user, int id) {
        super("Awesome Todo List");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.user = user;
        this.id = id;
        setPreferredSize(new Dimension(800, 600));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));

        setLayout(new FlowLayout());
        item = user.getRecurringTodoList().getItem(id);
        taskName = new JTextField(item.getTaskName());
        location = new JTextField(item.getLocation());
        edit = new JButton("Edit");
        edit.setActionCommand("edit");
        edit.addActionListener(this);
        back = new JButton("Back");
        back.setActionCommand("back");
        back.addActionListener(this);


        JPanel interval = new JPanel();
        JLabel intervalDescription = new JLabel("Interval");
        String[] intervalArray = {"daily", "weekly", "monthly", "yearly"};
        intervalDropDown = new JComboBox(intervalArray);
//        intervalDropDown.setActionCommand("interval");
        interval.add(intervalDescription);
        interval.add(intervalDropDown);

        JPanel form = new JPanel();
        form.add(taskName);
        form.add(interval);
        form.add(location);
        form.add(edit);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        add(form);
        add(back);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("edit")) {
            editUI();
            windowBack();
        } else if (e.getActionCommand().equals("back")) {
            windowBack();
        }
    }

    private void windowBack() {
        new MenuRecurringList(user);
        setVisible(false);
    }

    private void editUI() {
        String taskName = this.taskName.getText();
        String interval = (String) this.intervalDropDown.getSelectedItem();
        String location = this.location.getText();
        int id = this.id;
        user.getRecurringTodoList().editTodo(id, taskName, interval, location);
        Util.trySaving(user.getRecurringTodoList());
    }
}
