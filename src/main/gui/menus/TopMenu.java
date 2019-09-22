package gui.menus;

import gui.menus.bylocation.LocationMenu;
import gui.menus.duedatelist.MenuTodoListWithDueDate;
import gui.menus.recurringlist.MenuRecurringList;
import model.user.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TopMenu extends JFrame implements ActionListener {
    JButton btnSeeTodoWithDueDate;
    JButton btnSeeRecurringList;
    JButton btnSeeTodoByLocation;
    JButton btnSeeAll;
    User user;

    public TopMenu(User user) {
        super("Awesome Todo List");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.user = user;
        setPreferredSize(new Dimension(800, 600));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));

        setLayout(new FlowLayout());
        //that means that when the btn is clicked,
        //this.actionPerformed(ActionEvent e) will be called.
        //You could also set a different class, if you wanted
        //to capture the response behaviour elsewhere
        btnSeeTodoWithDueDate = new JButton("See Todo List With Due Date");
        btnSeeTodoWithDueDate.setActionCommand("seeTodoListWithDueDate");
        btnSeeTodoWithDueDate.addActionListener(this);
        btnSeeRecurringList = new JButton("See Recurring Todo List");
        btnSeeRecurringList.setActionCommand("seeRecurringList");
        btnSeeRecurringList.addActionListener(this);
        btnSeeTodoByLocation = new JButton("See Todos by Location");
        btnSeeTodoByLocation.setActionCommand("seeByLocation");
        btnSeeTodoByLocation.addActionListener(this);
        btnSeeAll = new JButton("See All Todos");
        btnSeeAll.setActionCommand("seeAll");
        btnSeeAll.addActionListener(this);

        JPanel btns = new JPanel();
        btns.setLayout(new BoxLayout(btns, BoxLayout.Y_AXIS));

        btns.add(btnSeeTodoWithDueDate);
        btns.add(btnSeeRecurringList);
        btns.add(btnSeeTodoByLocation);
        btns.add(btnSeeAll);
        add(btns);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "seeTodoListWithDueDate":
                MenuTodoListWithDueDate m = new MenuTodoListWithDueDate(this.user);
                break;
            case "seeRecurringList":
                new MenuRecurringList(this.user);
                break;
            case "seeByLocation":
                new LocationMenu(user);
                break;
            case "seeAll":
                new ShowAllItems(user);
                break;
            default:
        }
        setVisible(false);
    }
}
