package FPGrowth;

import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;

public class movie {
	class inner{
		public String $oid;
		public inner(String str){
			$oid = str;
		}
	}
	private inner _id;
	private String title;
	private String[] actor;
	private String director;
	private String[] genre;
	
	public movie(String _id,String title,String[] actor,String director,String[] genre){
		this._id = new inner(_id);
		this.title = title;
		this.actor = actor;
		this.director = director;
		this.genre = genre;
	}
	public inner get_id(){
		return _id;
	}
	public String getTitle(){
		return title;
	}
	public String[] getActor(){
		return actor;
	}
	public String getDirector(){
		return director;
	}
	public String[] getGenre(){
		return genre;
	}
	public String[] getUniqueActor(){
		String[] actors = getActor();
		Set<String>uniqueActors = new HashSet<String>();
		for(String actor:actors){
			if(actor.length() > 0 && !actor.equals("[更多]")){
				uniqueActors.add(actor);
			}
		}
		return (String[]) uniqueActors.toArray(new String[0]);
	}
	public static void main(String[] args) throws Exception{
		String[] actor = {"彭于晏","倪妮","欧豪","[更多]"};
		String director = "郭子健";
		String[] genre = {" 动作"," 剧情"," 奇幻"," 中国"};
		
		movie mv = new movie("58379420e138237f68d2c465","悟空传",actor,director,genre);
		Gson json = new Gson();
		System.out.println(json.toJson(mv));
		
	}

}

