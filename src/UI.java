import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.text.PlainDocument;

//查询界面
public class UI extends JFrame implements ActionListener,Runnable,KeyListener {

	//控件
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

	//初始化界面
	private void getUI(){
		
		jb1=new JButton("查询");
		jb1.setFont(new Font("宋体",Font.PLAIN,15));
		//注册监听
		jb1.addActionListener(this);
		jb1.setActionCommand("Search");
		
		jb2=new JButton("清空");
		jb2.setFont(new Font("宋体",Font.PLAIN,15));
		//注册监听
		jb2.addActionListener(this);
		jb2.setActionCommand("Empty");

		//查询文本框
		jf1=new JTextField(25);
		jf1.setFont(new Font("Times New Roman",Font.PLAIN,20));
		jf1.addKeyListener(this);

		
		//Top_k文本框
		jf2=new JTextField(5);
		jf2.setDocument(new NumberValidator(4));
		jf2.setText("10");
		
		//CNMax文本框
		jf3=new JTextField(5);
		jf3.setDocument(new NumberValidator(1));
		jf3.setText("7");
		
		jl1=new JLabel("关键字：");
		jl1.setFont(new Font("宋体",Font.PLAIN,20));
		
		jl2=new JLabel("       Top_k:   ");
		jl2.setFont(new Font("Times New Roman",Font.ITALIC,20));
		
		jl3=new JLabel("       CNMax: ");
		jl3.setFont(new Font("Times New Roman",Font.ITALIC,20));

		//状态栏
		jl4=new JLabel("空！！！");
		jl4.setFont(new Font("宋体",Font.PLAIN,20));

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
		//滚动条始终可见
		jsp2.setHorizontalScrollBarPolicy(  JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); 
		jsp2.setVerticalScrollBarPolicy(  JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		//分栏
		sp=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jsp1,jsp2);
		sp.setDividerLocation(150);
		sp.setDividerSize(10);
		sp.setOneTouchExpandable(true);
		
		//状态栏
		jtb=new JToolBar();
		jtb.setFloatable(false);

	
		//网格布局
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
				
		this.setTitle("关系数据库关键字查询");
		Image a = getToolkit().getImage("images/Search.png");
		this.setIconImage(a);
		this.setSize(1000, 600);
		//居中
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		int x = (int)(toolkit.getScreenSize().getWidth()-this.getWidth())/2;
		int y = (int)(toolkit.getScreenSize().getHeight()-this.getHeight())/2;
		this.setLocation(x, y);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		//置顶
		//this.setAlwaysOnTop(true);		
	}


	public void run() {
		getUI();		
	}


	private void search(){
		//点击查询，两个文本域置空
		ja1.setText("");
		ja2.setText("");
		
		//获取查询参数
		String Query=this.jf1.getText();
		String s1=this.jf2.getText();
		String s2=this.jf3.getText();
		int Top_k;
		int CNMax;
		//非法处理
		if(Query.equals("")||s1.equals("")||s2.equals("")){
			if(Query.equals("")){
				ja2.append("查询不能为空！！！\n");
			}
			if(s1.equals("")){
				ja2.append("Top_k不能为空！！！\n");
			}
			else{
				Top_k=Integer.valueOf(s1);
				if(Top_k==0){
					ja2.append("Top_k不能为0！！！\n");
				}
			}
			if(s2.equals("")){
				ja2.append("CNMax不能为空！！！\n");
			}
			else{
				CNMax=Integer.valueOf(s2);
				if(CNMax==0){
					ja2.append("CNMax不能为0！！！\n");
				}
			}
			jl4.setText("空！！！");
		}
		else{
			Top_k=Integer.valueOf(s1);
			CNMax=Integer.valueOf(s2);
			//非法处理
			if(Top_k==0||CNMax==0){
				if(Top_k==0){
					ja2.append("Top_k不能为0！！！\n");
				}
				if(CNMax==0){
					ja2.append("CNMax不能为0！！！\n");
				}
				jl4.setText("空！！！");

			}
			//显示查询结果
			else{
				//调用查询函数
				mo.search(Query, Top_k, CNMax);//mo---Module
				//获取查询时间				
				String[] Time=mo.getTime();
				//获取查询结果
				ArrayList<String[]> Results=mo.getResult();
				//展示查询结果
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

				//设置状态栏显示
				String temp="查询结果："+Results.size()+"条，"+Time[5];
				jl4.setText(temp);
				ja2.setCaretPosition(0);
			}
		}
	}


	//事件响应
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals("Search")){
			search();
		}
		//清空响应时间，查询，文本域，状态栏置空
		if(e.getActionCommand().equals("Empty")){
			jf1.setText("");
			ja1.setText("");
			ja2.setText("");
			jl4.setText("空！！！");
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


//限制输入为数字，以及长度
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
            // 插入数字  
            super.insertString(offset, new String(upper, 0, length), attr);  
        }  
    }  
  
} 

//初始化的等待界面
class WaitUI extends JWindow implements Runnable{
	
	JLabel jl;

	//初始化
	private void getUI(){
		
		//控件背景透明(应该放前)
		

		jl=new JLabel();
		//jlable添加动态图片
		Icon image = new ImageIcon("images/Wait.gif");
		jl.setIcon(image);
		
		this.add(jl);
		this.setSize(300,300);
		this.setVisible(true);
		//设为中间位置
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		int x = (int)(toolkit.getScreenSize().getWidth()-this.getWidth())/2;
		int y = (int)(toolkit.getScreenSize().getHeight()-this.getHeight())/2;
		this.setLocation(x, y);
		//空间指定
		//this.setAlwaysOnTop(true);

	}	
	
	//当收到结束消息后关闭该控件
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
