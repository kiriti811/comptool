package DbManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DbMethods {
	static DbConnection db = new DbConnection();
	static Statement st = null;
	static ResultSet res = null;
	public static void changePassword(String userName, String password) {
		// TODO Auto-generated method stub
		try {

			st = db.getConnection().createStatement();
			st.executeQuery("update userprofiles set password='"+password+"',confirmpassword='"+password+"' where username='"+userName+"'");
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String addUserProfiles(Vector columns, String formName) {
		// TODO Auto-generated method stub
		String patid = "user";
		int id = 0;
		try {

			Statement st1 = null;
			ResultSet res1 = null;
			st1 = db.getConnection().createStatement();
			res1 = st1
					.executeQuery("select userprofiles_seq.nextval from dual");
			while (res1.next()) {
				id = res1.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		patid = patid + id;
		String query = "Insert into userprofiles values(" + id + ",'" + patid
				+ "',";
		for (int i = 2; i < columns.size(); i++) {
			query = query + "'" + columns.get(i) + "',";
		}
		query = query.substring(0, query.length() - 1) + ",'customer')";
		try {
			st = db.getConnection().createStatement();
			st.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return patid;
	}


	public static String authenticate(String userName, String password, String userType) {
		String authStatus = "Fail$$$";
		try {
			st = db.getConnection().createStatement();
			res = st.executeQuery("select role,id from userprofiles where email='"
					+ userName + "' and password='"+ password +"'");
			while (res.next()) {
				authStatus = "Success$$$"+res.getString(2)+"$$$"+res.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return authStatus;
	}
	public static void add(Vector columns, String formName) {
		// TODO Auto-generated method stub
		String query	="Insert into "+formName+" "
				+ "values("+formName+"_seq.nextval,";
		for(int	i	=	0	;	i	<	columns.size();	i++)
		{
			query	=	query	+	"'"	+columns.get(i)+"',";
		}
		query	=	query.substring(0,query.length()-1)	+	")";
		try {
			st	=	db.getConnection().createStatement();
			System.out.println(query);
			st.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static int flightScheduleSeq(String formName){
		int 	fs	=	0;
		try {
			st	=	db.getConnection().createStatement();
			String query	=	"select "+formName+"_seq.nextval from dual";
			res	=	st.executeQuery(query);
			while(res.next()){
				fs	=	res.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return	fs;
	}
	public static void addFlights(Vector columns, String formName,Vector childData,String travelDate) {
		// TODO Auto-generated method stub
		int	Fs	= flightScheduleSeq(formName);
		String query	="Insert into "+formName+" "
				+ "values("+Fs+",";
		for(int	i	=	1	;	i	<	columns.size();	i++)
		{
			query	=	query	+	"'"	+columns.get(i)+"',";
		}
		String	Fsnumber	=	
				query	=	query.substring(0,query.length()-1)	+",'Fs"+Fs+"','"+travelDate+"')";
		try {
			st	=	db.getConnection().createStatement();
			st.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int	i	=	0;	i	<	childData.size();	i++){
			String query1	=	"insert into settimings values(settimings_seq.nextval,'Fs"+Fs+"','"+childData.get(i)+"','"+childData.get(i+1)+"','"+childData.get(i+2)+"','"+childData.get(i+3)+"','"+childData.get(i+4)+"','"+childData.get(i+5)+"','"+childData.get(i+6)+"')";	
			try {
				st.executeQuery(query1);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i	=	i	+	6;
		}
	}
	public static int listColumnCount(String formName) {
		// TODO Auto-generated method stub
		int columnCount	=	0;
		try {
			st	=	db.getConnection().createStatement();
			res = st.executeQuery("select COUNT(*) from dba_tab_columns where table_name = '"+formName+"'");
			while(res.next()){
				columnCount	=	res.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return columnCount;
	}
	public static Vector listColumns(String formName,int columnCount) {
		Vector vec	=	new	Vector();
		try {
			st	=	db.getConnection().createStatement();
			res	=	st.executeQuery("select column_name from dba_tab_columns where table_name = '"+formName+"'");
			while(res.next()){
				vec.add(res.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vec;
	}
	public static Vector listRows(String formName,int columnCount) {
		// TODO Auto-generated method stub
		Vector	vec	=	new	Vector();
		try {
			st	=	db.getConnection().createStatement();
			res	=	st.executeQuery("select * from "+formName+" order by id");
			while(res.next()){
				for(int i	=	0;	i	<	columnCount;	i++){
					vec.add(res.getString(i+1));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vec;
	}
	public static void edit(Vector variables,Vector values, String formName, int id) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		String query	="Update "+formName+" set ";
		for(int	i	=	0	;	i	<	values.size();	i++)
		{
			if(i==0)
			{
				query	=	query+variables.get(i)	+ "="	+values.get(i)+",";
			}
			else{
				query	=	query+variables.get(i)	+ "='"	+values.get(i)+"',";
			}
		}
		query	=	query.substring(0,query.length()-1)+ " where id="+id;
		try {
			st	=	db.getConnection().createStatement();
			st.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Vector listEdit(int id, String formName,int columnCount) {
		// TODO Auto-generated method stub
		Vector	vec	=	new	Vector();
		try {
			st	=	db.getConnection().createStatement();
			res	=	st.executeQuery("select * from "+formName+" where id="+id);
			while(res.next()){
				for(int i	=	0;	i	<	columnCount;	i++){
					vec.add(res.getString(i+1));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vec;
	}
	public static Vector dropDown(String columnName,String tableName) {
		Vector vec	=	new	Vector();
		try {
			st	=	db.getConnection().createStatement();
			res	=	st.executeQuery("select id,"+columnName+" from  "+tableName+"");

			while(res.next()){
				vec.add(res.getInt(1));
				vec.add(res.getString(2));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vec;
		// TODO Auto-generated method stub

	}
	public static ArrayList dropDown(String columnName,String tableName,String role) throws SQLException {
		ArrayList vec	=	new	ArrayList();
		String	query	=	"";
		System.out.println("role"+role);


		if(role=="firstName")
		{
			query	=	"select "+columnName+" from  "+tableName+" where role='"+role+"'";
		}
		else if(role.contains("###"))
		{

			role	=	role.substring(0,(role.indexOf("###")));
			query	=	"select "+columnName+" from  "+tableName+" where INSTRUCTOR='"+role+"'";

		}
		else if(role.contains("%%%"))
		{

			role	=	role.substring(0,(role.indexOf("%%%")));
			query	="select ac."+columnName+" from "+tableName+" ac,student_Course_details sc,userprofiles up ";
			query +="where ac.id=sc.course_id and sc.studentid=up.id and up.id='"+role+"'";

		}
		else if(role.contains("$$$"))
		{
			role	=	role.substring((role.indexOf("$$$")+3),role.length());
			query	=	"select "+columnName+" from  "+tableName+" where coursenumber='"+role+"'";
		}
		System.out.println("query"+query);

		try {
			st	=	db.getConnection().createStatement();
			res	=	st.executeQuery(query);
			while(res.next()){
				vec.add(res.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		res.close();
		return vec;
		// TODO Auto-generated method stub

	}
	public static String dropDownReference(String referenceId,String columnName,String tableName) {
		String str	=	"";
		try {
			st	=	db.getConnection().createStatement();
			res	=	st.executeQuery("select "+columnName+" from  "+tableName+" where id="+referenceId);
			while(res.next()){
				str=res.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
		// TODO Auto-generated method stub

	}

	public static Vector getAwarededBids(String userName) {
		// TODO Auto-generated method stub
		Vector vec	=	new	Vector();
		try {
			st	=	db.getConnection().createStatement();
			res	=	st.executeQuery("select username,projectName,bidamount from  userBids where status='awarded' and username='"+userName+"'");
			while(res.next()){
				Statement st1 = null;
				ResultSet res1 = null;
				st1	=	db.getConnection().createStatement();
				res1=	st1.executeQuery("select projectName from manageProject where id="+res.getString(2));
				vec.add(res.getString(1));
				while(res1.next()){
					vec.add(res1.getString(1));
				}
				vec.add(res.getString(3));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vec;
	}

	public static Vector getLocation() {
		Vector 	vec =	new Vector();
		try {
			st	=	db.getConnection().createStatement();
			res	=	st.executeQuery("select location from  airportdetails");
			while(res.next()){
				vec.add(res.getString(1));			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vec;
	}
	public static Vector searchFlights(String typeOfTrip, String from,
			String to, String travelDate) {
		// TODO Auto-generated method stub
		Vector	flightDetails	=	new	Vector();	 
		try {
			st	=	db.getConnection().createStatement();
			res	=	st.executeQuery("select * from  flightschedule where traveldate='"+travelDate+"'");
			while(res.next()){
				String route	=	res.getString(2);
				if(from.equals(route.substring(1,route.length()).substring(0,route.substring(1,route.length()).indexOf("-")))    &&      to.equals(route.substring(route.lastIndexOf("-")+1,route.length())))
				{
					if(res.getInt(5)>0){
						Statement st1 = null;
						ResultSet res1 = null;
						int	cost	=	res.getInt(5);
						st1	=	db.getConnection().createStatement();
						res1=	st1.executeQuery("select * from setTimings where flightscheduleid='"+res.getString(7)+"'");
						while(res1.next()){
							flightDetails.add(res1.getString(2));
							flightDetails.add(res1.getString(3));
							flightDetails.add(res1.getString(4));
							flightDetails.add(res1.getString(5));
							flightDetails.add(res1.getString(6));
							flightDetails.add(res1.getString(7));
							flightDetails.add(res1.getString(8));
							flightDetails.add(res1.getString(9));
						}
						flightDetails.add(res.getInt(6));

					}
				}
			}
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return	flightDetails;
	}
	public static Vector retriveFlightScheduleID() {
		// TODO Auto-generated method stub
		Vector flightScheduleID	=	new Vector();
		try{
			st	=	db.getConnection().createStatement();
			res=	st.executeQuery("select flightscheduleid from setTimings");
			while(res.next()){
				flightScheduleID.add(res.getString(2));
			}
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return flightScheduleID;
	}


	public static int customerDetailsSeq(String formName){
		int 	fs	=	0;
		try {
			st	=	db.getConnection().createStatement();
			String query	=	"select "+formName+"_seq.nextval from dual";
			res	=	st.executeQuery(query);
			while(res.next()){
				fs	=	res.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return	fs;
	}
	public static int TicketSeq(String formName){
		int 	fs	=	0;
		try {
			st	=	db.getConnection().createStatement();
			String query	=	"select "+formName+"_seq.nextval from dual";
			res	=	st.executeQuery(query);
			while(res.next()){
				fs	=	res.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return	fs;
	}
	public static String addCustomers(Vector childData, String fsCode,String date, String userName) {
		// TODO Auto-generated method stub
		String str	="";
		int count	=	0;
		int	ticket_seq	=	TicketSeq("ticket");
		for(int	i	=	0;	i	<	childData.size();	i++){
			int	Fs	= customerDetailsSeq("customerDetails");
			str	=	"TMS"+ticket_seq;
			String query1	=	"insert into customerDetails values("+Fs+",'"+fsCode+"','"+childData.get(i)+"','"+childData.get(i+1)+"','"+childData.get(i+2)+"','"+childData.get(i+3)+"','"+str+"','"+childData.get(i+4)+"','"+date+"','"+userName+"')";	
			try {
				st.executeQuery(query1);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			count	=	count	+	1;
			i	=	i	+	4;
		}
		String	query2	="update flightschedule set seatsavailable=(select seatsavailable from flightschedule where flightscheduleid='"+fsCode+"')-"+count+" where flightscheduleid='"+fsCode+"'";
		try {
			st.executeUpdate(query2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return	str;
	}
	public static Vector getTicket(String ticketCode) {
		Vector	ticketDetails	=	new	Vector();
		Vector	routeDetails	=	new	Vector();
		ticketDetails.add(ticketCode);
		Vector	totalDetails	=	new	Vector();
		String flighscheduleid	=	"";
		// TODO Auto-generated method stub
		try {
			st	=	db.getConnection().createStatement();
			res	=	st.executeQuery("select fullname,cost,flightscheduleid from customerdetails where ticketnumber='"+ticketCode+"'");
			while(res.next()){
				ticketDetails.add(res.getString(1));
				ticketDetails.add(res.getString(2));
				flighscheduleid	=	res.getString(3);
			}
			res	=	st.executeQuery("select * from settimings where flightscheduleid='"+flighscheduleid+"'");
			while(res.next()){
				routeDetails.add(res.getString(3));
				routeDetails.add(res.getString(4));
				routeDetails.add(res.getString(5));
				routeDetails.add(res.getString(6));
				routeDetails.add(res.getString(7));
				routeDetails.add(res.getString(8));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		totalDetails.add(ticketDetails);
		totalDetails.add(routeDetails);
		return	totalDetails;
	}
	public static Vector searchTickets(String ticketNumber) {
		// TODO Auto-generated method stub
		Vector	ticketDetails	=	new	Vector();
		try{
			String	temp1	=	"";
			String	query	=	"";
			if(ticketNumber.equals(temp1))
			{
				query	=	"select * from customerdetails order by id";
			}
			else if (!ticketNumber.equals(temp1)) {
				query	=	"select * from customerdetails where ticketnumber='"+ticketNumber+"' order by id";
			}
			String	temp2	=	"";
			st	=	db.getConnection().createStatement();
			res=	st.executeQuery(query);
			while(res.next()){
				if(!temp2.equals(res.getString(7))){
					ticketDetails.add(res.getString(7));
					ticketDetails.add(res.getString(9));
					ticketDetails.add(res.getString(10));
					temp2	=	res.getString(7);
				}
			}
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ticketDetails;
	}
	public static Vector searchUserTickets(String ticketNumber) {
		// TODO Auto-generated method stub
		Vector	ticketDetails	=	new	Vector();
		try{
			String	temp1	=	"";
			String	query	=	"";
			if(ticketNumber.equals(temp1))
			{
				query	=	"select * from customerdetails order by id";
			}
			else if (!ticketNumber.equals(temp1)) {
				query	=	"select * from customerdetails where username='"+ticketNumber+"' order by id";
			}
			String	temp2	=	"";
			st	=	db.getConnection().createStatement();
			res=	st.executeQuery(query);
			while(res.next()){
				if(!temp2.equals(res.getString(7))){
					ticketDetails.add(res.getString(7));
					ticketDetails.add(res.getString(9));
					ticketDetails.add(res.getString(10));
					ticketDetails.add("Booked");
					temp2	=	res.getString(7);
				}
			}
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ticketDetails;
	}
	public static String getInstructorId(String insturctor) {
		// TODO Auto-generated method stub
		String	id	=	"";
		try{
			String	temp2	=	"";
			st	=	db.getConnection().createStatement();
			res=	st.executeQuery("select id from userprofiles where firstname||' '||lastname='"+insturctor+"'");
			while(res.next()){
				id	=	res.getString(1);
			}
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}
	public static ArrayList<String> getInstructorName() {
		// TODO Auto-generated method stub
		ArrayList<String> instructors = new ArrayList<String>();
		try{
			String	temp2	=	"";
			st	=	db.getConnection().createStatement();
			res=	st.executeQuery("select firstname||' '||lastname from userprofiles where role='Faculty'");
			while(res.next()){
				instructors.add(res.getString(1));
			}
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return instructors;
	}
	public static Vector getDailyReport(String date) {
		// TODO Auto-generated method stub
		Vector	dailyReport	=	new	Vector();
		try{
			String	temp1	=	"";
			String	query	=	"";
			if(date.equals(temp1))
			{
				query	=	"select * from customerdetails order by id";
			}
			else if (!date.equals(temp1)) {
				query	=	"select * from customerdetails where bookeddate='"+date+"' order by id";
			}
			int	temp2	=	0;
			st	=	db.getConnection().createStatement();
			res=	st.executeQuery(query);
			int 	count	=	0;
			while(res.next()){
				dailyReport.add(res.getString(7));
				dailyReport.add(res.getString(8));
			}
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dailyReport;
	}
	public static Vector getYearlyReport(String date) {
		// TODO Auto-generated method stub
		Vector	dailyReport	=	new	Vector();
		try{
			String	temp1	=	"";
			String	query	=	"";
			if(date.equals(temp1))
			{
				query	=	"select * from customerdetails order by id";
			}
			else if (!date.equals(temp1)) {
				query	=	"select * from customerdetails where bookeddate like '%"+date+"%' order by id";
			}
			int	temp2	=	0;
			st	=	db.getConnection().createStatement();
			res=	st.executeQuery(query);
			int 	count	=	0;
			while(res.next()){
				dailyReport.add(res.getString(7));
				dailyReport.add(res.getString(8));
			}
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dailyReport;
	}

	public static void cancelTicket(String ticketCode,String CancelDate) {
		// TODO Auto-generated method stub
		Vector	ticketDetails	=	new	Vector();
		Vector	routeDetails	=	new	Vector();
		Vector	totalDetails	=	new	Vector();
		String flighscheduleid	=	"";
		// TODO Auto-generated method stub
		try {
			st	=	db.getConnection().createStatement();
			res	=	st.executeQuery("select * from customerdetails where ticketnumber='"+ticketCode+"'");
			while(res.next()){
				ticketDetails.add(res.getString(2));
				ticketDetails.add(res.getString(3));
				ticketDetails.add(res.getString(4));
				ticketDetails.add(res.getString(5));
				ticketDetails.add(res.getString(6));
				ticketDetails.add(res.getString(7));
				ticketDetails.add(res.getString(8));
				ticketDetails.add(res.getString(9));
				ticketDetails.add(res.getString(10));
			}
			for(int	i	=	0;	i	<	ticketDetails.size()/9	;	i++)
			{
				st.executeUpdate("insert into cancelcustomerdetails values(cancelcustomerdetails_seq.nextval,'"+ticketDetails.get(i+0)+"','"+ticketDetails.get(i+1)+"','"+ticketDetails.get(i+2)+"','"+ticketDetails.get(i+3)+"','"+ticketDetails.get(i+4)+"','"+ticketDetails.get(i+5)+"','"+ticketDetails.get(i+6)+"','"+ticketDetails.get(i+7)+"','"+ticketDetails.get(i+8)+"','"+CancelDate+"')");
				i	=	i	+	1;
			}
			st.executeUpdate("Delete from customerdetails where ticketnumber='"+ticketCode+"'");
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static JSONObject getQuestions(String course) throws SQLException, JSONException{

		st	=	db.getConnection().createStatement();

		JSONObject questionsObject = new JSONObject();
		JSONArray questionsArray = new JSONArray();
		if(course!=null){
			StringTokenizer outcomes = new StringTokenizer(getOutcomes(course),",");
			while(outcomes.hasMoreElements())
			{
				res	=	st.executeQuery("select * from questions where selectedoutcome ='"+outcomes.nextToken()+"'");
				while(res.next()){
					JSONObject questionObject = new JSONObject();
					questionObject.put("description", res.getString(3));
					questionObject.put("id", res.getInt(1));

					JSONObject option1 = new JSONObject();
					JSONObject option2 = new JSONObject();
					JSONObject option3 = new JSONObject();
					JSONObject option4 = new JSONObject();
					JSONObject correct_option = new JSONObject();
					option1.put("id", res.getInt(1)+"_1");
					option1.put("description", res.getString(4));
					option2.put("id", res.getInt(1)+"_2");
					option2.put("description", res.getString(5));
					option3.put("id", res.getInt(1)+"_3");
					option3.put("description", res.getString(6));
					option4.put("id", res.getInt(1)+"_4");
					option4.put("description", res.getString(7));
					JSONArray optionsArray = new JSONArray();
					optionsArray.put(option1);
					optionsArray.put(option2);
					optionsArray.put(option3);
					optionsArray.put(option4);
					questionObject.put("options", optionsArray);
					questionsArray.put(questionObject);
				}
			}

		}
		else{
			res	=	st.executeQuery("select * from questions");
			while(res.next()){
				JSONObject questionObject = new JSONObject();
				questionObject.put("description", res.getString(3));
				questionObject.put("id", res.getInt(1));

				JSONObject option1 = new JSONObject();
				JSONObject option2 = new JSONObject();
				JSONObject option3 = new JSONObject();
				JSONObject option4 = new JSONObject();
				JSONObject correct_option = new JSONObject();
				option1.put("id", res.getInt(1)+"_1");
				option1.put("description", res.getString(4));
				option2.put("id", res.getInt(1)+"_2");
				option2.put("description", res.getString(5));
				option3.put("id", res.getInt(1)+"_3");
				option3.put("description", res.getString(6));
				option4.put("id", res.getInt(1)+"_4");
				option4.put("description", res.getString(7));
				JSONArray optionsArray = new JSONArray();
				optionsArray.put(option1);
				optionsArray.put(option2);
				optionsArray.put(option3);
				optionsArray.put(option4);
				questionObject.put("options", optionsArray);
				questionsArray.put(questionObject);
			}			
		}
		questionsObject.put("questions", questionsArray);
		return questionsObject;
	}
	public static JSONArray getReports(String college) throws SQLException, JSONException{
		Statement s	=	db.getConnection().createStatement();
		JSONObject questionsObject = new JSONObject();
		JSONArray questionsArray = new JSONArray();
		int i=1;
		res	=	s.executeQuery("select name||'_'||correct_count||'_'||total_count from store_data where college='"+college+"'");
		//System.out.println(res.getFetchSize());

		while(res.next()){
			JSONObject questionObject = new JSONObject();
			System.out.println("count "+res.getString(1));
			String data = res.getString(1);
			System.out.println("data : "+data);
			StringTokenizer token = new StringTokenizer(data, "_");

			int report_data[] = new int[3];
			int id = Integer.parseInt(data.split("_")[0]);
			int score = Integer.parseInt(data.split("_")[1]);
			int total = Integer.parseInt(data.split("_")[2]);
			System.out.println("id : "+id);
			System.out.println("score : "+score);
			System.out.println("total : "+total);
			//String name = getName(report_data[0]);
			//	System.out.println("name : "+name);
			questionObject.put("name", id);
			questionObject.put("score", score);
			questionObject.put("total", total);
			//	int score_count = res.getInt(2);
			//int total_count = res.getInt(3);
			//System.out.println("score"+res.getString(2));
			//String score = res.getString(2);
			//int questions = res.getInt(4);
			//System.out.println("score"+score);
			//System.out.println("questions"+questions);
			//System.out.println("name : : "+name);
			//questionObject.put("name", name);
			questionsArray.put(questionObject);
		}
		questionsObject.put("reports",questionsArray);

		return questionsArray;
	}

	public static JSONArray getUniversityReports() throws SQLException, JSONException{
		Statement s	=	db.getConnection().createStatement();
		JSONObject questionsObject = new JSONObject();
		JSONArray questionsArray = new JSONArray();
		int i=1;
		res	=	s.executeQuery("select college,avg(percentage) from store_data group by college");
		//System.out.println(res.getFetchSize());

		while(res.next()){
			JSONObject questionObject = new JSONObject();
			System.out.println("count "+res.getString(1));
			System.out.println("average"+res.getInt(2));
			String college=null;
			if(res.getString(1).equals("ucmo"))
				college="University of Central Missouri";
			else  if(res.getString(1).equals("umkc"))
				college="University of Missouri Kansas City";
			else if(res.getString(1).equals("kent"))
				college="Kent State University";
			int average = res.getInt(2);
			//String name = getName(report_data[0]);
			//	System.out.println("name : "+name);
			questionObject.put("name", college);
			questionObject.put("avg", average);
			//	int score_count = res.getInt(2);
			//int total_count = res.getInt(3);
			//System.out.println("score"+res.getString(2));
			//String score = res.getString(2);
			//int questions = res.getInt(4);
			//System.out.println("score"+score);
			//System.out.println("questions"+questions);
			//System.out.println("name : : "+name);
			//questionObject.put("name", name);
			questionsArray.put(questionObject);
		}
		questionsObject.put("reports",questionsArray);

		return questionsArray;
	}

	public static String getName(int id) throws SQLException{
		String name=null;
		st	=	db.getConnection().createStatement();
		res	=	st.executeQuery("select firstname||' '||lastname from userprofiles where id='"+id+"'");
		while(res.next()){

			name = res.getString(1);
		}
		return name;
	}
	public static String getOutcomes (String course) throws SQLException{
		String outcome=null;
		st	=	db.getConnection().createStatement();
		res	=	st.executeQuery("select courseoutcome from addcourses where coursenumber='"+course+"'");
		while(res.next()){
			outcome = res.getString(1);
		}
		res.close();
		return outcome;

	}
	public static ArrayList<String> getAnswers(String outcome,String college) throws SQLException{
		st	=	db.getConnection().createStatement();
		ArrayList<String> answers = new ArrayList<String>();

		if(outcome ==null){
			res	=	st.executeQuery("select * from questions where college='"+college+"'");	
			while(res.next()){
				answers.add(res.getInt(1)+"_"+res.getInt(8));
			}
		}
		else{
		StringTokenizer outcomes = new StringTokenizer(outcome,",");

		while(outcomes.hasMoreElements()){
			res	=	st.executeQuery("select * from questions where selectedoutcome ='"+outcomes.nextToken()+"'");	
			while(res.next()){
				answers.add(res.getInt(1)+"_"+res.getInt(8));
			}
		}
		}
		return answers;
	}
	/*public static int showResult(ArrayList<String> options) throws SQLException{
		st	=	db.getConnection().createStatement();
		for( int)
		role	=	role.substring(0,(role.indexOf("%%%")));
		res	=	st.executeQuery("select * from questions where id ='"++"'");
		return 0;
	}*/
}