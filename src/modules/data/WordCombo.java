package modules.data;

import java.util.ArrayList;

/**
 * Created by Erwan on 28/11/2016.
 */
public class WordCombo {

    public ArrayList<String> words;
    public String key;
    public int count;
    public float sum;

    public WordCombo(ArrayList<String> words) {
        this.words = words;
        this.count = 0;
        this.sum = 0;
        this.key = "";
        boolean first = true;
        this.words.sort(String::compareTo);

        for (String word : words) {
            if (!first) this.key += " ";
            this.key += word;
            first = false;
        }
    }

    public void update(float v) {
        sum += v;
        count++;
    }


}
