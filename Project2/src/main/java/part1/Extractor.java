package part1;
import java.util.*;
import org.json.*;
import part1.pojo.*;
/**
 * Created by ruiboli on 3/18/15.
 */
public class Extractor {
    public List<String> getEntity(JSONObject jsonObject){
        return null;
    }

    public Map<String,List<String>> getAttribute(JSONObject jsonObject){
        return null;
    }

/**
 * ********************************************
 * The following code are created by Ruibo    *
 * ********************************************
 */


    private List<String> getHelper(JSONObject jsonObject, String path){
        List <String> list = new ArrayList<String>();
        if(jsonObject.has(path)) {
            JSONObject Object  = jsonObject.getJSONObject(path);
            if(Object.has("values")) {
                JSONArray values = Object.getJSONArray("values");
                for(int i=0;i<values.length();i++) {
                    if(values.getJSONObject(i).has("text")) {
                        String ele = values.getJSONObject(i).getString("text");
                        list.add(ele);
                    }
                }
            }
        }
        if(list.size()==0) list.add("");
        return list;
    }



    public Map<String,List<Object>> getPerson(JSONObject jsonObject){
        Map<String,List<Object>> result = new HashMap<String,List<Object>>();

        getHelperText(result, jsonObject, "/type/object/name", "Name");
        getHelperText(result, jsonObject, "/people/person/date_of_birth", "Birthday");
        getHelperText(result, jsonObject, "/people/person/place_of_birth", "PlaceOfBirth");
        getHelperDeep(result, jsonObject, "/people/person/sibling_s", "/people/sibling_relationship/sibling", "Siblings");
        getHelperDeep(result, jsonObject, "/people/person/spouse_s", "/people/marriage/spouse", "Spouses");
        getHelperValue(result, jsonObject, "/common/topic/description", "Description");
        DeathPojo deathPojo = new DeathPojo();
        List <String> deathPlaceList = getHelper(jsonObject, "/people/deceased_person/place_of_death");
        String deathPlace="";
        for(String place: deathPlaceList){
            if(deathPlace.length()>0) deathPlace+=", ";
            deathPlace+=place;
        }
        List <String> deathDateList = getHelper(jsonObject, "/people/deceased_person/date_of_death");
        String deathDate="";
        for(String dt: deathDateList){
            if(deathDate.length()>0) deathDate+=", ";
            deathDate+=dt;
        }
        List <String> deathCauseList = getHelper(jsonObject, "/people/deceased_person/cause_of_death");
        String deathCause="";
        for(String c: deathCauseList){
            if(deathCause.length()>0) deathCause+=", ";
            deathCause+=c;
        }
        if(deathPlace.length()+deathDate.length()+deathCause.length()>0){
            deathPojo.setCause(deathCause);
            deathPojo.setDate(deathDate);
            deathPojo.setPlace(deathPlace);
            List<Object> lst = new ArrayList<Object>();
            lst.add(deathPojo);
            result.put("Death", lst);
        }
        return result;
    }




    public Map<String,List<Object>> getBusinessPerson(JSONObject jsonObject){
        Map<String,List<Object>> result = new HashMap<String,List<Object>>();

        if(jsonObject.has("/business/board_member/leader_of")) {
            JSONObject leadershipObject  = jsonObject.getJSONObject("/business/board_member/leader_of");
            if(leadershipObject.has("values")) {
                JSONArray leadershipValues = leadershipObject.getJSONArray("values");
                List <Object> leadershipList = new LinkedList <Object> ();
                for(int i=0;i<leadershipValues.length();i++) {
                    if(leadershipValues.getJSONObject(i).has("property")) {
                        JSONObject leadership = leadershipValues.getJSONObject(i).getJSONObject("property");
                        BusinessmanPojo element = new BusinessmanPojo();

                        element.From = getHelper(leadership,"/organization/leadership/from").get(0);
                        element.To = getHelper(leadership,"/organization/leadership/to").get(0);
                        element.Organization = getHelper(leadership,"/organization/leadership/organization").get(0);
                        element.Role = getHelper(leadership,"/organization/leadership/role").get(0);
                        element.Title = getHelper(leadership,"/organization/leadership/title").get(0);

                        leadershipList.add(element);
                    }
                }
                result.put("Leadership",leadershipList);
            }
        }

        if(jsonObject.has("/business/board_member/organization_board_memberships")) {
            JSONObject boardMemberObject  = jsonObject.getJSONObject("/business/board_member/organization_board_memberships");
            if(boardMemberObject.has("values")) {
                JSONArray boardMemberValues = boardMemberObject.getJSONArray("values");
                List <Object> boardMemberList = new LinkedList <Object> ();
                for(int i=0;i<boardMemberValues.length();i++) {
                    if(boardMemberValues.getJSONObject(i).has("property")) {
                        JSONObject boardMember = boardMemberValues.getJSONObject(i).getJSONObject("property");
                        BusinessmanPojo element = new BusinessmanPojo();

                        element.From = getHelper(boardMember,"/organization/organization_board_membership/from").get(0);
                        element.To = getHelper(boardMember,"/organization/organization_board_membership/to").get(0);
                        element.Organization = getHelper(boardMember,"/organization/organization_board_membership/organization").get(0);
                        element.Role = getHelper(boardMember,"/organization/organization_board_membership/role").get(0);
                        element.Title = getHelper(boardMember,"/organization/organization_board_membership/title").get(0);

                        boardMemberList.add(element);
                    }
                }
                result.put("BoardMember",boardMemberList);
            }
        }
        getHelperText(result, jsonObject,"/organization/organization_founder/organizations_founded" ,"Founded");
        return result;
    }

