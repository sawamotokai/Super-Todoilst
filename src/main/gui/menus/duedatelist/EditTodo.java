package gui.menus.duedatelist;

import gui.Util;
import model.item.ItemWithDueDate;
import model.user.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditTodo extends JFrame implements ActionListener {

    JTextField taskName;
    JTextField dueDate;
    JTextField location;
    JCheckBox isImportant;
    JButton edit;
    JButton back;
    User user;
    int id;
    ItemWithDueDate item;

    public EditTodo(User user, int id) {
        super("Awesome Todo List");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.user = user;
        this.id = id;
        setPreferredSize(new Dimension(800, 600));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));

        setLayout(new FlowLayout());
        item = user.getTodoListWithDueDate().getItem(id);
        taskName = new JTextField(item.getTaskName());
        dueDate = new JTextField(item.getDueDateInString());
        location = new JTextField(item.getLocation());
        isImportant = new JCheckBox("Important", item.getIsImportant());
        edit = new JButton("Edit");
        edit.setActionCommand("edit");
        edit.addActionListener(this);
        back = new JButton("Back");
        back.setActionCommand("back");
        back.addActionListener(this);

        JPanel form = new JPanel();
        form.add(taskName);
        form.add(dueDate);
        form.add(location);
        form.add(isImportant);
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
        setVisible(false);
        new MenuTodoListWithDueDate(user);
    }

    private void editUI() {
        String taskName = this.taskName.getText();
        String dueDate = this.dueDate.getText();
        String location = this.location.getText();
        int id = this.id;
        user.getTodoListWithDueDate().editTodo(id, taskName, dueDate, location);
        Util.trySaving(user.getTodoListWithDueDate());
    }
}
