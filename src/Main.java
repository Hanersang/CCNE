
public class Main {

	public static void main(String[] args) {

		//初始化界面
		System.out.println("初始化界面,notlong");
		WaitUI a=new WaitUI();
		Thread th=new Thread(a);
		th.start();
		System.out.println("初始化界面结束");
		//初始化
		Module mo=new Module();	
		System.out.println("初始化Module");
		
		//初始化结束，关闭初始化界面，打开查询界面
		UI b=new UI(mo);
		th.interrupt();
		Thread th1=new Thread(b);
		System.out.println("th1");
		th1.start();		
	}
}
