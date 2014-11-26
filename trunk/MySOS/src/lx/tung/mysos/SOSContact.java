package lx.tung.mysos;

public class SOSContact {
	private String _id;
	private String num;
	private String name;
	public SOSContact(){
		_id  = "";
		num  = "";
		name = "";
	}
	public SOSContact(String aId, String aName, String aNum){
		_id = aId;
		num = aNum;
		name = aName;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
