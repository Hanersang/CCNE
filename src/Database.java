import java.sql.*;
import java.util.*;
public class Database {
	
	Connection ct=null;	
	//��ʼ�����������ݿ�
	public  Database(){
		//���ݿ�����
		final String Driver="oracle.jdbc.driver.OracleDriver";
		final String Url="jdbc:oracle:thin:@127.0.0.1:1521:orcl";
		//���ݿ��û���
		//final String UserName="dblp";
		//final String UserName="myuser0";
		final String UserName="myuser";
		//���ݿ�����
		//final String Password="438488214";
		//final String Password="myuser0";
		final String Password="myuser";
		try {
			Class.forName(Driver);
			ct=DriverManager.getConnection(Url,UserName,Password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}	
}

class SchemaGraph extends Database{
	
	private ArrayList<int[]> schemastructure=new ArrayList<int[]>();
	
	//��ʼ��
	public SchemaGraph(){
		getSchemaStructure();
	}
	
	
	//����schema_structure��������
	private void getSchemaStructure(){
		//ʹ�����ݿ�ӿ�	
		ResultSet rs=null;
		PreparedStatement ps=null;
		
		try {
			String SQL="select * from schema_structure order by begin,end";
			ps=ct.prepareStatement(SQL);
			rs=ps.executeQuery();
			while(rs.next()){
				int[] temp=new int[2];
				temp[0]=rs.getInt(1);
				temp[1]=rs.getInt(2);
				schemastructure.add(temp);
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				rs.close();
				ps.close();
				ct.	close();
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		}
	}
	
	//��ȡSchemaGraph��
	public ArrayList<int[]> get(){
		return schemastructure;
	}
}

class BaseTable extends Database
{
	private ArrayList<String> Keyword;
	private HashMap<Integer, int[]> BaseTableSet=new HashMap<Integer, int[]>();
	ArrayOper array=new ArrayOper();
	
	//��ʼ��
	public BaseTable(HashSet<String> Keyword)
	{
		this.Keyword=new ArrayList<String>(Keyword);
		//this.Keyword = StrSplit(this.Keyword);
		getBaseTable();
	}
//	private int sizekeyword(ArrayList<String> keyword)
//	{
//		int count = 0;
//		String str = keyword.get(0);
//		for(int i = 0;i<str.length();i++)
//		{
//			char c = str.charAt(i);
//			if(c == ' ')
//			{count++;}
//		}
//		//String[] sArray=str.split("");
//		count++;
//		return count;		
//	}
//	private ArrayList<String> StrSplit(ArrayList<String> keyword)
//	{
//		ArrayList<String> k=new ArrayList<String>();
//		String str = keyword.get(0);
//		String[] sArray=str.split(" ");
//		for(int i = 0;i<sArray.length;i++)
//		{
//			k.add(sArray[i]);
//		}		
//		return k;		
//	}	
	//��������Ԫ�鼯
	private void getBaseTable()
	{		
		//�ջ���ı�־
		final int NullTag=99;
		//���ݿ��б������
		final int TableNum=4;		
		int KeywordSize=Keyword.size();		
		//int KeywordSize=sizekeyword(Keyword);
		
		//����һ���ؼ�����ϵ��б�
		ArrayList<int[]> KeyCombinationList=new ArrayList<int[]>();
		//�������
		int CombineNum=(int )Math.pow(2, KeywordSize)-1;
		//�ؼ������
		for(int j=1;j<=CombineNum;j++)
		{

			int[] KeyCombination=new int[KeywordSize];
	
			int Binary=Integer.parseInt(Integer.toBinaryString(j));
			//toBinaryString����ַ�����ʽ
			
			for(int i=0;i<KeywordSize;i++)
			{
				KeyCombination[i]=Binary%10;
				Binary=Binary/10;
			}
			//������йؼ�����ϣ����뵽KeyCombinationList����������
			KeyCombinationList.add(KeyCombination);
		}
		
		//�������л���
		for(int i=0;i<KeyCombinationList.size();i++)
		{
		
			//�ؼ����������
			int[] Order=new int[KeywordSize];
			int end=KeywordSize-1;
			int start=0;
			for(int j=0;j<KeywordSize;j++){
				if(KeyCombinationList.get(i)[j]==1)
				{
					Order[start]=j;
					start++;
				}
				else
				{
					Order[end]=j;
					end--;					
				}
			}			
			
			//����Author���Ӧ�Ĺؼ�������뼯��
			//String SQL="Select no from author where contains(name,'";
			String SQL="Select no from name_basic where contains(primaryname,'";
			int[] KeyCombination=KeyCombinationList.get(i);
			createKeyBaseTable(SQL, Order, KeyCombination,300);	
			//System.out.println("createKeyBaseTable(SQL, Order, KeyCombination,300);	");
			
			//����Paper���Ӧ�Ĺؼ�������뼯��
			//SQL="Select no from paper where contains(title,'";
			SQL="Select no from title_basics where contains(primarytitle,'";
			KeyCombination=KeyCombinationList.get(i);
			createKeyBaseTable(SQL, Order, KeyCombination,200);	
			//System.out.println("createKeyBaseTable(SQL, Order, KeyCombination,200);	");
		}		
		
		//���������Ӧ�Ŀջ�����뼯��
		//�ж����key199��499����ɾ��
		//˵�ǿձ���ʵ���հ������滹����ֵ�ģ���Ӧ��299key�������д�2��key��Ӧvalue�ĸ���
		//����ֻ����Ҫ�����鵫�ǣ���Ȼ�ܸ�ֵ���ﲻ����
		for(int i=1;i<=TableNum;i++)
		{
			//һ���ĸ���ѭ���ĴΣ�i=4��ԭ���������flagֻ����1234
			int[] BaseTable=new int[0];
			Iterator<Integer> iter=BaseTableSet.keySet().iterator();
			//ע���ⲻ��set��map,BaseTableSet��createKeyBaseTable()��ֵ��
			int Num=0;
			while(iter.hasNext())
			{
				int key=iter.next();
				if(key%1000/100==i)
				{
					Num++;
			        int strLen1=BaseTable.length;//�����һ�����鳤��
			        int strLen2=BaseTableSet.get(key).length;//����ڶ������鳤��
			        BaseTable=Arrays.copyOf(BaseTable,strLen1+strLen2);//����
			        System.arraycopy(BaseTableSet.get(key), 0, BaseTable, strLen1,strLen2 );//���ڶ����������һ������ϲ�
				}
			}
			if(Num>1)
			{
				array.quickSort(BaseTable, 0,BaseTable.length-1);
			}
			int[] Temp=Arrays.copyOf(BaseTable, BaseTable.length);
	        int TableName=i*100+NullTag;
	        BaseTableSet.put(TableName, Temp);
		}
		
	}
	
	//�����ؼ�����
	//��¼����no��ֻ������ʱ��Ƚ��٣����ݿ���no������300��400Ϊ��ͷ��
	private void createKeyBaseTable(String SQL,int[] Order,int[] KeyCombination,int table)
	{	
		ResultSet rs=null;
		PreparedStatement ps=null;
		int[] BaseTable=new int[10000];
		int KeywordSize=Keyword.size();
		//int KeywordSize=sizekeyword(Keyword);
		System.out.println("�����ؼ�����һ��");
		//����sql���
		for(int j=0;j<Order.length;j++)
		{
			if(j==0)
			{
				SQL=SQL+"\""+Keyword.get(Order[j])+"\"";
			}
			else if(KeyCombination[Order[j]]==1)
			{
				SQL=SQL+" and "+"\""+Keyword.get(Order[j])+"\"";
			}
			else
			{
				SQL=SQL+" not "+"\""+Keyword.get(Order[j])+"\"";
			}
		}
		SQL=SQL+"\')>0 order by no";
			
		//ִ��sql���
		int TupleNum=0;
		try {
			ps=ct.prepareStatement(SQL);
			rs=ps.executeQuery();
			int j=0;
			for(j=0;rs.next();j++){
				if(j+1==BaseTable.length){
					BaseTable=Arrays.copyOf(BaseTable, BaseTable.length+10000);
				}
				BaseTable[j]=rs.getInt(1);
			}
			TupleNum=j;

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//TupleNum����BaseTableSet��Key
		//TupleNum>0���ǲ�ѯ���˽��
		if(TupleNum>0){
			int[] Temp=Arrays.copyOf(BaseTable, TupleNum);
			int TableName=0;
	        for(int a=0;a<KeywordSize;a++){
	        	if(KeyCombination[a]==1){
			        TableName=TableName*1000+table+a;
			        //table�Ǵ�������200,300
	        	}
	        }
	        BaseTableSet.put(TableName, Temp);
		}
		
		
	}
	
	
	//��ȡ������
	public HashMap<Integer, int[]> get(){
		return BaseTableSet;
	}
}


class ForeignPrimary extends Database{	
	private int[][] foreign_primary=new int[2][110000000];	
	//��ʼ��
	//����������ͼ��
	public ForeignPrimary() {
		getForeignPrimary();
	}
	
	//���foreign_primary����
	public int[][] get(){
		return foreign_primary;
	}
	
	//foreign_primary�����ڴ�
	public int[][] getForeignPrimary(){
		
		ResultSet rs=null;
		PreparedStatement ps=null;
		
		int Sum=0;
		try {
			String SQL="select distinct * from foreign_primary order by foreign,primary";
			ps=ct.prepareStatement(SQL);
			rs=ps.executeQuery();
			while(rs.next()){
				foreign_primary[0][Sum]=rs.getInt(1);
				foreign_primary[1][Sum]=rs.getInt(2);
				Sum++; 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				rs.close();
				ps.close();
				ct.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		}
		foreign_primary[0]=Arrays.copyOf(foreign_primary[0], Sum);
		foreign_primary[1]=Arrays.copyOf(foreign_primary[1], Sum);
		return foreign_primary;
	}

}

class TupleResult extends Database{
	
	ArrayList<int[][]> MTJNTs;
	ArrayList<String[][]> MTJNTResult=new ArrayList<String[][]>();
	int count = 0;
	//��ʼ��
	public TupleResult(ArrayList<int[][]> MTJNTs){
		this.MTJNTs=MTJNTs;
	}
	
	//��ȡԪ����ʽ��Ԫ������
	public ArrayList<String[]> getResults(){
		
		ArrayList<String[]> Result=new ArrayList<String[]>();

		String SQL;
		for(int i=0;i<MTJNTs.size();i++){
			String[][] Tuples=new String[2][MTJNTs.get(i)[0].length];
			//Tuples01,0�������ж���Ҫ�Ӽ���\t��1������
			for(int j=0;j<MTJNTs.get(i)[0].length;j++){
				int id=MTJNTs.get(i)[0][j];
				switch (id/100000000) {
				case 1:
					//����Ʊ��
					if(MTJNTs.get(i)[0][j-1]/100000000==3){
						int t=Integer.valueOf(Tuples[0][j-1]);
						Tuples[0][j]=String.valueOf(++t);
					}
					else{
						int t=Integer.valueOf(Tuples[0][j-1]);
						if(t>0){
							Tuples[0][j]=String.valueOf(--t);
						}
						else{
							for(int k=0;k<j;k++){
								int m=Integer.valueOf(Tuples[0][k]);
								Tuples[0][k]=String.valueOf(++m);
							}
							Tuples[0][j]="0";
						}
					}
					
					Tuples[1][j]="����";
					break;
				case 2:
					ResultSet rs=null;
					PreparedStatement ps=null;
					try {
						
						SQL="select * from title_basics where no="+id;
						this.count++;
						ps=ct.prepareStatement(SQL);
						rs=ps.executeQuery();//rs�����ָ��
						if(rs.next()){
							StringBuffer sb=new StringBuffer();
							sb.append(rs.getString(3));
							sb.append("\t");
							sb.append(rs.getString(5));
							sb.append("\t");
							sb.append(rs.getString(8));
							Tuples[1][j]=sb.toString();//rs.getString(1)������ÿ�е�����
						}
						//����Ʊ��
						if(j==0){
							Tuples[0][j]="0";
						}
						else{
							int t1=Integer.valueOf(Tuples[0][j-2]);
							int t2=Integer.valueOf(Tuples[0][j-1]);
							if(t1<t2){
								Tuples[0][j]=String.valueOf(++t2);
							}
							else{
								if(t2>0){
									Tuples[0][j]=String.valueOf(--t2);
								}
								else{
									for(int k=0;k<j;k++){
										int m=Integer.valueOf(Tuples[0][k]);
										Tuples[0][k]=String.valueOf(++m);
									}
									Tuples[0][j]="0";
								}
							}

						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}finally{
						try {
							rs.close();
							ps.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}	
					}
					break;
				case 3:
					ResultSet rs3=null;
					PreparedStatement ps3=null;
					try {
						
						SQL="select * from name_basic where no="+id;
						this.count++;
						ps3=ct.prepareStatement(SQL);
						rs3=ps3.executeQuery();
						if(rs3.next()){
							Tuples[1][j]=rs3.getString(2);		
						}
						//����Ʊ��
						if(j==0){
							Tuples[0][j]="0";
						}
						else{
							int t=Integer.valueOf(Tuples[0][j-1]);
							if(t>0){
								Tuples[0][j]=String.valueOf(--t);
							}
							else{
								for(int k=0;k<j;k++){
									int m=Integer.valueOf(Tuples[0][k]);
									Tuples[0][k]=String.valueOf(++m);
								}
								Tuples[0][j]="0";
							}
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}finally{
						try {
							rs3.close();
							ps3.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}	
					}
					break;
				case 4:
					ResultSet rs4=null;
					PreparedStatement ps4=null;
					try {
						String temp="";
						String temp0="";
						SQL="select * from title_episode where no="+id;
						this.count++;
						ps4=ct.prepareStatement(SQL);
						rs4=ps4.executeQuery();
						if(rs4.next()){
							temp=rs4.getString(1);
						}
						SQL="select * from title_basics where no="+MTJNTs.get(i)[0][j-1];
						this.count++;
						ps4=ct.prepareStatement(SQL);
						rs4=ps4.executeQuery();
						//����Ʊ��
						if(rs4.next()){
							temp0=rs4.getString(4);
						}
						if(temp.equals(temp0)){
							int t=Integer.valueOf(Tuples[0][j-1]);
							Tuples[0][j]=String.valueOf(++t);
						}
						else{
							int t=Integer.valueOf(Tuples[0][j-1]);
							if(t>0){
								Tuples[0][j]=String.valueOf(--t);
							}
							else{
								for(int k=0;k<j;k++){
									int m=Integer.valueOf(Tuples[0][k]);
									Tuples[0][k]=String.valueOf(++m);
								}
								Tuples[0][j]="0";
							}
						}
						Tuples[1][j]="����";
					} catch (Exception e) {
						e.printStackTrace();
					}finally{
						try {
							rs4.close();
							ps4.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}	
					}

					break;
				}				
			}
			
			MTJNTResult.add(Tuples);
		}
	
		//ΪԪ������Ʊ��\t
		for(int i=0;i<MTJNTResult.size();i++){
			String[][] temp0=MTJNTResult.get(i);
			String[] temp=new String[temp0[0].length];
			for(int j=0;j<temp0[0].length;j++){
				StringBuffer sb=new StringBuffer();
				for(int k=0;k<Integer.valueOf(temp0[0][j]);k++){
					sb.append("\t");
				}
				sb.append(temp0[1][j]);
				temp[j]=sb.toString();
			}
			Result.add(temp);
		}
		
		return Result;
	}
}