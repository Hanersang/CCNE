import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.text.PlainDocument;

//��ѯ����
public class UI extends JFrame implements ActionListener,Runnable,KeyListener {

	//�ؼ�
	JButton jb1;
	JButton jb2;
	JTextField jf1;
	JTextField jf2;
	JTextField jf3;
	JLabel jl1;
	JLabel jl2;
	JLabel jl3;
	JLabel jl4;
	JPanel jp1;
	JPanel jp2;
	JPanel jp3;
	JPanel jp4;
	JPanel jp5;
	JTextArea ja1;
	JTextArea ja2;
	JScrollPane jsp1;
	JScrollPane jsp2;
	JSplitPane sp;
	JToolBar jtb;
	Module mo;
	
	public UI(Module mo){
		this.mo=mo;
	}

	//��ʼ������
	private void getUI(){
		
		jb1=new JButton("��ѯ");
		jb1.setFont(new Font("����",Font.PLAIN,15));
		//ע�����
		jb1.addActionListener(this);
		jb1.setActionCommand("Search");
		
		jb2=new JButton("���");
		jb2.setFont(new Font("����",Font.PLAIN,15));
		//ע�����
		jb2.addActionListener(this);
		jb2.setActionCommand("Empty");

		//��ѯ�ı���
		jf1=new JTextField(25);
		jf1.setFont(new Font("Times New Roman",Font.PLAIN,20));
		jf1.addKeyListener(this);

		
		//Top_k�ı���
		jf2=new JTextField(5);
		jf2.setDocument(new NumberValidator(4));
		jf2.setText("10");
		
		//CNMax�ı���
		jf3=new JTextField(5);
		jf3.setDocument(new NumberValidator(1));
		jf3.setText("7");
		
		jl1=new JLabel("�ؼ��֣�");
		jl1.setFont(new Font("����",Font.PLAIN,20));
		
		jl2=new JLabel("       Top_k:   ");
		jl2.setFont(new Font("Times New Roman",Font.ITALIC,20));
		
		jl3=new JLabel("       CNMax: ");
		jl3.setFont(new Font("Times New Roman",Font.ITALIC,20));

		//״̬��
		jl4=new JLabel("�գ�����");
		jl4.setFont(new Font("����",Font.PLAIN,20));

		jp1=new JPanel();
		jp2=new JPanel();
		jp3=new JPanel();
		jp4=new JPanel();
		jp5=new JPanel();
		
		ja1=new JTextArea();
		ja1.setEditable(false);
		ja2=new JTextArea();
		ja2.setEditable(false);
		
		jsp1=new JScrollPane(ja1);
		jsp2=new JScrollPane(ja2);
		//������ʼ�տɼ�
		jsp2.setHorizontalScrollBarPolicy(  JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); 
		jsp2.setVerticalScrollBarPolicy(  JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		//����
		sp=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jsp1,jsp2);
		sp.setDividerLocation(150);
		sp.setDividerSize(10);
		sp.setOneTouchExpandable(true);
		
		//״̬��
		jtb=new JToolBar();
		jtb.setFloatable(false);

	
		//���񲼾�
		this.setLayout(new BorderLayout());
		jp2.setLayout(new GridLayout(2,1));
		
		jp1.add(jl1);
		jp1.add(jf1);
		jp1.add(jb1);
		jp1.add(jb2);
		
		jp4.add(jl2);
		jp4.add(jf2);
		
		jp5.add(jl3);
		jp5.add(jf3);
		
		jp2.add(jp4);
		jp2.add(jp5);
		
		jp3.add(jp1);
		jp3.add(jp2);
		
		jtb.add(jl4);
				
		this.add(jp3,BorderLayout.NORTH);
		this.add(sp,BorderLayout.CENTER);
		this.add(jtb,BorderLayout.SOUTH);
				
		this.setTitle("��ϵ���ݿ�ؼ��ֲ�ѯ");
		Image a = getToolkit().getImage("images/Search.png");
		this.setIconImage(a);
		this.setSize(1000, 600);
		//����
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		int x = (int)(toolkit.getScreenSize().getWidth()-this.getWidth())/2;
		int y = (int)(toolkit.getScreenSize().getHeight()-this.getHeight())/2;
		this.setLocation(x, y);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		//�ö�
		//this.setAlwaysOnTop(true);		
	}


	public void run() {
		getUI();		
	}


