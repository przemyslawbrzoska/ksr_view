package Extractors;

import java.util.Comparator;

public class KeyWordComp implements Comparator<KeyWord> {


    public int compare(KeyWord s1, KeyWord s2) {
        if (s1.getOccurencies() < s2.getOccurencies())
            return 1;
        else if (s1.getOccurencies() > s2.getOccurencies())
            return -1;
        return 0;
    }
}