package GUI;


import java.util.Observable;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import view.View;
import java.util.Timer;
import java.util.TimerTask;


/**
 * BaseWindow class extends Observable implements View, Runnable 
 * the main game window
 * waiting for user action
 * @author Maor Shmueli
 *
 */
public abstract class BaseWindow extends Observable implements View, Runnable {


	protected Display display;
	protected Shell shell;
	protected Timer showSolutionByAnimation;
	protected TimerTask animationSolutionTask;
	
	
	protected abstract void initWidgets();
	
	@Override
	public void run() {
		display = new Display();  // our display
		shell = new Shell(display); // our window

		initWidgets();
		
		shell.open();
		
		// main event loop
		while(!shell.isDisposed()){ // while window isn't closed
		
		   // 1. read events, put then in a queue.
		   // 2. dispatch the assigned listener
		   if(!display.readAndDispatch()){ 	// if the queue is empty
		      display.sleep(); 			// sleep until an event occurs 
		   }
		
		} // shell is disposed

		exit(); // dispose OS components
		
	}
	
	public void exit() {
		if (this.showSolutionByAnimation != null)
			this.showSolutionByAnimation.cancel();
		if (this.animationSolutionTask != null)
			this.animationSolutionTask.cancel();
		
		//notify observers to for returning the maze
		setChanged();
		notifyObservers("exit");
		display.dispose();
	}

}