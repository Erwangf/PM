package modules.data;


import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;


public class Mongo {

    public final int pageSize = 100;

    private MongoCollection<Document> twitter;

    public Mongo() {
        super();
    }

    /**
     * [A EVITER]
     * Connexion locale à une base de donnée, sans identification.
     *
     * @param host          L'adresse du serveur MongoDB
     * @param port          Le port du serveur MongoDB
     * @param nomBase       Le nom de la base avec laquelle on travaille
     * @param nomCollection La collection sur laquelle on travaille
     */
    void ConnexionMongo(String host, int port, String nomBase, String nomCollection) {

        if (host.isEmpty()) host = "localhost";

        if (port == 0) port = 27017;

        MongoClient mongoClient = new MongoClient(host, port);
        MongoDatabase maBase = mongoClient.getDatabase(nomBase);

        this.twitter = maBase.getCollection(nomCollection);
    }

    /**
     * Connexion locale à une base de donnée, AVEC identification.
     *
     * @param host          L'adresse du serveur MongoDB
     * @param port          Le port du serveur MongoDB
     * @param nomBase       Le nom de la base avec laquelle on travaille
     * @param nomCollection La collection sur laquelle on travaille
     * @param user          nom d'utilisateur
     * @param pass          mot de passe
     */
    public void ConnexionMongo(String host, int port, String nomBase, String nomCollection, String user, String pass) {

        if (host.isEmpty()) host = "localhost";
        if (port == 0) port = 27017;
    /*
        List<ServerAddress> serverAddresses = new ArrayList<ServerAddress>();
        ServerAddress address = new ServerAddress(host, port);
        serverAddresses.add(address);
		
		List<MongoCredential> credentials = new ArrayList<MongoCredential>();
	  //  MongoCredential credential = MongoCredential.createPlainCredential(user, "$external", password);
	    MongoCredential credential = MongoCredential.createMongoCRCredential(user, nomBase, password);
	    credentials.add(credential);
	    MongoClient mongoClient = new MongoClient(serverAddresses, credentials);
	*/
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://" + user + ":" + pass + "@" + host + ":" + port + "/" + nomBase));

        MongoDatabase maBase = mongoClient.getDatabase(nomBase);

        this.twitter = maBase.getCollection(nomCollection);
        System.out.println("connexion distante réussie");
    }

    /**
     * Insertion d'une ArrayList de Tweets dans la base Mongo
     *
     * @param tweetList La liste de Tweets à insérer
     */
    public void InsertMongo(ArrayList<Tweet> tweetList) {
        twitter.drop();
        Iterator<Tweet> iter = tweetList.iterator();
        while (iter.hasNext()) {
            Tweet t = iter.next();

            Document doc = new Document();
            doc.append("user", t.getUser());
            doc.append("TweetId", t.getTweetId());
            doc.append("username", t.getUsername());
            doc.append("date", t.getTimeStamp().getTime());
            doc.append("content", t.getContent());
            doc.append("nbResponses", t.getNbResponses());
            doc.append("nbRetweets", t.getNbRetweets());
            doc.append("nbLikes", t.getNbLikes());
            //System.out.println(doc);
            twitter.insertOne(doc);
        }
        System.out.println("Insertion ok !");

    }

    /**
     * @return le nombre d'éléments dans la collection (nombre de Tweets ici)
     */
    public long GetNbTweets() {
        long nbTweet;
        nbTweet = twitter.count();
        return nbTweet;
    }

