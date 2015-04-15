package com.mytacks.group10.tagatTack.bean;

import com.mytacks.group10.tagatTack.model.FileDetails;

public class UpdateTackBean 
{
	private FileDetails tackImage;
	private String tackName;
	private String boardName;
	private String oldTackName;
	private String action;
	
	
	
	
	public UpdateTackBean() {
		
	}

	

	public UpdateTackBean(FileDetails tackImage, String tackName,String boardName, String oldTackName, String action) 
	{
		
		this.tackImage = tackImage;
		this.tackName = tackName;
		this.boardName = boardName;
		this.oldTackName = oldTackName;
		this.action = action;
	}


	
	
	public FileDetails getTackImage() {
		return tackImage;
	}



	public void setTackImage(FileDetails tackImage) {
		this.tackImage = tackImage;
	}



	public String getTackName() {
		return tackName;
	}



	public void setTackName(String tackName) {
		this.tackName = tackName;
	}



	public String getBoardName() {
		return boardName;
	}



	public void setBoardName(String boardName) {
		this.boardName = boardName;
	}



	public String getOldTackName() {
		return oldTackName;
	}



	public void setOldTackName(String oldTackName) {
		this.oldTackName = oldTackName;
	}



	public String getAction() {
		return action;
	}



	public void setAction(String action) {
		this.action = action;
	}

	
	
	
}
