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
 * Servlet implementation class TakeTests
 */
@WebServlet("/TakeTests")
public class TakeTests extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TakeTests() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		JSONObject object = null;
		try {
			object = DbMethods.getQuestions(null);
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		HttpSession session = request.getSession();
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
			 jsonObject = (JSONArray)object.get("options");
			 System.out.println("size"+jsonObject.size());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String option=null;
		for(int i=0;i<jsonObject.size();i++){
			System.out.println("value"+i+" : "+jsonObject.get(i));
			submittedOptions.add((jsonObject.get(i)==null)?"0":jsonObject.get(i).toString());
		}
		try {
			
			ArrayList<String> answers = DbMethods.getAnswers(null,(String)session.getAttribute("college"));
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
			vec.add((String)DbMethods.getName((Integer.parseInt((String)session.getAttribute("id")))));
			vec.add(count);
			vec.add(submittedOptions.size());
			vec.add("nullable");
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
