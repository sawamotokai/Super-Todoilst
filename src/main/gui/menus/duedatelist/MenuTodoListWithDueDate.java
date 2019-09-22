package gui.menus.duedatelist;

import gui.menus.TopMenu;
import model.item.ItemWithDueDate;
import model.user.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuTodoListWithDueDate extends JFrame implements ActionListener {
    JButton create;
    JButton crossOff;
    JButton edit;
    JButton remove;
    JButton topMenu;
    User user;

    public MenuTodoListWithDueDate(User user) {
        // set up buttons
        super("Awesome Todo List");
        this.user = user;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));

        setLayout(new FlowLayout());


        create = new JButton("Create New Todo");
        crossOff = new JButton("Cross Off Todo");
        edit = new JButton("Edit Todo");
        remove = new JButton("Remove Todo");
        topMenu = new JButton("Go to Top Menu");
        create.setActionCommand("create");
        crossOff.setActionCommand("crossOff");
        edit.setActionCommand("edit");
        remove.setActionCommand("remove");
        topMenu.setActionCommand("top");
        create.addActionListener(this);
        crossOff.addActionListener(this);
        edit.addActionListener(this);
        remove.addActionListener(this);
        topMenu.addActionListener(this);

        // place buttons
        JPanel btns = new JPanel();
        btns.setLayout(new BoxLayout(btns, BoxLayout.Y_AXIS));

        btns.add(create);
        btns.add(crossOff);
        btns.add(edit);
        btns.add(remove);
        btns.add(topMenu);

        JPanel items = new JPanel();
        items.setLayout(new BoxLayout(items, BoxLayout.Y_AXIS));

        for (ItemWithDueDate i : user.getTodoListWithDueDate()) {
            JLabel item = new JLabel(i.getRepr());
            items.add(item);
        }

        add(items);
        add(btns, BorderLayout.SOUTH);
        pack();

        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("create")) {
            new CreateTodo(user);
        } else if (e.getActionCommand().equals("crossOff")) {
            new CrossOff(user);
        } else if (e.getActionCommand().equals("edit")) {
            new EditGetId(user);
        } else if (e.getActionCommand().equals("remove")) {
            new RemoveTodo(user);
        } else if (e.getActionCommand().equals("top")) {
            new TopMenu(user);
        }
        this.setVisible(false);
    }
}