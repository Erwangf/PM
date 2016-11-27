package modules.data;

import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

//import java.util.Collection;
//import java.util.Iterator;
//import java.util.List;
//
import org.bson.Document;
//import org.bson.conversions.Bson;
//
//import com.mongodb.BasicDBObject;
//import com.mongodb.DBCollection;
//import com.mongodb.DBCursor;
//import com.mongodb.MongoClient;
//import com.mongodb.MongoClientURI;
//import com.mongodb.MongoCredential;
//import com.mongodb.ServerAddress;
//import com.mongodb.client.FindIterable;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoCursor;
//import com.mongodb.client.MongoDatabase;
//import com.mongodb.client.model.Projections;
//
//import java.sql.Timestamp;

public class Trend {

	public static void main(String[] args){
		
		//création de l'instance mongo
		Mongo base = new Mongo();
		
		//connexion à la base distante
		base.ConnexionMongo("ds147537.mlab.com",47537,"twitter_rumors", "Twitter", "root", "TwitterMongo2016");
		
		//System.out.println(base.GetAllDates());
		ArrayList<Document> datesList = new ArrayList<Document>();
		datesList = base.GetAllDates();
		
//		for (int i = 0; i < 10; i++) {
//			System.out.println(datesList.get(i));	
//		}
		
		ArrayList<Timestamp> dates = new ArrayList<Timestamp>();
		//ArrayList<String> dateString = new ArrayList<String>();
		
		Iterator<Document> iter = datesList.iterator();
		while (iter.hasNext()) {
			Document doc = iter.next();
			//System.out.println(doc);
			Timestamp nbs = new Timestamp(Long.parseLong(doc.get("date").toString())*1000);
			
			dates.add(nbs);
			
			
			
		}
		SimpleDateFormat formatdate = new SimpleDateFormat("dd/MM/yyyy");
		
		for (int i = 0; i < 10; i++) {
			String string = formatdate.format(dates.get(i));
			
			System.out.println(string);	
		}
		
		
	}
	
}
