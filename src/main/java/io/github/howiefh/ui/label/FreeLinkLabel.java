package io.github.howiefh.ui.label;

import javax.swing.Icon;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;

public class FreeLinkLabel extends JLabel{

	private static final long serialVersionUID = -1937051442446988088L;
	
	private Color hoverColor = Color.BLUE; 
	private Color defaultColor = Color.BLACK; 
	private Runnable link;
	
	public FreeLinkLabel(final String text, final Icon icon,final int horizontalAlignment,final Runnable link){
		super(text, icon, horizontalAlignment);
		this.link = link;
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				new Thread(link).start();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseEntered(e);
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				setForeground(hoverColor);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseExited(e);
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				setForeground(defaultColor);
			}
		});
	}
	public FreeLinkLabel(String text, Icon icon, Runnable link){
		this(text, icon, JLabel.CENTER, link);
	}
	public FreeLinkLabel(String text,Runnable link){
		this(text, null, link);
	}
	public FreeLinkLabel(Icon icon,Runnable link){
		this("", icon, link);
	}
	public FreeLinkLabel(String text,File target){
		this(text, new FileLink(target));
	}
	public FreeLinkLabel(String text,URI target){
		this(text, new URILink(target));
	}
	public FreeLinkLabel(Icon icon, File target){
		this(icon, new FileLink(target));
	}
	public FreeLinkLabel(Icon icon,URI target){
		this(icon, new URILink(target));
	}
	
	public Color getHoverColor() {
		return hoverColor;
	}
	public void setHoverColor(Color hoverColor) {
		this.hoverColor = hoverColor;
	}
	public Color getDefaultColor() {
		return defaultColor;
	}
	public void setDefaultColor(Color defaultColor) {
		this.defaultColor = defaultColor;
	}
	public Runnable getLink() {
		return link;
	}
	public void setLink(Runnable link) {
		this.link = link;
	}
	public void setFileLink(String text,File target) {
		setText(text);
		this.link = new FileLink(target);
	}
	public void setURILink(String text,URI target) {
		setText(text);
		this.link = new URILink(target);
	}
	public void setFileLink(File target) {
		this.link = new FileLink(target);
	}
	public void setURILink(URI target) {
		this.link = new URILink(target);
	}
	
	public static class URILink implements Runnable{
		private URI target;
		public URILink(URI target){
			this.target = target;
		}
		@Override
		public void run() {
			try {
				Desktop.getDesktop().browse(target);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public static class FileLink implements Runnable{
		private File target;
		public FileLink(File target){
			this.target = target;
		}
		@Override
		public void run() {
			try {
				Desktop.getDesktop().open(target);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
