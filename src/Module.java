import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


public class Module {
	
	//参数	
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
	ArrayList<int[][]> MTJNTs;//元组网络（id形式）
	ArrayList<String []> MTJNTSet;//元组网络(元组形式)
	String[] Time;
	
	//构造函数
	public Module(){
		System.out.println("initialization()");
		initialization();
		//System.out.println("Preheat");//注释掉完全不影响使用
		//Preheat("keyword,study,relational,test,oracle");//数值以赋予
	}
	
	//初始化阶段
	private void initialization(){
		//可能是用来得到实验数据的
		//SchemaGraph是一个8行的二维表，表达主外键关系
		//数据图
		//包含数据的主外键关系图
		//主要卡在数据图
		System.out.println("计算数据图时间开始");
		double ForeignPrimaryTime=System.currentTimeMillis();
		ForeignPrimary fp=new ForeignPrimary();
		ForeignPrimary=fp.get();
		System.out.println((System.currentTimeMillis()-ForeignPrimaryTime)/1000+"s，数据图");
		System.out.println("计算数据图时间结束");
		//模式图
		//模式图仅表达主外键关系
		double SchemaStructureTime=System.currentTimeMillis();
		SchemaGraph sg=new SchemaGraph();
		SchemaGraph=sg.get();
		System.out.println((System.currentTimeMillis()-SchemaStructureTime)/1000+"s,模式图");

	}
	
	//返回结果集
	public ArrayList<String []> getResult(){
		return MTJNTSet;
	}
	
	//返回查询时间
	public String[] getTime(){
		return Time;
	}
	
	//查询输入
	//Query是用户输入的字符串
	//Top_k,CNMax都是用户输入的
	//只是把用户输入的数据储存起来，调用下一步。
	public void search(String Query,int Top_k,int CNMax){
		
		this.Top_k=Top_k;
		this.CNMax=CNMax;
		
		Keywords key=new Keywords(Query);//字符串传入Keywords类key
		keywords=key.get();
		this.KeywordNum=keywords.size();	
		results();
	}
	
	//基表预热
	//依我看就是做了一次keyword,study,relational,test,oracle的查询然后把id存到数组里了
	//拿数据用的
//	private void Preheat(String Query){
//		//get()就是get其中的keyword
//		//String Query == keyword,study,relational,test,oracle
//		Keywords key=new Keywords(Query);
//		keywords=key.get();
//		//基表集合
//		//预热时Query == "keyword,study,relational,test,oracle"
//		double BaseTableTime=System.currentTimeMillis();
//		BaseTable bs=new BaseTable(keywords);
//		BaseTable=bs.get();//BaseTable是HashMap键值对
//		double time=(System.currentTimeMillis()-BaseTableTime)/1000;
//		System.out.println(time+"s，基表预热");
//	}
	
	//查询结果
	//统一处理
	private void results(){
		
		Time=new String[6];
		
		double TotalTime=System.currentTimeMillis();
		
		//基表集合
		double BaseTableTime=System.currentTimeMillis();
		BaseTable bs=new BaseTable(keywords);
		BaseTable=bs.get();
		double time=(System.currentTimeMillis()-BaseTableTime)/1000;
		System.out.println(time+"s，基表");
		Time[0]="元组集："+time+"秒";
		
		//候选网络
		double CandidateTreeTime=System.currentTimeMillis();
		CandidateTree=new ArrayList<int[][]>();
		CN cn=new CN(CNMax, KeywordNum, SchemaGraph, BaseTable);
		CandidateNetwork=cn.get();
		time=(System.currentTimeMillis()-CandidateTreeTime)/1000;
		System.out.println(time+"s,候选网络");
		Time[1]="候选网络："+time+"秒";

		//显示候选网络
		int sum=0;
		Iterator<ArrayList<long[]>> it=CandidateNetwork.iterator();
		//CandidateNetwork是个三维的
		while(it.hasNext())
		{
			//二维
			ArrayList<long[]> k=it.next();
			Iterator<long[]> itr=k.iterator();
			int ppp = -1;//分支标记
			while(itr.hasNext())
			{
				//一维
				//c[0]表名flag，c【1】当前位置，c【2】主外键起点，c【3】主外键终点
				long[] c=itr.next();
				System.out.print(c[0]+"_"+c[1]+"_"+c[2]+"_"+c[3]);
				System.out.println();
				
				if(c[1]!=ppp)
				{System.out.println("分支");}
				ppp++;
				
				//System.out.print(c[0]+" ");
				//System.out.println();
			}
			sum++;
			System.out.println("显示候选网络");
		}
		System.out.println("sum="+sum);
		
		//合并网络
		double CCNTime=System.currentTimeMillis();
		//KeywordNum就是KeywordSize
		CCN ccn=new CCN(KeywordNum, CandidateNetwork);
		CombinedCandidateNetwork=ccn.get();
		time=(System.currentTimeMillis()-CCNTime)/1000;
		System.out.println(time+"s,合并网络");
		Time[2]="合并网络："+time+"秒";
		
//		System.out.println("显示合并候选连接树--------");
//		//显示合并候选连接树
//		for (int i = 0; i < CombinedCandidateNetwork.size(); i++) {
//			int[][] com=CombinedCandidateNetwork.get(i);
//			for(int j=0;j<com[0].length;j++){
//				System.out.println(com[0][j]+"\t\t"+com[1][j]+"\t\t"+com[2][j]);
//			}
//			System.out.println("显示合并候选连接树-------");
//		}
		
		//元组网络
		double MTJNTTime=System.currentTimeMillis();
		MTJNT mt=new MTJNT(Top_k, CNMax,CombinedCandidateNetwork, BaseTable, ForeignPrimary);
		MTJNTs=mt.getTop_k();	
		time=(System.currentTimeMillis()-MTJNTTime)/1000;
		System.out.println(time+"s,元组网络");
		Time[3]="元组网络："+time+"秒";

		
		//显示元组连接树
		for(int i=0;i<MTJNTs.size();i++)
		{
			int[][] Tuples=MTJNTs.get(i);
			int ppp = -1;//分支标记
			for(int j=0;j<Tuples[0].length;j++)
			{
				//System.out.print(Tuples[0][j]+"\t");
				System.out.print(Tuples[0][j]+"_"+Tuples[1][j]);
				if(Tuples[1][j]!=ppp)
				{System.out.print("分支");}
				ppp++;
				System.out.println();				
			}
			System.out.println();
		}
		
		//元组结果集（元组形式）
		double ResultTime=System.currentTimeMillis();
		TupleResult ts=new TupleResult(MTJNTs);
		MTJNTSet=ts.getResults();
		time=(System.currentTimeMillis()-ResultTime)/1000;
		System.out.println(time+"s,元组结果集\n");
		System.out.println("累计单次查询"+ts.count+"次");
		Time[4]="结果集："+time+"秒";	
		Time[5]="总耗时："+(System.currentTimeMillis()-TotalTime)/1000+"秒";
		
		//显示元组连接树
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
