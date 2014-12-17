package lx.tung.mysos.entities;

public class FirstAid_Menu_Entity {
	public int mId;
	public String mTitle;
	public FirstAid_Menu_Entity(int id, String title){
		super();
		mId = id;
		mTitle = title;
	}
	public FirstAid_Menu_Entity(){
		super();
		mId = -1;
		mTitle = "";
	}
}
