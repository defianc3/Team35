
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
	final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	
	GameWindow() {
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
			Thread.sleep(400);
			clearWindow();
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
	
	public void updateScreen(boolean clicked) {
		Date date = new Date();  
        String time = timeFormat.format(date);
		RenderingHints renderHints = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.setRenderingHints(renderHints);
		if (clicked) {
			clearWindow();
		} else {
			clearTime();
		}
		
		graphics2D.drawString("Remaining move time: ", maxX-250, 40);
		graphics2D.drawString(time + " sec", maxX-100, 40);
		graphics2D.drawLine(20, 50, maxX-20, maxY-20);
	}
}