package boot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Observable;

import javax.annotation.processing.SupportedSourceVersion;

import GUI.MazeWindow;
import model.Model;
import model.MyModel;
import presenter.Presenter;
import presenter.Properties;
import view.MyView;
import view.View;

public class Run {

	public static void main(String[] args) {
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(System.out);
		
		
		
		//reading xml file properties
		XMLManager xml = new XMLManager();
		//xml.writeXml(new Properties());
		xml.readXML(new File("resources/properties.xml"));
		Properties properties = xml.getProperties();
		
		Model model = new MyModel(properties);
		View view = null;
		
		if(properties.getGameType().equals("GUI")){
			view = new MazeWindow(properties);
		}
		else {
			view = new MyView(in , out);
		}
		
		Presenter pres = new Presenter(model , view);
		((Observable) model).addObserver(pres);
		((Observable) view).addObserver(pres);
		
		view.start();

	}

}
