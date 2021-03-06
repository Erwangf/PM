package modules.data;

import modules.keywords.Keywords;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Created by Erwan on 24/11/2016.
 */
public class OpinionMining {

    public static Map<String, Float> scoreIndex;


    public static void main(String[] args) {


        System.out.println("TEST OPINION MINING");


        // 1) Récupérer les  Tweets et les notes depuis le fichier CSV
        // 2) créer une Map <Mot,{Nombre,Somme} appelée "wmap"
        // 3) ForEach Tweet.content, ajouter dans wmap le mot s'il n'y existe pas, sinon modifier le nombre et la somme
        //    avec la note obtenue.
        // 4) Créer une Map<Mot,score>scoreIndex, avec score = Somme/Nombre
        // 5) A partir de cet index, on crée une fonction qui le parcours, et qui pour chaque mot d'une chaine de caractères,
        // trouve son score, l'additionne à une somme, puis en fait la moyenne, et la retourne. Résultat = Moyenne(Score de chaque mot)


    }

    public static Map<String, Float> buildScoreIndex_v1(ArrayList<Tweet> tweets) {

        Map<String, NbSumCouple> wmap = new LinkedHashMap<>();
        tweets.forEach((t) -> {
            if (t.getNote() != 0) {


                ArrayList<String> words = Keywords.FilterStopWords(new ArrayList<>(Arrays.asList(t.getContent().split(" "))), Keywords.GetStopWords());
                words.forEach((w) -> {
                    if (wmap.get(w) == null) {
                        wmap.put(w, new NbSumCouple(1, t.getNote()));
                    } else {
                        wmap.get(w).somme += t.getNote();
                        wmap.get(w).nb++;
                    }
                });
            }
        });

        Map<String, Float> result = new LinkedHashMap<>();
        wmap.forEach((key, value) -> {
            result.put(key, value.somme / value.nb);
        });
        scoreIndex = result;
        System.out.println(result);
        return result;

    }


    public static Map<String, Float> buildScoreIndex_v2(ArrayList<Tweet> tweets) {

        Map<String, WordCombo> wmap = new LinkedHashMap<>();
        tweets.forEach((t) -> {
            if (t.getNote()!= null && t.getNote()!=0) {
                ArrayList<String> words = Keywords.FilterStopWords(new ArrayList<>(Arrays.asList(t.getContent().split(" "))), Keywords.GetStopWords());
                for (int i = 0; i < words.size(); i++) {
                    for (int j = 0; j < i; j++) {

                        String w1 = words.get(i);
                        String w2 = words.get(j);
                        ArrayList<String> l = new ArrayList<String>();
                        l.add(w1);

                        if(!w2.equals(w1))l.add(w2);
                        WordCombo wc = new WordCombo(l);
                        String key = wc.key;
                        wc.sum = t.getNote();
                        wc.count = 1;
                        if (wmap.get(key) == null) {
                            wmap.put(key, wc);
                        } else {
                            wmap.get(key).update(t.getNote());
                        }
                    }
                }
            }
        });

        Map<String, Float> result = new LinkedHashMap<>();
        wmap.forEach((key, value) -> {
            result.put(key, value.sum / value.count);
        });
        scoreIndex = result;
        return result;

    }


    public static float getScore_v1(String tweetContent) {
        float sum = 0;
        int n = 0;
        String[] words = tweetContent.split(" ");
        for (String word : words) {
            if (scoreIndex.containsKey(word)) {
                float score = scoreIndex.get(word);
                if (score > 0.5f || score < -0.1f) {
                    sum += scoreIndex.get(word);
                    n++;
                }
            }
        }
        if (n == 0) n = 1;
        return sum / n;
    }


    public static float getScore_v2(String tweetContent) {
        float sum = 0;
        int n = 0;
        String[] words = tweetContent.split(" ");
        for (int i=0; i<words.length; i++) {
            for(int j=0; j<=i; j++) {
                String key;
                if(words[i].compareTo(words[j])<0){
                    key = words[i] + " "+ words[j];
                }
                else if(words[i].compareTo(words[j])>0){
                        key = words[j] + " "+ words[i];
                    }
                else key = words[i];
                if (scoreIndex.containsKey(key)) {
                    float score = scoreIndex.get(key);
                    if (score > 0.5f || score < -0.1f) {
                        sum += scoreIndex.get(key);
                        n++;
                    }
                }
            }
        }
        if (n == 0) n = 1;
        return sum / n;
    }

    public static int getPrevision_v2(String tweetContent){
        float rawPrevision = getScore_v2(tweetContent);
        int prevision = 0;
        if (rawPrevision > 0.5f) {
            prevision = 1;
        } else if (rawPrevision < -0.1f) {
            prevision = -1;
        }
        return prevision;
    }

}
