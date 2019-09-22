package gui.menus.duedatelist;

import gui.Util;
import model.item.ImportantItemWithDueDate;
import model.item.ItemWithDueDate;
import model.user.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateTodo extends JFrame implements ActionListener {

    User user;
    JTextField taskName;
    JTextField dueDate;
    JTextField location;
    JCheckBox isImportant;
    JButton create;
    JButton back;
    JLabel taskNameLabel;
    JLabel duedateLable;
    JLabel locationLabel;
    JPanel taskPanel;
    JPanel dueDatePanel;
    JPanel locationPanel;

    public CreateTodo(User user) {
        super("Awesome Todo List");
        this.user = user;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));

        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));

        taskNameLabel = new JLabel("Task Name");
        duedateLable = new JLabel("Enter in: yyyy-MM-dd");
        locationLabel = new JLabel("Location. Leave blank if no location.");

        taskPanel = new JPanel();
        dueDatePanel = new JPanel();
        locationPanel = new JPanel();

        setLayout(new FlowLayout());
        taskName = new JTextField(5);
        dueDate = new JTextField(5);
        location = new JTextField(5);
        isImportant = new JCheckBox("Important");
        create = new JButton("Create");
        create.setActionCommand("create");
        create.addActionListener(this);
        back = new JButton("Back");
        back.setActionCommand("back");
        back.addActionListener(this);

        taskPanel.add(taskNameLabel);
        taskPanel.add(taskName);
        dueDatePanel.add(duedateLable);
        dueDatePanel.add(dueDate);
        locationPanel.add(locationLabel);
        locationPanel.add(location);



        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        form.add(taskPanel);
        form.add(dueDatePanel);
        form.add(locationPanel);
        form.add(isImportant);
        form.add(create);
        add(form);
        add(back);
        pack();

        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("create")) {
            String taskName = this.taskName.getText();
            String dateInput = this.dueDate.getText();
            String location = this.location.getText();
            ItemWithDueDate item;
            if (isImportant.isSelected()) {
                item = new ImportantItemWithDueDate(taskName, dateInput, false, user.getAvailableId());
            } else {
                item = new ItemWithDueDate(taskName, dateInput, false, user.getAvailableId());
            }
            user.getTodoListWithDueDate().addTodo(item);
            item.setLocation(location);
            Util.trySaving(user.getTodoListWithDueDate());
            windowback();
        } else if (e.getActionCommand().equals("back")) {
            windowback();
        }
    }


    private void windowback() {
        new MenuTodoListWithDueDate(user);
        setVisible(false);
    }
}
