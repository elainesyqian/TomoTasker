import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.*;

import javax.swing.*;

public class Tasklist extends JPanel implements ActionListener{
	public static final int MENU_WIDTH = 350;
	public int menu_height = 85;
	
	public int listX = 100;
	public int listY = 270;
	
	public boolean mouseDragging = false;
	
	public Container c;
	public TomoFrame frame;
	
	public JButton addTask;
	public ArrayList<JTextField> tasks = new ArrayList<JTextField>();
	
	public int firstMouseX;
	public int firstMouseY;
	public int firstListX;
	public int firstListY;
	
	public int taskMoveDown = 20;
	
	public static int taskVis;
	
	public Tasklist(Container c, TomoFrame frame) {
		//super(850, 50, MENU_WIDTH, MENU_HEIGHT);
		this.c = c;
		this.frame = frame;
		
		addTask = new JButton("+");
		c.add(addTask);
		addTask.addActionListener(this);
		
		tasks.add(new JTextField("default task", 20));
		
		c.add(tasks.get(0));
		tasks.get(0).setBounds(listX + 5,listY + taskMoveDown, 200, 15);
		
		taskVis = -1;
	}
	
	public void draw(Graphics g) {
		
		if (taskVis == 1) {
			// draws the rectangle
			g.drawRect(listX, listY, MENU_WIDTH, menu_height);
			g.setColor(Color.PINK);
			g.fillRect(listX, listY, MENU_WIDTH, 30);
			
			addTask.setVisible(true);
			
			// adds the TODO title text
			g.setColor(Color.BLACK);
			g.drawString("TO DO", listX + 10, listY + 15);
			
			addTask.setBounds(listX + MENU_WIDTH - 25,listY + 35,20,20);
			
			drawTasks();
		} else {
			addTask.setVisible(false);
			drawTasks();
		}

	}
	
	public void drawTasks() {
		taskMoveDown = 20;
		for (int i = 0; i < tasks.size(); i++) {
			
			if (taskVis == 1) {
				tasks.get(i).setVisible(true);
				tasks.get(i).setBounds(listX + 5,listY + 25 + taskMoveDown, 200, 20);
				taskMoveDown = taskMoveDown + 20;
			} else {
				tasks.get(i).setVisible(false);
			}
		
		}
		
	}
	
	  public void mousePressed(MouseEvent e){
		  firstMouseX = e.getX();
		  firstMouseY = e.getY();
		  firstListX = listX;
		  firstListY = listY;
	  }
	  
		
	  public void mouseDragged(MouseEvent e){
		  if (firstMouseX >= firstListX && firstMouseX <= firstListX + MENU_WIDTH && firstMouseY >= firstListY && firstMouseY <= firstListY + 30) {
			  mouseDragging = true;
		  } else {
			  mouseDragging = false;
		  }
		  
		  if (mouseDragging) {
			  listX = e.getX() - (firstMouseX - firstListX);
			  listY = e.getY() - (firstMouseY - firstListY);
			  
			  if (listX < 0) { 
				  listX = 0;
			  } else if (listX > TomoPanel.PANEL_WIDTH  - MENU_WIDTH) {
				  listX = TomoPanel.PANEL_WIDTH - MENU_WIDTH;
			  }
			  
			  if (listY < 0) {
				  listY = 0;
			  } else if (listY + 30 > TomoPanel.PANEL_HEIGHT) {
				  listY = TomoPanel.PANEL_HEIGHT - 30;
			  }

			  repaint();
		  }
	  }
	  
	  public void actionPerformed(ActionEvent evt){
		  JTextField newTask;
			// TODO set up all buttons reactions to being clicked
			
			if (evt.getSource() == addTask) {
				
				newTask = new JTextField("add your task here!", 10);
				tasks.add(newTask);
				c.add(newTask, 0);
				
				menu_height = 65 + tasks.size()*20;
				/*
				for (int i = 0; i < tasks.size(); i++) {
					c.add(tasks.get(i));
				} */
			}
		    repaint(); //update screen to show changes
	  }
}