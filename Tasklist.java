/* Elaine Qian and Shiloh Zheng
 * January 17th, 2024
 * Tasklist
 * This class creates a checklist of tasks, which can be added, edited, or removed (yet to be implemented)
*/

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.*;

import javax.swing.*;

public class Tasklist extends JPanel implements ActionListener {
	// creates the dimensions of the list
	public static final int LIST_WIDTH = 350;
	public int list_height = 85;

	// the inital position of the list
	public int listX = 100;
	public int listY = 300;

	// boolean that saves whether or not the mouse is currently dragging the list
	public boolean mouseDragging = false;

	public Container c;
	public TomoFrame frame;

	public JButton addTask;
	
	// arraylist that contains the textfields of each task
	public ArrayList<JTextField> tasks = new ArrayList<JTextField>();
	public ArrayList<JCheckBox> checkBoxs = new ArrayList<JCheckBox>();

	public int firstMouseX;
	public int firstMouseY;
	public int firstListX;
	public int firstListY;

	public static int taskVis;
	
	public static JPanel panel;
	public static JScrollPane scrollPart;
	
	public GridBagConstraints gridLay;

	public Tasklist(Container c, TomoFrame frame) {
		// constructor of the Tasklist

		// gets the container and frame, in order to place things later
		this.c = c;
		this.frame = frame;

		// adds a + button that adds new tasks
		addTask = new JButton("+");

		// adds the + button to the container
		c.add(addTask);

		// adds an action listener to the + button
		addTask.addActionListener(this);
		
		// adds a first task to the list so it doesn't start empty
		tasks.add(new JTextField("add more tasks!", 25));
		
		checkBoxs.add(new JCheckBox());
		
		// creates a new panel
		panel = new JPanel();
		
		// sets the layout of the panel
		panel.setLayout(new GridBagLayout());
		gridLay = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,GridBagConstraints.NORTH, GridBagConstraints.NORTH, new Insets(0, 0, 0, 0), 0, 0);
		
		// sets the position of the first checkbox
		gridLay.gridx = 0;
		gridLay.gridy = 0;
		
		// adds the first checkbox to the panel
		panel.add(checkBoxs.get(0), gridLay);
		
		// sets the position of the first textfield
		gridLay.gridx = 1;
		gridLay.gridy = 0;
		
		// adds the first task textfield to the panel
		panel.add(tasks.get(0), gridLay);
		
		// creats a scrollabel pane to contain the tasklist, from the panel
		scrollPart = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
        scrollPart.setMinimumSize(new Dimension(LIST_WIDTH , list_height));
        scrollPart.setMaximumSize(new Dimension(LIST_WIDTH , list_height + 200));
        scrollPart.setPreferredSize(new Dimension(LIST_WIDTH , list_height));
		
        // adds the scrollable pane to the container
		c.add(scrollPart);

		// sets the tasklist to be initially hidden
		taskVis = -1;
		
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
			
			// show the scrollable pane
			scrollPart.setVisible(true);

			// adds the TO DO title text, and positions it
			g.setColor(Color.BLACK);
			g.drawString("TO DO", listX + 10, listY + 15);

			addTask.setBounds(listX + LIST_WIDTH - 42, listY + 5, 42, 20);
			
			scrollPart.setBounds(listX, listY + 30, LIST_WIDTH, 125);

			// calls a method that will add all the tasks on screen
			drawTasks();
		} else {
			// hides the + task button
			addTask.setVisible(false);
			
			// hide the scrollable pane
			scrollPart.setVisible(false);
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
			} else {
				// don't show the current task
				tasks.get(i).setVisible(false);
				checkBoxs.get(i).setVisible(false);
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
		// checks if the x and y values of the mouse fall within the draggable title tab when the mouse
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
		
		JTextField newTask;
		JCheckBox newBox;
		
		if (evt.getSource() == addTask) {
			// if the add new task + button was pressed
			
			// remove the current scrollable pane from the screen
			c.remove(scrollPart);
			
			//create a new checkbox
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
			
			// add it to the arraylist of tasks
			tasks.add(newTask);
			
			// set the position of the new task textbox
			gridLay.gridy = tasks.size() - 1;
			gridLay.gridx = 1;
			
			// add the new task textbox to the panel
			panel.add(newTask, gridLay);
			
			// update the list borders to fit the additional task
			list_height = tasks.size() * 25;
			
			// changes the size of the panel to fit the new task
			panel.setPreferredSize(new Dimension(LIST_WIDTH, list_height));
			
			// creates a new scrollable pane based off the panel, with the new task
			scrollPart = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

			// adds the new scrollable panel to the screen
			c.add(scrollPart, 2);
			
			// refreshes the screen
			scrollPart.revalidate();
			this.revalidate();
			c.revalidate();
			frame.revalidate();
			frame.repaint();
		}
		repaint(); // update screen to show changes
	}
}
