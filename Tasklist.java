/* Elaine Qian and Shiloh Zheng
 * January 21st, 2024
 * Tasklist
 * This class creates a checklist of tasks, which can be added, edited, or removed
*/

//import statements
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import javax.swing.*;

public class Tasklist extends JPanel implements ActionListener {
	// creates the dimensions of the list
	public static final int LIST_WIDTH = 350;
	public int list_height = 45;

	// the inital position of the list
	public int listX = 100;
	public int listY = 300;

	public Container c;
	public TomoFrame frame;

	public JButton addTask;
	public JButton closeButton;

	// arraylist that contains the textfields of each task
	public ArrayList<JTextField> tasks = new ArrayList<JTextField>();
	public ArrayList<JCheckBox> checkBoxs = new ArrayList<JCheckBox>();
	public ArrayList<JCheckBox> deleteButtons = new ArrayList<JCheckBox>();

	public ImageIcon trashIcon;

	// variables for reading file
	public static File file;
	public static BufferedWriter bw;
	public static BufferedReader br;

	// variables for mouse dragging
	public int firstMouseX;
	public int firstMouseY;
	public int firstListX;
	public int firstListY;

	// variable that controls visibility
	public static int taskVis;

	public static JPanel panel;
	public static JScrollPane scrollPart;

	public GridBagConstraints gridLay;

	public ImageIcon x, plus;

	public Tasklist(Container c, TomoFrame frame) {
		// constructor of the Tasklist

		// get the file with the saved tasks
		file = new File("tasks.txt").getAbsoluteFile();

		// get the trashcan icon for the delete checkboxes
		trashIcon = new ImageIcon(new ImageIcon("trash.png").getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));

		// gets the container and frame, in order to place things later
		this.c = c;
		this.frame = frame;

