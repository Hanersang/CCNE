
public class Main {

	public static void main(String[] args) {

		//��ʼ������
		System.out.println("��ʼ������,notlong");
		WaitUI a=new WaitUI();
		Thread th=new Thread(a);
		th.start();
		System.out.println("��ʼ���������");
		//��ʼ��
		Module mo=new Module();	
		System.out.println("��ʼ��Module");
		
		//��ʼ���������رճ�ʼ�����棬�򿪲�ѯ����
		UI b=new UI(mo);
		th.interrupt();
		Thread th1=new Thread(b);
		System.out.println("th1");
		th1.start();		
	}
}
