package ssh.model;

public class TextValue {

	private String value;
	private String text;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public TextValue(){}
	
	public TextValue(String tableName){
		this.text=tableName;
		this.value=tableName;
	}
}
