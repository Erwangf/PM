package lib;

import modules.data.Tweet;
import modules.keywords.Keywords;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Erwan on 24/11/2016.
 */
public class OpinionMining {

    public static Map<String,Integer> scoreIndex = new LinkedHashMap<>();


    public static void main(String[] args){
        System.out.println("TEST OPINION MINING");
        //
        // 1) Récupérer les  Tweets et les notes depuis le fichier CSV
        // 2) créer une Map <Mot,{Nombre,Somme} appelée "wmap"
        // 3) ForEach Tweet.content, ajouter dans wmap le mot s'il n'y existe pas, sinon modifier le nombre et la somme
        //    avec la note obtenue.
        // 4) Créer une Map<Mot,score>scoreIndex, avec score = Somme/Nombre
        // 5) A partir de cet index, on crée une fonction qui le parcours, et qui pour chaque mot d'une chaine de caractères,
        // trouve son score, l'additionne à une somme, puis en fait la moyenne, et la retourne. Résultat = Moyenne(Score de chaque mot)



    }

    public static Map<String,Float> buildScoreIndex(ArrayList<Tweet> tweets){
        class NbSumCouple{
            public int nb;
            public float somme;

            public NbSumCouple(int nb, float somme) {
                this.nb = nb;
                this.somme = somme;
            }
        }
        Map<String,NbSumCouple> wmap = new LinkedHashMap<>();
        tweets.forEach((t)->{
            ArrayList<String> words = Keywords.FilterStopWords(new ArrayList<>(Arrays.asList(t.getContent().split(" "))),Keywords.GetStopWords());
            words.forEach((w)->{
                if(wmap.get(w)==null){
                    wmap.put(w,new NbSumCouple(1,t.getNote()));
                }
                else{
                    wmap.get(w).somme+=t.getNote();
                    wmap.get(w).nb++;
                }
            });
        });

        Map<String,Float> result = new LinkedHashMap<>();
        wmap.forEach((key,value)->{
            result.put(key,value.somme/value.nb);
        });

        return result;

    }



}
