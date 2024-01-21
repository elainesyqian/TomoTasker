/* Elaine Qian and Shiloh Zheng
 * January 17th, 2024
 * Tasklist
 * This class creates a checklist of tasks, which can be added, edited, or removed (yet to be implemented)
*/

//import statements
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
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

	// boolean that saves whether or not the mouse is currently dragging the list
	public boolean mouseDragging = false;

	public Container c;
	public TomoFrame frame;

	public JButton addTask;
	public JButton closeButton;

	// arraylist that contains the textfields of each task
	public ArrayList<JTextField> tasks = new ArrayList<JTextField>();
	public ArrayList<JCheckBox> checkBoxs = new ArrayList<JCheckBox>();
	public ArrayList<JCheckBox> deleteButtons = new ArrayList<JCheckBox>();
	
	public ImageIcon trashIcon;
	
	//variables for reading file
	public static File file;
	public static BufferedWriter bw;
	public static BufferedReader br;

	public int firstMouseX;
	public int firstMouseY;
	public int firstListX;
	public int firstListY;

	public static int taskVis;

	public static JPanel panel;
	public static JScrollPane scrollPart;

	public GridBagConstraints gridLay;
	
	public ImageIcon x, plus;

	public Tasklist(Container c, TomoFrame frame) {
		// constructor of the Tasklist
		
		file = new File("tasks.txt").getAbsoluteFile();
		
		trashIcon = new ImageIcon(new ImageIcon("trash.png").getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));

		// gets the container and frame, in order to place things later
		this.c = c;
		this.frame = frame;

		plus = new ImageIcon(new ImageIcon("plus.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		addTask = new JButton("+");
		c.add(addTask);
		addTask.addActionListener(this);
		addTask.setFocusable(false);
		addTask.setMargin(new Insets(20, 33, 21, 20));
		addTask.setIcon(plus);
		
		x = new ImageIcon(new ImageIcon("x.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		closeButton = new JButton("X");
		c.add(closeButton);
		closeButton.addActionListener(this);
		closeButton.setFocusable(false);
		closeButton.setMargin(new Insets(20, 33, 21, 20));
		closeButton.setIcon(x);

		// adds a first task to the list so it doesn't start empty
		//tasks.add(new JTextField("add more tasks!", 25));
		
		// adds an action listener that detects when enter is pressed in the textfield
		//tasks.get(0).addActionListener(this);

		//checkBoxs.add(new JCheckBox());
		//deleteButtons.add(new JCheckBox());

		// creates a new panel
		panel = new JPanel();

		// sets the layout of the panel
		panel.setLayout(new GridBagLayout());
		gridLay = new GridBagConstraints(0, 0, 1, 1, 0.5, 0, GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0);
		
		gridLay.gridheight = 1;
		
		//set and rest weightx at ened of adding new components (0, then 1 after) ???? to prevent centering
		

		// creats a scrollable pane to contain the tasklist, from the panel
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
		
		//gridLay.weighty = 1;

	}

	// draws the parts of the tasklist
	public void draw(Graphics g) {
		// if the tasklist is currently supposed to be visible
		if (taskVis == 1) {
			// draws the rectangle border
			g.drawRect(listX, listY, LIST_WIDTH, 150);
			// drags the title tab that can be dragged
			g.setColor(Color.PINK);
			g.fillRect(listX, listY, LIST_WIDTH, 30);

			// sets the add task + button to be visible
			addTask.setVisible(true);
			addTask.setBounds(listX + LIST_WIDTH - 54, listY + 5, 21, 21);

			closeButton.setVisible(true);
			closeButton.setBounds(listX + LIST_WIDTH - 27, listY + 5, 21, 21);

			// show the scrollable pane
			scrollPart.setVisible(true);
			scrollPart.setBounds(listX, listY + 30, LIST_WIDTH, 125);

			// adds the TO DO title text, and positions it
			g.setColor(Color.BLACK);
			g.drawString("TO DO", listX + 10, listY + 15);

			// calls a method that will add all the tasks on screen
			drawTasks();
		} else {
			// hides the + task button
			addTask.setVisible(false);

			// hide the scrollable pane
			scrollPart.setVisible(false);

			closeButton.setVisible(false);
			drawTasks();
		}

	}

	// called from draw
	// sets all the tasks to visible
	public void drawTasks() {
		// for every task in the array of tasks
		for (int i = 0; i < tasks.size(); i++) {

			if (taskVis == 1) {
				// if the tasklist is supposed to be visible right now
				// shows the current task
				tasks.get(i).setVisible(true);
				checkBoxs.get(i).setVisible(true);
				deleteButtons.get(i).setVisible(true);
				writeToSave();
			} else {
				// don't show the current task
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
			mouseDragging = true;
		} else {
			// otherwise, don't drag the list
			mouseDragging = false;
		}

		if (mouseDragging) {
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

		// set the position of the new checkbox
		gridLay.gridx = 0;
		gridLay.gridy = checkBoxs.size() - 1;

		// adds it to the panel
		panel.add(newBox, gridLay);

		// create a new JTextFieldd
		newTask = new JTextField("type here", 25);
		
		// adds an actionlistner that detects when enter is pressed
		newTask.addActionListener(this);

		// add it to the arraylist of tasks
		tasks.add(newTask);

		gridLay.weighty = 1;
		
		// set the position of the new task textbox
		gridLay.gridy = tasks.size() - 1;
		gridLay.gridx = 1;
		
		// add the new task textbox to the panel
		panel.add(newTask, gridLay);
		
		newDeleteButton = new JCheckBox();
		
		// adds an action listener that detects when this delete task checkbox is selected
		newDeleteButton.addActionListener(this);
		newDeleteButton.setIcon(trashIcon);
		
		deleteButtons.add(newDeleteButton);
		
		gridLay.gridx = 2;
		gridLay.gridy = tasks.size() - 1;
		
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
		c.remove(scrollPart);
		
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		gridLay.weightx = 1;
		
		for (int i = 0; i < tasks.size(); i++) {
			gridLay.weighty = 0;
			
			gridLay.gridx = 0;
			gridLay.gridy = i;
			
			panel.add(checkBoxs.get(i), gridLay);
			
			gridLay.weighty = 1;
			
			gridLay.gridx = 1;
			gridLay.gridy = i;
			
			panel.add(tasks.get(i), gridLay);
			tasks.get(i).addActionListener(this);
			
			gridLay.gridx = 2;
			gridLay.gridy = i;
			
			panel.add(deleteButtons.get(i), gridLay);
			deleteButtons.get(i).addActionListener(this);
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
	
	public void readSaved() {
		String str;
		int linesRead = 0;
		
		try {
			//setup buffered reader to read from the txt file
			br = new BufferedReader(new FileReader(file));
			
			//while the next line has content
			while ((str = br.readLine()) != null) {
				tasks.add(new JTextField(str, 25));
				checkBoxs.add(new JCheckBox());
				
				if (br.readLine().equals("true")) {
					checkBoxs.get(linesRead).setSelected(true);
				}
				deleteButtons.add(new JCheckBox());
				linesRead++;
			}
			
			//if the save file was empty, write a default task
			if (linesRead == 0) {
				
				// adds a first task to the list so it doesn't start empty
				tasks.add(new JTextField("add more tasks!", 25));
				
				// adds an action listener that detects when enter is pressed in the textfield
				tasks.get(0).addActionListener(this);

				checkBoxs.add(new JCheckBox());
				deleteButtons.add(new JCheckBox());
				
				deleteButtons.get(0).addActionListener(this);
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
				
				gridLay.gridx = 2;
				gridLay.gridy = 0;
				
				panel.add(deleteButtons.get(0), gridLay);
				
				// adds an action listener that detects when the delete task checkbox is selected
				deleteButtons.get(0).addActionListener(this);
			} else {
				rewriteList();
			}
			
			//closes buffered reader
			br.close();
		} catch (Exception e) {
			// nothing will happen if crashes
		}
	}
	
	public void writeToSave() {
		try {
			bw = new BufferedWriter(new FileWriter(file));
			for (int i = 0; i < tasks.size(); i++) {
				bw.write(tasks.get(i).getText() + "\n");
				if (checkBoxs.get(i).isSelected()) {
					bw.write("true" + "\n");
				} else {
					bw.write("false" + "\n");
				}
			}
			bw.close();
			//closes the buffered reader
		} catch (Exception e) {
			// nothing will happen if crashes
		}
	}
}
