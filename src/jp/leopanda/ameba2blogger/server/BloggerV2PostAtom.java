package jp.leopanda.ameba2blogger.server;
/**
 * Blogger Post用 Atom クラス
 * API V2用
 * @author LeoPanda
 *
 */
public class BloggerV2PostAtom {
	private String title = null;
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
	
	public String getPostAtom(){
		String refContent = this.content.replaceAll("&.+?;","");
		refContent = refContent.replaceAll("<","&lt;");
		refContent = refContent.replaceAll(">","&gt;");
		String ret = "<entry xmlns=\"http://www.w3.org/2005/Atom\">" +"\n" ;
		ret = ret + "<published>" + this.published + "</published>" + "\n";
		ret = ret + "<updated>" + this.published + "</updated>" + "\n";
		ret = ret + "<title type='text'>" + this.title +"</title>" +"\n";
		ret = ret + "<content type='html'>";
		ret = ret + refContent;
		ret = ret + "</content>" +"\n";
		if(isDraft){
			ret = ret + "<app:control xmlns:app='http://www.w3.org/2007/app'>" +"\n";
			ret = ret + "<app:draft>yes</app:draft>" +"\n";
		    ret = ret + "</app:control>" +"\n";
		}
		ret = ret + "</entry>" ;	
		return ret;
	}	
}
