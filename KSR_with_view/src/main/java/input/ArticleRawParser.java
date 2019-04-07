package input;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArticleRawParser {
    ArticleRaw source;

    public Article parseArticle(ArticleRaw source) throws Exception {
        SnowballStemmer snowballStemmer = new englishStemmer();
        Article parsed = new Article();
        this.source = source;
        parsed.setArticleTagTopics(extractUniversalArray("TOPICS"));
        parsed.setArticleTagPlaces(extractUniversalArray("PLACES"));
        parsed.setArticleBody(stemAndLowercase(extractUniversalArray("BODY")[0], snowballStemmer));
        return parsed;
    }

    public List<String> stemAndLowercase(String body, SnowballStemmer snowballStemmer){
        String[] words = null;
       try {
            words = body.split(" ");
       }catch(NullPointerException e){
           words = new String[1];
           words[0] = " ";
       }
       List<String> toWorkWith =new ArrayList<>(Arrays.asList(words));
       List<String> toReturn = new ArrayList<>();
       for(String word : toWorkWith){
           if(word.contains(".") || word.contains(",")){
               toReturn.addAll(Arrays.asList(word.split("\\.|,")));
           }
           else {
               toReturn.add(word);
           }
       }
       toWorkWith.clear();
       toWorkWith.addAll(toReturn);
       toReturn.clear();
        for(String word : toWorkWith){
            word = word.replaceAll("[^A-Za-z ]", "");
           if(!word.equals("")) {
               word = word.toLowerCase();
               snowballStemmer.setCurrent(word);
               snowballStemmer.stem();
               toReturn.add(snowballStemmer.getCurrent());
           }
        }
        return toReturn;
    }
    public String[] extractUniversalArray(String tag){
        String regex = "<" + tag + ">.*</" + tag + ">";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source.getRawText());
        String found;
        String[] returnAr = null;
        List<String> list = new ArrayList<String>();
        if(matcher.find()){
            found=matcher.group();
            returnAr=found.split("<D>");
            for (int i =0; i < returnAr.length; i++){
                returnAr[i]=returnAr[i].replaceAll("</D>", "");
                returnAr[i]=returnAr[i].replaceAll("<" + tag + ">", "");
                returnAr[i]=returnAr[i].replaceAll("</" + tag+ ">", "");
                if(!returnAr[i].equals(""))
                    list.add(returnAr[i]);
            }
            return list.toArray(new String[list.size()]);
        }
        else {
            returnAr = new String[1];
            returnAr[0]=null;
        };
        return returnAr;
    }







}
