package com.mytacks.group10.tagatTack.dao;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.mytacks.group10.tagatTack.model.BoardMaster;
import com.mytacks.group10.tagatTack.model.CategoryMaster;
import com.mytacks.group10.tagatTack.model.FileDetails;
import com.mytacks.group10.tagatTack.model.TackMaster;
import com.mytacks.group10.tagatTack.model.UserMaster;
import com.mytacks.group10.tagatTack.utility.DataConnection;

public class UserDAO 
{



	public boolean signUpUser(UserMaster userInfo)
	{
		boolean flag = false;
		Connection con = null;
		PreparedStatement ps = null;
		
		try 
		{
			con = DataConnection.getConnection();
			ps = con.prepareStatement("insert into UserMaster values(?,?,?,?,?,?,?,?,?,?)");
			
			ps.setString(1, userInfo.getEmailAddress());
			ps.setString(2, userInfo.getUserPassword());
			ps.setString(3, userInfo.getFirstName());
			ps.setString(4, userInfo.getLastName());
			ps.setString(5, userInfo.getGender());
			ps.setString(6, userInfo.getEmailAddress());
			ps.setString(7, userInfo.getPhoneNo());
			ps.setString(8, userInfo.getConfirmationCode());
			ps.setString(9, userInfo.getActivationStatus());
			ps.setString(10, userInfo.getLastLoginTime());
			
			int check = ps.executeUpdate();
			
			if(check > 0)
			{
				flag = true;
			}
		}
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			DataConnection.closeConnection(con);
			DataConnection.closeStatement(ps);
		}
		
