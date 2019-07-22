import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


public class Module {
	
	//����	
	int Top_k;
	int CNMax;
	int KeywordNum;

	int[][] ForeignPrimary;
	ArrayList<int[]> SchemaGraph;
	HashSet<String> keywords;
	HashMap<Integer, int[]> BaseTable;
	ArrayList<int[][]> CandidateTree;
	ArrayList<ArrayList<long[]>> CandidateNetwork;
	ArrayList<long[][]> CombinedCandidateNetwork;
	ArrayList<int[][]> MTJNTs;//Ԫ�����磨id��ʽ��
	ArrayList<String []> MTJNTSet;//Ԫ������(Ԫ����ʽ)
	String[] Time;
	
	//���캯��
	public Module(){
		System.out.println("initialization()");
		initialization();
		//System.out.println("Preheat");//ע�͵���ȫ��Ӱ��ʹ��
		//Preheat("keyword,study,relational,test,oracle");//��ֵ�Ը���
	}
	
	//��ʼ���׶�
	private void initialization(){
		//�����������õ�ʵ�����ݵ�
		//SchemaGraph��һ��8�еĶ�ά������������ϵ
		//����ͼ
		//�������ݵ��������ϵͼ
		//��Ҫ��������ͼ
		System.out.println("��������ͼʱ�俪ʼ");
		double ForeignPrimaryTime=System.currentTimeMillis();
		ForeignPrimary fp=new ForeignPrimary();
		ForeignPrimary=fp.get();
		System.out.println((System.currentTimeMillis()-ForeignPrimaryTime)/1000+"s������ͼ");
		System.out.println("��������ͼʱ�����");
		//ģʽͼ
		//ģʽͼ������������ϵ
		double SchemaStructureTime=System.currentTimeMillis();
		SchemaGraph sg=new SchemaGraph();
		SchemaGraph=sg.get();
		System.out.println((System.currentTimeMillis()-SchemaStructureTime)/1000+"s,ģʽͼ");

	}
	
	//���ؽ����
	public ArrayList<String []> getResult(){
		return MTJNTSet;
	}
	
	//���ز�ѯʱ��
	public String[] getTime(){
		return Time;
	}
	
	//��ѯ����
	//Query���û�������ַ���
	//Top_k,CNMax�����û������
	//ֻ�ǰ��û���������ݴ���������������һ����
	public void search(String Query,int Top_k,int CNMax){
		
		this.Top_k=Top_k;
		this.CNMax=CNMax;
		
		Keywords key=new Keywords(Query);//�ַ�������Keywords��key
		keywords=key.get();
		this.KeywordNum=keywords.size();	
		results();
	}
	
	//����Ԥ��
	//���ҿ���������һ��keyword,study,relational,test,oracle�Ĳ�ѯȻ���id�浽��������
	//�������õ�
//	private void Preheat(String Query){
//		//get()����get���е�keyword
//		//String Query == keyword,study,relational,test,oracle
//		Keywords key=new Keywords(Query);
//		keywords=key.get();
//		//������
//		//Ԥ��ʱQuery == "keyword,study,relational,test,oracle"
//		double BaseTableTime=System.currentTimeMillis();
//		BaseTable bs=new BaseTable(keywords);
//		BaseTable=bs.get();//BaseTable��HashMap��ֵ��
//		double time=(System.currentTimeMillis()-BaseTableTime)/1000;
//		System.out.println(time+"s������Ԥ��");
//	}
	
