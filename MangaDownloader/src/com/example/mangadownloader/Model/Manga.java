package com.example.mangadownloader.Model;

import java.util.List;

public class Manga {
	private long _id;
	private String mangaName;
	private String link;
	private int favourite;
	private List<Chapter> chapters;
	
	public List<Chapter> getChapters() {
		return chapters;
	}
	public void setChapters(List<Chapter> chapters) {
		this.chapters = chapters;
	}
	@Override
	public String toString() {
		return mangaName;
	}
	public long get_id() {
		return _id;
	}
	public void set_id(long _id) {
		this._id = _id;
	}
	public String getMangaName() {
		return mangaName;
	}
	public void setMangaName(String mangaName) {
		mangaName = mangaName;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public int getFavourite() {
		return favourite;
	}
	public Manga(long _id, String mangaName, String link, int favourite) {
		super();
		this._id = _id;
		this.mangaName = mangaName;
		this.link = link;
		this.favourite = favourite;
	}
	public void setFavourite(int favourite) {
		this.favourite = favourite;
	}
	public Manga(String mangaName, String link, int favourite) {
		super();
		this.mangaName = mangaName;
		this.link = link;
		this.favourite = favourite;
	}
	public Manga(String mangaName, String link) {
		this(mangaName,link,0);
	}

}
