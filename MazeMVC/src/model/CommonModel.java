package model;



import controller.Controller;
/**
 * abstract class which has the common methods and data members of all models
 * @author Maor Shmueli
 *
 */
public abstract class CommonModel implements Model 
{
	protected Controller c;
	/**
	 * constructor using fields
	 * @param c the controller that this model use.all the calculation passes to this controller
	 */
	public CommonModel(Controller c) {
		super();
		this.c = c;
	}

}