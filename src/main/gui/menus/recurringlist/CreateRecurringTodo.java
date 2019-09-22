package gui.menus.recurringlist;

import gui.Util;
import model.item.RecurringItem;
import model.user.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateRecurringTodo extends JFrame implements ActionListener {

    User user;
    JTextField taskName;
    JComboBox intervalDropDown;
    JTextField location;
    JButton create;
    JButton back;
    JLabel taskNameLabel;
    JLabel locationLabel;
    JPanel taskPanel;
    JPanel locationPanel;

    public CreateRecurringTodo(User user) {
        super("Awesome Todo List");
        this.user = user;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));

        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));

        taskNameLabel = new JLabel("Task Name");
        locationLabel = new JLabel("Location. Leave blank if no location.");

        taskPanel = new JPanel();
        locationPanel = new JPanel();

        setLayout(new FlowLayout());
        taskName = new JTextField(5);
        location = new JTextField(5);
        create = new JButton("Create");
        create.setActionCommand("create");
        create.addActionListener(this);
        back = new JButton("Back");
        back.setActionCommand("back");
        back.addActionListener(this);


        taskPanel.add(taskNameLabel);
        taskPanel.add(taskName);
        locationPanel.add(locationLabel);
        locationPanel.add(location);

        JPanel interval = new JPanel();
        JLabel intervalDescription = new JLabel("Interval");
        String[] intervalArray = {"daily", "weekly", "monthly", "yearly"};
        intervalDropDown = new JComboBox(intervalArray);
        interval.add(intervalDescription);
        interval.add(intervalDropDown);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        form.add(taskPanel);
        form.add(interval);
        form.add(locationPanel);
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
            String interval = (String) this.intervalDropDown.getSelectedItem();
            String location = this.location.getText();
            RecurringItem item = new RecurringItem(taskName, interval, false, user.getAvailableId());
            user.getRecurringTodoList().addTodo(item);
            item.setLocation(location);
            Util.trySaving(user.getRecurringTodoList());
            windowback();
        } else if (e.getActionCommand().equals("back")) {
            windowback();
        }
    }


    private void windowback() {
        new MenuRecurringList(user);
        setVisible(false);
    }
}
