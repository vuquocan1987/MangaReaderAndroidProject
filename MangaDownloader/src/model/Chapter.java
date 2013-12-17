package model;

public class Chapter {
	String mangaName;
	int chapterNo;
	String chapterLink;
	String link_first_page;
	long _id;
	int pageNo;
	String chapterName;
	String link=null;
	String localPath;
	int status;
	public static final int STATUS_CHAPTER_NOTDOWNLOAD = 0;
	public static final int STATUS_CHAPTER_DOWNLOADING = STATUS_CHAPTER_NOTDOWNLOAD+1;
	public static final int STATUS_CHAPTER_STOPPED = STATUS_CHAPTER_DOWNLOADING+1;
	public static final int STATUS_CHAPTER_DOWNLOADED = STATUS_CHAPTER_STOPPED+1;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getChapterName() {
		return chapterName;
	}
	public String getLink_first_page() {
		return link_first_page;
	}
	public void setLink_first_page(String link_first_page) {
		this.link_first_page = link_first_page;
	}
	public long get_id() {
		return _id;
	}
	public void set_id(long _id) {
		this._id = _id;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;
	}
	public Chapter (int chapterNo,String chapterLink,String chapterName ){
		this.chapterNo = chapterNo;
		this.chapterLink = chapterLink;
		this.chapterName = chapterName;
	}
	public Chapter(String mangaName, int chapterNo, String chapterLink,
			int pageNo) {
		super();
		this.mangaName = mangaName;
		this.chapterNo = chapterNo;
		this.chapterLink = chapterLink;
		this.pageNo = pageNo;
	}
	public Chapter(String mangaName, int chapterNo, String chapterLink) {
		super();
		this.mangaName = mangaName;
		this.chapterNo = chapterNo;
		this.chapterLink = chapterLink;
	}
	public Chapter(String mangaName, int chapterNo, String chapterLink, String chapterName) {
		// TODO Auto-generated constructor stub
		this(mangaName,chapterNo,chapterLink);
		this.chapterName=chapterName;
	}
	public Chapter(long _id,String mangaName, int chapterNo, String chapterLink, String chapterName){
		this (mangaName,chapterNo,chapterLink,chapterName);
		this._id=_id;
	}
	public Chapter(long _id,String mangaName, int chapterNo, String chapterLink, String chapterName,int status){
		this (_id,mangaName,chapterNo,chapterLink,chapterName);
		this.status = status;
	}
	public Chapter(long _id,String mangaName, int chapterNo, String chapterLink, String chapterName,int status,String localPath){
		this (_id,mangaName,chapterNo,chapterLink,chapterName,status);
		this.localPath = localPath;
	}
	
	public String getMangaName() {
		return mangaName;
	}
	public void setMangaName(String mangaName) {
		this.mangaName = mangaName;
	}
	public int getChapterNo() {
		return chapterNo;
	}
	public void setChapterNo(int chapterNo) {
		this.chapterNo = chapterNo;
	}
	public String getChapterLink() {
		return chapterLink;
	}
	public void setChapterLink(String chapterLink) {
		this.chapterLink = chapterLink;
	}
}
