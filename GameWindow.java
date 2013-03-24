
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JViewport;

public class GameWindow extends JFrame implements ActionListener, WindowListener {
	private static final long serialVersionUID = 1L;
	static JFrame frame;
	GameWindow() {
		createWindow();
	}
	
	public final void createWindow() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		
		setTitle("Fanorona");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400,400);
		setLocationRelativeTo(null);
		
		addMouseListener(new MouseAdapter() {
			@Override
            public void mousePressed(MouseEvent event) {
                if (event.getButton() == MouseEvent.BUTTON1) {
                    int x = event.getX();
                    int y = event.getY();
                    drawBoard();
                }

                if (event.getButton() == MouseEvent.BUTTON3) {

                }
			}
		});
	}
	
	public void drawBoard() {
		Graphics graphics = this.getGraphics();
		Graphics2D graphics2D = (Graphics2D) graphics;
		Rectangle rec = new Rectangle();
		//graphics.getClipBounds(rec);
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
		
		graphics2D.drawLine(xOrg+20, yOrg+20, winWidth-20, winHeight-20);
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void windowClosing(WindowEvent event) {
		
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}