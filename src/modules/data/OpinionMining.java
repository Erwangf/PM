package modules.data;

import modules.keywords.Keywords;
import org.apache.commons.collections4.map.MultiKeyMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;



/**
 * Created by Erwan on 24/11/2016.
 */
public class OpinionMining {

    public static Map<String, Float> scoreIndex = new LinkedHashMap<>();


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
         class NbSumCouple {
            public int nb;
            public float somme;

            public NbSumCouple(int nb, float somme) {
                this.nb = nb;
                this.somme = somme;
            }
        }
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

    public static float getScore_v1(String tweetContent) {
        float sum = 0;
        int n = 0;
        String[] words = tweetContent.split(" ");
        for (String word : words) {
            if (scoreIndex.containsKey(word)) {
                float score = scoreIndex.get(word);
                if(score>0.0f || score<-0.0f){
                    sum += scoreIndex.get(word);
                    n++;
                }
            }
        }
        if (n == 0) n = 1;
        return sum / n;

    }

}
