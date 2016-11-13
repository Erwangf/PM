import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;

import java.sql.Timestamp;


public class Mongo {

	private MongoCollection<Document> twitter;
	
	public Mongo(){
		super();
	
	}
	
	//Connexion mongo simple/locale
	public void ConnexionMongo(String host, int port, String nomBase, String nomCollection){
		
		if(host.isEmpty()){
			host = "localhost";
						
		}
		
		if( port == 0){
			port = 27017;			
		}
		
		MongoClient mongoClient = new MongoClient(host, port);
		MongoDatabase maBase = mongoClient.getDatabase(nomBase);
	
		this.twitter = maBase.getCollection(nomCollection);
	}
	
	
	//Connexion mongo authentifiée
	public void ConnexionMongo(String host, int port, String nomBase, String nomCollection, String user, String pass){
		
		if(host.isEmpty()){
			host = "localhost";
						
		}
		
		if( port == 0){
			port = 27017;			
		}
		
		char[] password = pass.toCharArray();
		
		List<ServerAddress> serverAddresses = new ArrayList<ServerAddress>();
        ServerAddress address = new ServerAddress(host, port);
        serverAddresses.add(address);
		
		List<MongoCredential> credentials = new ArrayList<MongoCredential>();
	    MongoCredential credential = MongoCredential.createPlainCredential(user, "$external", password);
	    credentials.add(credential);
	    MongoClient mongoClient = new MongoClient(serverAddresses, credentials);
	
		MongoDatabase maBase = mongoClient.getDatabase(nomBase);
	
		this.twitter = maBase.getCollection(nomCollection);
		System.out.println("connexion distante réussie");
	}
	
	//Insertion d'une ArrayList de Tweets dans la base Mongo
	public void InsertMongo(ArrayList<Tweet> tweetList){
		Iterator<Tweet> iter = tweetList.iterator();
		while (iter.hasNext()) {
			Tweet t = iter.next();
			
			Document doc = new Document();
			doc.append("user", t.getUser());
			doc.append("TweetId", t.getTweetId());
			doc.append("username", t.getUsername());
			doc.append("date",  t.getTimeStamp().getTime());
			doc.append("content", t.getContent());
			doc.append("nbResponses", t.getNbResponses());
			doc.append("nbRetweets", t.getNbRetweets());
			doc.append("nbLikes", t.getNbLikes());
			//System.out.println(doc);
			twitter.insertOne(doc);
		}
		System.out.println("Insertion ok !");	
			
	}
	

	//renvoie le nombre d'éléments dans la collection (nombre de Tweets ici)
	public long GetNbTweets(){
		long nbTweet;
		nbTweet = twitter.count();
		return nbTweet;
	}
	
	
	//renvoie tous les Tweets de la collection dans une ArrayList de type Tweet
	public ArrayList<Tweet> GetAllTweets(){
		ArrayList<Tweet> tweetsV2 = new ArrayList<Tweet>();
		ArrayList<Document> tweetsV1;
		
		tweetsV1 = twitter.find().into(new ArrayList<Document>());

		Iterator<Document> iter = tweetsV1.iterator();
		while (iter.hasNext()) {
			Document doc = iter.next();
			//System.out.println(doc);
			Tweet t = new Tweet(doc.get("user").toString(), doc.get("TweetId").toString(), doc.get("username").toString(), new Timestamp(Long.parseLong(doc.get("date").toString())), doc.get("content").toString(), Integer.parseInt(doc.get("nbResponses").toString()), Integer.parseInt(doc.get("nbRetweets").toString()), Integer.parseInt(doc.get("nbLikes").toString()));
			
			tweetsV2.add(t);
		}
		return tweetsV2;
	}
	
	
	//renvoie toutes les dates en timestamp (pour l'instant ?)
	public ArrayList<Document> GetAllDates(){
		ArrayList<Document> tweetsDates;
		
		Bson projection = Projections.fields(Projections.include("date"),Projections.excludeId());
		tweetsDates = twitter.find().projection(projection).into(new ArrayList<Document>());
		return tweetsDates;
	}

}