    public Map<String,List<String>> getActor(JSONObject jsonObject){
        Map result = new HashMap<String,List<String>>();
        if(jsonObject.has("/film/actor/film")) {
            JSONObject filmCharObject  = jsonObject.getJSONObject("/film/actor/film");
            if(filmCharObject.has("values")) {
                List <String> filmList = new LinkedList<String>();
                List <String> charList = new LinkedList<String>();
                JSONArray filmCharValues = filmCharObject.getJSONArray("values");
                for(int i=0;i<filmCharValues.length();i++) {
                    if(filmCharValues.getJSONObject(i).has("property")) {
                        JSONObject property = filmCharValues.getJSONObject(i).getJSONObject("property");
                        //charList = getHelper(property,"/film/performance/character");
                        //filmList = getHelper(property,"/film/performance/film");

                        if(property.has("/film/performance/character")) {
                            JSONObject characters = property.getJSONObject("/film/performance/character");
                            if(characters.has("values")) {
                                JSONArray charactersValues = characters.getJSONArray("values");
                                for(int j=0;j<charactersValues.length();j++) {
                                    if(charactersValues.getJSONObject(j).has("text")) {
                                        String character = charactersValues.getJSONObject(j).getString("text");
                                        charList.add(character);
                                    }
                                }
                            }
                        }
                        if(property.has("/film/performance/film")) {
                            JSONObject films = property.getJSONObject("/film/performance/film");
                            if(films.has("values")) {
                                JSONArray filmsValues = films.getJSONArray("values");
                                for(int j=0;j<filmsValues.length();j++) {
                                    if(filmsValues.getJSONObject(j).has("text")) {
                                        String film = filmsValues.getJSONObject(j).getString("text");
                                        filmList.add(film);
                                    }
                                }
                            }
                        }
                    }
                }
                result.put("Film",filmList);
                result.put("Character",charList);
            }
        }
        return result;
    }


/**
 * ********************************************
 *  The following codes are created by: Siyao *
 * ********************************************
 */

    public Map<String,List<Object>> getAuthor(JSONObject jsonObject){
        Map<String, List<Object>>  map = new HashMap<String, List<Object>>();
        //Book(Title)
        getHelperText(map, jsonObject, "/book/author/works_written", "Book");
        //Book About(the author)
        getHelperText(map, jsonObject, "/book/book_subject/works", "Book About");
        //Influenced
        getHelperText(map, jsonObject, "/influence/influence_node/influenced", "Influenced");
        //Influenced By
        getHelperText(map, jsonObject, "/influence/influence_node/influenced_by", "Influenced By");
        return map;
    }

    private void getHelperText(Map<String, List<Object>>  map, JSONObject jsonObject, String path, String title){
        if(jsonObject.isNull(path) || jsonObject.getJSONObject(path).isNull("values")) return;
        JSONObject pathObj = jsonObject.getJSONObject(path);
        if(pathObj.isNull("values")) return;
        JSONArray jsonArray = pathObj.getJSONArray("values");
        if(jsonArray.length()==0) return;
        List<String> lst = new ArrayList<String>();
        for(int i=0; i<jsonArray.length(); ++i){
            JSONObject subObj = jsonArray.getJSONObject(i);
            if(subObj.isNull("text")) continue;
            String content = subObj.getString("text");
            lst.add(content);
        }
        if(lst.size()==0) return;
        if(!map.containsKey(title)){
            map.put(title, new ArrayList<Object>());
        }
        List<Object> oldList = map.get(title);
        oldList.addAll(lst);
    }