		// get the plus icon
		plus = new ImageIcon(new ImageIcon("plus.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		// creates the add task button, and adds it onscreen
		addTask = new JButton("+");
		c.add(addTask);
		// adds an ActionListener to detect when it's clicked
		addTask.addActionListener(this);
		addTask.setFocusable(false);
		addTask.setMargin(new Insets(20, 33, 21, 20));
		// adds the plus sign icon to the button
		addTask.setIcon(plus);

		// gets the x icon
		x = new ImageIcon(new ImageIcon("x.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		// creates the close window button, and adds it onscreen
		closeButton = new JButton("X");
		c.add(closeButton);
		// adds an ActionListener to detect when it's clicked
		closeButton.addActionListener(this);
		closeButton.setFocusable(false);
		closeButton.setMargin(new Insets(20, 33, 21, 20));
		// adds the x icon to the button
		closeButton.setIcon(x);

		// creates a new panel
		panel = new JPanel();

		// sets the layout of the panel
		panel.setLayout(new GridBagLayout());
		gridLay = new GridBagConstraints(0, 0, 1, 1, 0.5, 0, GridBagConstraints.PAGE_START,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);

		gridLay.gridheight = 1;

		// creates a scrollable pane to contain the tasklist, from the panel
		scrollPart = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		scrollPart.setMinimumSize(new Dimension(LIST_WIDTH, list_height + 100));
		scrollPart.setMaximumSize(new Dimension(LIST_WIDTH, list_height + 200));
		scrollPart.setPreferredSize(new Dimension(LIST_WIDTH, list_height));

		// adds the scrollable pane to the container
		c.add(scrollPart);

		readSaved();

		// sets the tasklist to be initially hidden
		taskVis = -1;

	}

	// draws the parts of the tasklist
	public void draw(Graphics g) {

		g.setFont(new Font("Tahoma", Font.PLAIN, 17));

		// if the tasklist is currently supposed to be visible
		if (taskVis == 1) {
			// draws the rectangle border
			g.drawRect(listX, listY, LIST_WIDTH, 150);
			// drags the title tab that can be dragged
			g.setColor(Color.PINK);
			g.fillRect(listX, listY, LIST_WIDTH, 30);

			// sets the add task + button to be visible, and positions it onscreen
			addTask.setVisible(true);
			addTask.setBounds(listX + LIST_WIDTH - 54, listY + 5, 21, 21);

			// sets the close window button to be visible, and positions it onscreen
			closeButton.setVisible(true);
			closeButton.setBounds(listX + LIST_WIDTH - 27, listY + 5, 21, 21);

			// show the scrollable pane
			scrollPart.setVisible(true);
			scrollPart.setBounds(listX, listY + 30, LIST_WIDTH, 125);

			// adds the TO DO title text, and positions it
			g.setColor(Color.BLACK);
			g.drawString("TO DO", listX + 10, listY + 21);

			// calls a method that will add all the tasks on screen
			drawTasks();
		} else {
			// hides the + task button
			addTask.setVisible(false);

			// hide the scrollable pane
			scrollPart.setVisible(false);

			// hides the close window button
			closeButton.setVisible(false);
			drawTasks();
		}

	}

	// called from draw
	// sets all the tasks to visible
	public void drawTasks() {
		// for every task in the array of tasks
		for (int i = 0; i < tasks.size(); i++) {
			// set the font of the text fields
			tasks.get(i).setFont(new Font("Tahoma", Font.PLAIN, 12));

			if (taskVis == 1) {
				// if the tasklist is supposed to be visible right now
				// shows the current task, its checkbox, and its delete button
				tasks.get(i).setVisible(true);
				checkBoxs.get(i).setVisible(true);
				deleteButtons.get(i).setVisible(true);
				writeToSave();
			} else {
				// don't show the current task or its boxs
				tasks.get(i).setVisible(false);
				checkBoxs.get(i).setVisible(false);
				deleteButtons.get(i).setVisible(false);
			}

		}

	}

	// called from TomoPanel when the mouse is pressed
	// saves the x and y values of the mouse press and list
	public void mousePressed(MouseEvent e) {
		// gets the x and y of the mouse and list when the mouse is pressed
		firstMouseX = e.getX();
		firstMouseY = e.getY();
		firstListX = listX;
		firstListY = listY;
	}

	// called from TomoPanel when the mouse is dragged
	// and uses the x and y values at the original click
	// to see if the list should be dragged
	public void mouseDragged(MouseEvent e) {
		// checks if the x and y values of the mouse fall within the draggable title tab
		// when the mouse
		// was first pressed down
		if (firstMouseX >= firstListX && firstMouseX <= firstListX + LIST_WIDTH && firstMouseY >= firstListY
				&& firstMouseY <= firstListY + 30) {
			// if the mouse was in the title tab, allow for the list to be dragged
			// if the list is currently being dragged, make it follow the mouse
			listX = e.getX() - (firstMouseX - firstListX);
			listY = e.getY() - (firstMouseY - firstListY);

			// if the mouse drags the title tab offscreen, move it back onscreen
			if (listX < 0) {
				listX = 0;
			} else if (listX > TomoPanel.PANEL_WIDTH - LIST_WIDTH) {
				listX = TomoPanel.PANEL_WIDTH - LIST_WIDTH;
			}

			if (listY < 0) {
				listY = 0;
			} else if (listY + 30 > TomoPanel.PANEL_HEIGHT) {
				listY = TomoPanel.PANEL_HEIGHT - 30;
			}

			repaint(); // updates screen to show changes
		}
	}

	// checks what button was pressed and performs the corresponding action
	public void actionPerformed(ActionEvent evt) {

		if (evt.getSource() == addTask) {
			// if the add new task + button was pressed

			// remove the current scrollable pane from the screen
			c.remove(scrollPart);

			// calls a method that adds a new task
			addTask();

			// adds the new scrollable panel to the screen
			c.add(scrollPart, 2);

			// refreshes the screen
			scrollPart.revalidate();

		} else if (evt.getSource() == closeButton) {
			taskVis = -1;

		} else if (tasks.contains(evt.getSource())) {

			// remove the current scrollable pane from the screen
			c.remove(scrollPart);

			// calls a method that adds a new task
			addTask();

			// adds the new scrollable panel to the screen
			c.add(scrollPart, 2);

			// refreshes the screen
			scrollPart.revalidate();

		} else if (deleteButtons.contains(evt.getSource())) {

			tasks.remove(deleteButtons.indexOf(evt.getSource()));
			checkBoxs.remove(deleteButtons.indexOf(evt.getSource()));
			deleteButtons.remove(deleteButtons.indexOf(evt.getSource()));

			rewriteList();
			writeToSave();

		}
		repaint(); // update screen to show changes
	}

	public void addTask() {
		JTextField newTask;
		JCheckBox newBox;
		JCheckBox newDeleteButton;

		gridLay.weighty = 0;

		// create a new checkbox
		newBox = new JCheckBox();

		// add it to the arraylist of checkboxs
		checkBoxs.add(newBox);

		// sets the position of the new checkbox
		gridLay.gridx = 0;
		gridLay.gridy = checkBoxs.size() - 1;

		// adds it to the panel
		panel.add(newBox, gridLay);

		// create a new JTextFieldd
		newTask = new JTextField("type here", 25);

		// adds an ActionListener that detects when enter is pressed
		newTask.addActionListener(this);

		// add it to the arraylist of tasks
		tasks.add(newTask);

		gridLay.weighty = 1;

		// sets the position of the new task textbox
		gridLay.gridy = tasks.size() - 1;
		gridLay.gridx = 1;

		// add the new task textbox to the panel
		panel.add(newTask, gridLay);

		// creates a new JCheckBox for the delete button
		newDeleteButton = new JCheckBox();

		// adds an action listener that detects when this delete task checkbox is
		// selected
		newDeleteButton.addActionListener(this);
		newDeleteButton.setIcon(trashIcon);

		// adds it to the arraylist of delete buttons
		deleteButtons.add(newDeleteButton);

		// sets the position of the delete button
		gridLay.gridx = 2;
		gridLay.gridy = tasks.size() - 1;

		// adds it to the panel
		panel.add(newDeleteButton, gridLay);

		// update the list borders to fit the additional task
		list_height = tasks.size() * 25;

		// changes the size of the panel to fit the new task
		panel.setPreferredSize(new Dimension(LIST_WIDTH, list_height));

		// creates a new scrollable pane based off the panel, with the new task
		scrollPart = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}

	public void rewriteList() {
		// remove the current scrollable area from the screen
		c.remove(scrollPart);

		// create a fresh panel
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		gridLay.weightx = 1;

		// for every task in the array list of tasks
		for (int i = 0; i < tasks.size(); i++) {
			// position its JCheckBox

			gridLay.weighty = 0;

			gridLay.gridx = 0;
			gridLay.gridy = i;

			// add it on panel
			panel.add(checkBoxs.get(i), gridLay);

			// position the task
			gridLay.weighty = 1;

			gridLay.gridx = 1;
			gridLay.gridy = i;

			// add it on panel
			panel.add(tasks.get(i), gridLay);
			// adds an ActionListener that detects when the enter key is pressed on it
			tasks.get(i).addActionListener(this);

			// position the delete check box
			gridLay.gridx = 2;
			gridLay.gridy = i;

			// add it to the panel
			panel.add(deleteButtons.get(i), gridLay);
			deleteButtons.get(i).addActionListener(this);
			// sets its icon to a trash can
			deleteButtons.get(i).setIcon(trashIcon);
		}

		// update the list borders
		list_height = tasks.size() * 25;

		// changes the size of the panel to fit the new task
		panel.setPreferredSize(new Dimension(LIST_WIDTH, list_height));

		// creates a new scrollable pane based off the panel, with the new task
		scrollPart = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		// adds the new scrollable panel to the screen
		c.add(scrollPart, 2);

		// refreshes the screen
		scrollPart.revalidate();
	}

	// this method read in tasks saved in the file
	// and adds them back into the arraylist and onto the screen
	public void readSaved() {
		String str;
		int linesRead = 0;

		try {
			// setup buffered reader to read from the txt file
			br = new BufferedReader(new FileReader(file));

			// while the next line has content
			while ((str = br.readLine()) != null) {
				// add the task with the read in content to a JTextField
				// then add that JTextField to the array of tasks
				tasks.add(new JTextField(str, 25));
				// creates a new JCheckBox
				// and adds it to the array of checkboxes
				checkBoxs.add(new JCheckBox());

				// if the next line says that the task was checked off
				if (br.readLine().equals("true")) {
					// check the new checkbox
					checkBoxs.get(linesRead).setSelected(true);
				}
				// add a delete button to the array of delete buttons
				deleteButtons.add(new JCheckBox());
				// increase the amount of tasks read in by 1
				linesRead++;
			}

			// if the save file was empty, write a default task
			if (linesRead == 0) {

				// adds a first task to the list so it doesn't start empty
				tasks.add(new JTextField("add more tasks!", 25));

				// adds an action listener that detects when enter is pressed in the textfield
				tasks.get(0).addActionListener(this);

				// creats a new check box and delete button
				checkBoxs.add(new JCheckBox());
				deleteButtons.add(new JCheckBox());

				// adds an ActionListener to the delete button
				deleteButtons.get(0).addActionListener(this);
				// sets it's icon to a trashcan
				deleteButtons.get(0).setIcon(trashIcon);

				// sets the position of the first checkbox
				gridLay.weighty = 0;

				gridLay.gridx = 0;
				gridLay.gridy = 0;

				// adds the first checkbox to the panel
				panel.add(checkBoxs.get(0), gridLay);

				gridLay.weighty = 1;

				// sets the position of the first textfield
				gridLay.gridx = 1;
				gridLay.gridy = 0;

				// adds the first task textfield to the panel
				panel.add(tasks.get(0), gridLay);

				// sets the position of the delete button
				gridLay.gridx = 2;
				gridLay.gridy = 0;

				// adds the delete button to the screen
				panel.add(deleteButtons.get(0), gridLay);

				// adds an action listener that detects when the delete task checkbox is
				// selected
				deleteButtons.get(0).addActionListener(this);
			} else {
				// if there were tasks read in
				// add them to screen using rewriteList
				rewriteList();
			}

			// closes buffered reader
			br.close();
		} catch (Exception e) {
			// nothing will happen if crashes
		}
	}

	// a method that writes the contents of the current tasks
	// and whether or not they've been checked
	// into a save file
	public void writeToSave() {
		try {
			// create a new bufferedWriter
			// to write information to the file
			bw = new BufferedWriter(new FileWriter(file));
			// for every task in the array of tasks
			for (int i = 0; i < tasks.size(); i++) {
				// write down the text in the task's JTextField in one line
				bw.write(tasks.get(i).getText() + "\n");
				// check if it's checkbox was checked
				if (checkBoxs.get(i).isSelected()) {
					// if it was, write true in the next line
					bw.write("true" + "\n");
				} else {
					// otherwise, write false in the next line
					bw.write("false" + "\n");
				}
			}
			bw.close();
			// closes the buffered writer
		} catch (Exception e) {
			// nothing will happen if crashes
		}
	}
}
