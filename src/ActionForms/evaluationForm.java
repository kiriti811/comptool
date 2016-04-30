package ActionForms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.sql.SQLException;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import DbManager.DbConnection;
import DbManager.DbMethods;

/**
 * Servlet implementation class 
 */
@WebServlet(asyncSupported=true,urlPatterns={"/evaluationForm"})
public class evaluationForm extends HttpServlet {
	DbConnection db = new DbConnection();
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public evaluationForm() {
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
		String courseNum = request.getParameter("course");
		System.out.println("course"+courseNum);
		if(courseNum==null)
		{
			HttpSession		session		=	request.getSession();
			ArrayList coursesArray = null;
			try {
				coursesArray = DbMethods.dropDown("coursenumber", "addcourses", (String)session.getAttribute("id")+"###id");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			JSONObject result = new JSONObject();
			try {
				result.put("courses", coursesArray);
				JSONArray array =DbMethods.getReports((String)session.getAttribute("college"));
				result.put("reports", array);
			} catch (JSONException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.write(result.toString());
			out.flush();
			out.close();
			System.out.println("evaluationFrom Get");
	
		}
		else{

			ArrayList outcomeArray = null;
			try {
				outcomeArray = DbMethods.dropDown("courseoutcome", "addcourses", "COURSE$$$"+courseNum);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println(outcomeArray);
			String outcome = (String)outcomeArray.get(0);
			System.out.println(outcomeArray.size());
			JSONObject result = new JSONObject();
			ArrayList<String>outcomesList = new ArrayList<String>();
			StringTokenizer outcomes = new StringTokenizer(outcome,",");
			while(outcomes.hasMoreTokens())
				outcomesList.add(outcomes.nextToken());
			try {
				result.put("outcomes", outcomesList);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.write(result.toString());
			out.flush();
			out.close();
			System.out.println("evaluationFrom Get");
	
			
		}
		
		
		
		
		

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		String course	=	"";
		try {
			BufferedReader reader = request.getReader();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				course	=	line.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();

		}

		JSONParser parser = new JSONParser();
		JSONObject joUser = null;
		try {
			JSONObject jsonObject = (JSONObject) parser.parse(course);
			 joUser = (JSONObject) parser.parse(course);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String courseNumber = null;
		try {
			courseNumber = (String) joUser.get("selectedCourse");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList outcomeArray = null;
		try {
			outcomeArray = DbMethods.dropDown("courseoutcome", "addcourses", "COURSE$$$"+courseNumber);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ArrayList	outcomes		=	new 	ArrayList();
		StringTokenizer	st	=	new	StringTokenizer((String)outcomeArray.get(0),",");
		while(st.hasMoreElements())
		{
			outcomes.add(st.nextToken());
		}
		JSONObject result = new JSONObject();
		try {
			result.put("outcomes", outcomes);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.write(result.toString());
		out.flush();
		out.close();
		System.out.println("evaluationFrom Get");
	}

}
