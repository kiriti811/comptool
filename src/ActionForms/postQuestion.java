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
@WebServlet(asyncSupported=true,urlPatterns={"/postQuestion"})
public class postQuestion extends HttpServlet {
	DbConnection db = new DbConnection();
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public postQuestion() {
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
				System.out.println("Post Question"+line);
			}
		} catch (Exception e) {
			e.printStackTrace();

		}

		JSONParser parser = new JSONParser();
		JSONObject joUser = null;
		try {
			JSONObject jsonObject = (JSONObject) parser.parse(sb.toString());
			 joUser = (JSONObject) parser.parse(sb.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		HttpSession session = request.getSession();
		Vector vec = new Vector();
		vec.add((String) joUser.get("selectedOutcome"));
		vec.add((String) joUser.get("question"));
		vec.add((String) joUser.get("option1"));
		vec.add((String) joUser.get("option2"));
		vec.add((String) joUser.get("option3"));
		vec.add((String) joUser.get("option4"));
		vec.add((String) joUser.get("correctOption"));
		vec.add((String)session.getAttribute("college"));
		DbMethods.add(vec, "QUESTIONS");
		System.out.println(vec);
		JSONObject result = new JSONObject();
		result.put("isValid", true);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.write(result.toString());
		out.flush();
		out.close();
		System.out.println("postQuestion");
	}

}
