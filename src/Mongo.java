import java.util.ArrayList;
import java.util.Iterator;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


public class Mongo {

	//private MongoCollection<Document> Twitter;
	
	public Mongo(){
		super();
	
	}
	
	public void connexionMongo(String host, int port, String nomBase, String nomCollection){
		
		if(host.isEmpty() || port == 0){
			host = "localhost";
			port = 27017;			
		}
		
		MongoClient mongoClient = new MongoClient(host, port);
		MongoDatabase maBase = mongoClient.getDatabase(nomBase);
	
		MongoCollection<Document> Twitter = maBase.getCollection(nomCollection);
	}
	
	public void insertMongo(ArrayList<Tweet> tweetList){
		Iterator<Tweet> iter = tweetList.iterator();
		while (iter.hasNext()) {
			Tweet t = iter.next();
			
			Document doc = new Document();
			doc.append("user", t.getUser());
			doc.append("TweetId", t.getTweetId());
			doc.append("username", t.getUsername());
			doc.append("date", t.getTimeStamp());
			doc.append("content", t.getContent());
			doc.append("nbResponses", t.getNbResponses());
			doc.append("nbRetweets", t.getNbRetweets());
			doc.append("nbLikes", t.getNbLikes());
			
			Twitter.insertOne(doc);
		}
		System.out.println("Insertion ok !");	
			
	}
	
	
	
	
	
	/**
	 * @param args
	 */
	/* public static void main(String[] args) {
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
*/
}
