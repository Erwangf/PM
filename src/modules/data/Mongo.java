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

    public final String defaultHost = "ds147537.mlab.com";
    public final int defaultPort = 47537;
    public final String defaultBase = "twitter_rumors";
    public final String defaultCollection = "Twitter";
    public final String defaultUser = "root";
    public final String defaultPassword = "TwitterMongo2016";


    private MongoCollection<Document> twitter;

    public Mongo() {
        super();
    }

    /**
     * Connexion à la base de donnée par défaut.
     */
    public void ConnexionMongoDefault(){
        ConnexionMongo(defaultHost,defaultPort,defaultBase,defaultCollection,defaultUser,defaultPassword);
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
     * Connexion à une base de donnée, AVEC identification.
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

            twitter.insertOne(doc);
        }
        System.out.println("Insertion ok !");

    }

    /**
     * @return le nombre d'éléments dans la collection (nombre de Tweets ici)
     */
    public long GetNbTweets() {
        return twitter.count();
    }

    /**
     * [NE PAS UTILISER] Retourne TOUS les Tweets de la base de donnée
     * @return tous les Tweets de la collection dans une ArrayList de type Tweet
     */
    public ArrayList<Tweet> GetAllTweets() {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();
        ArrayList<Document> documents;

        documents = twitter.find().into(new ArrayList<Document>());
        Iterator<Document> iter = documents.iterator();
        while (iter.hasNext()) {
            Document doc = iter.next();
            Tweet t = DocToTweet(doc);
            tweets.add(t);
        }




        return tweets;
    }

    /**
     * renvoie toutes les dates en timestamp
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
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();
        ArrayList<Document> tweetsV1 = new ArrayList<Document>();

        //	tweetsV1 = twitter.find({"content":keyWord}).into(new ArrayList<Document>());

        //	BasicDBObject whereQuery = new BasicDBObject();
        //	whereQuery.put("content", ".*"+keyWord+".*");

        BasicDBObject regexQuery = new BasicDBObject();
        regexQuery.put("content",
                new BasicDBObject("$regex", ".*" + keyWord + ".*")
                        .append("$options", "i"));

        MongoCursor<Document> cursor = twitter.find(regexQuery).iterator();
        for (Document doc : twitter.find(regexQuery)) {
            Tweet t = DocToTweet(doc);
            tweets.add(t);
        }
        return tweets;
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

    /**
     * Retourne un block de Tweet, de longueur limitée (100 normalement, voir Mongo.pageSize )
     *
     * @param page - le numéro de la page (ça commence à la page 1)
     * @return Une ArrayList de Tweets
     */
    public ArrayList<Tweet> GetTweetsBlocks(int page) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();
        ArrayList<Document> documents;


        //pagination limitée en paramètre
        documents = twitter.find().skip((page - 1) * pageSize).limit(pageSize).into(new ArrayList<Document>());

        Iterator<Document> iter = documents.iterator();
        while (iter.hasNext()) {
            Document doc = iter.next();
            Tweet t = DocToTweet(doc);
            tweets.add(t);
        }
        return tweets;
    }

    /**
     * Vide toute la base de donnée.
     */
    public void DropMongo() {
        twitter.drop();
    }



    /**
     * Document to Tweet converter
     * @param doc the document to convert
     * @return a tweet
     */
    private Tweet DocToTweet(Document doc) {
        Tweet t = new Tweet(
                doc.get("user").toString(),
                doc.get("TweetId").toString(),
                doc.get("username").toString(),
                new Timestamp(Long.parseLong(doc.get("date").toString())),
                doc.get("content").toString(),
                Integer.parseInt(doc.get("nbResponses").toString()),
                Integer.parseInt(doc.get("nbRetweets").toString()),
                Integer.parseInt(doc.get("nbLikes").toString()));
        if(doc.containsKey("note") && doc.get("note")!=null){
            t.setNote(Float.parseFloat(doc.get("note").toString()));
        }

        return t;
    }



    /**
     * Retourne la liste des tweets ayant une note non vide
     * @return une liste de tweets
     */
    public ArrayList<Tweet> GetTweetsAppge() {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();
        ArrayList<Document> documents;

        //uniquement les tweets avec une note non nulle
        BasicDBObject NNQuery = new BasicDBObject();
        NNQuery.put("note", new BasicDBObject("$exists", true));
        documents = twitter.find(NNQuery).into(new ArrayList<Document>());

        Iterator<Document> iter = documents.iterator();
        while (iter.hasNext()) {
            Document doc = iter.next();
            Tweet t = DocToTweet(doc);
            tweets.add(t);
        }
        return tweets;
    }



    /**
     * Met à jour le champ note pour une liste de tweets
     * @param tweetList ArrayList de tweets
     */
    public void UpdateMongo(ArrayList<Tweet> tweetList) {

        Iterator<Tweet> iter = tweetList.iterator();
        while (iter.hasNext()) {
            Tweet t = iter.next();
            if(t.getNote()!= null){
                BasicDBObject docUPD = new BasicDBObject();
                docUPD.append("$set", new BasicDBObject().append("note", t.getNote()));

                BasicDBObject searchQuery = new BasicDBObject().append("TweetId", t.getTweetId());

                twitter.updateOne(searchQuery, docUPD);
            }
        }
    }

    public void UpdateMongoItem(Tweet t){

            BasicDBObject docUPD = new BasicDBObject();
            docUPD.append("$set", new BasicDBObject().append("note", t.getNote()));

            BasicDBObject searchQuery = new BasicDBObject().append("TweetId", t.getTweetId());

            twitter.updateOne(searchQuery, docUPD);

    }





    /**
     * Juste pour la démo, j'l'ai fait quand même :p ça peut servir on sait jamais !
     * @param documents une liste de documents
     * @return une liste de tweets
     */
    private ArrayList<Tweet> DocsToTweetsList(ArrayList<Document>documents){
        ArrayList<Tweet> tweets = new ArrayList<>();

        Iterator<Document> iter = documents.iterator();
        while (iter.hasNext()) {
            Document doc = iter.next();
            Tweet t = DocToTweet(doc);
            tweets.add(t);
        }

        return tweets;
    }


}