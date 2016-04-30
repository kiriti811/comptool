package ActionForms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;



import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import DbManager.DbConnection;
import DbManager.DbMethods;

/**
 * Servlet implementation class 
 */
@WebServlet(asyncSupported=true,urlPatterns={"/loginForm"})
public class loginForm extends HttpServlet {
	DbConnection db = new DbConnection();
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public loginForm() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader reader = request.getReader();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();

		}

		JSONParser parser = new JSONParser();
		JSONObject joUser = null;
		String user = null;
		String password = null;
		String userType = null;
		try {
			JSONObject jsonObject = (JSONObject) parser.parse(sb.toString());
			 joUser = (JSONObject) parser.parse(sb.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String	role	=	null;
		String	id		=	null;
		user = (String) joUser.get("email");
		password = (String) joUser.get("password");
		userType = (String) joUser.get("userType");
		String college = user.substring(6, 10);
		System.out.println("college"+college);
		Vector vec = new Vector();
		String authStatus = DbMethods.authenticate(user, password, userType);
		
		if (!authStatus.equals("Fail$$$")) {
			StringTokenizer	st	=	new StringTokenizer(authStatus,"$$$");
			while(st.hasMoreElements())
			{
				st.nextToken();
				id	=	st.nextToken();
				role	=	st.nextToken();
			}
			HttpSession session = request.getSession();
			session.setAttribute("id", id);
			session.setAttribute("college", college);

			
		} else {
			role	=	"inValid";
			id		=	"0";
		}
		JSONObject result = new JSONObject();
		result.put("isValid", true);
		result.put("role", role);
		result.put("id", id);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.write(result.toString());
		out.flush();
		out.close();
		System.out.println("loginForm");
	}

}