	//��ѯ���
	//ͳһ����
	private void results(){
		
		Time=new String[6];
		
		double TotalTime=System.currentTimeMillis();
		
		//������
		double BaseTableTime=System.currentTimeMillis();
		BaseTable bs=new BaseTable(keywords);
		BaseTable=bs.get();
		double time=(System.currentTimeMillis()-BaseTableTime)/1000;
		System.out.println(time+"s������");
		Time[0]="Ԫ�鼯��"+time+"��";
		
		//��ѡ����
		double CandidateTreeTime=System.currentTimeMillis();
		CandidateTree=new ArrayList<int[][]>();
		CN cn=new CN(CNMax, KeywordNum, SchemaGraph, BaseTable);
		CandidateNetwork=cn.get();
		time=(System.currentTimeMillis()-CandidateTreeTime)/1000;
		System.out.println(time+"s,��ѡ����");
		Time[1]="��ѡ���磺"+time+"��";

		//��ʾ��ѡ����
		int sum=0;
		Iterator<ArrayList<long[]>> it=CandidateNetwork.iterator();
		//CandidateNetwork�Ǹ���ά��
		while(it.hasNext())
		{
			//��ά
			ArrayList<long[]> k=it.next();
			Iterator<long[]> itr=k.iterator();
			int ppp = -1;//��֧���
			while(itr.hasNext())
			{
				//һά
				//c[0]����flag��c��1����ǰλ�ã�c��2���������㣬c��3��������յ�
				long[] c=itr.next();
				System.out.print(c[0]+"_"+c[1]+"_"+c[2]+"_"+c[3]);
				System.out.println();
				
				if(c[1]!=ppp)
				{System.out.println("��֧");}
				ppp++;
				
				//System.out.print(c[0]+" ");
				//System.out.println();
			}
			sum++;
			System.out.println("��ʾ��ѡ����");
		}
		System.out.println("sum="+sum);
		
		//�ϲ�����
		double CCNTime=System.currentTimeMillis();
		//KeywordNum����KeywordSize
		CCN ccn=new CCN(KeywordNum, CandidateNetwork);
		CombinedCandidateNetwork=ccn.get();
		time=(System.currentTimeMillis()-CCNTime)/1000;
		System.out.println(time+"s,�ϲ�����");
		Time[2]="�ϲ����磺"+time+"��";
		
//		System.out.println("��ʾ�ϲ���ѡ������--------");
//		//��ʾ�ϲ���ѡ������
//		for (int i = 0; i < CombinedCandidateNetwork.size(); i++) {
//			int[][] com=CombinedCandidateNetwork.get(i);
//			for(int j=0;j<com[0].length;j++){
//				System.out.println(com[0][j]+"\t\t"+com[1][j]+"\t\t"+com[2][j]);
//			}
//			System.out.println("��ʾ�ϲ���ѡ������-------");
//		}
		
		//Ԫ������
		double MTJNTTime=System.currentTimeMillis();
		MTJNT mt=new MTJNT(Top_k, CNMax,CombinedCandidateNetwork, BaseTable, ForeignPrimary);
		MTJNTs=mt.getTop_k();	
		time=(System.currentTimeMillis()-MTJNTTime)/1000;
		System.out.println(time+"s,Ԫ������");
		Time[3]="Ԫ�����磺"+time+"��";

		
		//��ʾԪ��������
		for(int i=0;i<MTJNTs.size();i++)
		{
			int[][] Tuples=MTJNTs.get(i);
			int ppp = -1;//��֧���
			for(int j=0;j<Tuples[0].length;j++)
			{
				//System.out.print(Tuples[0][j]+"\t");
				System.out.print(Tuples[0][j]+"_"+Tuples[1][j]);
				if(Tuples[1][j]!=ppp)
				{System.out.print("��֧");}
				ppp++;
				System.out.println();				
			}
			System.out.println();
		}
		
		//Ԫ��������Ԫ����ʽ��
		double ResultTime=System.currentTimeMillis();
		TupleResult ts=new TupleResult(MTJNTs);
		MTJNTSet=ts.getResults();
		time=(System.currentTimeMillis()-ResultTime)/1000;
		System.out.println(time+"s,Ԫ������\n");
		System.out.println("�ۼƵ��β�ѯ"+ts.count+"��");
		Time[4]="�������"+time+"��";	
		Time[5]="�ܺ�ʱ��"+(System.currentTimeMillis()-TotalTime)/1000+"��";
		
		//��ʾԪ��������
//		for(int i=0;i<MTJNTSet.size();i++){
//			System.out.println("-------------------------------------------------------------------------------------------------");
//			String[] c=MTJNTSet.get(i);
//			for(int j=0;j<c.length;j++){
//				System.out.println(c[j]);
//			}
//			System.out.println();
//		}

	}

}
