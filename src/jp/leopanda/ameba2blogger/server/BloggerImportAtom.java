package jp.leopanda.ameba2blogger.server;
/**
 * Blogger Importファイル用 Atom クラス
 * @author LeoPanda
 *
 */
public class BloggerImportAtom {
	private String title = null;
	private String label = null;
	private String published = null;
	private String content = null;
	private boolean isDraft = true;

	public void setTitle(String title){
		this.title = title;
	}
	public void setPublished(String dateTime){
		String tmpDate = dateTime.split(" ")[0].trim();
		String tmpTime = dateTime.split(" ")[1].trim();
		this.published = tmpDate + "T" + tmpTime + "+09:00";
	}
	public void setContent(String content){
		this.content = content;
	}
	public void setDraft(boolean isDraft){
		this.isDraft = isDraft;
	}
	public void setLabel(String label){
		this.label = label;
	}
	
	public String getPostAtom(){
		String refContent = this.content.replaceAll("&.+?;","");
		refContent = refContent.replaceAll("<","&lt;");
		refContent = refContent.replaceAll(">","&gt;");
		String ret = "<entry xmlns=\"http://www.w3.org/2005/Atom\">" +"\n" 
					+ "<category scheme=\"http://schemas.google.com/g/2005#kind\" "
					+ "	term=\"http://schemas.google.com/blogger/2008/kind#post\" />"  +"\n" 
					+ "<category scheme=\"http://www.blogger.com/atom/ns#\" term=\"" + this.label + "\" />"  +"\n" 
					+ "<id>xxxx</id>" +"\n" 
					+ "<author>" +"\n" 
					+ "<name>xxxx</name>" +"\n" 
					+ "</author>" +"\n" 
					+ "<published>" + this.published + "</published>" + "\n"
					+ "<title type='text'>" + this.title +"</title>" +"\n"
					+ "<content type='html'>"
					+ refContent
					+ "</content>" +"\n";
		if(isDraft){
			ret += 	"<app:control xmlns:app='http://www.w3.org/2007/app'>" +"\n"
				+ 	"<app:draft>yes</app:draft>" +"\n"
				+ 	"</app:control>" +"\n";
		}
		ret += "</entry>" ;	
		return ret;
	}
}
