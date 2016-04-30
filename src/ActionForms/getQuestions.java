package ActionForms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import DbManager.DbMethods;

/**
 * Servlet implementation class getQuestions
 */
@WebServlet("/getQuestions")
public class getQuestions extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public getQuestions() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String course = request.getParameter("course");
		System.out.println("course"+course);
		if(course==null){
		HttpSession		session		=	request.getSession();
		ArrayList coursesArray = null;
		try {
			coursesArray = DbMethods.dropDown("coursenumber", "addcourses", (String)session.getAttribute("id")+"%%%id");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JSONObject result = new JSONObject();
		try {
			result.put("courses", coursesArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.write(result.toString());
		out.flush();
		out.close();
		System.out.println("getQuestions Get");
	}
		else
		{
			System.out.println("inside elese");
		/*JSONObject object = new JSONObject();
		JSONArray array = new JSONArray();
		
		JSONObject arrayElementOne = new JSONObject();
		try {
			arrayElementOne.put("description", "what is java");
			arrayElementOne.put("id", "1");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONArray arrayElementOneArray = new JSONArray();
		
		JSONObject arrayElementOneArrayElementOne = new JSONObject();
		JSONObject arrayElementOneArrayElementTwo = new JSONObject();
		JSONObject arrayElementOneArrayElementThree = new JSONObject();
		try {
			arrayElementOneArrayElementOne.put("id", "22");
			arrayElementOneArrayElementOne.put("description", "STRING");
			arrayElementOneArrayElementTwo.put("id", "23");
			arrayElementOneArrayElementTwo.put("description", "int");
			arrayElementOneArrayElementThree.put("id", "24");
			arrayElementOneArrayElementThree.put("description", "varchar");
			
			arrayElementOneArray.put(arrayElementOneArrayElementOne);
			arrayElementOneArray.put(arrayElementOneArrayElementTwo);
			arrayElementOneArray.put(arrayElementOneArrayElementThree);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			arrayElementOne.put("options", arrayElementOneArray);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		JSONObject arrayElementTwo = new JSONObject();
		try {
			arrayElementTwo.put("description", "what is C");
			arrayElementTwo.put("id", "2");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONArray arrayElementTwoArray = new JSONArray();
		
		JSONObject arrayElementTwoArrayElementOne = new JSONObject();
		JSONObject arrayElementTwoArrayElementTwo = new JSONObject();
		JSONObject arrayElementTwoArrayElementThree = new JSONObject();
		try {
			arrayElementTwoArrayElementOne.put("id", "25");
			arrayElementTwoArrayElementOne.put("description", "STRING");
			arrayElementTwoArrayElementTwo.put("id", "26");
			arrayElementTwoArrayElementTwo.put("description", "int");
			arrayElementTwoArrayElementThree.put("id", "27");
			arrayElementTwoArrayElementThree.put("description", "varchar");
			
			arrayElementTwoArray.put(arrayElementTwoArrayElementOne);
			arrayElementTwoArray.put(arrayElementTwoArrayElementTwo);
			arrayElementTwoArray.put(arrayElementTwoArrayElementThree);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			arrayElementTwo.put("options", arrayElementTwoArray);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			array.put(arrayElementOne);
			array.put(arrayElementTwo);
			object.put("questions",array);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
			JSONObject object = null;
			try {
				object = DbMethods.getQuestions(course);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.write(object.toString());
		out.flush();
		out.close();
	}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		String course	=	"";
		try {
			BufferedReader reader = request.getReader();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				System.out.println("line : "+line);
			}
		} catch (Exception e) {
			e.printStackTrace();

		}

		JSONParser parser = new JSONParser();
		JSONArray jsonObject =null;
		String courseNum = null;
		int count=0;
		ArrayList<String> submittedOptions = new ArrayList<String>();
		try {
			org.json.simple.JSONObject object = (org.json.simple.JSONObject)parser.parse(sb.toString());
			courseNum = (String)object.get("course");
			 jsonObject = (JSONArray)object.get("options");
			 System.out.println("size"+jsonObject.size());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		HttpSession session   = request.getSession();
		String option=null;
		for(int i=0;i<jsonObject.size();i++){
			System.out.println("value"+i+" : "+jsonObject.get(i));
			submittedOptions.add((jsonObject.get(i)==null)?"0":jsonObject.get(i).toString());
		}
		try {
			
			System.out.println("cours"+courseNum);
			String outcomes = DbMethods.getOutcomes(courseNum);
			ArrayList<String> answers = DbMethods.getAnswers(outcomes,(String)session.getAttribute("college"));
			System.out.println("answers"+answers);
			System.out.println("submitted"+submittedOptions);
			for(String x:answers){
				for(String y:submittedOptions){
					if(x.equals(y)){
						System.out.println("count true");
						count+=1;
						continue;
					}
				}
				System.out.println("total Correct"+count);
			}
			Vector vec = new Vector();
			vec.add((String)session.getAttribute("id"));
			vec.add(count);
			vec.add(submittedOptions.size());
			vec.add(outcomes);
			vec.add((String)session.getAttribute("college"));
			vec.add((count/submittedOptions.size()*100));
			DbMethods.add(vec, "STORE_DATA");
			System.out.println("vec"+vec);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject result = new JSONObject();
		try {
			result.put("count", count);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.write(result.toString());
		out.flush();
		out.close();		
		
		System.out.println("arraylist size"+submittedOptions.size());
		System.out.println("evaluationFrom Get");
	}
		
}
