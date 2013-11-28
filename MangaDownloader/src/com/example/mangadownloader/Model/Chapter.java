package com.example.mangadownloader.Model;

public class Chapter {
	String mangaName;
	int chapterNo;
	String chapterLink;
	int pageNo;
	
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
