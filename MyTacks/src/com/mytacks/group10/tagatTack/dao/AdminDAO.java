package com.mytacks.group10.tagatTack.dao;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.mytacks.group10.tagatTack.model.BoardMaster;
import com.mytacks.group10.tagatTack.model.CategoryMaster;
import com.mytacks.group10.tagatTack.model.FileDetails;
import com.mytacks.group10.tagatTack.utility.DataConnection;

public class AdminDAO 
{
	public boolean addCategory(CategoryMaster categoryMaster)
	{
		boolean flag=false;
		Connection con=null;
		PreparedStatement ps=null;
		
		try
		{
			con=DataConnection.getConnection();
			ps=con.prepareStatement("insert into CategoryMaster(CategoryName,CategoryImage) values(?,?)");
			ps.setString(1, categoryMaster.getCategoryName());
			ByteArrayInputStream bas1 = new ByteArrayInputStream(categoryMaster.getCategoryImage().getCategoryImage());
			ps.setBinaryStream(2, bas1, categoryMaster.getCategoryImage().getCategoryImage().length);
		
			int added=ps.executeUpdate();
			if(added>0)
			{
				flag=true;
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			DataConnection.closeConnection(con);
			DataConnection.closeStatement(ps);
		}
		
		return flag;
	}
	
	public CategoryMaster getCategoryDetails(String categoryName) throws IOException
	{
		CategoryMaster categoryMaster=null;
		Connection con=null;
		PreparedStatement ps=null;
		
		try
		{
			con=DataConnection.getConnection();
			ps=con.prepareStatement("select * from CategoryMaster where CategoryName=?");
			ps.setString(1, categoryName);
			ResultSet rs=ps.executeQuery();
			ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
			if(rs.next())
			{
				categoryMaster=new CategoryMaster();
				InputStream img1 = rs.getBinaryStream(3);
				BufferedImage bf1 = ImageIO.read(img1);
				ImageIO.write(bf1, "jpeg", bos1);
				byte[] data1 = bos1.toByteArray();
				FileDetails fd1 = new FileDetails();
				fd1.setCategoryImage(data1);
				categoryMaster.setCategoryImage(fd1);
			
			}
			
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			DataConnection.getConnection();
			DataConnection.closeStatement(ps);
		}
		
		return categoryMaster;
		
		
	}
	
	public ArrayList<CategoryMaster> listAllCategories() throws IOException
	{
		
		Connection con=null;
		PreparedStatement ps=null;
		ArrayList<CategoryMaster> listOfCategories=null;
		
		try
		{
			listOfCategories=new ArrayList<CategoryMaster>();
			con=DataConnection.getConnection();
			ps=con.prepareStatement("select * from CategoryMaster");
			
			ResultSet rs=ps.executeQuery();
			
			while(rs.next())
			{
				ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
				CategoryMaster categoryMaster=new CategoryMaster();
				InputStream img1 = rs.getBinaryStream(3);
				BufferedImage bf1 = ImageIO.read(img1);
				ImageIO.write(bf1, "jpeg", bos1);
				byte[] data1 = bos1.toByteArray();
				FileDetails fd1 = new FileDetails();
				fd1.setCategoryImage(data1);
				categoryMaster.setCategoryImage(fd1);
				categoryMaster.setCategoryName(rs.getString(2));
				listOfCategories.add(categoryMaster);
				
			}	
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			DataConnection.getConnection();
			DataConnection.closeStatement(ps);
		}
		
		return listOfCategories;
		
		
	}
	

		
	
	
}
