import java.util.HashSet;


public class Keywords {
	
	private HashSet<String> Keywords=new HashSet<String>();
	private String Query;
	
	//初始化
	public Keywords(String Query){
		this.Query=Query;
		getKeyword();
	}
		
	//查询语句中关键词间以“，”和“ ”区分,数组中除有关键字其余皆为null
	private  void getKeyword(){				
		String[] Keyword=Query.trim().split(" *,+ *");
		for(int i=0;i<Keyword.length;i++){
			Keywords.add(Keyword[i]);//直接添加在HashSet Keywords中
		}
	}
	
	//获取关键字集合
	public HashSet<String> get(){
		return Keywords;
	}
	
	//关键字数量
	public int size(){
		return Keywords.size();
	}
}
