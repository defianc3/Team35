
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.Timer;

public class GameWindow extends JFrame {
	private static final long serialVersionUID = 1L; //To suppress warning
	static JFrame frame;
	JPanel panel;
	Graphics graphics;
	Graphics2D graphics2D;
	Rectangle rec;
	int maxX;
	int maxY;
	int xBoardDim;
	int yBoardDim;
	
	final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	
	GameWindow(int _xBoardDim, int _yBoardDim) {
		xBoardDim = _xBoardDim;
		yBoardDim = _yBoardDim;
		createWindow();
	}
	
	public final void createWindow() {
		setTitle("Fanorona");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 600);
		setLocationRelativeTo(null);
		rec = new Rectangle();
		if (getParent() instanceof JViewport) {
	        JViewport vp = (JViewport) getParent();
	        rec = vp.getViewRect();
	    } else {
	        rec = new Rectangle(0, 0, getWidth(), getHeight());
	    }
		maxX = (int) rec.getMaxX();
		maxY = (int) rec.getMaxY();
		
        ActionListener timerListener = new ActionListener()  
        {  
            public void actionPerformed(ActionEvent e)  
            {  
            	clearTime();
                updateScreen(false);
            }  
        };  
        Timer timer = new Timer(1000, timerListener);  
        // to make sure it doesn't wait one second at the start  
        //timer.setInitialDelay(0);  
        timer.start();
		
		addMouseListener(new MouseAdapter() {
			@Override
            public void mousePressed(MouseEvent event) {
                if (event.getButton() == MouseEvent.BUTTON1) {
                    /*int x = event.getX();
                    int y = event.getY();*/
                    updateScreen(true);
                }

                if (event.getButton() == MouseEvent.BUTTON3) {

                }
			}
		});
		setVisible(true);
		graphics = this.getGraphics();
		graphics2D = (Graphics2D) graphics;
		/* To clear the window initially, a paused is needed
		 * otherwise the graphics objects are not usable */
		try {
			Thread.sleep(1000);
			clearWindow();
			drawGrid();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	private void clearWindow() {
		graphics.setColor(Color.GRAY);
        graphics.fillRect(0, 0, maxX, maxY);
        graphics.setColor(Color.BLACK);
	}
	
	private void clearTime() {
		graphics.setColor(Color.GRAY);
        graphics.fillRect(maxX-250, 0, 240, 45);
        graphics.setColor(Color.BLACK);
	}
	
	public void drawGrid() {
		/* 40 pixel margin on all sides */
		/* Top left point is (40, 70) */
		int xSpacing = (maxX - 2*40)/xBoardDim;
		int ySpacing = (((maxY-30) - 2*40)/yBoardDim)/2;
		int xGridMin = 40;
		int yGridMin = 70;
		int xGridMax = xBoardDim * xSpacing + xGridMin;
		int yGridMax = yBoardDim * ySpacing + yGridMin;
		int xCurrent = xGridMin;
		int yCurrent = yGridMin;
		
		System.out.println("\nyGridMax: " + Integer.toString(yGridMax));
		System.out.println("yGridMin: " + Integer.toString(yGridMin));
		System.out.println("xSpacing: " + Integer.toString(xSpacing));
		xGridMax = 553;
		yGridMax = 266;
		int i = 0;
		boolean altLeft = true;
		while(i < 60) {
			if (yCurrent != yGridMax) {
				//Draw down line
				graphics2D.drawLine(xCurrent, yCurrent, xCurrent,
						yCurrent + ySpacing);
			}
			if ((xCurrent != xGridMin) && (yCurrent != yGridMax) && altLeft) {
				//Draw left diagonal
				graphics2D.drawLine(xCurrent, yCurrent, xCurrent - xSpacing,
						yCurrent + ySpacing);
			}
			if ((xCurrent != xGridMax) && (yCurrent != yGridMax) && altLeft) {
				//Draw right diagonal
				graphics2D.drawLine(xCurrent, yCurrent, xCurrent + xSpacing,
						yCurrent + ySpacing);
			}
			if (xCurrent != xGridMin) {
				//Draw left line (needed?)
			}
			if (xCurrent != xGridMax) {
				//Draw right line
				graphics2D.drawLine(xCurrent, yCurrent, xCurrent + xSpacing,
						yCurrent);
			}
			if ((xCurrent == xGridMax) && (yCurrent == yGridMax)) {
				break;
			}
			xCurrent += xSpacing;
			System.out.println("xCurrent: " + Integer.toString(xCurrent));
			if (xCurrent == xGridMax) {
				xCurrent = xGridMin;
				yCurrent += ySpacing;
			}
			altLeft = !altLeft;
			i++;
		}
	}
	
	public void updateScreen(boolean clicked) {
		Date date = new Date();  
        String time = timeFormat.format(date);
		RenderingHints renderHints = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.setRenderingHints(renderHints);
		if (clicked) {
			clearWindow();
			drawGrid();
		} else {
			clearTime();
		}
		
		
		graphics2D.drawString("Remaining move time: ", maxX-250, 40);
		graphics2D.drawString(time + " sec", maxX-100, 40);
		//graphics2D.drawLine(20, 50, maxX-20, maxY-20);
	}
}