    /**
     * [NE PAS UTILISER] Retourne TOUS les Tweets de la base de donnée
     *
     * @return tous les Tweets de la collection dans une ArrayList de type Tweet
     */
    public ArrayList<Tweet> GetAllTweets() {
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

    /**
     * renvoie toutes les dates en timestamp (pour l'instant ?)
     *
     * @return une liste de Documents, avec pour champ "date", en timestamp
     */
    public ArrayList<Document> GetAllDates() {
        ArrayList<Document> tweetsDates;

        Bson projection = Projections.fields(Projections.include("date"), Projections.excludeId());
        tweetsDates = twitter.find().projection(projection).into(new ArrayList<Document>());
        return tweetsDates;
    }

    /**
     * Fait une recherche dans la base pour un seul mot clé
     *
     * @param keyWord Le mot clé à rechercher
     * @return Une ArrayList de Tweets contenant ce mot clé.
     */
    public ArrayList<Tweet> GetTweetsKeyWord(String keyWord) {
        ArrayList<Tweet> tweetsV2 = new ArrayList<Tweet>();
        ArrayList<Document> tweetsV1 = new ArrayList<Document>();

        //	tweetsV1 = twitter.find({"content":keyWord}).into(new ArrayList<Document>());

        //	BasicDBObject whereQuery = new BasicDBObject();
        //	whereQuery.put("content", ".*"+keyWord+".*");

        BasicDBObject regexQuery = new BasicDBObject();
        regexQuery.put("content",
                new BasicDBObject("$regex", ".*" + keyWord + ".*")
                        .append("$options", "i"));

        MongoCursor<Document> cursor = twitter.find(regexQuery).iterator();
        while (cursor.hasNext()) {
            tweetsV1.add(cursor.next());
        }

        Iterator<Document> iter = tweetsV1.iterator();
        while (iter.hasNext()) {
            Document doc = iter.next();
            //System.out.println(doc);
            Tweet t = new Tweet(doc.get("user").toString(), doc.get("TweetId").toString(), doc.get("username").toString(), new Timestamp(Long.parseLong(doc.get("date").toString())), doc.get("content").toString(), Integer.parseInt(doc.get("nbResponses").toString()), Integer.parseInt(doc.get("nbRetweets").toString()), Integer.parseInt(doc.get("nbLikes").toString()));

            tweetsV2.add(t);
        }
        return tweetsV2;
    }


    /**
     *
     * @param keyWords an array of keywords (max = pageSize)&
     * @return an ArrayList of Tweets containing all the keywords
     */
    public ArrayList<Tweet> GetTweetsKeyWordsArray(String[] keyWords) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();
        String str_keyWords = "";
        for (String keyword : keyWords) {
            str_keyWords = str_keyWords.concat("(?=.*" + keyword + ")");
        }
        str_keyWords = str_keyWords.concat(".+");

        BasicDBObject regexQuery = new BasicDBObject();
        regexQuery.put("content",
                new BasicDBObject("$regex", str_keyWords)
                        .append("$options", "i"));

        for (Document doc : twitter.find(regexQuery)) {
            Tweet t = DocToTweet(doc);
            tweets.add(t);
            if(tweets.size()==pageSize) return tweets;

        }
        return tweets;
    }

    private Tweet DocToTweet(Document doc) {
        return new Tweet(
                doc.get("user").toString(),
                doc.get("TweetId").toString(),
                doc.get("username").toString(),
                new Timestamp(Long.parseLong(doc.get("date").toString())),
                doc.get("content").toString(),
                Integer.parseInt(doc.get("nbResponses").toString()),
                Integer.parseInt(doc.get("nbRetweets").toString()),
                Integer.parseInt(doc.get("nbLikes").toString()));
    }


    /**
     * Retourne un block de Tweet, de longueur limitée (100 normalement, voir Mongo.pageSize )
     *
     * @param page - le numéro de la page (ça commence à la page 1)
     * @return Une ArrayList de Tweets
     */
    public ArrayList<Tweet> GetTweetsBlocks(int page) {
        ArrayList<Tweet> tweetsV2 = new ArrayList<Tweet>();
        ArrayList<Document> tweetsV1;


        //pagination limitée en paramètre
        tweetsV1 = twitter.find().skip((page - 1) * pageSize).limit(pageSize).into(new ArrayList<Document>());

        Iterator<Document> iter = tweetsV1.iterator();
        while (iter.hasNext()) {
            Document doc = iter.next();
            Tweet t = new Tweet(doc.get("user").toString(), doc.get("TweetId").toString(), doc.get("username").toString(), new Timestamp(Long.parseLong(doc.get("date").toString())), doc.get("content").toString(), Integer.parseInt(doc.get("nbResponses").toString()), Integer.parseInt(doc.get("nbRetweets").toString()), Integer.parseInt(doc.get("nbLikes").toString()));

            tweetsV2.add(t);
        }
        return tweetsV2;
    }

    /**
     * Vide toute la base de donnée. A éviter :p
     */
    public void DropMongo() {
        twitter.drop();
    }


}