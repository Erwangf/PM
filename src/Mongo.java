import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


public class Mongo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		//Mongo mongoClient = new Mongo();
		MongoClient mongoClient = new MongoClient();
		MongoDatabase maBase = mongoClient.getDatabase("BaseTest");
	
		MongoCollection<Document> Twitter = maBase.getCollection("Twitter");
		
		System.out.println(Twitter.count());
		//DBCollection logCollection = mongoClient.getCollection("app_logs");
		
		
		Document doc = new Document();
        doc.append("tweet", "c'est mon tweet de Test, signé Franck ! :p");
        doc.append("location", "chez moi");
        doc.append("isretweet", true);

        Twitter.insertOne(doc);
		
		
		
	}

}