	private void search(){
		//�����ѯ�������ı����ÿ�
		ja1.setText("");
		ja2.setText("");
		
		//��ȡ��ѯ����
		String Query=this.jf1.getText();
		String s1=this.jf2.getText();
		String s2=this.jf3.getText();
		int Top_k;
		int CNMax;
		//�Ƿ�����
		if(Query.equals("")||s1.equals("")||s2.equals("")){
			if(Query.equals("")){
				ja2.append("��ѯ����Ϊ�գ�����\n");
			}
			if(s1.equals("")){
				ja2.append("Top_k����Ϊ�գ�����\n");
			}
			else{
				Top_k=Integer.valueOf(s1);
				if(Top_k==0){
					ja2.append("Top_k����Ϊ0������\n");
				}
			}
			if(s2.equals("")){
				ja2.append("CNMax����Ϊ�գ�����\n");
			}
			else{
				CNMax=Integer.valueOf(s2);
				if(CNMax==0){
					ja2.append("CNMax����Ϊ0������\n");
				}
			}
			jl4.setText("�գ�����");
		}
		else{
			Top_k=Integer.valueOf(s1);
			CNMax=Integer.valueOf(s2);
			//�Ƿ�����
			if(Top_k==0||CNMax==0){
				if(Top_k==0){
					ja2.append("Top_k����Ϊ0������\n");
				}
				if(CNMax==0){
					ja2.append("CNMax����Ϊ0������\n");
				}
				jl4.setText("�գ�����");

			}
			//��ʾ��ѯ���
			else{
				//���ò�ѯ����
				mo.search(Query, Top_k, CNMax);//mo---Module
				//��ȡ��ѯʱ��				
				String[] Time=mo.getTime();
				//��ȡ��ѯ���
				ArrayList<String[]> Results=mo.getResult();
				//չʾ��ѯ���
				for(int i=0;i<Time.length;i++){
					ja1.append(Time[i]+"\n");
				}
				for(int i=0;i<Results.size();i++){
					ja2.append(i+1+"*");
					String[] temp=Results.get(i);
					for(int j=0;j<temp.length;j++){
						ja2.append("\t"+temp[j]+"\n");
					}
					ja2.append("\t____________________________________________________" +
							"______________________________________________________________\n");
				}

				//����״̬����ʾ
				String temp="��ѯ�����"+Results.size()+"����"+Time[5];
				jl4.setText(temp);
				ja2.setCaretPosition(0);
			}
		}
	}


	//�¼���Ӧ
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals("Search")){
			search();
		}
		//�����Ӧʱ�䣬��ѯ���ı���״̬���ÿ�
		if(e.getActionCommand().equals("Empty")){
			jf1.setText("");
			ja1.setText("");
			ja2.setText("");
			jl4.setText("�գ�����");
		}
		
	}
	
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_ENTER){
			search();
		}
		
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}


//��������Ϊ���֣��Լ�����
class NumberValidator extends PlainDocument {  
	  
    private static final long serialVersionUID = 1L;  
  
    private int limit;  
  
    public NumberValidator(int limit) {  
        super();  
        this.limit = limit;  
    }  
  
    public void insertString(int offset, String str, javax.swing.text.AttributeSet attr)  
            throws javax.swing.text.BadLocationException {  
        if (str == null) {  
            return;  
        }  
        if ((getLength() + str.length()) <= limit) {  
            char[] upper = str.toCharArray();  
            int length = 0;  
            for (int i = 0; i < upper.length; i++) {  
                if (upper[i] >= '0' && upper[i] <= '9') {  
                    upper[length++] = upper[i];  
                }  
            }  
            // ��������  
            super.insertString(offset, new String(upper, 0, length), attr);  
        }  
    }  
  
} 

//��ʼ���ĵȴ�����
class WaitUI extends JWindow implements Runnable{
	
	JLabel jl;

	//��ʼ��
	private void getUI(){
		
		//�ؼ�����͸��(Ӧ�÷�ǰ)
		

		jl=new JLabel();
		//jlable��Ӷ�̬ͼƬ
		Icon image = new ImageIcon("images/Wait.gif");
		jl.setIcon(image);
		
		this.add(jl);
		this.setSize(300,300);
		this.setVisible(true);
		//��Ϊ�м�λ��
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		int x = (int)(toolkit.getScreenSize().getWidth()-this.getWidth())/2;
		int y = (int)(toolkit.getScreenSize().getHeight()-this.getHeight())/2;
		this.setLocation(x, y);
		//�ռ�ָ��
		//this.setAlwaysOnTop(true);

	}	
	
	//���յ�������Ϣ��رոÿؼ�
	public void run(){
		getUI();
		while(true){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				this.dispose();
				return;
			}	
		}
	}

}