    private void getHelperValue(Map<String, List<Object>>  map, JSONObject jsonObject, String path, String title){
        if(jsonObject.isNull(path) || jsonObject.getJSONObject(path).isNull("values")) return;
        JSONObject pathObj = jsonObject.getJSONObject(path);
        if(pathObj.isNull("values")) return;
        JSONArray jsonArray = pathObj.getJSONArray("values");
        if(jsonArray.length()==0) return;
        List<String> lst = new ArrayList<String>();
        for(int i=0; i<jsonArray.length(); ++i){
            JSONObject subObj = jsonArray.getJSONObject(i);
            if(subObj.isNull("value")) continue;
            String content = subObj.getString("value");
            lst.add(content);
        }
        if(lst.size()==0) return;
        if(!map.containsKey(title)){
            map.put(title, new ArrayList<Object>());
        }
        List<Object> oldList = map.get(title);
        oldList.addAll(lst);
    }


    public Map<String,List<Object>> getLeague(JSONObject jsonObject){
        Map<String, List<Object>>  map = new HashMap<String, List<Object>>();
        //Name
        getHelperText(map, jsonObject, "/type/object/name", "Name");
        //Championship
        getHelperText(map, jsonObject, "/sports/sports_league/championship", "Championship");
        //Sport
        getHelperText(map, jsonObject, "/sports/sports_league/sport", "Sport");
        //Slogan
        getHelperValue(map, jsonObject, "/organization/organization/slogan", "Slogan");
        //OfficialWebsite
        getHelperValue(map, jsonObject, "/common/topic/official_website", "OfficialWebsite");
        //Description
        getHelperValue(map, jsonObject, "/common/topic/description", "Description");
        //Teams
        getHelperDeep(map, jsonObject, "/sports/sports_league/teams", "/sports/sports_league_participation/team", "Teams");
        return map;
    }

    private void getHelperDeep(Map<String, List<Object>> map, JSONObject jsonObject, String path, String pathDeep, String title){
        if(jsonObject.isNull(path)) return;
        JSONObject subObj = jsonObject.getJSONObject(path);
        if(subObj.isNull("values")) return;
        JSONArray teamObjLst = subObj.getJSONArray("values");
        for(int i=0; i<teamObjLst.length(); ++i){
            JSONObject teamObj = teamObjLst.getJSONObject(i);
            if(teamObj.isNull("property")) continue;
            teamObj = teamObj.getJSONObject("property");
            getHelperText(map, teamObj, pathDeep, title);
        }
    }

    public Map<String,List<Object>> getSportsTeam(JSONObject jsonObject){
        Map<String, List<Object>>  map = new HashMap<String, List<Object>>();
        //Name
        getHelperText(map, jsonObject, "/type/object/name", "Name");
        //Sport
        getHelperText(map, jsonObject, "/sports/sports_team/sport", "Sport");
        //Arena
        getHelperText(map, jsonObject, "/sports/sports_team/arena_stadium", "Arena");
        //Championships
        getHelperText(map, jsonObject, "/sports/sports_team/championships", "Championships");
        //Founded
        getHelperValue(map, jsonObject, "/sports/sports_team/founded", "Founded");
        //Leagues
        getHelperDeep(map, jsonObject, "/sports/sports_team/league", "/sports/sports_team/league", "Leagues");
        //Location
        getHelperText(map, jsonObject, "/sports/sports_team/location", "Location");
        //Coaches
        getCoaches(map, jsonObject);
        //PlayersRoster
        getPlayersRoster(map, jsonObject);
        return map;
    }

    private void getPlayersRoster(Map<String, List<Object>> map, JSONObject jsonObject){
        String path = "/sports/sports_team/roster";
        if(jsonObject.isNull(path)) return;
        JSONObject subObj = jsonObject.getJSONObject(path);
        if(subObj.isNull("values")) return;
        JSONArray prObjLst = subObj.getJSONArray("values");
        for(int i=0; i<prObjLst.length(); ++i){
            JSONObject prObj = prObjLst.getJSONObject(i);
            if(prObj.isNull("property")) continue;
            prObj = prObj.getJSONObject("property");
            getPlayersRosterHelper(map, prObj);
        }
    }

