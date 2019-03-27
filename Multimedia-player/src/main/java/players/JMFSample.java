package main.java.players;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.EndOfMediaEvent;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.PrefetchCompleteEvent;
import javax.media.RealizeCompleteEvent;
import javax.media.Time;

public class JMFSample implements ControllerListener {
	public static void main(String[] args) {
		JMFSample sp=new JMFSample();
		sp.play();
	}
	private Frame f;
	private Player player;
	//private Panel panel;
	private Component visual;
	private Component control = null;

	public void play() {
		f = new Frame("JMF Sample");
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				if (player != null) {
					player.close();
				}
				System.exit(0);
			}
		});
		f.setSize(640, 360);
		f.setVisible(true);

		URL url = null;
		
		// video url goes here
		try {
			url = new URL("file:/F:/4.mov");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			// create a player object using Manager.createPlayer
			// core controll
			player = Manager.createPlayer(url);
		} catch (NoPlayerException e1) {

			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		player.addControllerListener(this);
		player.realize();
	}

	private int videoWidth = 0;
	private int videoHeight = 0;
	private int controlHeight = 30;
	private int insetWidth = 10;
	private int insetHeight = 30;

	// player event listener
	public void controllerUpdate(ControllerEvent ce) {
		if (ce instanceof RealizeCompleteEvent) {
			player.prefetch();
		} else if (ce instanceof PrefetchCompleteEvent) {
			if (visual != null)
				return;

			if ((visual = player.getVisualComponent()) != null) {
				Dimension size = visual.getPreferredSize();
				videoWidth = size.width;
				videoHeight = size.height;
				f.add(visual);
			} else {
				videoWidth = 640;
			}
			// player control bar then add to frame
			if ((control = player.getControlPanelComponent()) != null) {
				controlHeight = control.getPreferredSize().height;
				f.add(control, BorderLayout.SOUTH);
			}
			// set frame size, fit the video default size
			f.setSize(videoWidth + insetWidth, videoHeight + controlHeight + insetHeight);
			f.validate();
			// start play
			player.start();
		} else if (ce instanceof EndOfMediaEvent) {
			player.setMediaTime(new Time(0));
			player.start();
		}
	}
}