		return flag;
}
	
	public boolean activateUser(String confirmationCode)
	{
		boolean flag=false;
		Connection con=null;
		PreparedStatement ps=null;
		
		
		try
		{
			con=DataConnection.getConnection();
			ps=con.prepareStatement("update UserMaster set ActivationStatus=? where ConfirmationCode=?");
			ps.setString(1, "ACTIVE");
			ps.setString(2, confirmationCode);
			
			int j=ps.executeUpdate();
			if(j>0)
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

	public String checkUserLoginDetails(UserMaster userLoginDetails)
	{
		boolean flag=false;
		Connection con=null;
		PreparedStatement ps=null;
		String lastLogin=null;
		String updateLastLogin=null;
		PreparedStatement statement = null;
		try
		{
			java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
			con=DataConnection.getConnection();
			ps=con.prepareStatement("select * from UserMaster where UserID=? and Password=?");
			ps.setString(1, userLoginDetails.getUserId());
			ps.setString(2, userLoginDetails.getUserPassword());
			ResultSet rs=ps.executeQuery();
			if(rs.next())
			{
				if(rs.getString(9).equals("ACTIVE"))
				{
					flag=true;
				}
				lastLogin = rs.getString("LastLoginTime");
			}
			
			statement = con.prepareStatement("update UserMaster set LastLoginTime = ? where UserID = ? && Password = ?");
			statement.setTimestamp(1, date);
			statement.setString(2, userLoginDetails.getUserId());
			statement.setString(3, userLoginDetails.getUserPassword());
			
			int i=statement.executeUpdate();
			if(i>0)
			{
				System.out.println("Updated.");
			}
			else
			{
				System.out.println("Not Updated.");
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
		
		return lastLogin;
		
	}
	
	public String updateLastLoginTimeForFacebookUser(String userID)
	{
		String lastLogin="";
		Connection con=null;
		PreparedStatement ps=null;
		PreparedStatement ps1=null;
		try
		{
			
			
			java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
			con=DataConnection.getConnection();
			
			ps1=con.prepareStatement("select LastLoginTime from UserMaster where UserID=?");
			ps1.setString(1, userID);
			ResultSet rs=ps1.executeQuery();
			if(rs.next())
			{
			lastLogin=rs.getString(1);
			}
			
			
			ps=con.prepareStatement("update UserMaster set LastLoginTime = ? where UserID = ? ");
			ps.setTimestamp(1, date);
			ps.setString(2, userID);
			int i=ps.executeUpdate();
			
			if(i>0)
			{
				System.out.println("Updated Successfully.");
				
			}
			else
			{
				System.out.println("Not Updated Successfully.");
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
			DataConnection.closeStatement(ps1);
		}
		return lastLogin;
	}
	
	
	public UserMaster sendOldPassword(String userID)
	{
		UserMaster userDetails=null;
		Connection con=null;
		PreparedStatement ps=null;
		
		try
		{
			con=DataConnection.getConnection();
			ps=con.prepareStatement("select * from UserMaster where UserID=? and ActivationStatus=?");
			ps.setString(1, userID);
			ps.setString(2, "ACTIVE");
			ResultSet rs=ps.executeQuery();
			if(rs.next())
			{	
				userDetails=new UserMaster();
				userDetails.setUserId(rs.getString(1));
				userDetails.setUserPassword(rs.getString(2));
				userDetails.setFirstName(rs.getString(3));
				userDetails.setLastName(rs.getString(4));
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
		return userDetails;
		
	}
	
	public boolean addBoard(BoardMaster boardMaster,String categoryName,String userID)
	{
		Connection con=null;
		PreparedStatement ps=null;
		PreparedStatement ps1=null;
		boolean flag=false;
		int categoryID=0;
		try
		{
			con=DataConnection.getConnection();
			ps=con.prepareStatement("select CategoryID from CategoryMaster where CategoryName=?");
			ps.setString(1, categoryName);
			ResultSet rs=ps.executeQuery();
			if(rs.next())
			{
			categoryID=rs.getInt(1);	
			}
			
			ps1=con.prepareStatement("insert into BoardMaster(BoardName,BoardImage,BoardType,CategoryID,UserID) values(?,?,?,?,?)");
			ps1.setString(1,boardMaster.getBoardName());
			ByteArrayInputStream bas1 = new ByteArrayInputStream(boardMaster.getBoardImage().getBoardImage());
			ps1.setBinaryStream(2, bas1, boardMaster.getBoardImage().getBoardImage().length);
			ps1.setString(3,boardMaster.getBoardType());
			ps1.setInt(4, categoryID);
			ps1.setString(5, userID);
		
			int i=ps1.executeUpdate();
			if(i>0)
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
			DataConnection.closeStatement(ps1);
			
		}
		return flag;
		
		
	}
	

	public boolean addTack(TackMaster tackMaster,String boardName)
	{
		Connection con=null;
		PreparedStatement ps=null;
		PreparedStatement ps1=null;
		boolean flag=false;
		int boardID=0;
		
		try
		{
			con=DataConnection.getConnection();
			ps=con.prepareStatement("select BoardID from BoardMaster where BoardName=?");
			ps.setString(1, boardName);
			ResultSet rs=ps.executeQuery();
			if(rs.next())
			{
			boardID=rs.getInt(1);	
			}
			
			ps1=con.prepareStatement("insert into TackMaster(TackName,TackImage,BoardID,TackURL) values(?,?,?,?)");
			ps1.setString(1,tackMaster.getTackName());
			ByteArrayInputStream bas1 = new ByteArrayInputStream(tackMaster.getTackImage().getTackImage());
			ps1.setBinaryStream(2, bas1, tackMaster.getTackImage().getTackImage().length);
			ps1.setInt(3,boardID);
			ps1.setString(4,tackMaster.getTackURL());
		
			int i=ps1.executeUpdate();
			if(i>0)
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
			DataConnection.closeStatement(ps1);
			
		}
		return flag;
		
		
	}
	
	public ArrayList<TackMaster> listAllTacks(String boardName) throws IOException
	{
		
		Connection con=null;
		PreparedStatement ps=null;
		PreparedStatement ps1=null;
		ArrayList<TackMaster> listOfTacks=null;
		int boardID=0;
		try
		{
			listOfTacks=new ArrayList<TackMaster>();
			con=DataConnection.getConnection();
			ps=con.prepareStatement("select BoardID from BoardMaster where BoardName=?");
			ps.setString(1, boardName);
			
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				boardID=rs.getInt(1);
			}
			
			ps1=con.prepareStatement("select * from TackMaster where BoardID=?");
			ps1.setInt(1, boardID);
			
			
			ResultSet rs1=ps1.executeQuery();
			
			while(rs1.next())
			{
				ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
				TackMaster tackMaster=new TackMaster();
				InputStream img1 = rs1.getBinaryStream(3);
				BufferedImage bf1 = ImageIO.read(img1);
				ImageIO.write(bf1, "jpeg", bos1);
				byte[] data1 = bos1.toByteArray();
				FileDetails fd1 = new FileDetails();
				fd1.setTackImage(data1);
				tackMaster.setTackImage(fd1);
				tackMaster.setTackName(rs1.getString(2));
				tackMaster.setTackURL(rs1.getString(5));
				listOfTacks.add(tackMaster);
				
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
		return listOfTacks;
	}
	
	public ArrayList<BoardMaster> listAllBoardsByCategory(String categoryName,String userID) throws IOException
	{
		
		Connection con=null;
		PreparedStatement ps=null;
		PreparedStatement ps1=null;
		ArrayList<BoardMaster> listOfBoards=null;
		int categoryID=0;
		try
		{
			listOfBoards=new ArrayList<BoardMaster>();
			con=DataConnection.getConnection();
			ps=con.prepareStatement("select CategoryID from CategoryMaster where CategoryName=?");
			ps.setString(1, categoryName);
			
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				categoryID=rs.getInt(1);
			}
			
			ps1=con.prepareStatement("select * from BoardMaster where CategoryID=? and UserID <> ? and BoardType = ?");
			ps1.setInt(1, categoryID);
			ps1.setString(2, userID);
			ps1.setString(3, "PUBLIC");
			ResultSet rs1=ps1.executeQuery();
			
			while(rs1.next())
			{
				ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
				BoardMaster boardMaster=new BoardMaster();
				InputStream img1 = rs1.getBinaryStream(3);
				BufferedImage bf1 = ImageIO.read(img1);
				ImageIO.write(bf1, "jpeg", bos1);
				byte[] data1 = bos1.toByteArray();
				FileDetails fd1 = new FileDetails();
				fd1.setBoardImage(data1);
				boardMaster.setBoardImage(fd1);
				boardMaster.setBoardName(rs1.getString(2));
				listOfBoards.add(boardMaster);
				
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
		return listOfBoards;
	}

	
	
	
	public ArrayList<BoardMaster> listAllBoards(String userID) throws IOException
	{
		
		Connection con=null;
		PreparedStatement ps=null;
		ArrayList<BoardMaster> listOfBoards=null;
		
		try
		{
			listOfBoards=new ArrayList<BoardMaster>();
			con=DataConnection.getConnection();
			ps=con.prepareStatement("select * from BoardMaster where UserID=?");
			ps.setString(1, userID);
			
			ResultSet rs=ps.executeQuery();
			
			while(rs.next())
			{
				ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
				BoardMaster boardMaster=new BoardMaster();
				InputStream img1 = rs.getBinaryStream(3);
				BufferedImage bf1 = ImageIO.read(img1);
				ImageIO.write(bf1, "jpeg", bos1);
				byte[] data1 = bos1.toByteArray();
				FileDetails fd1 = new FileDetails();
				fd1.setBoardImage(data1);
				boardMaster.setBoardImage(fd1);
				boardMaster.setBoardName(rs.getString(2));
				listOfBoards.add(boardMaster);
				
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
		
		return listOfBoards;
		
		
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
	
	public String getCategoryName(String boardName)
	{
		Connection con=null;
		PreparedStatement ps=null;
		PreparedStatement ps1=null;
		String categoryName="";
		int categoryID=0;
		try
		{
			con=DataConnection.getConnection();
			ps=con.prepareStatement("select CategoryID from BoardMaster where BoardName=?");
			ps.setString(1, boardName);
			ResultSet rs=ps.executeQuery();
			if(rs.next())
			{
			categoryID=rs.getInt(1);	
			}
			ps1=con.prepareStatement("select CategoryName from CategoryMaster where CategoryID=?");
			ps1.setInt(1, categoryID);
			ResultSet rs1=ps1.executeQuery();
			if(rs1.next())
			{
				categoryName=rs1.getString(1);
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
			DataConnection.closeStatement(ps1);
		}
		return categoryName;
		
		
	}
	
	public String getBoardName(String tackName)
	{
		Connection con=null;
		PreparedStatement ps=null;
		PreparedStatement ps1=null;
		String boardName="";
		int boardID=0;
		try
		{
			con=DataConnection.getConnection();
			ps=con.prepareStatement("select BoardID from TackMaster where TackName=?");
			ps.setString(1, tackName);
			ResultSet rs=ps.executeQuery();
			if(rs.next())
			{
			boardID=rs.getInt(1);	
			}
			ps1=con.prepareStatement("select BoardName from BoardMaster where BoardID=?");
			ps1.setInt(1, boardID);
			ResultSet rs1=ps1.executeQuery();
			if(rs1.next())
			{
				boardName=rs1.getString(1);
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
			DataConnection.closeStatement(ps1);
		}
		return boardName;
		
		
	}
	
	
	public boolean updateBoardDetails(BoardMaster boardMaster,String categoryName,String oldBoardName)
	{
		Connection con=null;
		PreparedStatement ps=null;
		PreparedStatement ps1=null;
		boolean flag=false;
		int categoryID=0;
		try
		{
			con=DataConnection.getConnection();
			ps1=con.prepareStatement("select CategoryID from CategoryMaster where CategoryName=?");
			ps1.setString(1, categoryName);
			ResultSet rs=ps1.executeQuery();
			if(rs.next())
			{
				categoryID=rs.getInt(1);
			}
			
			ps=con.prepareStatement("update BoardMaster set BoardImage=?,BoardName=?,CategoryID=?,BoardType=? where BoardName=?");
			ByteArrayInputStream bas1 = new ByteArrayInputStream(boardMaster.getBoardImage().getBoardImage());
			ps.setBinaryStream(1, bas1, boardMaster.getBoardImage().getBoardImage().length);
			ps.setString(2, boardMaster.getBoardName());
			ps.setInt(3, categoryID);
			ps.setString(4, boardMaster.getBoardType());
			ps.setString(5, oldBoardName);
			int i=ps.executeUpdate();
			
			if(i>0)
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
	
	public boolean updateTackDetails(TackMaster tackMaster,String boardName,String oldTackName)
	{
		Connection con=null;
		PreparedStatement ps=null;
		PreparedStatement ps1=null;
		boolean flag=false;
		int boardID=0;
		try
		{
			con=DataConnection.getConnection();
			ps1=con.prepareStatement("select BoardID from BoardMaster where BoardName=?");
			ps1.setString(1, boardName);
			ResultSet rs=ps1.executeQuery();
			if(rs.next())
			{
				boardID=rs.getInt(1);
			}
			
			ps=con.prepareStatement("update TackMaster set TackImage=?,TackName=?,BoardID=? where TackName=?");
			ByteArrayInputStream bas1 = new ByteArrayInputStream(tackMaster.getTackImage().getTackImage());
			ps.setBinaryStream(1, bas1, tackMaster.getTackImage().getTackImage().length);
			ps.setString(2, tackMaster.getTackName());
			ps.setInt(3, boardID);
			ps.setString(4, oldTackName);
			int i=ps.executeUpdate();
			
			if(i>0)
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
	
		
	public boolean updateBoardDetailsWithoutImage(BoardMaster boardMaster,String categoryName,String oldBoardName)
	{
		Connection con=null;
		PreparedStatement ps=null;
		PreparedStatement ps1=null;
		boolean flag=false;
		int categoryID=0;
		try
		{
			con=DataConnection.getConnection();
			ps1=con.prepareStatement("select CategoryID from CategoryMaster where CategoryName=?");
			ps1.setString(1, categoryName);
			ResultSet rs=ps1.executeQuery();
			while(rs.next())
			{
				categoryID=rs.getInt(1);
			}
			
			ps=con.prepareStatement("update BoardMaster set BoardName=?,CategoryID=?,BoardType=? where BoardName=?");
			ps.setString(1, boardMaster.getBoardName());
			ps.setInt(2, categoryID);
			ps.setString(3, boardMaster.getBoardType());
			ps.setString(4, oldBoardName);
			int i=ps.executeUpdate();
			
			if(i>0)
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
	
	public boolean updateTackDetailsWithoutImage(TackMaster tackMaster,String boardName,String oldTackName)
	{
		Connection con=null;
		PreparedStatement ps=null;
		PreparedStatement ps1=null;
		boolean flag=false;
		int boardID=0;
		try
		{
			con=DataConnection.getConnection();
			ps1=con.prepareStatement("select BoardID from BoardMaster where BoardName=?");
			ps1.setString(1, boardName);
			ResultSet rs=ps1.executeQuery();
			if(rs.next())
			{
				boardID=rs.getInt(1);
			}
			
			ps=con.prepareStatement("update TackMaster set TackName=?,BoardID=? where BoardName=?");
			ps.setString(1, tackMaster.getTackName());
			ps.setInt(2, boardID);
			ps.setString(3, oldTackName);
			int i=ps.executeUpdate();
			
			if(i>0)
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
	
	/*public boolean likeTack(String userID,TackMaster tackMaster)
	{
		boolean flag=false;
		Connection con=null;
		PreparedStatement ps=null;
		
		try
		{
			con=DataConnection.getConnection();
			ps=con.prepareStatement("select ")
		}
		
		
		
		return flag;
	}*/
	
	

	public int noOfLikesForPin()
	{
		Connection con=null;
		PreparedStatement ps=null;
		int counterForLikes=0;
		
		try
		{
			con=DataConnection.getConnection();
			ps=con.prepareStatement("select count(*) from LikeMaster");
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				counterForLikes=rs.getInt(1);
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

		return counterForLikes;
	}
	
	
	public int noOfFollowersForBoard()
	{
		Connection con=null;
		PreparedStatement ps=null;
		int counterFollowers=0;
		
		try
		{
			con=DataConnection.getConnection();
			ps=con.prepareStatement("select count(*) from FollowMaster");
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				counterFollowers=rs.getInt(1);
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

		return counterFollowers;
	}
	
	
	
	
	
	public boolean deleteBoard(String boardName)
	{
		boolean flag=false;
		Connection con=null;
		PreparedStatement ps=null;
		PreparedStatement ps1=null;
		int boardID=0;
		
		try
		{
			con=DataConnection.getConnection();
			ps=con.prepareStatement("select BoardID from BoardMaster where BoardName=?");
			ps.setString(1, boardName);
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				boardID=rs.getInt(1);
			}
			
			ps1=con.prepareStatement("delete from BoardMaster where BoardID=?");
			ps1.setInt(1, boardID);
			int i=ps1.executeUpdate();
			if(i>0)
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
			DataConnection.closeStatement(ps1);
		}

		return flag;
	}

	public boolean deleteTack(String tackName)
	{
		boolean flag=false;
		Connection con=null;
		PreparedStatement ps=null;
		PreparedStatement ps1=null;
		int tackID=0;
		
		try
		{
			con=DataConnection.getConnection();
			ps=con.prepareStatement("select TackID from TackMaster where TackName=?");
			ps.setString(1, tackName);
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				tackID=rs.getInt(1);
			}
			
			ps1=con.prepareStatement("delete from TackMaster where TackID=?");
			ps1.setInt(1, tackID);
			int i=ps1.executeUpdate();
			if(i>0)
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
			DataConnection.closeStatement(ps1);
		}

		return flag;
	}
	
	
	public UserMaster viewDetailsForUpdate(String userId)
	{
		UserMaster viewUserDetails = new UserMaster();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet resultSet=null;
		
		con = DataConnection.getConnection();
		try 
		{
			statement = con.prepareStatement("select * from UserMaster where UserID=?");
			statement.setString(1, userId);
			resultSet = statement.executeQuery();
			while(resultSet.next())
			{
				viewUserDetails.setFirstName(resultSet.getString("FirstName"));
				viewUserDetails.setLastName(resultSet.getString("LastName"));
				viewUserDetails.setGender(resultSet.getString("Gender"));
				viewUserDetails.setEmailAddress(resultSet.getString("EmailAddress"));
				viewUserDetails.setPhoneNo(resultSet.getString("PhoneNo"));
			}
		}
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			DataConnection.closeConnection(con);
			DataConnection.closeStatement(statement);
		}
		return viewUserDetails;
	}
	
	public boolean updateProfile(UserMaster userDetails, String userId)
	{
		boolean flag= false;
		Connection con = null;
		PreparedStatement prpStmnt = null;
		PreparedStatement statement=null;
		
		con = DataConnection.getConnection();
		try 
		{
		prpStmnt = con.prepareStatement("update UserMaster set UserID = ?, EmailAddress = ?, PhoneNo = ? where UserID = ?");
		prpStmnt.setString(1, userDetails.getUserId());
		prpStmnt.setString(2, userDetails.getEmailAddress());
		prpStmnt.setString(3, userDetails.getPhoneNo());
		prpStmnt.setString(4, userId);
		
		int check = prpStmnt.executeUpdate();
		
		statement = con.prepareStatement("update BoardMaster set UserID = ? where UserID=?");
		statement.setString(1, userDetails.getUserId());
		statement.setString(2, userId);
		
		int check1 = statement.executeUpdate();
		
		if(check > 0 && check1 > 0)
		{
			flag = true;
		}
		}
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			DataConnection.closeConnection(con);
			DataConnection.closeStatement(prpStmnt);
			DataConnection.closeStatement(statement);
		}
		
		return flag;
	}
	
	public boolean changePassword(String oldPassword,String NewPassword, String userId )
	{
		boolean flag=false;
		Connection con = null;
		PreparedStatement statement=null;
		PreparedStatement preparedStatement=null;
		String checkPwd = null;
		
		con= DataConnection.getConnection();
		
		try 
		{
			statement = con.prepareStatement("select * from  UserMaster where UserID = ? ");
			statement.setString(1, userId);
			ResultSet result = statement.executeQuery();
			 
			while(result.next())
			{
				checkPwd = result.getString("Password");
			
			if(checkPwd.equals(oldPassword))
			{
				preparedStatement = con.prepareStatement("update UserMaster set Password = ? where UserID=?");
				preparedStatement.setString(1, NewPassword);
				preparedStatement.setString(2, userId);
				
				int check = preparedStatement.executeUpdate();
				
				if(check>0)
				{
					flag = true;
				}
			}
			}
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			DataConnection.closeConnection(con);
			DataConnection.closeStatement(statement);
			DataConnection.closeStatement(preparedStatement);
		}
		return flag;
	}
	
	
	public int getResponseCode(String urlString) throws MalformedURLException, IOException
	{
		HttpURLConnection huc= null;
		try
	    {
		
		URL u = new URL(urlString); 
	    huc =  (HttpURLConnection) u.openConnection();  
	    
	    huc.setRequestMethod("GET"); 
	    huc.connect(); 
	    
	    }
		catch(Exception e3)
	    {
	    	System.out.println("General");
	    	e3.printStackTrace();
	    }
		
	    return huc.getResponseCode();
	
	}
	
	public boolean followBoard(String boardName,String userFollowerID)
	{
		boolean flag=false;
		Connection con=null;
		PreparedStatement ps=null;
		PreparedStatement ps1=null;
		int boardID=0;
		String userOwnerID="";
		try
		{
			con=DataConnection.getConnection();
			ps=con.prepareStatement("select BoardID,UserID from BoardMaster where BoardName=?");
			ps.setString(1, boardName);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next())
			{
				boardID=rs.getInt(1);
				userOwnerID=rs.getString(2);
			}
			
			ps1=con.prepareStatement("insert into FollowMaster(UserFollowerID,UserID,BoardID) values(?,?,?)");
			ps1.setString(1, userFollowerID);
			ps1.setString(2,userOwnerID);
			ps1.setInt(3, boardID);
			
			int i=ps1.executeUpdate();
			if(i>0)
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
			DataConnection.closeStatement(ps1);
		}
		return flag;
	}
	
	public boolean likeTack(String tackName,String userLikerID)
	{
		boolean flag=false;
		Connection con=null;
		PreparedStatement ps=null;
		PreparedStatement ps1=null;
		PreparedStatement ps2=null;
		int tackID=0;
		String userOwnerID="";
		int boardID=0;
		try
		{
			con=DataConnection.getConnection();
			ps=con.prepareStatement("select tackID,BoardID from TackMaster where TackName=?");
			ps.setString(1, tackName);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next())
			{
				tackID=rs.getInt(1);
				boardID=rs.getInt(2);
			}
			
			ps2=con.prepareStatement("select UserID from BoardMaster where BoardID=?");
			ps2.setInt(1, boardID);
			
			ResultSet rs2=ps2.executeQuery();
			while(rs2.next())
			{
				userOwnerID=rs2.getString(1);
			}
			
			ps1=con.prepareStatement("insert into LikeMaster(UserLikerID,UserID,TackID) values(?,?,?)");
			ps1.setString(1, userLikerID);
			ps1.setString(2,userOwnerID);
			ps1.setInt(3, tackID);
			
			int i=ps1.executeUpdate();
			if(i>0)
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
			DataConnection.closeStatement(ps1);
		}
		return flag;
		
	}
	
}
