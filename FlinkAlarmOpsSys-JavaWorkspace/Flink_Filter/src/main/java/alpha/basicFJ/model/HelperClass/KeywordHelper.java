package alpha.basicFJ.model.HelperClass;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeywordHelper {
    public String match(String input, List<String> keywords){
        for(int i=0; i < keywords.size(); i++){
            Pattern pattern = Pattern.compile(keywords.get(i));//过滤关键字
            Matcher matcher = pattern.matcher(input);
            if(matcher.find())
                return matcher.group();
        }
        return "";
    }
}
