package com.kyh.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import codes.libs.NetUtils;

import com.kyh.R;
import com.kyh.objects.HorseObj;
import com.kyh.objects.HorseSearchObj;
import com.kyh.objects.PhotoObj;
import com.kyh.objects.ReportObj;
import com.kyh.objects.UserObj;

import android.content.Context;
import android.graphics.BitmapFactory;

public class JsonParser {
// =========================================================================
	public static ArrayList<UserObj> getLoginData(String email, Context core){
		
		ArrayList<UserObj> UserList = new ArrayList<UserObj>();
		String JSON_KEY_VALID = "valid";
		String JSON_KEY_USERDATA = "userdata";
		String ValidVal = "";
		
		String URL = Configs.KYH_HOST_LINK+"userAccount.php?u="+email+"";
		
		List<NameValuePair> params = new ArrayList<NameValuePair>(); //?
		JsonParsing jsonParser = new JsonParsing();
        JSONObject json = jsonParser.getJSONFromUrl(URL, "GET", params);
        
        try{
        	
        	if (json != null){
        		
        		
        		ValidVal = json.get(JSON_KEY_VALID).toString();
            	
            	if(ValidVal.equals("1")){
            		
            		JSONArray UserData = json.getJSONArray(JSON_KEY_USERDATA);
            		
            		for(int x = 0; x < UserData.length(); x++){
            			
            			UserObj User = new UserObj();
            			JSONObject user = UserData.getJSONObject(x);
            			
            			User.VALID = ValidVal;
            			
            			User.USERID = user.getString("user_id");
            			User.DISPLAYNAME = user.getString("displayname");
            			User.BIRTHDATE = user.getString("birthdate");
            			User.GENDER = user.getString("gender");
            			User.USERNAME = user.getString("username");
            			User.MEMBERSHIPTYPE = user.getString("membershiptype");
            			User.DESCRIPTION = user.getString("description");
            			User.TIMEZONE = user.getString("timezone");
            			User.LOCALE = user.getString("locale");
            			User.EMAIL = user.getString("email");
            			User.PICTURE = user.getString("picture");
            			
            			UserList.add(User);
            		}
            		
            	} else if(ValidVal.equals("0")){
            		
            		UserObj User = new UserObj();
            		User.VALID = ValidVal;
            		UserList.add(User);
            	} 
            	
        	} else {
        		
        		UserObj User = new UserObj();
        		User.VALID = "false";
        		UserList.add(User);
        	}
        	
        }catch(Exception e){
        	e.printStackTrace();
        }
        
        return UserList;
	}
	// =========================================================================
	public static String updateProfile(String id,String firstname, String lastname, String birthdate, String gender, String desc){
		
		String JSON_KEY_SUCCESS = "success";
		String success = "";
		
		String URL = Configs.KYH_HOST_LINK+"userAccountEdit.php?t=1&e1="+id+"&e2="+firstname+"&e3="+lastname+"&e4="+gender+"&e5="+birthdate+"&e6="+desc;
		
		List<NameValuePair> params = new ArrayList<NameValuePair>(); //?
		JsonParsing jsonParser = new JsonParsing();
        JSONObject json = jsonParser.getJSONFromUrl(URL, "GET", params);
        
        try{
        	
        	if(json != null){
        		
        		Object Valid = json.get(JSON_KEY_SUCCESS);
            	String Succeed = Valid.toString();
            	
            	if(Succeed.equals("1")){
            		success = "Success";
            	}else{
            		success = "Failed";
            	}
            	
        	} else {
        		success = "False";
        	}
        	
        }catch(Exception e){
        	
        }
        return success;
	}
// =========================================================================
	public static String updateGeneralSet(String id, String email, String profile, String timezone, String locale){
		
		String JSON_KEY_SUCCESS = "success";
		String success = "";
		
		//String URL = Configs.KYH_HOST_LINK+"userAccountEdit.php?t=1&e1="+id+"&e2="+firstname+"&e3="+lastname+"&e4="+gender+"&e5="+birthdate+"&e6="+desc;
		String URL = Configs.KYH_HOST_LINK+"userAccountEdit.php?t=2&e1="+id+"&e2="+email+"&e3="+profile+"&e4="+timezone+"&e5="+locale;
		
		List<NameValuePair> params = new ArrayList<NameValuePair>(); //?
		JsonParsing jsonParser = new JsonParsing();
        JSONObject json = jsonParser.getJSONFromUrl(URL, "GET", params);
        
        try{
        	
        	if(json != null){
        		
        		Object Valid = json.get(JSON_KEY_SUCCESS);
            	String Succeed = Valid.toString();
            	
            	if(Succeed.equals("1")){
            		success = "Success";
            	}else{
            		success = "Failed";
            	}
            	
        	} else {
        		success = "False";
        	}
        	
        }catch(Exception e){
        	
        }
        
        return success;
	}
	// =========================================================================
	public static ArrayList<HorseObj> getOwnedHorse(String trainerID, Context context){
		
		String JSON_KEY_SUCCESS = "success";
		String JSON_KEY_HORSEDATA = "horse_data";
		ArrayList<HorseObj> HorseList = new ArrayList<HorseObj>();
		
		String URL = Configs.KYH_HOST_LINK+"ownedhorse.php?uid="+trainerID+"";
		
		List<NameValuePair> params = new ArrayList<NameValuePair>(); //?
		JsonParsing jsonParser = new JsonParsing();
        JSONObject json = jsonParser.getJSONFromUrl(URL, "GET", params);
        
        try{
        	
        	if( json != null){
        		
        		Object Valid = json.get(JSON_KEY_SUCCESS);
            	String successValue = Valid.toString();
            	
            	if(successValue.equals("1")){
            		
            		JSONArray HorseData = json.getJSONArray(JSON_KEY_HORSEDATA);
            		
            		for(int x = 0; x < HorseData.length(); x ++){
                    	
                    	HorseObj  Horse = new HorseObj();
                    	JSONObject DataHorse = HorseData.getJSONObject(x);
                    	
                    	Horse.SUCCESS = successValue;
                    	Horse.UPDATE = "true";
                    	
                    	Horse.ID = DataHorse.getString("horse_id");
                    	Horse.TITLE = DataHorse.getString("title");
                    	Horse.DOB = DataHorse.getString("dob");
                    	Horse.GENDER = DataHorse.getString("gender");
                    	Horse.DESCRIPTION = DataHorse.getString("descri");
                    	Horse.AGE = DataHorse.getString("age");
                    	Horse.BREEDING = DataHorse.getString("breeding");
                    	Horse.CONFORMATON = DataHorse.getString("conformation");
                    	Horse.HEIGHT = DataHorse.getString("height");
                    	Horse.WEIGHT = DataHorse.getString("weight");
                    	Horse.TYPE = DataHorse.getString("type");
                    	Horse.TRAINER = DataHorse.getString("trainer");
                    	Horse.OWNER = DataHorse.getString("owners");
                    	Horse.PICPATH = DataHorse.getString("path");
                    	
                    	HorseList.add(Horse);
                    }
            	}else{
            		
            		HorseObj  Horse = new HorseObj();
            		
                	Horse.SUCCESS = successValue;
                	HorseList.add(Horse);
            	}
        	} else {
        		
        		HorseObj  Horse = new HorseObj();
        		
            	Horse.SUCCESS = "false";
            	HorseList.add(Horse);
        	}
        	
        }catch(Exception e){
        	
        }
        
        return HorseList;
	}
	// =========================================================================
	public static String updateHorseProfile(String id, String title, String dob, String desc, String breed, String confor, String height, String weight, String type){
		
		String JSON_KEY_SUCCESS = "success";
		String success = "";
		
		String URL = Configs.KYH_HOST_LINK+"horseedit.php?u1="+id+"&u2="+title+"&u3="+dob+"&u4="+desc+"&u5="+breed+"&u6="+confor+"&u7="+height+"&u8="+weight+"&u9="+type;
		
		List<NameValuePair> params = new ArrayList<NameValuePair>(); //?
		JsonParsing jsonParser = new JsonParsing();
        JSONObject json = jsonParser.getJSONFromUrl(URL, "GET", params);
        
        
        try{
        	
        	if(json != null){
        		
        		Object Valid = json.get(JSON_KEY_SUCCESS);
            	String Succeed = Valid.toString();
            	
            	if(Succeed.equals("1")){
            		success = "Success";
            	}else{
            		success = "Failed";
            	}
            	
        	} else {
        		success = "False";
        	}
        	
        }catch(Exception e){
        	
        }
        return success;
	}
	// =========================================================================
	public static ArrayList<HorseSearchObj> getHorseSearch(String key,int numpage){
		
		String JSON_KEY_SUCCESS = "success";
		String JSON_KEY_MAXPAGE = "maxpage";
		String JSON_KEY_PAGENUM = "pagenum";
		String JSON_KEY_HORSEDATA = "horsedata";
		
		String success = "";
		String maxpage = "";
		String pagenum = "";
		ArrayList<HorseSearchObj> HorseListSearch = new ArrayList<HorseSearchObj>();
		
		String URL = Configs.KYH_HOST_LINK + "horse.php?search=" +key+ "&page=" +numpage+ "&sort=1";
		
		List<NameValuePair> params = new ArrayList<NameValuePair>(); //?
		JsonParsing jsonParser = new JsonParsing();
        JSONObject json = jsonParser.getJSONFromUrl(URL, "GET", params);
        
        try{
        	
        	if( json != null){
        		
        		success = json.get(JSON_KEY_SUCCESS).toString();
        		
        		if(success.equals("1")){
        			
        			maxpage = json.get(JSON_KEY_MAXPAGE).toString();
            		pagenum = json.get(JSON_KEY_PAGENUM).toString();
        			
        			JSONArray HorseData = json.getJSONArray(JSON_KEY_HORSEDATA);
        			
        			for (int x = 0; x < HorseData.length(); x++){
        				
        				HorseSearchObj Horse = new HorseSearchObj();
        				JSONObject DataHorse = HorseData.getJSONObject(x);
        				
        				Horse.SUCCESS = success;
        				Horse.MAXPAGE = maxpage;
        				Horse.PAGENUM = pagenum;
        				
        				Horse.ID = DataHorse.getString("horseid");
                    	Horse.TITLE = DataHorse.getString("title");
                    	Horse.DOB = DataHorse.getString("dob");
                    	Horse.GENDER = DataHorse.getString("gender");
                    	Horse.DESCRIPTION = DataHorse.getString("descri");
                    	Horse.AGE = DataHorse.getString("age");
                    	Horse.BREEDING = DataHorse.getString("breeding");
                    	Horse.CONFORMATON = DataHorse.getString("conformation");
                    	Horse.HEIGHT = DataHorse.getString("height");
                    	Horse.WEIGHT = DataHorse.getString("weight");
                    	Horse.TYPE = DataHorse.getString("type");
                    	Horse.TRAINER = DataHorse.getString("trainer");
                    	Horse.OWNER = DataHorse.getString("owners");
                    	Horse.PICPATH = DataHorse.getString("path");
                    	
                    	HorseListSearch.add(Horse);
        			}
        			
        		} else {
        			
        			HorseSearchObj Horse = new HorseSearchObj();
        			Horse.SUCCESS = success;
        			HorseListSearch.add(Horse);
        		}
        		
        	} else {
        		
        		HorseSearchObj Horse = new HorseSearchObj();
    			Horse.SUCCESS = "False";
    			HorseListSearch.add(Horse);
        	}
        	
        }catch(Exception e){
        	
        }
        
        return HorseListSearch;
	}
 // =========================================================================
	public static ArrayList<PhotoObj> GetImageUrl(Context core,String hid){
		// Step 1
		ArrayList<PhotoObj> Photos = new ArrayList<PhotoObj>();
		HttpResponse res;
		
		String JSON_KEY_SUCCESS = "success";
		String JSON_KEY_PHOTO = "photo";
		String URL = Configs.KYH_HOST_LINK+"hphoto.php?t=1&id="+hid+"";
		
		List<NameValuePair> params = new ArrayList<NameValuePair>(); //?
		JsonParsing jsonParser = new JsonParsing();
        JSONObject json = jsonParser.getJSONFromUrl(URL, "GET", params);
        // Step 2
        try{
        	
        	Object Valid = json.get(JSON_KEY_SUCCESS);
        	String Succeed = Valid.toString();
        	
        	if(Succeed.equals("1")){
        		
        		JSONArray PhotoData = json.getJSONArray(JSON_KEY_PHOTO);
        		// Step 3
        		for(int x = 0; x < PhotoData.length(); x ++){
        			
        			PhotoObj Pics = new PhotoObj();
        			JSONObject photos = PhotoData.getJSONObject(x);
        			
//        			String path = PhotoData.getString(x).replace("|", "/");
        			String path = photos.getString("file").replace("|", "/");
        			String photoID = photos.getString("id");
        			
        			path = "http://knowyourhorse.com.au/" + path;
        			Pics.URL = path;
        			
        			res = NetUtils.PingURL(path);
        			
        			if (res != null){
        				
    					Pics.BITMAP = BitmapFactory.decodeStream(res.getEntity().getContent());
    					Pics.AVAILABLE = true;
    				} else {
    					
    					Pics.BITMAP = BitmapFactory.decodeResource(core.getResources(), R.drawable.picture_nai);
    					Pics.AVAILABLE = false;
    				}
        			
        			Photos.add(Pics);
        		}
        	}
        }catch(Exception e){
        }
        
        return Photos;
	}
	// =========================================================================
	public static ArrayList<ReportObj> getHorseReport(String type,Context context){
		
		String JSON_KEY_SUCCESS = "success";
		String JSON_KEY_REPORT = "report";
		ArrayList<ReportObj> ReportList = new ArrayList<ReportObj>();
		
		String URL = Configs.KYH_HOST_LINK+"report.php?r="+type+"";
		
		List<NameValuePair> params = new ArrayList<NameValuePair>(); //?
		JsonParsing jsonParser = new JsonParsing();
        JSONObject json = jsonParser.getJSONFromUrl(URL, "GET", params);
        
        try{
        	
        	if (json != null){
        	
	        	Object Valid = json.get(JSON_KEY_SUCCESS);
	        	String successValue = Valid.toString();
	        	
	        	if(successValue.equals("1")){
	        		
	        		JSONArray ReportData = json.getJSONArray(JSON_KEY_REPORT);
	        		
	        		for(int x = 0; x < ReportData.length(); x ++){
	                	
	                	ReportObj  Report = new ReportObj();
	                	JSONObject DataReport = ReportData.getJSONObject(x);
	                	
	                	Report.SUCCESS = successValue;
	                	Report.LABEL = DataReport.getString("label");
	                	Report.TYPE = DataReport.getString("type");
	                	Report.SELECTION = DataReport.getString("selection");
	                	
	                	ReportList.add(Report);
	                }
	        	}else{
	        		ReportObj  Report = new ReportObj();
	        		Report.SUCCESS = successValue;
	        		ReportList.add(Report);
	        	}
        	
        	} else {
        		ReportObj  Report = new ReportObj();
        		Report.SUCCESS = "False";
        		ReportList.add(Report);
        	}
        	
        } catch(Exception e){
        	
        }
		
        return ReportList;
	}
 // =========================================================================
 // TODO Final
}