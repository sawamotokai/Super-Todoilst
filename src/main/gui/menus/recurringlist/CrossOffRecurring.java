package gui.menus.recurringlist;

import gui.Util;
import model.item.RecurringItem;
import model.user.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CrossOffRecurring extends JFrame implements ActionListener {

    User user;
    JComboBox intervalDropDown;
    JPanel items;
    JLabel msg;
    JTextField id;
    JButton submit;
    JButton back;

    public CrossOffRecurring(User user) {
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

//        JPanel interval = new JPanel();
//        JLabel intervalDescription = new JLabel("Interval");
//        String[] intervalArray = {"daily", "weekly", "monthly", "yearly"};
//        intervalDropDown = new JComboBox(intervalArray);
////        intervalDropDown.setActionCommand("interval");
//        interval.add(intervalDescription);
//        interval.add(intervalDropDown);
//        intervalDropDown.addItemListener(this);
//        items = new JPanel();
//        items.setLayout(new BoxLayout(items, BoxLayout.Y_AXIS));
        items = new JPanel();
        items.setLayout(new BoxLayout(items, BoxLayout.Y_AXIS));

        for (RecurringItem i : user.getRecurringTodoList()) {
            JLabel item = new JLabel(i.getRepr());
            items.add(item);
        }

//        showItemsByInterval((String) intervalDropDown.getSelectedItem());

        add(items);


        add(msg);
//        add(interval);
        add(id);
        add(submit);
        add(back);
        pack();
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
            Util.tryCrossOff(user.getRecurringTodoList(), idInt);
            Util.trySaving(user.getRecurringTodoList());

            new MenuRecurringList(user);
            setVisible(false);
        } else if (e.getActionCommand().equals("back")) {
            new MenuRecurringList(user);
            setVisible(false);
        }
    }

//    @Override
//    public void itemStateChanged(ItemEvent e) {
//        if (e.getSource() == intervalDropDown) {
//            System.out.println(intervalDropDown.getSelectedItem());
//            showItemsByInterval((String) intervalDropDown.getSelectedItem());
//        }
//    }
//
//    private void showItemsByInterval(String interval) {
//        items = new JPanel();
//        items.setLayout(new BoxLayout(items, BoxLayout.Y_AXIS));
//
//        for (RecurringItem i: user.getRecurringTodoList()) {
//            if (i.getInterval().equals(interval)) {
//                JLabel item = new JLabel(i.getRepr());
//                items.add(item);
//            }
//        }
//    }
}

