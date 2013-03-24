
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

public class GameWindow extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	static JFrame frame;
	JPanel panel;
	Graphics graphics;
	Graphics2D graphics2D;
	int clicks = 0;
	GameWindow() {
		createWindow();
	}
	
	public final void createWindow() {
		frame = new JFrame();
		panel = new JPanel();
		panel.setLayout(new GridLayout(3,3));
		graphics = this.getGraphics();
		graphics2D = (Graphics2D) graphics;
		
		setTitle("Fanorona");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400,400);
		setLocationRelativeTo(null);
   
        final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");  
        ActionListener timerListener = new ActionListener()  
        {  
            public void actionPerformed(ActionEvent e)  
            {  
                Date date = new Date();  
                String time = timeFormat.format(date);                
                updateTime(Integer.toString(date.getSeconds()));
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
                    int x = event.getX();
                    int y = event.getY();
                    clicks++;
                    drawBoard();
                    updateTime("1235");
                }

                if (event.getButton() == MouseEvent.BUTTON3) {

                }
			}
		});
	}
	
	private void clearWindow() {
		graphics = this.getGraphics();
		graphics2D = (Graphics2D) graphics;
		Rectangle rec = new Rectangle();
		if (getParent() instanceof JViewport) {
	        JViewport vp = (JViewport) getParent();
	        rec = vp.getViewRect();
	    } else {
	        rec = new Rectangle(0, 0, getWidth(), getHeight());
	    }
		graphics2D.clearRect(0, 0, (int)rec.getMaxX(), (int)rec.getMaxY());
	}
	
	public void updateTime(String time) {
		graphics = this.getGraphics();
		graphics2D = (Graphics2D) graphics;
		clearWindow();
		graphics2D.drawString(time, 40, 40);
	}
	
	public void drawBoard() {
		graphics = this.getGraphics();
		graphics2D = (Graphics2D) graphics;
		Rectangle rec = new Rectangle();
		if (getParent() instanceof JViewport) {
	        JViewport vp = (JViewport) getParent();
	        rec = vp.getViewRect();
	    } else {
	        rec = new Rectangle(0, 0, getWidth(), getHeight());
	    }
		
		int winWidth = (int) rec.getMaxX();
		int winHeight = (int) rec.getMaxY();
		int xOrg = rec.x;
		int yOrg = rec.y;
		
		RenderingHints renderHints = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		graphics2D.setRenderingHints(renderHints);
		graphics2D.clearRect(0, 0, winWidth, winHeight);
		
		
		graphics2D.drawLine(winHeight-20, yOrg+20, winWidth-20, winHeight-20);
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}