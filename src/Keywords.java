import java.util.HashSet;


public class Keywords {
	
	private HashSet<String> Keywords=new HashSet<String>();
	private String Query;
	
	//��ʼ��
	public Keywords(String Query){
		this.Query=Query;
		getKeyword();
	}
		
	//��ѯ����йؼ��ʼ��ԡ������͡� ������,�����г��йؼ��������Ϊnull
	private  void getKeyword(){				
		String[] Keyword=Query.trim().split(" *,+ *");
		for(int i=0;i<Keyword.length;i++){
			Keywords.add(Keyword[i]);//ֱ�������HashSet Keywords��
		}
	}
	
	//��ȡ�ؼ��ּ���
	public HashSet<String> get(){
		return Keywords;
	}
	
	//�ؼ�������
	public int size(){
		return Keywords.size();
	}
}
