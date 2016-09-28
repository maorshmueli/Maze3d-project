package boot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Observable;

import model.Model;
import model.MyModel;
import presenter.Presenter;
import presenter.Properties;
import view.MazeWindow;
import view.MyView;
import view.View;

public class Run {

	public static void main(String[] args) {
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(System.out);
		
		/*
		View view = new MyView(in , out);
		Model model = new MyModel(new Properties());
		
		Presenter pres = new Presenter(model , view);
		
		((Observable) model).addObserver(pres);
		((Observable) view).addObserver(pres);
		
		//CLI
		view.start();
		*/
		
		//XMLManager.writeXml(new Properties());
		
		
	
		//GUI
		View view = new MyView(in , out);
		Model model = new MyModel(new Properties());
		
		view = new MazeWindow(new Properties());
		Presenter pres = new Presenter(model , view);
		((Observable) model).addObserver(pres);
		((Observable) view).addObserver(pres);
		view.start();
		

	}

}
