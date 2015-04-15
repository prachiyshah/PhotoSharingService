package com.mytacks.group10.tagatTack.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.mytacks.group10.tagatTack.bean.BoardBean;
import com.mytacks.group10.tagatTack.bean.CategoryBean;
import com.mytacks.group10.tagatTack.bean.TackBean;
import com.mytacks.group10.tagatTack.bean.UpdateBoardBean;
import com.mytacks.group10.tagatTack.bean.UpdateTackBean;
import com.mytacks.group10.tagatTack.manager.UserManager;
import com.mytacks.group10.tagatTack.model.BoardMaster;
import com.mytacks.group10.tagatTack.model.CategoryMaster;
import com.mytacks.group10.tagatTack.model.FileDetails;
import com.mytacks.group10.tagatTack.model.TackMaster;
import com.mytacks.group10.tagatTack.model.UserMaster;

/**
 * Servlet implementation class UserServlet
 */
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
  UserManager userManager=new UserManager();  

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	
	doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	
		String action=request.getParameter("action");
		HttpSession userSession=request.getSession(false);
		
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);

				
		if(isMultipart)
		{
			System.out.println("in multipart");
			DiskFileItemFactory itemfactory = new DiskFileItemFactory();


			ServletFileUpload fileupload = new ServletFileUpload(itemfactory);

			Map<String, Object> map = new HashMap<String, Object>();
			
			try 
			{
				System.out.println("customer controllerererere try blcolsdldsflsdfl");

				List listitems =  fileupload.parseRequest(request);
				Iterator it = listitems.iterator();
					
				while(it.hasNext())
				{

					FileItem fileitem = (FileItem)it.next();


					if(fileitem.isFormField())
					{


						String name =   fileitem.getFieldName();
						String value = new String(fileitem.get());
						map.put(name, value);

					}
					else if(!fileitem.isFormField() )
					{


						BufferedImage bf = ImageIO.read(fileitem.getInputStream());
						String name = fileitem.getFieldName();
						String contenttype = fileitem.getContentType();
						System.out.println(contenttype);
						if(bf!=null && contenttype!=null && (contenttype.equals("image/jpeg") ||(contenttype.equals("image/png"))))
						{


						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						ImageIO.write(bf, "jpeg", bos);
						byte[] image = bos.toByteArray();
						
						FileDetails details = new FileDetails();
						details.setContentType(contenttype);
						
						details.setBoardImage(image);
						details.setTackImage(image);
						map.put(name, details);

						
						}

					}
			}



			
			BoardBean boardBean=new BoardBean();
			TackBean tackBean=new TackBean();
			UpdateBoardBean updateBoardBean=new UpdateBoardBean();
			UpdateTackBean updateTackBean=new UpdateTackBean();
			BeanUtils.populate(boardBean, map);
			BeanUtils.populate(tackBean, map);
			BeanUtils.populate(updateBoardBean, map);
			BeanUtils.populate(updateTackBean, map);
			if(boardBean.getAction().equals("addBoardDetails"))
			{
				BoardMaster boardMaster=new BoardMaster();
				boardMaster.setBoardName(boardBean.getBoardName());
				boardMaster.setBoardImage(boardBean.getBoardImage());
				boardMaster.setBoardType("PUBLIC");
				String categoryName=boardBean.getCategoryName();
				boolean flag=false;
				String userID=(String)userSession.getAttribute("userID");
				flag=userManager.addBoard(boardMaster, categoryName, userID);
				
				if(flag==true)
				{
					RequestDispatcher rd=request.getRequestDispatcher("/");
					try
					{	request.setAttribute("RedirectFrom", "AddBoard");
						ArrayList<BoardMaster> listOfAll=new ArrayList<BoardMaster>();
						 userID=(String)userSession.getAttribute("userID");
						listOfAll=userManager.listAllBoards(userID);
						 rd = request.getRequestDispatcher("/jsp/ListAllBoards.jsp");
						for(int i=0;i<listOfAll.size();i++)
						{
							File to = new File( getServletContext().getRealPath("/images")+"/"+listOfAll.get(i).getBoardName()+".jpg");
							ByteArrayInputStream bis = new ByteArrayInputStream(listOfAll.get(i).getBoardImage().getBoardImage());
							BufferedImage bf = ImageIO.read(bis);

							ImageIO.write(bf, "jpeg", to);

							request.setAttribute("boardImage"+i,listOfAll.get(i).getBoardName()+".jpg");
						}
						request.setAttribute("totalcount", listOfAll.size());
					}
					
					catch (Exception e) 
					{
						e.printStackTrace();
					}
				
					
					rd.forward(request, response);
					

				}
				
			}
			else if(updateBoardBean.getAction().equals("updateBoardDetails"))
			{	
				boolean flag=false;
				String boardType="";
				String categoryName=updateBoardBean.getCategoryName();
				String oldBoardName=updateBoardBean.getOldBoardName();
				if(updateBoardBean.getBoardType()==null)
				{
					
				}
				else
				{
					boardType=updateBoardBean.getBoardType();
				}
				BoardMaster boardMaster=new BoardMaster();
				boardMaster.setBoardName(updateBoardBean.getBoardName());
				boardMaster.setBoardImage(updateBoardBean.getBoardImage());
				boardMaster.setBoardType(boardType);
				if(updateBoardBean.getBoardImage()==null)
				{
					flag=userManager.updateBoardDetailsWithoutImage(boardMaster,categoryName,oldBoardName);
				}
				else
				{
				flag=userManager.updateBoardDetails(boardMaster,categoryName,oldBoardName);
				}
				if(flag==true)
				{
					System.out.println("Updated Successfully.");
					
				}
				else
				{
					System.out.println("Not Updated Successfully.");
				}
			}
			else if(updateTackBean.getAction().equals("updateTackDetails"))
			{	
				boolean flag=false;
				
				
				String boardName=updateTackBean.getBoardName();
				String oldTackName=updateTackBean.getOldTackName();
				
				
				TackMaster tackMaster=new TackMaster();
				tackMaster.setTackName(updateTackBean.getTackName());
				tackMaster.setTackImage(updateTackBean.getTackImage());
				
				if(updateTackBean.getTackImage()==null)
				{
					
					flag=userManager.updateTackDetailsWithoutImage(tackMaster, boardName, oldTackName);
					
				}
				else
				{
					flag=userManager.updateTackDetails(tackMaster, boardName, oldTackName);
				
				}
				if(flag==true)
				{
					System.out.println("Updated Successfully.");
					
				}
				else
				{
					System.out.println("Not Updated Successfully.");
				}
			}
			
			
			
			
			//************ ADD TACK DETAILS METHOD STARTS ******************////////////////////////////////////	
			else if(tackBean.getAction().equals("addTackDetails"))
			{
				TackMaster tackMaster=new TackMaster();
				tackMaster.setTackName(tackBean.getTackName());
				tackMaster.setTackImage(tackBean.getTackImage());
				
				String boardName=tackBean.getBoardName();
				boolean flag=false;
				int responseCode=0;
				responseCode=userManager.getResponseCode(tackBean.getTackURL());
				if(responseCode==200)
				{
				tackMaster.setTackURL(tackBean.getTackURL());
				flag=userManager.addTack(tackMaster,boardName);
				}
				else
				{
					System.out.println("URL Does not exist.");
				}
				if(flag==true)
				{
					RequestDispatcher rd=request.getRequestDispatcher("/jsp/Success.jsp");
					request.setAttribute("Message", tackMaster.getTackName()+" added Successfully.");
					rd.forward(request,response);
				}
				
			}
			//************ ADD TACK DETAILS METHOD ENDS ******************////////////////////////////////////
			
			//************ BEAN TRY BLOCK ENDS ***************************////////////////////////////////////
			}
			catch (FileUploadException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IllegalAccessException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (InvocationTargetException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch(NullPointerException e)
			{
			}
	
		}	
		
		
		//************GO TO ADD BOARD PAGE METHOD STARTS ************************////////////////
		
		else if(action.equals("goToBoardPage"))
		{
			String categoryImageName=request.getParameter("categoryName");
			String categoryName=categoryImageName.substring(0, categoryImageName.length()-4);
			System.out.println("The CategoryImageName is"+categoryImageName);
			System.out.println("The Category Name is:"+categoryName);
			RequestDispatcher rd=request.getRequestDispatcher("/jsp/AddBoard.jsp");
			request.setAttribute("categoryName", categoryName);
			rd.forward(request, response);
			
		}

		//************GO TO ADD BOARD PAGE METHOD ENDS ************************///////////////////
		
		//************SIGN UP METHOD STARTS************************//////////////////////////////
		
		else if(action.equalsIgnoreCase("signup"))
		{
			boolean flag=false;

			String fName = request.getParameter("FirstName");
			String lName = request.getParameter("LastName");
			String gender = request.getParameter("gender");
			String emailId= request.getParameter("EmailID");
			String passWord = request.getParameter("Password");
			String phNum = request.getParameter("PhoneNo");
			
			UserMaster userInfo = new UserMaster();

			userInfo.setUserId(emailId);
			userInfo.setUserPassword(passWord);
			userInfo.setFirstName(fName);
			userInfo.setLastName(lName);
			userInfo.setGender(gender);
			userInfo.setEmailAddress(emailId);
			userInfo.setPhoneNo(phNum);
			java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
			userInfo.setLastLoginTime(date.toString());
			
			flag=userManager.signUpUser(userInfo);
			
			
			if(flag==true)
			{
				RequestDispatcher rd=request.getRequestDispatcher("/jsp/Success.jsp");
				request.setAttribute("Message","Registration Link has been sent to your Email Address.Click on the link to activate your account.");
				rd.forward(request, response);
				
				
			}
			else
			{
				RequestDispatcher rd=request.getRequestDispatcher("/jsp/Success.jsp");
				request.setAttribute("Message","The Email Address you provided does not exist.");
				rd.forward(request, response);
			}
			
		
		}
		//************SIGN UP METHOD ENDS************************///////////////////////////////
		
		//************ACTIVATE USER METHOD STARTS************************///////////////////////
		
		else if(action.equalsIgnoreCase("activateUser"))
		{
			
			boolean flag=false;
			String confirmationCode=request.getParameter("num");
			flag=userManager.activateUser(confirmationCode);
			
			if(flag==true)
			{
				RequestDispatcher rd=request.getRequestDispatcher("/jsp/Success.jsp");
				request.setAttribute("Message", "Your Account is activated successfully.");
				rd.forward(request, response);
			}
			else
			{
				System.out.println("User Not Activated.");
			}
			
		}
		
		//************ACTIVATE USER METHOD ENDS************************///////////////////////
		
		//************SIGN IN METHOD STARTS************************///////////////////////////
		else if(action.equals("signIN"))
		{
			String lastLogin="";
			boolean flag=false;
			String userID=request.getParameter("UserID");
			String pwd=request.getParameter("UserPwd");
			userSession=request.getSession(true);
			userSession.setAttribute("userID", userID);
			UserMaster userLoginDetails=new UserMaster();
			userLoginDetails.setUserId(userID);
			userLoginDetails.setUserPassword(pwd);
			
			
			
			lastLogin=userManager.checkUserLoginDetails(userLoginDetails);
			
			
			if(lastLogin != null)
			{
				userSession.setAttribute("lastLogin", lastLogin);
				RequestDispatcher rd=request.getRequestDispatcher("/jsp/index.jsp");
				rd.forward(request, response);
			}
			else
			{
				RequestDispatcher rd=request.getRequestDispatcher("/jsp/Success.jsp");
				request.setAttribute("Message", "Sorry the UserName and Password are Incorrect.");
				rd.forward(request, response);
			}
			
		}
		//************SIGN IN METHOD STARTS************************///////////////////////////

		//************ FB LOGIN METHOD STARTS ******************////////////////////////////////////
		else if(action.equals("FBLogin"))
		{
			
			
			String code = request.getParameter("code");
	        if (code == null || code.equals("")) {
	            // an error occurred, handle this
	        }

	        String token = null;
	        try {
	            String g = "https://graph.facebook.com/oauth/access_token?client_id=241581129322350&redirect_uri=" + URLEncoder.encode("http://localhost:98/MyTacks/UserServlet?action=FBLogin", "UTF-8") + "&client_secret=fd41f90d0401a8262ba5ee78a5d12865&code=" + code;
	            URL u = new URL(g);
	            URLConnection c = u.openConnection();
	            BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
	            String inputLine;
	            StringBuffer b = new StringBuffer();
	            while ((inputLine = in.readLine()) != null)
	                b.append(inputLine + "\n");            
	            in.close();
	            token = b.toString();
	            if (token.startsWith("{"))
	                throw new Exception("error on requesting token: " + token + " with code: " + code);
	        } catch (Exception e) {
	                // an error occurred, handle this
	        }

	        String graph = null;
	        try {
	            String g = "https://graph.facebook.com/me?" + token;
	            URL u = new URL(g);
	            URLConnection c = u.openConnection();
	            BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
	            String inputLine;
	            StringBuffer b = new StringBuffer();
	            while ((inputLine = in.readLine()) != null)
	                b.append(inputLine + "\n");            
	            in.close();
	            graph = b.toString();
	        } catch (Exception e) {
	                // an error occurred, handle this
	        }

	        String facebookId;
	        String firstName;
	        String middleNames;
	        String lastName;
	        String email;
	        int id;
	        String locale;
	        int agerange;
	        String lastLogin="";
	        try {
	            JSONObject json = new JSONObject(graph);
	            facebookId = json.getString("id");
	            
	            firstName = json.getString("first_name");
	            if (json.has("middle_name"))
	               middleNames = json.getString("middle_name");
	            else
	                middleNames = null;
	            if (middleNames != null && middleNames.equals(""))
	                middleNames = null;
	            lastName = json.getString("last_name");
	            
	            email = json.getString("email");
	        
	            id=json.getInt("id");
	        
	            locale=json.getString("locale");
	            
	            lastLogin=userManager.updateLastLoginTimeForFacebookUser(email);
	            
	            if(lastLogin != null)
				{
					userSession.setAttribute("lastLogin", lastLogin);
					userSession.setAttribute("userID", email);
					RequestDispatcher rd=request.getRequestDispatcher("/jsp/index.jsp");
					rd.forward(request, response);
				}
				else
				{
					RequestDispatcher rd=request.getRequestDispatcher("/jsp/Success.jsp");
					request.setAttribute("Message", "Sorry the UserName and Password are Incorrect.");
					rd.forward(request, response);
				}
	            
	            
	            
	            
	        } 
	        catch (JSONException e) 
	        {
	            // an error occurred, handle this
	        }
		
		}
		//************ FB LOGIN METHOD ENDS ******************////////////////////////////////////
		
		//************FORGOT PASSWORD METHOD STARTS************************///////////////////////////
		else if(action.equalsIgnoreCase("forgotPassword"))
		{
			boolean flag=false;
			String userID=request.getParameter("UserID");
			flag=userManager.sendldOldPassword(userID);
			
			if(flag==true)
			{
				RequestDispatcher rd=request.getRequestDispatcher("/jsp/Success.jsp");
				request.setAttribute("Message", "The Password has been sent to your registered Email Address Successflyy.");
				rd.forward(request, response);
			}
			else
			{
				System.out.println("Password has not been Sent.");
			}
			
			
		}
		
		//************FORGOT PASSWORD METHOD ENDS************************///////////////////////////
		
		//************ VIEW ALL BOARDS METHOD STARTS ******************////////////////////////////
		
		else if(action.equals("listAllBoards"))
		{
			RequestDispatcher rd=request.getRequestDispatcher("/");
			try
			{
				String redirectFrom=request.getParameter("redirectFrom");
				request.setAttribute("RedirectFrom", redirectFrom);
				ArrayList<BoardMaster> listOfAll=new ArrayList<BoardMaster>();
				String userID=(String)userSession.getAttribute("userID");
				listOfAll=userManager.listAllBoards(userID);
				 rd = request.getRequestDispatcher("/jsp/ListAllBoards.jsp");
				for(int i=0;i<listOfAll.size();i++)
				{
					File to = new File( getServletContext().getRealPath("/images")+"/"+listOfAll.get(i).getBoardName()+".jpg");
					ByteArrayInputStream bis = new ByteArrayInputStream(listOfAll.get(i).getBoardImage().getBoardImage());
					BufferedImage bf = ImageIO.read(bis);

					ImageIO.write(bf, "jpeg", to);

					request.setAttribute("boardImage"+i,listOfAll.get(i).getBoardName()+".jpg");
				}
				request.setAttribute("totalcount", listOfAll.size());
			}
			
			
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		
			
			rd.forward(request, response);
		}
		
		//************ VIEW ALL BOARDS METHOD ENDS ******************////////////////////////////////////
		
		//************ UPDATE BOARD METHOD STARTS ******************////////////////////////////////////
		else if(action.equals("updateBoardPage"))
		{
			String boardName=request.getParameter("boardName");
			String categoryName=userManager.getCategoryName(boardName);
			ArrayList<CategoryMaster> listOfCategories=new ArrayList<CategoryMaster>();
			listOfCategories=userManager.listAllCategories();
			RequestDispatcher rd=request.getRequestDispatcher("/jsp/UpdateBoard.jsp");
			request.setAttribute("boardName", boardName);
			request.setAttribute("categoryName", categoryName);
			request.setAttribute("listOfCategories", listOfCategories);
			rd.forward(request, response);
			
			
			
			
		}
		else if(action.equals("updateTack"))
		{
			String userID=(String)userSession.getAttribute("userID");
			String tackName=request.getParameter("tackName");
			String boardName=userManager.getBoardName(tackName);
			String categoryName=userManager.getCategoryName(boardName);
			
			ArrayList<CategoryMaster> listOfCategories=new ArrayList<CategoryMaster>();
			listOfCategories=userManager.listAllCategories();
			ArrayList<BoardMaster> listOfBoards=new ArrayList<BoardMaster>();
			listOfBoards=userManager.listAllBoards(userID);
			RequestDispatcher rd=request.getRequestDispatcher("/jsp/UpdateTack.jsp");
			request.setAttribute("tackName", tackName);
			request.setAttribute("categoryName", categoryName);
			request.setAttribute("listOfCategories", listOfCategories);
			request.setAttribute("boardName", boardName);
			request.setAttribute("listOfBoards",listOfBoards);
			rd.forward(request, response);
		}
		
		else if(action.equals("deleteTack"))
		{
			boolean flag=false;
			String tackName=request.getParameter("tackName");
			flag=userManager.deleteTack(tackName);
			if(flag==true)
			{
				System.out.println("Deleted Successfully.");
			}
			else
			{
				System.out.println("Not Deleted Successfully.");
			}
		}
		//************ UPDATE BOARD METHOD ENDS ******************////////////////////////////////////
		
		else if(action.equals("goToAddTacksPage"))
		{
			String boardName=request.getParameter("boardName");
			RequestDispatcher rd=request.getRequestDispatcher("/jsp/AddTack.jsp");
			request.setAttribute("boardName", boardName);
			rd.forward(request, response);
		}
		
	
		
		//************ VIEW ALL CATEGORIES METHOD STARTS ******************////////////////////////////////////

		else if(action.equals("listAllCategories"))
		{
			RequestDispatcher rd=request.getRequestDispatcher("/");
			try
			{
				String redirectFrom=request.getParameter("redirectFrom");
				request.setAttribute("RedirectFrom", redirectFrom);
				ArrayList<CategoryMaster> listOfAll=new ArrayList<CategoryMaster>();
				listOfAll=userManager.listAllCategories();
				 rd = request.getRequestDispatcher("/jsp/ListOfCategories.jsp");
				for(int i=0;i<listOfAll.size();i++)
				{
					File to = new File( getServletContext().getRealPath("/images")+"/"+listOfAll.get(i).getCategoryName()+".jpg");
					ByteArrayInputStream bis = new ByteArrayInputStream(listOfAll.get(i).getCategoryImage().getCategoryImage());
					BufferedImage bf = ImageIO.read(bis);

					ImageIO.write(bf, "jpeg", to);

					request.setAttribute("categoryImage"+i,listOfAll.get(i).getCategoryName()+".jpg");
				}
				request.setAttribute("totalcount", listOfAll.size());
			}
			
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			rd.forward(request, response);
			
			
		}

		//************ VIEW ALL CATEGORIES METHOD ENDS ******************////////////////////////////////////
		
		else if(action.equals("listAllTacks"))
		{
			RequestDispatcher rd=request.getRequestDispatcher("/");
			try
			{
				String redirectFrom=request.getParameter("redirectFrom");
				request.setAttribute("RedirectFrom", redirectFrom);
				String boardName=request.getParameter("boardName");
				ArrayList<TackMaster> listOfAll=new ArrayList<TackMaster>();
				listOfAll=userManager.listAllTacks(boardName);
				 rd = request.getRequestDispatcher("/jsp/ListAllTacks.jsp");
				for(int i=0;i<listOfAll.size();i++)
				{
					File to = new File( getServletContext().getRealPath("/images")+"/"+listOfAll.get(i).getTackName()+".jpg");
					ByteArrayInputStream bis = new ByteArrayInputStream(listOfAll.get(i).getTackImage().getTackImage());
					BufferedImage bf = ImageIO.read(bis);

					ImageIO.write(bf, "jpeg", to);
					request.setAttribute("tackURL"+i, listOfAll.get(i).getTackURL());
					request.setAttribute("tackImage"+i,listOfAll.get(i).getTackName()+".jpg");
				}
				request.setAttribute("totalcount", listOfAll.size());
			}
			
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			rd.forward(request, response);
			
			
		}
		
		else if(action.equals("viewTacksOfOthers"))
		{
			
		}
		
		
		//************VIEW DETAILS FOR UPDATING PROFILE START*****************///////////////////////////
		else if(action.equalsIgnoreCase("viewUser"))
		{
			UserMaster viewUserDetails =  new UserMaster();
			
			String userId = (String)userSession.getAttribute("userID");
			viewUserDetails = userManager.viewDetailsForUpdate(userId);
			request.setAttribute("viewUserDetails", viewUserDetails);
			
			if(viewUserDetails != null)
			{
				RequestDispatcher rd =  request.getRequestDispatcher("/jsp/UpdateProfile.jsp");
				rd.forward(request, response);
			}
			else
			{
				System.out.println("Error in viewUser method");
			}
		}
		//************VIEW DETAILS FOR UPDATING PROFILE START*****************///////////////////////////

		//************ UPDATING PROFILE START*****************///////////////////////////
		else if(action.equalsIgnoreCase("updateProfile"))
		{
			boolean flag=false;
			
			String userId = (String) userSession.getAttribute("userID");

			String emailId= request.getParameter("EmailID");
			String phNum = request.getParameter("PhoneNo");
			
			UserMaster userDetails = new UserMaster();

			userDetails.setUserId(emailId);
			userDetails.setEmailAddress(emailId);
			userDetails.setPhoneNo(phNum);
			
			flag=userManager.updateProfile(userDetails, userId);
			
			if(flag==true)
			{
				userSession.setAttribute("userID", userDetails.getUserId());
				RequestDispatcher rd=request.getRequestDispatcher("/jsp/Success.jsp");
				request.setAttribute("Message","Details Updated Successfully.");
				rd.forward(request, response);
				
				
			}
			else
			{
				RequestDispatcher rd=request.getRequestDispatcher("/jsp/Success.jsp");
				request.setAttribute("Message","Some Problem has been occured.Please try again.");
				rd.forward(request, response);
			}
			
		}
		//************ UPDATING PROFILE END*****************///////////////////////////		
		
		//************ CHANGE PASSWORD START*****************///////////////////////////
		else if (action.equalsIgnoreCase("changePassword")) 
		{
			
			String userId = (String)userSession.getAttribute("userID");
			boolean flag = false;
			
			String oldPassword = request.getParameter("OldPassword");
			String NewPassword = request.getParameter("Password");
			
			flag = userManager.changePassword(oldPassword, NewPassword, userId);
			
			if(flag == true)
			{
				RequestDispatcher rd=request.getRequestDispatcher("/jsp/Success.jsp");
				request.setAttribute("Message","Password has been changed successfully.");
				rd.forward(request, response);
			}
			else
			{
				RequestDispatcher rd=request.getRequestDispatcher("/jsp/Success.jsp");
				request.setAttribute("Message","Old Password is not matching.");
				rd.forward(request, response);
			}
			
			
		}
		
		//************ CHANGE PASSWORD END*****************///////////////////////////
	
		

		else if(action.equals("deleteBoardPage"))
		{
			String boardName=request.getParameter("boardName");
			boolean flag=false;
			flag=userManager.deleteBoard(boardName);
			if(flag==true)
			{
			RequestDispatcher rd=request.getRequestDispatcher("/");
			try
			{
				String redirectFrom="deleteBoardPage";
				request.setAttribute("RedirectFrom", redirectFrom);
				ArrayList<BoardMaster> listOfAll=new ArrayList<BoardMaster>();
				String userID=(String)userSession.getAttribute("userID");
				listOfAll=userManager.listAllBoards(userID);
				 rd = request.getRequestDispatcher("/jsp/ListAllBoards.jsp");
				for(int i=0;i<listOfAll.size();i++)
				{
					File to = new File( getServletContext().getRealPath("/images")+"/"+listOfAll.get(i).getBoardName()+".jpg");
					ByteArrayInputStream bis = new ByteArrayInputStream(listOfAll.get(i).getBoardImage().getBoardImage());
					BufferedImage bf = ImageIO.read(bis);

					ImageIO.write(bf, "jpeg", to);

					request.setAttribute("boardImage"+i,listOfAll.get(i).getBoardName()+".jpg");
				}
				request.setAttribute("totalcount", listOfAll.size());
			}
			
			
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		
			
			rd.forward(request, response);
			}
			else
				
			{
				System.out.println("Something went wrong.");
			}
			
			
			
		}
		
		else if(action.equals("goToAddBoardPage"))
		{
			String categoryName=request.getParameter("categoryName");
			RequestDispatcher rd=request.getRequestDispatcher("/jsp/AddBoard.jsp");
			request.setAttribute("categoryName", categoryName);
			rd.forward(request, response);
		}
		
		else if(action.equals("viewBoardsPage"))
		{
			RequestDispatcher rd=request.getRequestDispatcher("/");
			try
			{	String redirectFrom="viewBoardsPage";
				request.setAttribute("RedirectFrom", redirectFrom);
				String userID=(String)userSession.getAttribute("userID");
				String categoryName=request.getParameter("categoryName");
				ArrayList<BoardMaster> listOfAll=new ArrayList<BoardMaster>();
				listOfAll=userManager.listAllBoardsByCategory(categoryName,userID);
				 rd = request.getRequestDispatcher("/jsp/ListAllBoards.jsp");
				for(int i=0;i<listOfAll.size();i++)
				{
					File to = new File( getServletContext().getRealPath("/images")+"/"+listOfAll.get(i).getBoardName()+".jpg");
					ByteArrayInputStream bis = new ByteArrayInputStream(listOfAll.get(i).getBoardImage().getBoardImage());
					BufferedImage bf = ImageIO.read(bis);

					ImageIO.write(bf, "jpeg", to);

					request.setAttribute("boardImage"+i,listOfAll.get(i).getBoardName()+".jpg");
				}
				request.setAttribute("totalcount", listOfAll.size());
			}
			
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			rd.forward(request, response);
			

			
		}
		
		else if(action.equals("followBoard"))
		{
			String boardName=request.getParameter("boardName");
			String userFollowerID=(String)userSession.getAttribute("userID");
			boolean flag=false;
			
			flag=userManager.followBoard(boardName, userFollowerID);
			if(flag==true)
			{
				System.out.println("Follow done Successfully.");
			}
			else
			{
				System.out.println("Follow is not done Successfully.");
			}
		}
		
		
		else if(action.equals("likeTack"))
		{
			String tackName=request.getParameter("tackName");
			String userLikerID=(String)userSession.getAttribute("userID");
			boolean flag=false;
			
			flag=userManager.likeTack(tackName, userLikerID);
			if(flag==true)
			{
				System.out.println("Like Done Successfully.");
			}
			else
			{
				System.out.println("Like not done Successfully.");
			}
		
		}
	
		else if(action.equals("newOneTry"))
		{
			System.out.println("Added Successfully.");
		}
	}

	}