    private void getPlayersRosterHelper(Map<String, List<Object>> map, JSONObject jsonObject){
        PlayersRosterPojo playersRosterPojo = new PlayersRosterPojo();
        String playerPath = "/sports/sports_team_roster/player";
        String fromPath = "/sports/sports_team_roster/from";
        String toPath = "/sports/sports_team_roster/to";
        String posPath = "/sports/sports_team_roster/position";
        String numPath = "/sports/sports_team_roster/number";
        boolean hasInfo = false;
        if(!jsonObject.isNull(playerPath)) {
            JSONObject coachObj = jsonObject.getJSONObject(playerPath);
            String coach = getText(coachObj);
            if(coach.length()>0) hasInfo = true;
            playersRosterPojo.setName(coach);
        }
        if(!jsonObject.isNull(fromPath)){
            JSONObject fromObj = jsonObject.getJSONObject(fromPath);
            String from = getText(fromObj);
            if(from.length()>0) hasInfo = true;
            playersRosterPojo.setFrom(from);
        }
        if(!jsonObject.isNull(toPath)){
            JSONObject toObj = jsonObject.getJSONObject(toPath);
            String to = getText(toObj);
            if(to.length()>0) hasInfo = true;
            playersRosterPojo.setTo(to);
        }
        if(!jsonObject.isNull(posPath)){
            JSONObject posObj = jsonObject.getJSONObject(posPath);
            String pos = getText(posObj);
            if(pos.length()>0) hasInfo = true;
            playersRosterPojo.setPosition(pos);
        }
        if(!jsonObject.isNull(numPath)){
            JSONObject numObj = jsonObject.getJSONObject(numPath);
            String num = getText(numObj);
            if(num.length()>0) hasInfo = true;
            playersRosterPojo.setNumber(num);
        }
        if(!hasInfo) return;
        if(!map.containsKey("PlayersRoster")){
            map.put("PlayersRoster", new ArrayList<Object>());
        }
        List<Object> lst = map.get("PlayersRoster");
        lst.add(playersRosterPojo);
    }

    private void getCoaches(Map<String, List<Object>> map, JSONObject jsonObject){
        String path = "/sports/sports_team/coaches";
        if(jsonObject.isNull(path)) return;
        JSONObject subObj = jsonObject.getJSONObject(path);
        if(subObj.isNull("values")) return;
        JSONArray coachesObjLst = subObj.getJSONArray("values");
        for(int i=0; i<coachesObjLst.length(); ++i){
            JSONObject coachObj = coachesObjLst.getJSONObject(i);
            if(coachObj.isNull("property")) continue;
            coachObj = coachObj.getJSONObject("property");
            getCoachesHelper(map, coachObj);
        }

    }

    private void getCoachesHelper(Map<String, List<Object>> map, JSONObject jsonObject){
        CoachesPojo coachesPojo = new CoachesPojo();
        String coachPath = "/sports/sports_team_coach_tenure/coach";
        String fromPath = "/sports/sports_team_coach_tenure/from";
        String toPath = "/sports/sports_team_coach_tenure/to";
        String posPath = "/sports/sports_team_coach_tenure/position";
        boolean hasInfo = false;
        if(!jsonObject.isNull(coachPath)) {
            JSONObject coachObj = jsonObject.getJSONObject(coachPath);
            String coach = getText(coachObj);
            if(coach.length()>0) hasInfo = true;
            coachesPojo.setName(coach);
        }
        if(!jsonObject.isNull(fromPath)){
            JSONObject fromObj = jsonObject.getJSONObject(fromPath);
            String from = getText(fromObj);
            if(from.length()>0) hasInfo = true;
            coachesPojo.setFrom(from);
        }
        if(!jsonObject.isNull(toPath)){
            JSONObject toObj = jsonObject.getJSONObject(toPath);
            String to = getText(toObj);
            if(to.length()>0) hasInfo = true;
            coachesPojo.setTo(to);
        }
        if(!jsonObject.isNull(posPath)){
            JSONObject posObj = jsonObject.getJSONObject(posPath);
            String pos = getText(posObj);
            if(pos.length()>0) hasInfo = true;
            coachesPojo.setPosition(pos);
        }
        if(!hasInfo) return;
        if(!map.containsKey("Coaches")){
            map.put("Coaches", new ArrayList<Object>());
        }
        List<Object> lst = map.get("Coaches");
        lst.add(coachesPojo);
    }

    /**
     * Get text from the following json structure:
     * {values:[{text:t1}, {text:t2}]} and return t1, t2
     * @param jsonObject
     * @return
     */
    private String getText(JSONObject jsonObject){
        if(jsonObject.isNull("values")) return "";
        JSONArray jsonArray = jsonObject.getJSONArray("values");
        if(jsonArray.length()<=0) return "";
        String rst = "";
        for(int i=0; i<jsonArray.length();++i){
            jsonObject = jsonArray.getJSONObject(i);
            if(jsonObject.isNull("text")) continue;
            if(rst.length()>0) rst+=", ";
            rst+=jsonObject.getString("text");
        }
        return rst;
    }


}
