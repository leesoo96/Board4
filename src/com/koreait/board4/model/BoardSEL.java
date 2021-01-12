package com.koreait.board4.model;
// model 클래스 이름은 보통 Entity , Domain, DTO, VO(값 변경 불가 클래스) 
public class BoardSEL extends BoardModel{

	private String nm; // 작성자 이름
	private int is_favorite; // 좋아요 
	private int favorite_cnt;
	
	public int getFavorite_cnt() {
		return favorite_cnt;
	}

	public void setFavorite_cnt(int favorite_cnt) {
		this.favorite_cnt = favorite_cnt;
	}

	public int getIs_favorite() {
		return is_favorite;
	}

	public void setIs_favorite(int is_favorite) {
		this.is_favorite = is_favorite;
	}

	public String getNm() {
		return nm;
	}

	public void setNm(String nm) {
		this.nm = nm;
	}
	
	
}
